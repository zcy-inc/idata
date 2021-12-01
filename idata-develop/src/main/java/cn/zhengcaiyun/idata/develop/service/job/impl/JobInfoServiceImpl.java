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
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobEventLog;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobDryRunDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.manager.JobManager;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 17:07
 **/
@Service
public class JobInfoServiceImpl implements JobInfoService {

    private final JobInfoRepo jobInfoRepo;
    private final JobDependenceRepo jobDependenceRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final DAGRepo dagRepo;
    private final JobManager jobManager;
    private final JobEventPublisher jobEventPublisher;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;

    @Autowired
    public JobInfoServiceImpl(JobInfoRepo jobInfoRepo,
                              JobDependenceRepo jobDependenceRepo,
                              JobExecuteConfigRepo jobExecuteConfigRepo,
                              JobPublishRecordRepo jobPublishRecordRepo,
                              DAGRepo dagRepo,
                              JobManager jobManager,
                              JobEventPublisher jobEventPublisher,
                              DevTreeNodeLocalCache devTreeNodeLocalCache) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.dagRepo = dagRepo;
        this.jobManager = jobManager;
        this.jobDependenceRepo = jobDependenceRepo;
        this.jobEventPublisher = jobEventPublisher;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
    }

    @Override
    @Transactional
    public Long addJob(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameRecords), "作业名称已存在");
        dto.setStatus(UsingStatusEnum.ONLINE.val);
        dto.setOperator(operator);

        JobInfo info = dto.toModel();
        Long jobId = jobInfoRepo.saveJobInfo(info);
        // 发布job创建事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.CREATED, operator);
        jobEventPublisher.whenCreated(eventLog);

        devTreeNodeLocalCache.invalidate(dto.getJobType().belong());
        return jobId;
    }

    @Override
    @Transactional
    public Boolean editJobInfo(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        JobInfo oldJobInfo = tryFetchJobInfo(dto.getId());

        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        if (ObjectUtils.isNotEmpty(dupNameRecords)) {
            checkArgument(Objects.equals(dto.getId(), dupNameRecords.get(0).getId()), "作业名称已存在");
        }

        dto.setStatus(null);
        dto.resetEditor(operator);
        JobInfo newJobInfo = dto.toModel();
        Boolean ret = jobInfoRepo.updateJobInfo(newJobInfo);
        if (BooleanUtils.isTrue(ret)) {
            if (checkJobInfoUpdated(newJobInfo, oldJobInfo)) {
                // 保存后发布job更新事件
                JobEventLog eventLog = jobManager.logEvent(newJobInfo.getId(), EventTypeEnum.UPDATED, operator);
                jobEventPublisher.whenUpdated(eventLog);
            }
        }
        devTreeNodeLocalCache.invalidate(dto.getJobType().belong());
        return ret;
    }

    @Override
    public JobInfoDto getJobInfo(Long id) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        return JobInfoDto.from(jobInfo);
    }

    @Override
    public JobDetailsDto getJobDetails(Long jobId, Integer version, Boolean isDryRun) {
        return null;
    }

    @Override
    @Transactional
    public Boolean removeJob(Long id, Operator operator) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        List<JobExecuteConfig> executeConfigs = jobExecuteConfigRepo.queryList(id, new JobExecuteConfigCondition());
        // 检查是否已停用，只有停用后才能更改
        checkArgument(!isRunning(executeConfigs), "先在所有环境下暂停作业，再删除作业");

        List<JobDependence> postJobs = jobDependenceRepo.queryPostJob(id);
        checkArgument(ObjectUtils.isEmpty(postJobs), "先删除所有环境下的作业依赖关系，再删除作业");

        // 发布job删除事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.DELETED, operator);
        jobEventPublisher.whenDeleted(eventLog);

        Boolean ret = jobInfoRepo.deleteJobAndSubInfo(jobInfo, operator.getNickname());
        JobTypeEnum.getEnum(jobInfo.getJobType()).ifPresent(jobTypeEnum -> devTreeNodeLocalCache.invalidate(jobTypeEnum.belong()));
        return ret;
    }

    @Override
    @Transactional
    public Boolean resumeJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能恢复运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.equals(RunningStateEnum.pause.val, executeConfig.getRunningState()), "作业在%s环境已运行，勿重复操作", environment);
        //作业未发布，不能恢复运行
        checkState(isJobPublished(id, environment), "作业未发布，不能恢复");

        jobExecuteConfigRepo.switchRunningState(executeConfig.getId(), RunningStateEnum.resume, operator.getNickname());
        // 发布job恢复事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.JOB_RESUME, environment, operator);
        jobEventPublisher.whenResumed(eventLog);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean pauseJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能暂停运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.equals(RunningStateEnum.resume.val, executeConfig.getRunningState()), "作业在%s环境已暂停，勿重复操作", environment);

        jobExecuteConfigRepo.switchRunningState(executeConfig.getId(), RunningStateEnum.pause, operator.getNickname());
        // 发布job暂停事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.JOB_PAUSE, environment, operator);
        jobEventPublisher.whenPaused(eventLog);
        return Boolean.TRUE;
    }

    @Override
    public Boolean runJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.nonNull(executeConfig.getSchDagId()), "作业在%s环境未关联DAG，请先关联DAG", environment);
        //作业未发布，不能恢复运行
        checkState(isJobPublished(id, environment), "作业未发布，不能运行");
        checkState(Objects.equals(RunningStateEnum.resume.val, executeConfig.getRunningState()), "作业在%s环境已暂停，请先恢复", environment);
        // dag 必须上线
        checkState(isDAGOnline(executeConfig.getSchDagId()), "作业关联DAG未上线，请先上线DAG");

        // 发布job暂停事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.JOB_RUN, environment, operator);
        jobEventPublisher.whenToRun(eventLog);
        return Boolean.TRUE;
    }

    // TODO 进勇脚本支持
    @Override
    public JobDryRunDto dryRunJob(Long jobId, Integer version) {
        return null;
    }

    private JobInfo tryFetchJobInfo(Long id) {
        checkArgument(Objects.nonNull(id), "作业编号不存在");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(id);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        return jobInfoOptional.get();
    }

    private void checkJobInfo(JobInfoDto dto) {
        checkArgument(StringUtils.isNotBlank(dto.getName()), "作业名为空");
        checkArgument(StringUtils.isNotBlank(dto.getDwLayerCode()), "作业分层为空");
        checkArgument(Objects.nonNull(dto.getFolderId()), "作业所属文件夹为空");
        checkArgument(Objects.nonNull(dto.getJobType()), "作业类型为空");
        checkArgument(JobTypeEnum.getEnum(dto.getJobType().toString()).isPresent(), "作业类型有误");
    }

    private boolean isJobPublished(Long jobId, String environment) {
        JobPublishRecordCondition condition = new JobPublishRecordCondition();
        condition.setJobId(jobId);
        condition.setEnvironment(environment);
        condition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        return jobPublishRecordRepo.count(condition) > 0;
    }

    private boolean isDAGOnline(Long dagId) {
        Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(dagId);
        if (dagInfoOptional.isEmpty()) return false;
        return Objects.equals(dagInfoOptional.get().getStatus(), UsingStatusEnum.ONLINE.val);
    }

    private boolean checkJobInfoUpdated(JobInfo newJobInfo, JobInfo oldJobInfo) {
        return !Objects.equals(newJobInfo.getName(), oldJobInfo.getName());
    }

    private boolean isRunning(JobExecuteConfig config) {
        return Objects.equals(RunningStateEnum.resume.val, config.getRunningState());
    }

    private boolean isRunning(List<JobExecuteConfig> executeConfigs) {
        if (CollectionUtils.isEmpty(executeConfigs)) return false;

        for (JobExecuteConfig config : executeConfigs) {
            if (isRunning(config)) return true;
        }
        return false;
    }
}
