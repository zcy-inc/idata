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
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
@Service
public class DIJobContentServiceImpl implements DIJobContentService {

    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobPublishManager jobPublishManager;

    @Autowired
    public DIJobContentServiceImpl(DIJobContentRepo diJobContentRepo,
                                   JobInfoRepo jobInfoRepo,
                                   JobPublishRecordRepo jobPublishRecordRepo,
                                   JobPublishManager jobPublishManager) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobPublishManager = jobPublishManager;
    }

    @Override
    public DIJobContentDto save(Long jobId, DIJobContentDto contentDto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkJobContent(contentDto);
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        // 判断目标表是否在其他job中已经存在
        checkArgument(unUsedDestTable(contentDto.getDestTable(), jobId), "目标表已经被其他作业使用");

        Integer version = contentDto.getVersion();
        boolean startNewVersion = true;
        if (Objects.nonNull(version)) {
            Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
            checkArgument(jobContentOptional.isPresent(), "作业版本不存在或已删除");
            DIJobContent existJobContent = jobContentOptional.get();
            if (existJobContent.getEditable().equals(EditableEnum.YES.val)) {
                // 版本可以编辑，直接覆盖
                startNewVersion = false;
                contentDto.setId(existJobContent.getId());
                contentDto.setJobId(jobId);
                contentDto.resetEditor(operator);
                DIJobContent updateContent = contentDto.toModel();
                updateContent.setEditable(EditableEnum.YES.val);
                diJobContentRepo.update(updateContent);
            }
        }

        if (startNewVersion) {
            // 版本为空或不可编辑，新增版本
            version = diJobContentRepo.newVersion(jobId);
            contentDto.setId(null);
            contentDto.setJobId(jobId);
            contentDto.setVersion(version);
            contentDto.setOperator(operator);
            DIJobContent newVersionContent = contentDto.toModel();
            newVersionContent.setEditable(EditableEnum.YES.val);
            diJobContentRepo.save(newVersionContent);
        }

        return get(jobId, version);
    }

    @Override
    public DIJobContentDto get(Long jobId, Integer version) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");

        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        return DIJobContentDto.from(jobContentOptional.get());
    }

    @Override
    public DIJobContentDto submit(Long jobId, Integer version, String env, String remark, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");
        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        Optional<EnvEnum> envEnumOptional = EnvEnum.getEnum(env);
        checkArgument(envEnumOptional.isPresent(), "提交环境为空");

        jobPublishManager.submit(jobContentOptional.get(), envEnumOptional.get(), remark, operator);
        return DIJobContentDto.from(jobContentOptional.get());
    }

    @Override
    public List<JobContentVersionDto> getVersions(Long jobId) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");

        List<JobPublishRecord> publishRecordList = jobPublishRecordRepo.queryList(jobId);
        List<DIJobContent> contentList = diJobContentRepo.queryList(jobId);
        return assembleContentVersion(publishRecordList, contentList);
    }

    private List<JobContentVersionDto> assembleContentVersion(List<JobPublishRecord> publishRecordList,
                                                              List<DIJobContent> contentList) {
        Map<Integer, List<JobPublishRecord>> versionMap;
        if (ObjectUtils.isNotEmpty(publishRecordList)) {
            versionMap = publishRecordList.stream()
                    .collect(Collectors.groupingBy(JobPublishRecord::getJobContentVersion));
        } else {
            versionMap = Maps.newHashMap();
        }

        List<JobContentVersionDto> versionDtoList = Lists.newArrayList();
        if (ObjectUtils.isNotEmpty(contentList)) {
            for (DIJobContent content : contentList) {
                List<JobPublishRecord> publishRecords = versionMap.get(content.getVersion());
                if (publishRecords == null || publishRecords.size() == 0) {
                    versionDtoList.add(JobContentVersionDto.from(content));
                    continue;
                }

                for (JobPublishRecord record : publishRecords) {
                    JobContentVersionDto versionDto = JobContentVersionDto.from(content);
                    versionDto.setEnvironment(record.getEnvironment());
                    versionDto.setVersionStatus(record.getPublishStatus());
                    versionDtoList.add(versionDto);
                }
            }
        }
        return versionDtoList.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private void checkJobContent(DIJobContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcReadMode()), "读取模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestDataSourceType()), "目标数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "目标数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestTable()), "目标数据表为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestWriteMode()), "写入模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcTables()), "来源数据表为空");
        checkArgument(ObjectUtils.isNotEmpty(contentDto.getSrcCols()), "来源数据表字段为空");
        checkArgument(ObjectUtils.isNotEmpty(contentDto.getDestCols()), "目标数据表字段为空");

        List<MappingColumnDto> mappingColumnDtoList = contentDto.getSrcCols().stream()
                .filter(columnDto -> Objects.nonNull(columnDto.getMappedColumn()))
                .collect(Collectors.toList());
        checkArgument(ObjectUtils.isNotEmpty(mappingColumnDtoList), "映射字段为空");
    }

    private boolean unUsedDestTable(String destTable, Long excludeJobId) {
        List<DIJobContent> diJobContents = diJobContentRepo.queryList(destTable);
        if (ObjectUtils.isEmpty(diJobContents)) return true;

        Set<Long> jobIdSet = diJobContents.stream()
                .map(DIJobContent::getJobId)
                .distinct()
                .filter(job_id -> !job_id.equals(excludeJobId))
                .collect(Collectors.toSet());
        return ObjectUtils.isEmpty(jobIdSet);
    }

}
