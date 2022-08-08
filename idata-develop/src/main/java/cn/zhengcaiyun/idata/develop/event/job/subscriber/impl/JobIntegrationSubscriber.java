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

package cn.zhengcaiyun.idata.develop.event.job.subscriber.impl;

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.constant.Constants;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.event.core.Subscriber;
import cn.zhengcaiyun.idata.develop.event.job.*;
import cn.zhengcaiyun.idata.develop.event.job.bus.JobEventBus;
import cn.zhengcaiyun.idata.develop.event.job.subscriber.IJobEventSubscriber;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:57
 **/
@Component
@Subscriber(JobEventBus.EVENT_BUS_EXPRESSION)
public class JobIntegrationSubscriber implements IJobEventSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobIntegrationSubscriber.class);

    private final IJobIntegrator jobIntegrator;
    private final JobInfoRepo jobInfoRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;

    @Autowired
    public JobIntegrationSubscriber(IJobIntegrator jobIntegrator,
                                    JobInfoRepo jobInfoRepo,
                                    JobExecuteConfigRepo jobExecuteConfigRepo) {
        this.jobIntegrator = jobIntegrator;
        this.jobInfoRepo = jobInfoRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
    }

    @Override
    @Subscribe
    public void onCreated(JobCreatedEvent event) {
        // 暂不实现
    }

    @Override
    @Subscribe
    public void onUpdated(JobUpdatedEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            List<JobExecuteConfig> executeConfigList = jobExecuteConfigRepo.queryList(event.getJobId(), new JobExecuteConfigCondition());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }
            if (CollectionUtils.isEmpty(executeConfigList)) {
                LOGGER.warn("Job: {} has no execute config, just update it.", event.getJobId());
                return;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            for (JobExecuteConfig executeConfig : executeConfigList) {
                jobIntegrator.update(jobInfo, executeConfig, executeConfig.getEnvironment());
            }
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onUpdated failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onUpdated failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onScheduleUpdated(JobScheduleUpdatedEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(event.getJobId(), event.getEnvironment());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }
            if (executeConfigOptional.isEmpty()) {
                event.processFailed("作业调度配置不存在");
                return;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            JobExecuteConfig executeConfig = executeConfigOptional.get();
            jobIntegrator.update(jobInfo, executeConfig, executeConfig.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onScheduleUpdated failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onScheduleUpdated failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onDeleted(JobDeletedEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            List<JobExecuteConfig> executeConfigList = jobExecuteConfigRepo.queryList(event.getJobId(), new JobExecuteConfigCondition());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }
            if (CollectionUtils.isEmpty(executeConfigList)) {
                LOGGER.warn("Job: {} has no execute config, just delete it.", event.getJobId());
                return;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            for (JobExecuteConfig executeConfig : executeConfigList) {
                if (Constants.DEFAULT_DAG_ID == executeConfig.getSchDagId()) {
                    LOGGER.warn("Job: {} dag id is 0, do not need to delete it.", event.getJobId());
                    continue;
                }
                jobIntegrator.unBindDag(jobInfo, executeConfig.getSchDagId(), executeConfig.getEnvironment());
                jobIntegrator.delete(jobInfo, executeConfig.getEnvironment());
            }
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onDeleted failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onDeleted failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onEnabled(JobEnabledEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }

            JobInfo jobInfo = jobInfoOptional.get();
            jobIntegrator.enableRunning(jobInfo, event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onEnabled failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onEnabled failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onDisabled(JobDisabledEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }

            JobInfo jobInfo = jobInfoOptional.get();
            jobIntegrator.disableRunning(jobInfo, event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onDisabled failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onDisabled failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onPublished(JobPublishedEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }

            JobInfo jobInfo = jobInfoOptional.get();
            jobIntegrator.publish(jobInfo, event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onPublished failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onPublished failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onUnbindDag(JobUnBindDagEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }

            JobInfo jobInfo = jobInfoOptional.get();
            jobIntegrator.unBindDag(jobInfo, event.getUnbindDagId(), event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onUnbindDag failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onUnbindDag failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void onBindDag(JobBindDagEvent event) {
        try {
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(event.getJobId(), event.getEnvironment());
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }
            if (executeConfigOptional.isEmpty()) {
                event.processFailed("作业调度配置不存在");
                return;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            JobExecuteConfig executeConfig = executeConfigOptional.get();
            if (BooleanUtils.isTrue(event.getFirstBind())) {
                // 先创建作业
                jobIntegrator.create(jobInfo, executeConfig, event.getEnvironment());
            }
            jobIntegrator.bindDag(jobInfo, event.getBindDagId(), event.getEnvironment());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.onBindDag failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.onBindDag failed. ex: {}.", ex);
        }
    }

    @Override
    @Subscribe
    public void buildJobRelation(JobBuildPrevRelationEvent event) {
        try {
            String environment = event.getEnvironment();
            Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(event.getJobId());
            Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(event.getJobId(), environment);
            if (jobInfoOptional.isEmpty()) {
                event.processFailed("作业不存在");
                return;
            }
            if (executeConfigOptional.isEmpty()) {
                event.processFailed("作业调度配置不存在");
                return;
            }
            JobInfo jobInfo = jobInfoOptional.get();
            JobExecuteConfig executeConfig = executeConfigOptional.get();

            // 查询前置作业状态，用于设置跨DAG依赖创建dependent节点时指定相同状态
            List<DagJobPair> addingPrevRelations = event.getAddingPrevRelations();
            if (!CollectionUtils.isEmpty(addingPrevRelations)) {
                for (DagJobPair dagJobPair : addingPrevRelations) {
                    if (executeConfig.getSchDagId().equals(dagJobPair.getDagId())) {
                        continue;
                    }

                    List<Long> prevJobIds = dagJobPair.getJobIds();
                    if (!CollectionUtils.isEmpty(prevJobIds)) {
                        Map<Long, RunningStateEnum> jobStateMap = Maps.newHashMap();
                        for (Long prevJobId : prevJobIds) {
                            Optional<JobExecuteConfig> prevJobExecCfgOptional = jobExecuteConfigRepo.query(prevJobId, environment);
                            if (prevJobExecCfgOptional.isPresent()) {
                                Optional<RunningStateEnum> stateEnumOptional = RunningStateEnum.getEnum(prevJobExecCfgOptional.get().getRunningState());
                                RunningStateEnum stateEnum = stateEnumOptional.isPresent() ? stateEnumOptional.get() : RunningStateEnum.resume;
                                jobStateMap.put(prevJobId, stateEnum);
                            }
                        }
                        dagJobPair.setJobStateMap(jobStateMap);
                    }
                }
            }

            jobIntegrator.buildJobRelation(jobInfo, executeConfig, event.getEnvironment(),
                    event.getAddingPrevRelations(), event.getRemovingPrevRelations());
        } catch (ExternalIntegrationException iex) {
            event.processFailed(iex.getMessage());
            LOGGER.warn("JobIntegrationSubscriber.buildJobRelation failed. ex: {}.", iex);
        } catch (Exception ex) {
            event.processFailed("同步DS失败，请稍后重试");
            LOGGER.warn("JobIntegrationSubscriber.buildJobRelation failed. ex: {}.", ex);
        }
    }

}
