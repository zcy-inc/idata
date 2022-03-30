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

package cn.zhengcaiyun.idata.develop.spi.tree.impl;

import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:29
 **/
@Component
public class DevJobTreeNodeSupplier implements BizTreeNodeSupplier<JobInfo> {

    @Autowired
    private JobInfoRepo jobInfoRepo;

    @PostConstruct
    public void register() {
        BizTreeNodeSupplierFactory.register(FunctionModuleEnum.DEV_JOB, this);
    }

    @Override
    public List<DevTreeNodeDto> supply(FunctionModuleEnum moduleEnum) {
        JobInfoCondition condition = new JobInfoCondition();
        condition.setJobTypeCodes(Lists.newArrayList(JobTypeEnum.SQL_SPARK.getCode(), JobTypeEnum.SPARK_PYTHON.getCode(),
                JobTypeEnum.SPARK_JAR.getCode(), JobTypeEnum.SCRIPT_PYTHON.getCode(), JobTypeEnum.SCRIPT_SHELL.getCode(),
                JobTypeEnum.KYLIN.getCode(), JobTypeEnum.SQL_FLINK.getCode(), JobTypeEnum.SQL_DORIS.getCode()));
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(condition);
        return jobInfoList.stream()
                .map(jobInfo -> assemble(moduleEnum, jobInfo))
                .collect(Collectors.toList());
    }

    @Override
    public Long countBizNode(FunctionModuleEnum moduleEnum, Long folderId) {
        JobInfoCondition condition = new JobInfoCondition();
        condition.setFolderId(folderId);
        return jobInfoRepo.count(condition);
    }

    @Override
    public DevTreeNodeDto assemble(FunctionModuleEnum moduleEnum, JobInfo bizRecord) {
        DevTreeNodeDto dto = new DevTreeNodeDto();
        dto.setId(bizRecord.getId());
        dto.setName(bizRecord.getName());
        dto.setParentId(bizRecord.getFolderId());
        dto.setType(TreeNodeTypeEnum.RECORD.name());
        dto.setBelong(moduleEnum.code);
        return dto;
    }
}
