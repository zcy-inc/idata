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
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobEventLog;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobEventLogRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 17:07
 **/
@Service
public class JobInfoServiceImpl implements JobInfoService {

    private final JobInfoRepo jobInfoRepo;
    private final JobEventLogRepo jobEventLogRepo;
    private final JobDependenceRepo jobDependenceRepo;
    private final JobEventPublisher jobEventPublisher;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;

    @Autowired
    public JobInfoServiceImpl(JobInfoRepo jobInfoRepo,
                              JobEventLogRepo jobEventLogRepo,
                              JobDependenceRepo jobDependenceRepo,
                              JobEventPublisher jobEventPublisher,
                              DevTreeNodeLocalCache devTreeNodeLocalCache) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobEventLogRepo = jobEventLogRepo;
        this.jobDependenceRepo = jobDependenceRepo;
        this.jobEventPublisher = jobEventPublisher;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
    }

    @Override
    public Long addJobInfo(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameRecords), "作业名称已存在");

        // 作业默认下线
        dto.setStatus(UsingStatusEnum.OFFLINE.val);
        dto.setOperator(operator);

        JobInfo info = dto.toModel();
        Long jobId = jobInfoRepo.saveJobInfo(info);
        // 保存后发布job创建事件
        JobEventLog eventLog = logEvent(jobId, EventTypeEnum.CREATED, operator);
        jobEventPublisher.whenCreated(eventLog);

        devTreeNodeLocalCache.invalidate(dto.getJobType().belong());
        return jobId;
    }

    private JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, Operator operator) {
        return logEvent(jobId, typeEnum, null, operator);
    }

    private JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, String eventInfo, Operator operator) {
        JobEventLog eventLog = new JobEventLog();
        eventLog.setJobId(jobId);
        eventLog.setJobEvent(typeEnum.name());
        eventLog.setHandleStatus(EventStatusEnum.PENDING.val);
        eventLog.setCreator(operator.getNickname());
        eventLog.setEditor(operator.getNickname());
        eventLog.setEventInfo(eventInfo);
        jobEventLogRepo.create(eventLog);
        return eventLog;
    }

    private boolean checkJobInfoUpdated(JobInfo newJobInfo, JobInfo oldJobInfo) {
        return !Objects.equals(newJobInfo.getName(), oldJobInfo.getName());
    }

    @Override
    public Boolean editJobInfo(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        JobInfo oldJobInfo = tryFetchJobInfo(dto.getId());
        // 检查是否已停用，只有停用后才能更改
        checkArgument(Objects.equals(oldJobInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "作业未停用，不能修改");

        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        if (ObjectUtils.isNotEmpty(dupNameRecords)) {
            checkArgument(Objects.equals(dto.getId(), dupNameRecords.get(0).getId()), "作业名称已存在");
        }

        dto.setStatus(null);
        dto.resetEditor(operator);
        JobInfo newJobInfo =dto.toModel();
        Boolean ret = jobInfoRepo.updateJobInfo(newJobInfo);
        if (BooleanUtils.isTrue(ret)){
            if(checkJobInfoUpdated(newJobInfo,oldJobInfo)){
                // 保存后发布job更新事件
                JobEventLog eventLog = logEvent(newJobInfo.getId(), EventTypeEnum.UPDATED, operator);
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
    public Boolean removeJobInfo(Long id, Operator operator) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        // 检查是否已停用，只有停用后才能更改
        checkArgument(Objects.equals(jobInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "作业未停用，不能删除");

        List<JobDependence> postJobs = jobDependenceRepo.queryPostJob(id);
        checkArgument(ObjectUtils.isEmpty(postJobs),"作业被其他作业依赖，不能删除");

        Boolean ret = jobInfoRepo.deleteJobAndSubInfo(jobInfo, operator.getNickname());
        if (BooleanUtils.isTrue(ret)){
            // 发布job删除事件
            JobEventLog eventLog = logEvent(id, EventTypeEnum.DELETED, operator);
            jobEventPublisher.whenDeleted(eventLog);
        }
        JobTypeEnum.getEnum(jobInfo.getJobType()).ifPresent(jobTypeEnum -> devTreeNodeLocalCache.invalidate(jobTypeEnum.belong()));
        return ret;
    }

    @Override
    public Boolean enableJobInfo(Long id, Operator operator) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        // 检查是否已停用，只有停用后才能更改
        checkArgument(Objects.equals(jobInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "作业已启用，请勿重复操作");

        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(id);
        updateJobInfo.setStatus(UsingStatusEnum.ONLINE.val);
        updateJobInfo.setEditor(operator.getNickname());
        Boolean ret =  jobInfoRepo.updateJobInfo(updateJobInfo);
        if (BooleanUtils.isTrue(ret)){
            // 发布job启用事件
            JobEventLog eventLog = logEvent(id, EventTypeEnum.JOB_ENABLE, operator);
            jobEventPublisher.whenEnabled(eventLog);
        }
        return ret;
    }

    @Override
    public Boolean disableJobInfo(Long id, Operator operator) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        // 检查是否已停用，只有停用后才能更改
        checkArgument(Objects.equals(jobInfo.getStatus(), UsingStatusEnum.ONLINE.val), "作业已停用，请勿重复操作");

        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(id);
        updateJobInfo.setStatus(UsingStatusEnum.OFFLINE.val);
        updateJobInfo.setEditor(operator.getNickname());
        Boolean ret =  jobInfoRepo.updateJobInfo(updateJobInfo);
        if (BooleanUtils.isTrue(ret)){
            // 发布job停用事件
            JobEventLog eventLog = logEvent(id, EventTypeEnum.JOB_DISABLE, operator);
            jobEventPublisher.whenDisabled(eventLog);
        }
        return ret;
    }

    @Override
    public Boolean runJob(Long id, Operator operator) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        // 检查是否已停用，只有停用后才能更改
        checkArgument(Objects.equals(jobInfo.getStatus(), UsingStatusEnum.ONLINE.val), "作业已停用，先启用再运行");

        // 发布job 运行事件
        JobEventLog eventLog = logEvent(id, EventTypeEnum.JOB_RUN, operator);
        jobEventPublisher.whenToRun(eventLog);
        return Boolean.TRUE;
    }

    private JobInfo tryFetchJobInfo(Long id) {
        checkArgument(Objects.nonNull(id), "作业编号不存在");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(id);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        return jobInfoOptional.get();
    }

    private void checkJobInfo(JobInfoDto dto) {
        checkArgument(StringUtils.isNotBlank(dto.getName()), "作业名为空");
        checkArgument(Objects.nonNull(dto.getJobType()), "作业类型为空");
        checkArgument(StringUtils.isNotBlank(dto.getDwLayerCode()), "作业分层为空");
        checkArgument(Objects.nonNull(dto.getFolderId()), "作业所属文件夹为空");
    }
}
