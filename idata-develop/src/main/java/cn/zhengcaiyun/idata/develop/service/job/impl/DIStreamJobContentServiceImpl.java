/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobTableRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobTableDto;
import cn.zhengcaiyun.idata.develop.manager.DIStreamJobContentManager;
import cn.zhengcaiyun.idata.develop.service.job.DIStreamJobContentService;
import cn.zhengcaiyun.idata.develop.util.ShardingTableUtil;
import cn.zhengcaiyun.idata.develop.validator.DIStreamJobContentValidator;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-16 17:46
 **/
@Service
public class DIStreamJobContentServiceImpl implements DIStreamJobContentService {

    private final DIStreamJobContentManager streamJobContentManager;
    private final DIStreamJobContentRepo streamJobContentRepo;
    private final DIStreamJobTableRepo streamJobTableRepo;
    private final JobInfoRepo jobInfoRepo;
    private final DataSourceApi dataSourceApi;

    @Autowired
    public DIStreamJobContentServiceImpl(DIStreamJobContentManager streamJobContentManager,
                                         DIStreamJobContentRepo streamJobContentRepo,
                                         DIStreamJobTableRepo streamJobTableRepo,
                                         JobInfoRepo jobInfoRepo,
                                         DataSourceApi dataSourceApi) {
        this.streamJobContentManager = streamJobContentManager;
        this.streamJobContentRepo = streamJobContentRepo;
        this.streamJobTableRepo = streamJobTableRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    public Integer save(Long jobId, DIStreamJobContentDto contentDto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        DIStreamJobContentValidator.checkContent(contentDto);
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        // 判断来源表，对于相同来源数据源和目标数据源的CDC作业，同一张来源表限配置在一个作业中
        checkTableUseInOtherJob(jobId, contentDto);
        Integer version = contentDto.getVersion();

        boolean startNewVersion = true;
        //更新
        if (Objects.nonNull(version)) {
            Optional<DIStreamJobContent> contentOptional = streamJobContentRepo.query(jobId, version);
            checkArgument(contentOptional.isPresent(), "作业版本不存在或已删除");
            DIStreamJobContent existContent = contentOptional.get();
            if (version > 1 || (version == 1 && existContent.getEditable().equals(EditableEnum.NO.val))) {
                checkUnchangeableContent(contentDto, existContent);
            }

            if (existContent.getEditable().equals(EditableEnum.YES.val)) {
                // 版本可以编辑，直接覆盖
                startNewVersion = false;
                contentDto.setId(existContent.getId());
                contentDto.setJobId(jobId);
                contentDto.resetEditor(operator);
                contentDto.setEditable(EditableEnum.YES.val);
                streamJobContentManager.save(contentDto);
            }
        }

        // 保存
        if (startNewVersion) {
            // 版本为空或不可编辑，新增版本
            version = streamJobContentRepo.newVersion(jobId);
            contentDto.setId(null);
            contentDto.setJobId(jobId);
            contentDto.setVersion(version);
            contentDto.setOperator(operator);
            contentDto.setEditable(EditableEnum.YES.val);
            streamJobContentManager.save(contentDto);
        }
        return version;
    }

    @Override
    public DIStreamJobContentDto get(Long jobId, Integer version) {
        return streamJobContentManager.getJobContentDto(jobId, version);
    }

    @Override
    public List<String> transformDestTable(String srcDataSourceType, Long srcDataSourceId, String destDataSourceType, List<String> srcTables, Integer enableSharding) {
        checkArgument(StringUtils.isNotBlank(srcDataSourceType), "获取目标表名 - 来源数据源类型为空");
        checkArgument(Objects.nonNull(srcDataSourceId), "获取目标表名 - 来源数据源为空");
        checkArgument(StringUtils.isNotBlank(destDataSourceType), "获取目标表名 - 去向数据源类型为空");
        checkArgument(CollectionUtils.isNotEmpty(srcTables), "获取目标表名 - 来源表名为空");
        checkArgument(Objects.nonNull(enableSharding), "获取目标表名 - 是否开启分表参数不正确");

        checkArgument(DataSourceTypeEnum.mysql.name().equals(srcDataSourceType)
                || DataSourceTypeEnum.postgresql.name().equals(srcDataSourceType), "暂不支持来源数据源类型%s", srcDataSourceType);
        if (DataSourceTypeEnum.starrocks.name().equals(destDataSourceType)) {
            if (enableSharding == 0) {
                return srcTables;
            } else {
                String srcTable = srcTables.get(0);
                int idx = srcTable.indexOf("[");
                checkArgument(idx > 0, "分表表名配置不合法");
                String destTable = srcTable.substring(0, idx) + "_auto_shard";
                return Lists.newArrayList(destTable);
            }
        } else if (DataSourceTypeEnum.kafka.name().equals(destDataSourceType)) {
            DataSourceDto dataSourceDto = dataSourceApi.getDataSource(srcDataSourceId);
            List<DbConfigDto> dbConfigDtoList = dataSourceDto.getDbConfigList();
            checkArgument(CollectionUtils.isNotEmpty(dbConfigDtoList), "获取目标表名 - 来源数据源配置不合法");
            final String dbName = dbConfigDtoList.get(0).getDbName();
            checkArgument(StringUtils.isNotBlank(dbName), "获取目标表名 - 来源数据源库名配置不合法");

            if (enableSharding == 0) {
                return srcTables.stream()
                        .map(srcTbl -> "ods_" + dbName + "_" + srcTbl)
                        .collect(Collectors.toList());
            } else {
                String srcTable = srcTables.get(0);
                int idx = srcTable.indexOf("[");
                checkArgument(idx > 0, "分表表名配置不合法");
                String destBaseTable = srcTable.substring(0, idx);
                return Lists.newArrayList("ods_" + dbName + "_" + destBaseTable + "_shard");
            }
        } else {
            throw new IllegalArgumentException(String.format("暂不支持去向数据源类型%s", destDataSourceType));
        }
    }

    private void checkTableUseInOtherJob(Long jobId, DIStreamJobContentDto contentDto) {
        Set<Long> existJobIdSet = streamJobContentRepo.queryJobIdByDataSource(contentDto.getSrcDataSourceId(), contentDto.getDestDataSourceId());
        // 去除当前作业
        existJobIdSet.remove(jobId);

        List<DIStreamJobTableDto> tableDtoList = contentDto.getTableDtoList();
        for (DIStreamJobTableDto tableDto : tableDtoList) {
            Set<Long> existJobIdSetOfTable;
            if (contentDto.getEnableSharding() == 1) {
                String baseTable = ShardingTableUtil.parseBaseTable(tableDto.getSrcTable());
                existJobIdSetOfTable = streamJobTableRepo.queryJobIdByShardingTable(baseTable + "[");
            } else {
                existJobIdSetOfTable = streamJobTableRepo.queryJobIdBySrcTable(tableDto.getSrcTable());
            }
            existJobIdSetOfTable.remove(jobId);
            if (existJobIdSetOfTable.size() == 0) {
                continue;
            }

            for (Long existJobIdOfTable : existJobIdSetOfTable) {
                checkArgument(!existJobIdSet.contains(existJobIdOfTable), "来源表 %s 已在作业 %s 中使用", tableDto.getSrcTable(), existJobIdOfTable.toString());
            }
        }
    }

    private void checkUnchangeableContent(DIStreamJobContentDto updateContentDto, DIStreamJobContent existContent) {
        checkArgument(Objects.equals(updateContentDto.getSrcDataSourceId(), existContent.getSrcDataSourceId()), "作业数据源保存第一个版本后不可再变更");
        checkArgument(Objects.equals(updateContentDto.getDestDataSourceId(), existContent.getDestDataSourceId()), "作业数据源保存第一个版本后不可再变更");
    }

}
