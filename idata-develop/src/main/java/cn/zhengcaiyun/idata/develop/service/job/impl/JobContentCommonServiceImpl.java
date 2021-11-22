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
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentDto;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
@Service
public class JobContentCommonServiceImpl implements JobContentCommonService {

    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobPublishManager jobPublishManager;

    @Autowired
    public JobContentCommonServiceImpl(DIJobContentRepo diJobContentRepo,
                                       JobInfoRepo jobInfoRepo,
                                       JobPublishRecordRepo jobPublishRecordRepo,
                                       JobPublishManager jobPublishManager) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobPublishManager = jobPublishManager;
    }

    @Override
    public DIJobContentDto submit(Long jobId, Integer version, String env, String remark, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");
        // todo 支持不同作业
        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        Optional<EnvEnum> envEnumOptional = EnvEnum.getEnum(env);
        checkArgument(envEnumOptional.isPresent(), "提交环境为空");

        JobPublishContent publishContent = assemblePublishContent(jobContentOptional.get());
        jobPublishManager.submit(publishContent, envEnumOptional.get(), remark, operator);
        return DIJobContentDto.from(jobContentOptional.get());
    }

    private JobPublishContent assemblePublishContent(DIJobContent diJobContent) {
        return new JobPublishContent(diJobContent.getId(), diJobContent.getJobId(), diJobContent.getVersion());
    }

    @Override
    public List<JobContentVersionDto> getVersions(Long jobId) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");

        List<JobPublishRecord> publishRecordList = jobPublishRecordRepo.queryList(jobId);
        // todo 支持不同类型作业
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

}
