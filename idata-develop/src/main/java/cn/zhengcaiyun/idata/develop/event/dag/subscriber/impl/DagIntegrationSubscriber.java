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

package cn.zhengcaiyun.idata.develop.event.dag.subscriber.impl;

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.event.core.Subscriber;
import cn.zhengcaiyun.idata.develop.event.dag.*;
import cn.zhengcaiyun.idata.develop.event.dag.bus.DagEventBus;
import cn.zhengcaiyun.idata.develop.event.dag.subscriber.IDagEventSubscriber;
import cn.zhengcaiyun.idata.develop.integration.schedule.IDagIntegrator;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:56
 **/
@Component
//@Subscriber(DagEventBus.EVENT_BUS_EXPRESSION)
public class DagIntegrationSubscriber implements IDagEventSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(DagIntegrationSubscriber.class);

    private final DAGRepo dagRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final IDagIntegrator dagIntegrator;

    @Autowired
    public DagIntegrationSubscriber(DAGRepo dagRepo,
                                    JobExecuteConfigRepo jobExecuteConfigRepo,
                                    IDagIntegrator dagIntegrator) {
        this.dagRepo = dagRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.dagIntegrator = dagIntegrator;
    }

    @Override
    @Subscribe
    public void onCreated(DagCreatedEvent event) {
        try {
            Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(event.getDagId());
            Optional<DAGSchedule> scheduleOptional = dagRepo.queryDAGSchedule(event.getDagId());
            if (dagInfoOptional.isEmpty() || scheduleOptional.isEmpty()) {
                event.processFailed("DAG不存在");
                return;
            }
            DAGInfo dagInfo = dagInfoOptional.get();
            DAGSchedule dagSchedule = scheduleOptional.get();
            dagIntegrator.create(dagInfo, dagSchedule);
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onCreated failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onCreated failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onUpdated(DagUpdatedEvent event) {
        try {
            Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(event.getDagId());
            Optional<DAGSchedule> scheduleOptional = dagRepo.queryDAGSchedule(event.getDagId());
            if (dagInfoOptional.isEmpty() || scheduleOptional.isEmpty()) {
                event.processFailed("DAG不存在");
                return;
            }
            DAGInfo dagInfo = dagInfoOptional.get();
            DAGSchedule dagSchedule = scheduleOptional.get();
            dagIntegrator.update(dagInfo, dagSchedule);
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onUpdated failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onUpdated failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onDeleted(DagDeletedEvent event) {
        try {
            dagIntegrator.delete(event.getDagId(), event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onDeleted failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onDeleted failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onOnline(DagOnlineEvent event) {
        try {
            Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(event.getDagId());
            if (dagInfoOptional.isEmpty()) {
                event.processFailed("DAG不存在");
                return;
            }
            DAGInfo dagInfo = dagInfoOptional.get();
            dagIntegrator.online(dagInfo);
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onOnline failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onOnline failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onOffline(DagOfflineEvent event) {
        try {
            Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(event.getDagId());
            if (dagInfoOptional.isEmpty()) {
                event.processFailed("DAG不存在");
                return;
            }
            DAGInfo dagInfo = dagInfoOptional.get();
            dagIntegrator.offline(dagInfo);
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onOffline failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onOffline failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onScheduleUpdated(DagScheduleUpdatedEvent event) {
        try {
            Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(event.getDagId());
            Optional<DAGSchedule> scheduleOptional = dagRepo.queryDAGSchedule(event.getDagId());
            if (dagInfoOptional.isEmpty() || scheduleOptional.isEmpty()) {
                event.processFailed("DAG不存在");
                return;
            }
            DAGInfo dagInfo = dagInfoOptional.get();
            DAGSchedule dagSchedule = scheduleOptional.get();
            dagIntegrator.updateSchedule(dagInfo, dagSchedule);
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("DagIntegrationSubscriber.onScheduleUpdated failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("DagIntegrationSubscriber.onScheduleUpdated failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onRun(DagRunEvent event) {

    }

    @Override
    @Subscribe
    @Deprecated
    public void addDependence(DagAddDependenceEvent event) {

    }

    @Override
    @Subscribe
    @Deprecated
    public void removeDependence(DagRemoveDependenceEvent event) {

    }

    private List<Long> getDagJobs(DAGInfo dagInfo) {
        JobExecuteConfigCondition condition = new JobExecuteConfigCondition();
        condition.setEnvironment(condition.getEnvironment());
        List<JobExecuteConfig> jobExecuteConfigs = jobExecuteConfigRepo.queryDagJobList(dagInfo.getId(), condition);
        if (ObjectUtils.isEmpty(jobExecuteConfigs)) {
            return Lists.newArrayList();
        }
        return jobExecuteConfigs.stream()
                .filter(this::isDagLevelDependence)
                .map(JobExecuteConfig::getJobId)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isDagLevelDependence(JobExecuteConfig config) {
        // 默认根据任务优先级判断，最高优先级的任务单独创建依赖
        return true;
    }
}
