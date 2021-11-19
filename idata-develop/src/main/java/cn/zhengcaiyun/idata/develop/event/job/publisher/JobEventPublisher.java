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

package cn.zhengcaiyun.idata.develop.event.job.publisher;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobEventLog;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobEventLogRepo;
import cn.zhengcaiyun.idata.develop.event.job.*;
import cn.zhengcaiyun.idata.develop.event.job.bus.JobEventBus;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:45
 **/
@Component
public class JobEventPublisher {

    private final JobEventLogRepo jobEventLogRepo;

    @Autowired
    public JobEventPublisher(JobEventLogRepo jobEventLogRepo) {
        this.jobEventLogRepo = jobEventLogRepo;
    }

    public void whenCreated(JobEventLog eventLog) {
        if (!EventTypeEnum.CREATED.name().equals(eventLog.getJobEvent()))
            return;

        JobCreatedEvent event = new JobCreatedEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);

    }

    public void whenUpdated(JobEventLog eventLog) {
        if (!EventTypeEnum.UPDATED.name().equals(eventLog.getJobEvent()))
            return;

        JobUpdatedEvent event = new JobUpdatedEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenDeleted(JobEventLog eventLog) {
        if (!EventTypeEnum.DELETED.name().equals(eventLog.getJobEvent()))
            return;

        JobDeletedEvent event = new JobDeletedEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenEnabled(JobEventLog eventLog) {
        if (!EventTypeEnum.JOB_ENABLE.name().equals(eventLog.getJobEvent()))
            return;

        JobEnabledEvent event = new JobEnabledEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenDisabled(JobEventLog eventLog) {
        if (!EventTypeEnum.JOB_DISABLE.name().equals(eventLog.getJobEvent()))
            return;

        JobDisabledEvent event = new JobDisabledEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenPublished(JobEventLog eventLog) {
        if (!EventTypeEnum.JOB_PUBLISH.name().equals(eventLog.getJobEvent()))
            return;

        JobPublishedEvent event = new JobPublishedEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenBindDag(JobEventLog eventLog, Boolean isFirst) {
        if (!EventTypeEnum.JOB_BIND_DAG.name().equals(eventLog.getJobEvent()))
            return;

        JobBindDagEvent event = new JobBindDagEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setFirstBind(isFirst);
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenUnBindDag(JobEventLog eventLog) {
        if (!EventTypeEnum.JOB_UNBIND_DAG.name().equals(eventLog.getJobEvent()))
            return;

        JobUnBindDagEvent event = new JobUnBindDagEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenToRun(JobEventLog eventLog) {
        if (!EventTypeEnum.JOB_RUN.name().equals(eventLog.getJobEvent()))
            return;

        JobRunEvent event = new JobRunEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenBuildPrevRelation(JobEventLog eventLog, List<DagJobPair> addingPrevRelations, List<DagJobPair> removingPrevRelations) {
        if (!EventTypeEnum.JOB_RUN.name().equals(eventLog.getJobEvent()))
            return;

        JobBuildPrevRelationEvent event = new JobBuildPrevRelationEvent();
        event.setJobId(event.getJobId());
        event.setEventId(eventLog.getId());
        event.setEnvironment(eventLog.getEnvironment());
        event.setAddingPrevRelations(addingPrevRelations);
        event.setRemovingPrevRelations(removingPrevRelations);
        // 发布事件
        JobEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    private void processResult(JobBaseEvent event) {
        JobEventLog updateEventLog = new JobEventLog();
        updateEventLog.setId(event.getEventId());
        if (event.hasFailed()) {
            // 处理失败
            updateEventLog.setHandleStatus(EventStatusEnum.FAIL.val);
            updateEventLog.setHandleMsg(Strings.nullToEmpty(event.fetchFailedMessage()));
            throw new GeneralException(event.fetchFailedMessage());
        } else {
            // 处理成功，标记事件已处理
            updateEventLog.setHandleStatus(EventStatusEnum.SUCCESS.val);
        }
        jobEventLogRepo.update(updateEventLog);
    }
}
