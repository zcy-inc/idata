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
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.*;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobOutputRepo;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.manager.JobConfigCombinationManager;
import cn.zhengcaiyun.idata.develop.manager.JobManager;
import cn.zhengcaiyun.idata.develop.service.job.JobExecuteConfigService;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-27 16:27
 **/
@Service
public class JobExecuteConfigServiceImpl implements JobExecuteConfigService {

    private final JobInfoRepo jobInfoRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobOutputRepo jobOutputRepo;
    private final JobDependenceRepo jobDependenceRepo;
    private final DAGRepo dagRepo;
    private final JobManager jobManager;
    private final JobConfigCombinationManager jobConfigCombinationManager;
    private final JobEventPublisher jobEventPublisher;
    private final DataSourceApi dataSourceApi;

    @Autowired
    public JobExecuteConfigServiceImpl(JobInfoRepo jobInfoRepo, JobExecuteConfigRepo jobExecuteConfigRepo, JobOutputRepo jobOutputRepo, JobDependenceRepo jobDependenceRepo, DAGRepo dagRepo, JobManager jobManager, JobConfigCombinationManager jobConfigCombinationManager, JobEventPublisher jobEventPublisher, DataSourceApi dataSourceApi) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobOutputRepo = jobOutputRepo;
        this.jobDependenceRepo = jobDependenceRepo;
        this.dagRepo = dagRepo;
        this.jobManager = jobManager;
        this.jobConfigCombinationManager = jobConfigCombinationManager;
        this.jobEventPublisher = jobEventPublisher;
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    @Transactional
    public JobConfigCombinationDto save(Long jobId, String environment, JobConfigCombinationDto dto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号不存在");
        checkArgument(Objects.nonNull(dto), "作业配置为空");
        checkArgument(StringUtils.isNotBlank(environment), "环境参数为空");
        JobExecuteConfigDto executeConfigDto = dto.getExecuteConfig();
        List<JobDependenceDto> dependenceDtoList = dto.getDependencies();
        JobOutputDto outputDto = dto.getOutput();
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        checkExecuteConfig(executeConfigDto);
        checkDependConfig(jobId, dependenceDtoList);
        checkOutputConfig(jobInfoOptional.get(), outputDto);

        Optional<JobConfigCombination> configCombinationOptional = jobConfigCombinationManager.getCombineConfig(jobId, environment);
        if (configCombinationOptional.isEmpty()) {
            // 新增配置
            addConfig(jobId, environment, executeConfigDto, dependenceDtoList, outputDto, operator);
        } else {
            JobConfigCombination configCombination = configCombinationOptional.get();
            checkState(Objects.equals(RunningStateEnum.pause.val, configCombination.getExecuteConfig().getRunningState()), "作业在%s环境未暂停，不能修改配置", environment);
            updateConfig(jobId, environment, configCombination, executeConfigDto, dependenceDtoList, outputDto, operator);
        }

        return getCombineConfig(jobId, environment);
    }

    @Override
    public JobConfigCombinationDto getCombineConfig(Long jobId, String environment) {
        checkArgument(Objects.nonNull(jobId), "作业编号不存在");
        checkArgument(StringUtils.isNotBlank(environment), "环境参数为空");

        Optional<JobConfigCombination> configCombinationOptional = jobConfigCombinationManager.getCombineConfig(jobId, environment);
        if (configCombinationOptional.isEmpty()) return null;

        JobConfigCombinationDto combinationDto = JobConfigCombinationDto.from(configCombinationOptional.get());
        combinationDto.setDependencies(fillDependenceInfo(combinationDto.getDependencies()));
        combinationDto.setOutput(fillJobOutputInfo(combinationDto.getOutput()));
        return combinationDto;
    }

    @Override
    public List<JobAndDagDto> getConfiguredJobList(String environment) {
        JobExecuteConfigCondition condition = new JobExecuteConfigCondition();
        condition.setEnvironment(environment);
        List<JobExecuteConfig> configList = jobExecuteConfigRepo.queryList(condition);
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(new JobInfoCondition());
        DAGInfoCondition dagCondition = new DAGInfoCondition();
        dagCondition.setEnvironment(environment);
        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo(dagCondition);
        return buildJobAndDag(configList, jobInfoList, dagInfoList);
    }

    private void updateConfig(Long jobId, String environment, JobConfigCombination existConfigCombination, JobExecuteConfigDto executeConfigDto, List<JobDependenceDto> dependenceDtoList, JobOutputDto outputDto, Operator operator) {
        JobExecuteConfig existExecuteConfig = existConfigCombination.getExecuteConfig();
        List<JobDependence> existDependenceList = existConfigCombination.getDependenceList();

        // 更新配置
        JobExecuteConfig executeConfig = buildExecuteConfig(jobId, environment, executeConfigDto, operator);
        executeConfig.setId(existExecuteConfig.getId());
        jobExecuteConfigRepo.update(executeConfig);
        // 更新依赖
        jobDependenceRepo.deleteDependence(jobId, environment);
        List<JobDependence> dependenceList = null;
        if (ObjectUtils.isNotEmpty(dependenceDtoList)) {
            dependenceList = buildDependenceConfig(jobId, environment, dependenceDtoList, operator);
            jobDependenceRepo.addDependence(dependenceList);
        }
        // 更新作业输出
        jobOutputRepo.delete(jobId, environment);
        JobOutput output;
        if (Objects.nonNull(outputDto)
                && Objects.nonNull(outputDto.getDestDataSourceId())
                && StringUtils.isNotBlank(outputDto.getDestTable())) {
            output = buildOutputConfig(jobId, environment, outputDto, operator);
            jobOutputRepo.save(output);
        }

        if (isDagChanged(executeConfig, existExecuteConfig)) {
            if (hasPrevOrPostRelation(jobId, environment, dependenceList, existDependenceList)) {
                throw new BizProcessException("作业存在上下游依赖关系，不能修改DAG");
            }
            changeDag(jobId, executeConfig, existExecuteConfig, environment, operator);
        } else {
            // dag不变，只变更作业依赖
            updatePrevJobDependency(jobId, environment, dependenceList, existDependenceList, operator);
        }

        // 修改调度配置
        if (isScheduleConfigChanged(executeConfig, existExecuteConfig)) {
            changeSchedule(jobId, environment, operator);
        }
    }

    private void addConfig(Long jobId, String environment, JobExecuteConfigDto executeConfigDto, List<JobDependenceDto> dependenceDtoList, JobOutputDto outputDto, Operator operator) {
        // 保存配置
        JobExecuteConfig executeConfig = buildExecuteConfig(jobId, environment, executeConfigDto, operator);
        jobExecuteConfigRepo.save(executeConfig);
        List<JobDependence> dependenceList = null;
        if (ObjectUtils.isNotEmpty(dependenceDtoList)) {
            dependenceList = buildDependenceConfig(jobId, environment, dependenceDtoList, operator);
            jobDependenceRepo.addDependence(dependenceList);
        }
        JobOutput output;
        if (Objects.nonNull(outputDto)
                && Objects.nonNull(outputDto.getDestDataSourceId())
                && StringUtils.isNotBlank(outputDto.getDestTable())) {
            output = buildOutputConfig(jobId, environment, outputDto, operator);
            jobOutputRepo.save(output);
        }

        bindDag(jobId, executeConfig.getSchDagId(), environment, true, operator);
        addPrevJobDependency(jobId, environment, dependenceList, operator);
    }

    private void changeDag(Long jobId, JobExecuteConfig executeConfig, JobExecuteConfig existExecuteConfig, String environment, Operator operator) {
        unbindDag(jobId, existExecuteConfig.getSchDagId(), environment, operator);
        Boolean isFirstBind = Boolean.FALSE;
        if (Objects.isNull(existExecuteConfig.getSchDagId()) || existExecuteConfig.getSchDagId() <= 0) {
            // 作业数据迁移时，dag id 默认为0，此时切换DAG需要新建DS任务
            isFirstBind = Boolean.TRUE;
        }
        bindDag(jobId, executeConfig.getSchDagId(), environment, isFirstBind, operator);
    }

    private void bindDag(Long jobId, Long bindDagId, String environment, Boolean isFirstBind, Operator operator) {
        // 发布job绑定DAG事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.JOB_BIND_DAG, environment, isFirstBind ? "{\"firstBind\":true, \"bindDagId\":" + bindDagId + "}" : "{\"bindDagId\":" + bindDagId + "}", operator);
        jobEventPublisher.whenBindDag(eventLog, bindDagId, isFirstBind);
    }

    private void unbindDag(Long jobId, Long unbindDagId, String environment, Operator operator) {
        if (Objects.isNull(unbindDagId) || unbindDagId <= 0) {
            // 作业数据迁移时，dag id 默认为0，此时切换DAG不需要解绑
            return;
        }
        // 发布job解绑DAG事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.JOB_UNBIND_DAG, environment, "{\"unbindDagId\":" + unbindDagId + "}", operator);
        jobEventPublisher.whenUnBindDag(eventLog, unbindDagId);
    }

    private void changeSchedule(Long jobId, String environment, Operator operator) {
        // 发布job调度配置变更事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.JOB_SCHEDULE_UPDATED, environment, operator);
        jobEventPublisher.whenScheduleUpdated(eventLog);
    }

    private void updatePrevJobDependency(Long jobId, String environment, List<JobDependence> newDependenceList, List<JobDependence> oldDependenceList, Operator operator) {
        if (!isChangePrevRelation(newDependenceList, oldDependenceList)) return;  // 依赖关系未变

        List<DagJobPair> addingPrevJobs = null;
        List<DagJobPair> removingPrevJobs = null;
        if (isOnlyAddingPrevRelation(newDependenceList, oldDependenceList)) {
            addingPrevJobs = groupJobDependence(newDependenceList);
        } else if (isOnlyRemovingPrevRelation(newDependenceList, oldDependenceList)) {
            removingPrevJobs = groupJobDependence(oldDependenceList);
        } else {
            Set<Long> newPrevJobIds = newDependenceList.stream().map(JobDependence::getPrevJobId).collect(Collectors.toSet());
            Set<Long> oldPrevJobIds = oldDependenceList.stream().map(JobDependence::getPrevJobId).collect(Collectors.toSet());

            List<JobDependence> addDependenceList = newDependenceList.stream().filter(dep -> !oldPrevJobIds.contains(dep.getPrevJobId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(addDependenceList)) {
                addingPrevJobs = groupJobDependence(addDependenceList);
            }

            List<JobDependence> removeDependenceList = oldDependenceList.stream().filter(dep -> !newPrevJobIds.contains(dep.getPrevJobId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(removeDependenceList)) {
                removingPrevJobs = groupJobDependence(removeDependenceList);
            }
        }

        if (!CollectionUtils.isEmpty(addingPrevJobs) || !CollectionUtils.isEmpty(removingPrevJobs)) {
            // 发布job绑定DAG事件
            JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.JOB_BUILD_PREV, environment, operator);
            jobEventPublisher.whenBuildPrevRelation(eventLog, addingPrevJobs, removingPrevJobs);
        }
    }

    private void addPrevJobDependency(Long jobId, String environment, List<JobDependence> dependenceList, Operator operator) {
        if (ObjectUtils.isEmpty(dependenceList)) return;

        List<DagJobPair> addingPrevJobs = groupJobDependence(dependenceList);
        // 发布job绑定DAG事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.JOB_BUILD_PREV, environment, operator);
        jobEventPublisher.whenBuildPrevRelation(eventLog, addingPrevJobs, null);
    }

    private boolean isDagChanged(JobExecuteConfig newConfig, JobExecuteConfig oldConfig) {
        return !Objects.equals(newConfig.getSchDagId(), oldConfig.getSchDagId());
    }

    private boolean isScheduleConfigChanged(JobExecuteConfig newConfig, JobExecuteConfig oldConfig) {
        // 判断作业优先级、超时和超时策略是否变更，如变更需要同步调度系统
        if (!Objects.equals(newConfig.getSchPriority(), oldConfig.getSchPriority())) return true;

        if (!Objects.equals(newConfig.getSchTimeOut(), oldConfig.getSchTimeOut())) return true;

        if (!Objects.equals(newConfig.getSchTimeOutStrategy(), oldConfig.getSchTimeOutStrategy())) return true;

        return false;
    }

    private boolean hasPrevOrPostRelation(Long jobId, String environment, List<JobDependence> newDependenceList, List<JobDependence> oldDependenceList) {
        if (!CollectionUtils.isEmpty(oldDependenceList)) return true;
        if (!CollectionUtils.isEmpty(newDependenceList)) return true;
        return !CollectionUtils.isEmpty(jobDependenceRepo.queryPostJob(jobId, environment));
    }

    private boolean isOnlyAddingPrevRelation(List<JobDependence> newDependenceList, List<JobDependence> oldDependenceList) {
        return CollectionUtils.isEmpty(oldDependenceList) && !CollectionUtils.isEmpty(newDependenceList);
    }

    private boolean isOnlyRemovingPrevRelation(List<JobDependence> newDependenceList, List<JobDependence> oldDependenceList) {
        return CollectionUtils.isEmpty(newDependenceList) && !CollectionUtils.isEmpty(oldDependenceList);
    }

    private boolean isChangePrevRelation(List<JobDependence> newDependenceList, List<JobDependence> oldDependenceList) {
        if (CollectionUtils.isEmpty(newDependenceList) && CollectionUtils.isEmpty(oldDependenceList)) {
            return false;
        } else if (!CollectionUtils.isEmpty(newDependenceList) && !CollectionUtils.isEmpty(oldDependenceList)) {
            if (newDependenceList.size() != oldDependenceList.size()) return true;

            Set<Long> newPrevJobIds = newDependenceList.stream().map(JobDependence::getPrevJobId).collect(Collectors.toSet());
            Set<Long> oldPrevJobIds = oldDependenceList.stream().map(JobDependence::getPrevJobId).collect(Collectors.toSet());
            Sets.SetView<Long> addJobs = Sets.difference(newPrevJobIds, oldPrevJobIds);
            if (!addJobs.isEmpty()) return true;

            Sets.SetView<Long> removeJobs = Sets.difference(oldPrevJobIds, newPrevJobIds);
            if (!removeJobs.isEmpty()) return true;

            return false;
        } else {
            return true;
        }
    }

    private List<DagJobPair> groupJobDependence(List<JobDependence> dependenceList) {
        Map<Long, List<Long>> dagJobMap = dependenceList.stream().collect(Collectors.groupingBy(JobDependence::getPrevJobDagId, Collectors.mapping(JobDependence::getPrevJobId, Collectors.toList())));
        return dagJobMap.entrySet().stream().map(entry -> new DagJobPair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    private JobExecuteConfig buildExecuteConfig(Long jobId, String environment, JobExecuteConfigDto dto, Operator operator) {
        dto.setOperator(operator);
        JobExecuteConfig config = dto.toModel();
        config.setId(null);
        config.setJobId(jobId);
        config.setEnvironment(environment);
        setDefaultValue(config);
        return config;
    }

    private List<JobDependence> buildDependenceConfig(Long jobId, String environment, List<JobDependenceDto> dependenceDtoList, Operator operator) {
        List<JobDependence> jobDependenceList = dependenceDtoList.stream().map(dto -> {
            dto.setOperator(operator);
            JobDependence dependence = dto.toModel();
            dependence.setId(null);
            dependence.setJobId(jobId);
            dependence.setEnvironment(environment);
            dependence.setDel(DeleteEnum.DEL_NO.val);
            return dependence;
        }).collect(Collectors.toList());
        return jobDependenceList;
    }

    private JobOutput buildOutputConfig(Long jobId, String environment, JobOutputDto dto, Operator operator) {
        dto.setOperator(operator);
        if (WriteModeEnum.SqlEnum.UPSERT.name().equals(dto.getDestWriteMode())) {
            checkArgument(StringUtils.isNotEmpty(dto.getJobTargetTablePk()), "写入模式为UPSERT时目标表主键不能为空");
        }
        JobOutput output = dto.toModel();
        output.setId(null);
        output.setJobId(jobId);
        output.setEnvironment(environment);
        return output;
    }

    private List<JobAndDagDto> buildJobAndDag(List<JobExecuteConfig> configList, List<JobInfo> jobInfoList, List<DAGInfo> dagInfoList) {
        Map<Long, JobInfo> jobMap = jobInfoList.stream().collect(Collectors.toMap(JobInfo::getId, Function.identity()));
        Map<Long, DAGInfo> dagMap = dagInfoList.stream().collect(Collectors.toMap(DAGInfo::getId, Function.identity()));
        return configList.stream().map(config -> {
            JobAndDagDto jobAndDagDto = new JobAndDagDto();
            jobAndDagDto.setJobId(config.getJobId());
            JobInfo jobInfo = jobMap.get(config.getJobId());
            jobAndDagDto.setJobName(jobInfo == null ? "" : jobInfo.getName());
            jobAndDagDto.setDwLayerCode(jobInfo == null ? "" : jobInfo.getDwLayerCode());
            jobAndDagDto.setDagId(config.getSchDagId());
            DAGInfo dagInfo = dagMap.get(config.getSchDagId());
            jobAndDagDto.setDagName(dagInfo == null ? "" : dagInfo.getName());
            return jobAndDagDto;
        }).collect(Collectors.toList());
    }

    private JobOutputDto fillJobOutputInfo(JobOutputDto output) {
        if (Objects.isNull(output)) return null;

        DataSourceDto dto = dataSourceApi.getDataSource(output.getDestDataSourceId());
        if (Objects.isNull(dto)) return output;

        output.setDestDataSourceName(dto.getName());
        return output;
    }

    private List<JobDependenceDto> fillDependenceInfo(List<JobDependenceDto> dependencies) {
        if (CollectionUtils.isEmpty(dependencies)) return dependencies;

        List<Long> jobIds = Lists.newArrayList();
        List<Long> dagIds = Lists.newArrayList();
        for (JobDependenceDto dto : dependencies) {
            jobIds.add(dto.getJobId());
            jobIds.add(dto.getPrevJobId());
            dagIds.add(dto.getPrevJobDagId());
        }
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(jobIds);
        Map<Long, JobInfo> jobInfoMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(jobInfoList)) {
            jobInfoMap = jobInfoList.stream().collect(Collectors.toMap(JobInfo::getId, Function.identity()));
        }

        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo(dagIds);
        Map<Long, DAGInfo> dagInfoMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(dagInfoList)) {
            dagInfoMap = dagInfoList.stream().collect(Collectors.toMap(DAGInfo::getId, Function.identity()));
        }

        for (JobDependenceDto dto : dependencies) {
            JobInfo jobInfo = jobInfoMap.get(dto.getJobId());
            dto.setJobName(jobInfo == null ? "" : jobInfo.getName());

            JobInfo prevJobInfo = jobInfoMap.get(dto.getPrevJobId());
            dto.setPrevJobName(prevJobInfo == null ? "" : prevJobInfo.getName());

            DAGInfo dagInfo = dagInfoMap.get(dto.getPrevJobDagId());
            dto.setPrevJobDagName(dagInfo == null ? "" : dagInfo.getName());
        }
        return dependencies;
    }

    private void checkExecuteConfig(JobExecuteConfigDto dto) {
        checkArgument(Objects.nonNull(dto.getSchDagId()), "DAG参数为空");
        checkArgument(StringUtils.isNotBlank(dto.getSchRerunMode()), "重跑属性为空");
        checkArgument(StringUtils.isNotBlank(dto.getExecQueue()), "运行队列为空");

        Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(dto.getSchDagId());
        checkArgument(dagInfoOptional.isPresent(), "DAG不存在或已删除");
    }

    private void checkDependConfig(Long currentJobId, List<JobDependenceDto> dependenceDtoList) {
        if (ObjectUtils.isEmpty(dependenceDtoList)) return;

        Set<Long> prevJobSet = Sets.newHashSet();
        for (JobDependenceDto dto : dependenceDtoList) {
            checkArgument(Objects.nonNull(dto.getPrevJobId()), "上游作业为空");
            checkArgument(Objects.nonNull(dto.getPrevJobDagId()), "上游作业所属DAG为空");
            checkArgument(prevJobSet.add(dto.getPrevJobId()), "上游依赖存在重复作业");
            checkArgument(!Objects.equals(currentJobId, dto.getPrevJobId()), "上游依赖作业不能选择当前作业");
        }
    }

    private void checkOutputConfig(JobInfo jobInfo, JobOutputDto outputDto) {
        if (Objects.isNull(outputDto)) return;

        if (!JobTypeEnum.SQL_SPARK.name().equals(jobInfo.getJobType())
                && !JobTypeEnum.SQL_DORIS.name().equals(jobInfo.getJobType()))
            return;

        checkArgument(Objects.nonNull(outputDto.getDestDataSourceId()), "作业输出数据源为空");
        checkArgument(StringUtils.isNotBlank(outputDto.getDestDataSourceType()), "作业输出数据源类型为空");
        checkArgument(StringUtils.isNotBlank(outputDto.getDestTable()), "作业输出表为空");
        checkArgument(StringUtils.isNotBlank(outputDto.getDestWriteMode()), "作业输出写入模式为空");
    }

    private void setDefaultValue(JobExecuteConfig config) {
        if (Objects.isNull(config.getSchTimeOut())) config.setSchTimeOut(0);
        if (Objects.isNull(config.getSchDryRun())) config.setSchDryRun(0);
        config.setExecQueue(Strings.nullToEmpty(config.getExecQueue()));
        config.setExecWarnLevel(Strings.nullToEmpty(config.getExecWarnLevel()));
        config.setSchTimeOutStrategy(Strings.nullToEmpty(config.getSchTimeOutStrategy()));
        config.setExecDriverMem(MoreObjects.firstNonNull(config.getExecDriverMem(), 2));
        config.setExecWorkerMem(MoreObjects.firstNonNull(config.getExecWorkerMem(), 2));
        config.setSchPriority(MoreObjects.firstNonNull(config.getSchPriority(), JobPriorityEnum.middle.val));
    }
}
