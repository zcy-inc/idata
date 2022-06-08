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

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caizhedong
 * @date 2022-06-08 上午10:22
 */

@Component
public class TableScheduleManager {

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private JobPublishRecordService jobPublishRecordService;
    @Autowired
    private DIJobContentRepo diJobContentRepo;
    @Autowired
    private DataSourceService dataSourceService;

    public List<TableInfoDto> syncTableColumnsSecurity() {
        // 获取DI作业同步表及字段（表名、字段名、字段类型）（暂不通过hdfs获取）
        JobPublishRecordCondition publishCondition = new JobPublishRecordCondition();
        publishCondition.setEnvironment(EnvEnum.prod.name());
        publishCondition.setJobTypeCode(JobTypeEnum.DI_BATCH.getCode());
        publishCondition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        List<JobPublishRecord> diJobPublishList = jobPublishRecordService.findJobs(publishCondition);
        // 获取所有DI线上作业
        Map<Long, Integer> jobIdVersionMap = diJobPublishList.stream()
                .collect(Collectors.toMap(JobPublishRecord::getJobId, JobPublishRecord::getJobContentVersion));
        Set<Long> diJobContentIds = diJobPublishList.stream().map(JobPublishRecord::getJobContentId).collect(Collectors.toSet());
        List<DIJobContent> diJobList = diJobContentRepo.queryList(new ArrayList<>(diJobContentIds));
        List<DIJobContent> diContentPublishList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : jobIdVersionMap.entrySet()) {
            DIJobContent diJobContentRecord = diJobList.stream().filter(c ->
                    entry.getKey().equals(c.getJobId()) && entry.getValue().equals(c.getVersion())).findFirst()
                    .orElse(null);
            diContentPublishList.add(diJobContentRecord);
        }
        // 获取所有ods表名
//        List<>

        // 获取字段安全等级信息，依赖DI作业同步表名
        // 获取数仓设计ods层所有表
        // 与需要同步的表比较，获取新增表list、修改表list（比较字段数量变化、字段名变化、安全等级变化）
        return null;
    }
}
