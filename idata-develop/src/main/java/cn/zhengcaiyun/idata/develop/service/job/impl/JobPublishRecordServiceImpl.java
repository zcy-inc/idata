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
import cn.zhengcaiyun.idata.commons.enums.WhetherEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.Constants;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobPublishRecordDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.table.TableSibshipService;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-29 14:34
 **/
@Service
public class JobPublishRecordServiceImpl implements JobPublishRecordService {

    private final JobInfoRepo jobInfoRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobPublishManager jobPublishManager;
    private final EnumService enumService;
    private final JobContentCommonService jobContentCommonService;

    @Autowired
    private TableSibshipService tableSibshipService;

    @Autowired
    public JobPublishRecordServiceImpl(JobInfoRepo jobInfoRepo,
                                       JobPublishRecordRepo jobPublishRecordRepo,
                                       JobPublishManager jobPublishManager,
                                       EnumService enumService,
                                       JobContentCommonService jobContentCommonService) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobPublishManager = jobPublishManager;
        this.enumService = enumService;
        this.jobContentCommonService = jobContentCommonService;
    }

    @Override
    public Page<JobPublishRecordDto> paging(JobPublishRecordCondition condition, PageParam pageParam) {
        Page<JobPublishRecord> page = jobPublishRecordRepo.paging(condition, pageParam);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }

        Map<Long, JobInfo> jobInfoMap = getJobInfo(page.getContent());
        Map<Long, Map<Integer, String>> jobVersionMap = getJobVersionDisplay(page.getContent());
        // 获取数仓分层
        Map<String, String> dwLayerMap = getDwLayer();
        List<JobPublishRecordDto> dtoList = page.getContent().stream()
                .map(record -> {
                    JobPublishRecordDto dto = JobPublishRecordDto.from(record);
                    dto.setDwLayerValue(dwLayerMap.get(dto.getDwLayerCode()));
                    JobInfo jobInfo = jobInfoMap.get(record.getJobId());
                    dto.setJobName(jobInfo == null ? "" : jobInfo.getName());

                    Map<Integer, String> versionMap = jobVersionMap.get(record.getJobId());
                    String versionDisplay = null;
                    if (!CollectionUtils.isEmpty(versionMap)) {
                        versionDisplay = versionMap.get(record.getJobContentVersion());
                    }
                    dto.setJobContentVersionDisplay(Strings.nullToEmpty(versionDisplay));
                    return dto;
                }).collect(Collectors.toList());
        return Page.newOne(dtoList, page.getTotal());
    }

    @Override
    public Boolean approve(Long id, String remark, Operator operator) {
        checkArgument(Objects.nonNull(id), "记录编号为空");
        Optional<JobPublishRecord> optional = jobPublishRecordRepo.query(id);
        checkArgument(optional.isPresent(), "编号%s记录不存在或已删除", id);
        JobPublishRecord record = optional.get();
        checkState(record.getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val), "作业" + record.getJobId() + "为非待发布状态");

        record.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        record.setApproveOperator(operator.getNickname());
        record.setApproveRemark(Strings.nullToEmpty(remark));
        record.setApproveTime(new Date());
        jobPublishManager.publish(record, operator);

        // 解析数据血缘
        tableSibshipService.sibParse(record.getJobId(),record.getJobContentVersion(),record.getJobTypeCode(),record.getEnvironment(),operator.getNickname());
        return Boolean.TRUE;
    }

    @Override
    public Boolean approve(List<Long> ids, String remark, Operator operator) {
        checkArgument(ObjectUtils.isNotEmpty(ids), "记录编号为空");
        ids.stream().forEach(id -> approve(id, remark, operator));
        return Boolean.TRUE;
    }

    @Override
    public Boolean reject(Long id, String remark, Operator operator) {
        checkArgument(Objects.nonNull(id), "记录编号为空");
        Optional<JobPublishRecord> optional = jobPublishRecordRepo.query(id);
        checkArgument(optional.isPresent(), "编号" + id + "记录不存在或已删除");
        JobPublishRecord existRecord = optional.get();
        checkState(existRecord.getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val), "作业" + existRecord.getJobId() + "为非待发布状态");
        // 驳回直接更新状态
        JobPublishRecord publishRecord = new JobPublishRecord();
        publishRecord.setId(existRecord.getId());
        publishRecord.setPublishStatus(PublishStatusEnum.REJECTED.val);
        publishRecord.setApproveOperator(operator.getNickname());
        publishRecord.setApproveRemark(Strings.nullToEmpty(remark));
        publishRecord.setApproveTime(new Date());
        jobPublishRecordRepo.update(publishRecord);
        return Boolean.TRUE;
    }

    @Override
    public Boolean reject(List<Long> ids, String remark, Operator operator) {
        checkArgument(ObjectUtils.isNotEmpty(ids), "记录编号为空");
        ids.stream().forEach(id -> reject(id, remark, operator));
        return Boolean.TRUE;
    }

    @Override
    public List<JobPublishRecord> findJobs(JobPublishRecordCondition condition) {
        return jobPublishRecordRepo.queryList(condition);
    }

    @Override
    public boolean delete(Long id) {
        JobPublishRecord jobPublishRecord = jobPublishRecordRepo.query(id).get();
        checkArgument(jobPublishRecord.getPublishStatus() != PublishStatusEnum.PUBLISHED.val, "禁止删除已发布版本");
        jobPublishRecord.setDel(WhetherEnum.YES.val);
        return jobPublishRecordRepo.update(jobPublishRecord);
    }

    private Map<String, String> getDwLayer() {
        List<EnumValueDto> enumValueDtoList = enumService.getEnumValues(Constants.DW_LAYER_ENUM_CODE);
        if (ObjectUtils.isEmpty(enumValueDtoList)) return Maps.newHashMap();

        return enumValueDtoList.stream()
                .collect(Collectors.toMap(EnumValueDto::getValueCode, EnumValueDto::getEnumValue));
    }

    private Map<Long, JobInfo> getJobInfo(List<JobPublishRecord> recordList) {
        List<Long> jobIds = recordList.stream()
                .map(JobPublishRecord::getJobId)
                .collect(Collectors.toList());
        if (ObjectUtils.isEmpty(jobIds)) return Maps.newHashMap();

        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(jobIds);
        return jobInfoList.stream()
                .collect(Collectors.toMap(JobInfo::getId, Function.identity()));
    }

    private Map<Long, Map<Integer, String>> getJobVersionDisplay(List<JobPublishRecord> recordList) {
        List<Long> jobIdList = recordList.stream()
                .map(JobPublishRecord::getJobId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> jobIdAndTypeMap = jobInfoRepo.queryJobInfo(jobIdList).stream()
                .collect(Collectors.toMap(JobInfo::getId, JobInfo::getJobType));

        Map<Long, Map<Integer, String>> jobVersionMap = Maps.newHashMap();
        jobIdList.forEach(jobId -> {
            Map<Integer, String> versionMap = jobVersionMap.get(jobId);
            if (Objects.isNull(versionMap)) {
                versionMap = Maps.newHashMap();
                jobVersionMap.put(jobId, versionMap);
            }
            List<JobContentBaseDto> contentList = jobContentCommonService.getJobContents(jobId, jobIdAndTypeMap.get(jobId));
            if (ObjectUtils.isNotEmpty(contentList)) {
                for (JobContentBaseDto content : contentList) {
                    versionMap.put(content.getVersion(), JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                }
            }
        });
        return jobVersionMap;
    }
}
