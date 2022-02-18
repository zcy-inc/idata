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
import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.service.ModifyDIJobNameService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-20 11:00
 **/
@Service
public class ModifyDIJobNameServiceImpl implements ModifyDIJobNameService {
    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private DIJobContentRepo diJobContentRepo;
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MigrateResultDto> modify() {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        JobInfoCondition condition = new JobInfoCondition();
        condition.setJobType(JobTypeEnum.DI_BATCH);
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(condition);
        if (CollectionUtils.isEmpty(jobInfoList))
            return resultDtoList;

        for (JobInfo jobInfo : jobInfoList) {
            List<DIJobContent> contentList = diJobContentRepo.queryList(jobInfo.getId());
            String destTable = getDestTable(contentList);
            if (StringUtils.isBlank(destTable)) {
                resultDtoList.add(new MigrateResultDto("ModifyDIJobName", String.format("作业[%s]目标表名为空", jobInfo.getName()), null));
                continue;
            }
            if (!destTable.startsWith("ods.ods_")) {
                resultDtoList.add(new MigrateResultDto("ModifyDIJobName", String.format("作业[%s]目标表名未改，不修改作业名称", jobInfo.getName()), null));
                continue;
            }

            JobInfoDto jobInfoDto = JobInfoDto.from(jobInfo);
            jobInfoDto.setName(destTable);
            Operator operator = new Operator.Builder(0L).nickname(jobInfoDto.getEditor()).build();
            jobInfoService.pauseJob(jobInfo.getId(), EnvEnum.prod.name(), operator);
            try {
                jobInfoService.editJobInfo(jobInfoDto, operator);
            } catch (IllegalAccessException ex) {
                throw new BizProcessException("无权限修改作业", ex);
            }
            jobInfoService.resumeJob(jobInfo.getId(), EnvEnum.prod.name(), operator);
        }
        return resultDtoList;
    }

    private String getDestTable(List<DIJobContent> contentList) {
        if (CollectionUtils.isEmpty(contentList)) return null;

        DIJobContent content = contentList.get(0);
        return content.getDestTable();
    }
}
