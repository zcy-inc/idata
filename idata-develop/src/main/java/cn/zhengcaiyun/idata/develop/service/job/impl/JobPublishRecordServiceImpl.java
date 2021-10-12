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
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.Constants;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobPublishRecordDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobPublishManager jobPublishManager;
    private final EnumService enumService;

    @Autowired
    public JobPublishRecordServiceImpl(JobPublishRecordRepo jobPublishRecordRepo,
                                       JobPublishManager jobPublishManager,
                                       EnumService enumService) {
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobPublishManager = jobPublishManager;
        this.enumService = enumService;
    }

    @Override
    public Page<JobPublishRecordDto> paging(JobPublishRecordCondition condition, PageParam pageParam) {
        Page<JobPublishRecord> page = jobPublishRecordRepo.paging(condition, pageParam);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }

        // 获取数仓分层
        Map<String, String> dwLayerMap = getDwLayer();
        List<JobPublishRecordDto> dtoList = page.getContent().stream()
                .map(record -> {
                    JobPublishRecordDto dto = JobPublishRecordDto.from(record);
                    dto.setDwLayerValue(dwLayerMap.get(dto.getDwLayerCode()));
                    return dto;
                }).collect(Collectors.toList());
        return Page.newOne(dtoList, page.getTotal());
    }

    @Override
    public Boolean approve(Long id, String remark, Operator operator) {
        checkArgument(jobPublishManager.hasPublishPermission(operator), "没有审批权限");
        checkArgument(Objects.nonNull(id), "记录编号为空");
        Optional<JobPublishRecord> optional = jobPublishRecordRepo.query(id);
        checkArgument(optional.isPresent(), "编号" + id + "记录不存在或已删除");
        JobPublishRecord record = optional.get();
        checkState(record.getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val), "作业" + record.getJobId() + "为非待发布状态");

        record.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        record.setApproveOperator(operator.getNickname());
        record.setApproveRemark(remark);
        record.setApproveTime(new Date());
        jobPublishManager.publish(record, operator);
        return Boolean.TRUE;
    }

    @Override
    public Boolean approve(List<Long> ids, String remark, Operator operator) {
        checkArgument(jobPublishManager.hasPublishPermission(operator), "没有审批权限");
        checkArgument(ObjectUtils.isNotEmpty(ids), "记录编号为空");
        ids.stream().forEach(id -> approve(id, remark, operator));
        return Boolean.TRUE;
    }

    @Override
    public Boolean reject(Long id, String remark, Operator operator) {
        checkArgument(jobPublishManager.hasPublishPermission(operator), "没有审批权限");
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
        publishRecord.setApproveRemark(remark);
        publishRecord.setApproveTime(new Date());
        jobPublishRecordRepo.update(publishRecord);
        return Boolean.TRUE;
    }

    @Override
    public Boolean reject(List<Long> ids, String remark, Operator operator) {
        checkArgument(jobPublishManager.hasPublishPermission(operator), "没有审批权限");
        checkArgument(ObjectUtils.isNotEmpty(ids), "记录编号为空");
        ids.stream().forEach(id -> reject(id, remark, operator));
        return Boolean.TRUE;
    }

    private Map<String, String> getDwLayer() {
        List<EnumValueDto> enumValueDtoList = enumService.getEnumValues(Constants.DW_LAYER_ENUM_CODE);
        if (ObjectUtils.isEmpty(enumValueDtoList)) return Maps.newHashMap();

        return enumValueDtoList.stream()
                .collect(Collectors.toMap(EnumValueDto::getValueCode, EnumValueDto::getEnumValue));
    }
}
