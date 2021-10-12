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
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
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
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;

    @Autowired
    public JobInfoServiceImpl(JobInfoRepo jobInfoRepo, DevTreeNodeLocalCache devTreeNodeLocalCache) {
        this.jobInfoRepo = jobInfoRepo;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
    }

    @Override
    public Long addJobInfo(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameRecords), "作业名称已存在");

        dto.setStatus(UsingStatusEnum.ENABLE.val);
        dto.setOperator(operator);

        JobInfo info = dto.toModel();
        Long jobId = jobInfoRepo.saveJobInfo(info);
        devTreeNodeLocalCache.invalidate(dto.getJobType().belong());
        return jobId;
    }

    @Override
    public Boolean editJobInfo(JobInfoDto dto, Operator operator) {
        checkJobInfo(dto);
        tryFetchJobInfo(dto.getId());
        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        if (ObjectUtils.isNotEmpty(dupNameRecords)) {
            checkArgument(Objects.equals(dto.getId(), dupNameRecords.get(0).getId()), "作业名称已存在");
        }

        dto.setStatus(null);
        dto.resetEditor(operator);

        Boolean ret = jobInfoRepo.updateJobInfo(dto.toModel());
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
        //todo 更新后通知 ds
        // todo 删除作业其他信息

        Boolean ret = jobInfoRepo.deleteJobInfo(jobInfo.getId(), operator.getNickname());

        JobTypeEnum.getEnum(jobInfo.getJobType()).ifPresent(jobTypeEnum -> devTreeNodeLocalCache.invalidate(jobTypeEnum.belong()));
        return ret;
    }

    @Override
    public Boolean enableJobInfo(Long id, Operator operator) {
        tryFetchJobInfo(id);

        JobInfo jobInfo = new JobInfo();
        jobInfo.setId(id);
        jobInfo.setStatus(UsingStatusEnum.ENABLE.val);
        jobInfo.setEditor(operator.getNickname());
        return jobInfoRepo.updateJobInfo(jobInfo);
    }

    @Override
    public Boolean disableJobInfo(Long id, Operator operator) {
        tryFetchJobInfo(id);

        JobInfo jobInfo = new JobInfo();
        jobInfo.setId(id);
        jobInfo.setStatus(UsingStatusEnum.DISABLE.val);
        jobInfo.setEditor(operator.getNickname());
        return jobInfoRepo.updateJobInfo(jobInfo);
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
