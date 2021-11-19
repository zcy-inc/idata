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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobEventLog;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobEventLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 18:04
 **/
@Component
public class JobManager {

    private final JobEventLogRepo jobEventLogRepo;

    @Autowired
    public JobManager(JobEventLogRepo jobEventLogRepo) {
        this.jobEventLogRepo = jobEventLogRepo;
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, Operator operator) {
        return logEvent(jobId, typeEnum, "", operator);
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, String environment, Operator operator) {
        return logEvent(jobId, typeEnum, environment, null, operator);
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, String environment, String eventInfo, Operator operator) {
        JobEventLog eventLog = new JobEventLog();
        eventLog.setJobId(jobId);
        eventLog.setJobEvent(typeEnum.name());
        eventLog.setEnvironment(environment);
        eventLog.setHandleStatus(EventStatusEnum.PENDING.val);
        eventLog.setCreator(operator.getNickname());
        eventLog.setEditor(operator.getNickname());
        eventLog.setEventInfo(eventInfo);
        jobEventLogRepo.create(eventLog);
        return eventLog;
    }

}
