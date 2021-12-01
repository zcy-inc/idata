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

package cn.zhengcaiyun.idata.develop.event.dag.publisher;

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGEventLog;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGEventLogRepo;
import cn.zhengcaiyun.idata.develop.event.dag.*;
import cn.zhengcaiyun.idata.develop.event.dag.bus.DagEventBus;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 15:42
 **/
@Component
public class DagEventPublisher {

    private final DAGEventLogRepo dagEventLogRepo;

    @Autowired
    public DagEventPublisher(DAGEventLogRepo dagEventLogRepo) {
        this.dagEventLogRepo = dagEventLogRepo;
    }

    public void whenCreated(DAGEventLog eventLog) {
        if (!EventTypeEnum.CREATED.name().equals(eventLog.getDagEvent())) return;

        DagCreatedEvent event = new DagCreatedEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    public void whenUpdated(DAGEventLog eventLog) {
        if (!EventTypeEnum.UPDATED.name().equals(eventLog.getDagEvent())) return;

        DagUpdatedEvent event = new DagUpdatedEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    public void whenDeleted(DAGEventLog eventLog) {
        if (!EventTypeEnum.DELETED.name().equals(eventLog.getDagEvent())) return;

        DagDeletedEvent event = new DagDeletedEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        JsonObject dagJson = new Gson().fromJson(eventLog.getEventInfo(), JsonObject.class);
        event.setEnvironment(dagJson.get("environment").getAsString());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    public void whenToOffline(DAGEventLog eventLog) {
        if (!EventTypeEnum.DAG_OFFLINE.name().equals(eventLog.getDagEvent())) return;

        DagOfflineEvent event = new DagOfflineEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    public void whenToOnline(DAGEventLog eventLog) {
        if (!EventTypeEnum.DAG_ONLINE.name().equals(eventLog.getDagEvent())) return;

        DagOnlineEvent event = new DagOnlineEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    public void whenToRun(DAGEventLog eventLog) {
        if (!EventTypeEnum.DAG_RUN.name().equals(eventLog.getDagEvent())) return;
        DagRunEvent event = new DagRunEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    public void whenScheduleUpdated(DAGEventLog eventLog) {
        if (!EventTypeEnum.DAG_SCHEDULE_UPDATED.name().equals(eventLog.getDagEvent())) return;

        DagScheduleUpdatedEvent event = new DagScheduleUpdatedEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
        return;
    }

    @Deprecated
    public void whenAddDependence(DAGEventLog eventLog, List<Long> dependenceIds) {
        if (!EventTypeEnum.DAG_ADD_DEPENDENCE.name().equals(eventLog.getDagEvent())) return;

        DagAddDependenceEvent event = new DagAddDependenceEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        event.setDependDagIds(dependenceIds);
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    @Deprecated
    public void whenRemoveDependence(DAGEventLog eventLog, List<Long> dependenceIds) {
        if (!EventTypeEnum.DAG_DEL_DEPENDENCE.name().equals(eventLog.getDagEvent())) return;

        DagRemoveDependenceEvent event = new DagRemoveDependenceEvent();
        event.setDagId(eventLog.getDagId());
        event.setEventId(eventLog.getId());
        event.setDependDagIds(dependenceIds);
        // 发布事件
        DagEventBus.getInstance().post(event);
        // 检查事件处理结果
        processResult(event);
    }

    private void processResult(DagBaseEvent event) {
        DAGEventLog updateEventLog = new DAGEventLog();
        updateEventLog.setId(event.getEventId());
        if (event.hasFailed()) {
            // 处理失败
            updateEventLog.setHandleStatus(EventStatusEnum.FAIL.val);
            updateEventLog.setHandleMsg(Strings.nullToEmpty(event.fetchFailedMessage()));
            throw new ExternalIntegrationException(event.fetchFailedMessage());
        } else {
            // 处理成功，标记事件已处理
            updateEventLog.setHandleStatus(EventStatusEnum.SUCCESS.val);
        }
        dagEventLogRepo.update(updateEventLog);
    }
}
