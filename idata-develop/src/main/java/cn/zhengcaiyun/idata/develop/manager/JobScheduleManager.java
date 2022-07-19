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

package cn.zhengcaiyun.idata.develop.manager;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobAnotherHistoryDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.IDagIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.JobRunOverviewDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.PageInfoDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskInstanceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-03 10:54
 **/
@Component
public class JobScheduleManager {

    private final IJobIntegrator jobIntegrator;
    private final IDagIntegrator dagIntegrator;
    private final JobInfoRepo jobInfoRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;

    @Autowired
    public JobScheduleManager(IJobIntegrator jobIntegrator,
                              IDagIntegrator dagIntegrator,
                              JobInfoRepo jobInfoRepo,
                              JobExecuteConfigRepo jobExecuteConfigRepo) {
        this.jobIntegrator = jobIntegrator;
        this.dagIntegrator = dagIntegrator;
        this.jobInfoRepo = jobInfoRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
    }

    public void runJob(Long jobId, String environment, boolean runPost) {
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(jobId, environment);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在");
        checkArgument(executeConfigOptional.isPresent(), "作业调度配置不存在");
        JobInfo jobInfo = jobInfoOptional.get();
        JobExecuteConfig executeConfig = executeConfigOptional.get();
        jobIntegrator.run(jobInfo, executeConfig, environment, runPost);
    }

    public String queryJobRunningLog(Long jobId, String environment, Integer jobInstanceId, Integer lineNum, Integer skipLineNum) {
        return jobIntegrator.queryLog(jobId, environment, jobInstanceId, lineNum, skipLineNum);
    }

    /**
     * 取最近的实例运行记录
     *
     * @param limit 记录数
     * @return
     */
    public List<JobRunOverviewDto> getJobLatestRecords(String environment, Integer limit) {
        List<JobRunOverviewDto> jobLatestRecords = jobIntegrator.getJobLatestRecords(environment, limit);
        jobLatestRecords.forEach(e -> {
            String jobIdStr = ReUtil.get(".*__(\\d*)", e.getName(), 1);
            if (NumberUtil.isNumber(jobIdStr)) {
                e.setJobId(Long.parseLong(jobIdStr));
            }
        });

        return jobLatestRecords;
    }

    public Page<JobAnotherHistoryDto> pagingJobHistory(Long jobId, String environment, Date startTime, Date endTime,
                                                       Integer pageNo, Integer pageSize) {
        PageInfoDto<TaskInstanceDto> pageInfoDto = jobIntegrator.pagingJobHistory(jobId, environment, null, startTime, endTime, pageNo, pageSize);
        if (Objects.isNull(pageInfoDto) || CollectionUtils.isEmpty(pageInfoDto.getTotalList())) {
            return Page.empty();
        }
        List<TaskInstanceDto> taskInstanceDtoList = pageInfoDto.getTotalList();
        List<JobAnotherHistoryDto> historyDtoList = taskInstanceDtoList.stream().map(taskInstanceDto -> {
            JobAnotherHistoryDto historyDto = new JobAnotherHistoryDto();
            BeanUtils.copyProperties(taskInstanceDto, historyDto);
            historyDto.setJobId(jobId);
            historyDto.setEnvironment(environment);
            return historyDto;
        }).collect(Collectors.toList());
        return Page.newOne(historyDtoList, pageInfoDto.getTotal());
    }

    public List<Integer> cleanDagExecutionHistory(EnvEnum envEnum) {
        return dagIntegrator.cleanExecutionHistory(envEnum.name());
    }
}
