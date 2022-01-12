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

package cn.zhengcaiyun.idata.merge.data.manager;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.util.DatasourceTool;
import cn.zhengcaiyun.idata.merge.data.util.JobMigrationContext;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 10:27
 **/
@Component
public class JobMigrateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMigrateManager.class);

    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobPublishRecordService jobPublishRecordService;
    @Autowired
    private DIJobContentService diJobContentService;
    @Autowired
    private JobExecuteConfigService jobExecuteConfigService;
    @Autowired
    private JobContentCommonService jobContentCommonService;
    @Autowired
    private JobPublishRecordRepo jobPublishRecordRepo;
    @Autowired
    private JobTableService jobTableService;

    @Transactional
    public List<MigrateResultDto> migrateJob(JobInfoDto jobInfoDto, JobConfigCombinationDto configCombinationDto,
                                             Operator jobOperator, JobMigrationDto migrationDto) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        try {
            // 保存job基本信息
            Long newJobId = jobInfoService.addJob(jobInfoDto, jobOperator);
            // 保存job配置信息
            JobConfigCombinationDto combinationDto = jobExecuteConfigService.save(newJobId, EnvEnum.prod.name(), configCombinationDto, jobOperator);
            // 保存job内容信息
            Operator contentOperator = getContentOperator(migrationDto.getOldJobContent());
            Integer contentVersion = migrateContentInfo(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
            if (Objects.isNull(contentVersion)) {
                // 提交作业内容
                jobContentCommonService.submit(newJobId, contentVersion, EnvEnum.prod.name(), "迁移自动提交", contentOperator);
                // 发布作业
                Optional<JobPublishRecord> publishRecordOptional = jobPublishRecordRepo.query(newJobId, contentVersion, EnvEnum.prod.name());
                if (publishRecordOptional.isPresent() && publishRecordOptional.get().getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val)) {
                    jobPublishRecordService.approve(publishRecordOptional.get().getId(), "迁移自动发布", contentOperator);
                }
            } else {
                LOGGER.warn("*** *** migrateContentInfo of newJob:{},oldJob:{}.", newJobId, migrationDto.getOldJobId());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有操作权限");
        }
        return resultDtoList;
    }

    private Integer migrateContentInfo(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                       Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        Integer contentVersion;
        switch (jobInfoDto.getJobType()) {
            case DI_BATCH:
                contentVersion = migrateDIContent(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
            case SQL_SPARK:
                // todo 迁移作业内容，包含数据回流
            case SPARK_PYTHON:
                // todo 迁移作业内容
            case SPARK_JAR:
                // todo 迁移作业内容
            case SCRIPT_PYTHON:
                // todo 迁移作业内容
            case SCRIPT_SHELL:
                // todo 迁移作业内容
            case KYLIN:
                // todo 迁移作业内容
            default:
                contentVersion = null;
        }
        return contentVersion;
    }

    private Integer migrateDIContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                     Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        DIJobContentContentDto contentDto = new DIJobContentContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        Long old_source_id = oldJobContent.getLong("source_id");
        String old_source_type = oldJobContent.getString("source_type");
        String old_source_table = oldJobContent.getString("source_table");
        String old_source_table_pk = oldJobContent.getString("source_table_pk");
        String old_source_sql = oldJobContent.getString("source_sql");
        Boolean old_is_recreate = oldJobContent.getBoolean("is_recreate");
        // 岛端的 sql类型抽数作业需要标识出来，迁移后重新配置
        if (!"tableName".equals(old_source_type)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", "source_type是" + old_source_type + "（非全量抽数），迁移完需要重新配置", oldJobContent.toJSONString()));
            return null;
        }
        if (StringUtils.isBlank(old_source_table)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", "source_table为空，迁移完需要重新配置", oldJobContent.toJSONString()));
            return null;
        }
        if (old_source_table.indexOf("[") > 0) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", "source_table为多表格式，暂不支持", oldJobContent.toJSONString()));
            return null;
        }

        Optional<DataSource> srcDataSourceOptional = DatasourceTool.findDatasource(old_source_id, JobMigrationContext.getDataSourceListIfPresent());
        checkArgument(srcDataSourceOptional.isPresent(), "旧DI作业[%s]未找到迁移后的来源数据源", migrationDto.getOldJobId());
        DataSource srcDataSource = srcDataSourceOptional.get();
        // 数据来源-数据源类型
        contentDto.setSrcDataSourceType(srcDataSource.getType());
        // 数据来源-数据源id
        contentDto.setSrcDataSourceId(srcDataSource.getId());
        // 数据来源-读取模式，all：全量，incremental：增量
        if ("tableName".equals(old_source_type)) {
            contentDto.setSrcReadMode("all");
        } else {
            contentDto.setSrcReadMode("incremental");
        }
        // 数据来源-过滤条件
        contentDto.setSrcReadFilter("");
        // 数据来源-切分键
        contentDto.setSrcReadShardKey(StringUtils.defaultString(old_source_table_pk));
        // 数据来源-分片数量（并行度）
        contentDto.setSrcShardingNum(4);

        Optional<DataSource> destDataSourceOptional = DatasourceTool.findDatasource(old_source_id, JobMigrationContext.getDataSourceListIfPresent());
        checkArgument(destDataSourceOptional.isPresent(), "旧DI作业[%s]未找到迁移后的目标数据源", migrationDto.getOldJobId());
        DataSource destDataSource = destDataSourceOptional.get();
        // 数据去向-数据源类型
        contentDto.setDestDataSourceType(destDataSource.getType());
        // 数据去向-数据源id
        contentDto.setDestDataSourceId(destDataSource.getId());
        // 数据去向-数仓表id
        String[] target_tables = migrationDto.getOldJobInfo().getJSONArray("target_tables").toArray(new String[0]);
        if (target_tables != null && target_tables.length > 0 && StringUtils.isNotBlank(target_tables[0])) {
            contentDto.setDestTable(target_tables[0]);
        } else {
            contentDto.setDestTable(parseDestTable(srcDataSource, old_source_table));
        }

        // 数据去向-写入模式，init: 重建表，override：覆盖表
        if (Objects.equals(Boolean.TRUE, old_is_recreate)) {
            contentDto.setDestWriteMode("init");
        } else {
            if ("increment".equalsIgnoreCase(old_source_type)) {
                contentDto.setDestWriteMode("append");
            } else {
                contentDto.setDestWriteMode("overwrite");
            }
        }
        // 数据去向-写入前语句
        contentDto.setDestBeforeWrite("");
        // 数据去向-写入后语句
        contentDto.setDestAfterWrite("");
        // 数据来源-表
        contentDto.setSrcTables(old_source_table);

        // 数据来源-字段信息
        List<MappingColumnDto> columnDtoList = jobTableService.getTableColumn(DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType()), srcDataSource.getId(), old_source_table);
        if (CollectionUtils.isEmpty(columnDtoList)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", "source_table为多表格式，暂不支持", oldJobContent.toJSONString()));
            return null;
        }
        List<MappingColumnDto> srcCols = columnDtoList;
        List<MappingColumnDto> destCols = new ArrayList<>();
        columnDtoList.stream().forEach(srcColumnDto -> {
            MappingColumnDto destColumnDto = new MappingColumnDto();
            destColumnDto.setName(srcColumnDto.getName());
            destColumnDto.setDataType(srcColumnDto.getDataType());
            destColumnDto.setPrimaryKey(srcColumnDto.getPrimaryKey());
            srcColumnDto.setMappedColumn(destColumnDto);
            destCols.add(destColumnDto);
        });
        contentDto.setSrcCols(srcCols);
        contentDto.setDestCols(destCols);

        DIJobContentContentDto saveContent = diJobContentService.save(newJobId, contentDto, contentOperator);
        return saveContent.getVersion();
    }

    private Integer migrateSQLContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private Integer migrateBackFlowContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private Integer migrateKylinContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private Integer migrateSparkContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private Integer migrateScriptContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private String parseDestTable(DataSource srcDataSource, String sourceTable) {
        DbConfigDto dbConfigDto = getDbConfigDto(srcDataSource);
        String dbName = dbConfigDto.getDbName();
        return "ods.ods_" + dbName + "_" + sourceTable;
    }

    private DbConfigDto getDbConfigDto(DataSource srcDataSource) {
        DataSourceDto dto = DataSourceDto.from(srcDataSource);
        return dto.getDbConfigList().stream()
                .filter(dbConfigDto -> dbConfigDto.getEnv() == EnvEnum.prod)
                .findFirst().get();
    }

    private Operator getContentOperator(JSONObject oldJobContent) {
        String old_creator = oldJobContent.getString("creator");
        String old_editor = oldJobContent.getString("editor");
        String nickname = StringUtils.isNotEmpty(old_creator) ? old_creator : old_editor;
        return new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();
    }
}
