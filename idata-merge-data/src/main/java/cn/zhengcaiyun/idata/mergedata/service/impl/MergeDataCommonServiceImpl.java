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

package cn.zhengcaiyun.idata.mergedata.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.service.MergeDataCommonService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-04-06 14:49
 **/
@Service
public class MergeDataCommonServiceImpl implements MergeDataCommonService {

    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private DIJobContentRepo diJobContentRepo;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobExecuteConfigRepo jobExecuteConfigRepo;

    @Override
    public List<MigrateResultDto> offlineJobs(String jobIds) {
        EnvEnum envEnum = EnvEnum.prod;
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        if (StringUtils.isBlank(jobIds)) {
            return resultDtoList;
        }

        String[] jobIdArray = jobIds.split(",");
        for (String jobIdStr : jobIdArray) {
            if (StringUtils.isBlank(jobIdStr)) {
                continue;
            }
            Long jobId = Long.parseLong(jobIdStr.trim());
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
            if (!jobInfoOptional.isPresent()) {
                resultDtoList.add(new MigrateResultDto("offlineJobs", String.format("作业id[%s]不存在", jobId), null));
                continue;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(jobInfo.getId(), envEnum.name());
            if (!executeConfigOptional.isPresent()) {
                resultDtoList.add(new MigrateResultDto("offlineJobs", String.format("作业id[%s]未配置prod配置", jobId), null));
                continue;
            }
            JobExecuteConfig executeConfig = executeConfigOptional.get();
            if (!executeConfig.getRunningState().equals(RunningStateEnum.resume.val)) {
                resultDtoList.add(new MigrateResultDto("offlineJobs", String.format("作业id[%s]在prod已经暂停，不需要重复暂停", jobId), null));
                continue;
            }
            Operator operator = new Operator.Builder(0L).nickname(jobInfo.getEditor()).build();
            jobInfoService.pauseJob(jobInfo.getId(), envEnum.name(), operator);
            resultDtoList.add(new MigrateResultDto("offlineJobs", String.format("作业id[%s]在prod暂停成功", jobId), null));
        }
        return resultDtoList;
    }

}
