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

package cn.zhengcaiyun.idata.connector.resourcemanager.impl;

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.YarnApiAgent;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.param.QueryClusterAppParam;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterApp;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterMetrics;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.system.dto.ConfigValueDto;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 15:17
 **/
@Service
public class ResourceManagerServiceImpl implements ResourceManagerService {

    private final YarnApiAgent yarnApiAgent;
    private final SystemConfigService systemConfigService;

    @Autowired
    public ResourceManagerServiceImpl(YarnApiAgent yarnApiAgent,
                                      SystemConfigService systemConfigService) {
        this.yarnApiAgent = yarnApiAgent;
        this.systemConfigService = systemConfigService;
    }


    @Override
    public ClusterMetricsDto fetchClusterMetrics() {
        ClusterMetrics clusterMetrics = yarnApiAgent.fetchClusterMetrics(getYarnServiceUrl());
        return toMetricsDto(clusterMetrics);
    }

    @Override
    public List<ClusterAppDto> fetchRunningClusterApps(EnvEnum envEnum) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        param.setStates("RUNNING");
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps, envEnum);
    }

    @Override
    public List<ClusterAppDto> fetchClusterApps(LocalDateTime startedTimeBegin, LocalDateTime startedTimeEnd, EnvEnum envEnum) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        if (Objects.nonNull(startedTimeBegin)) {
            param.setStartedTimeBegin(startedTimeBegin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        if (Objects.nonNull(startedTimeEnd)) {
            param.setStartedTimeEnd(startedTimeEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps, envEnum);
    }

    @Override
    public List<ClusterAppDto> fetchFinishedClusterApps(LocalDateTime finishedTimeBegin, LocalDateTime finishedTimeEnd, EnvEnum envEnum) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        if (Objects.nonNull(finishedTimeBegin)) {
            param.setFinishedTimeBegin(finishedTimeBegin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        if (Objects.nonNull(finishedTimeEnd)) {
            param.setFinishedTimeEnd(finishedTimeEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        param.setStates("FINISHED,FAILED,KILLED");
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps, envEnum);
    }

    private List<ClusterAppDto> filterAndConvertToDto(List<ClusterApp> clusterApps, EnvEnum envEnum) {
        if (CollectionUtils.isEmpty(clusterApps)) return Lists.newArrayList();

        return clusterApps.stream()
                .filter(this::isNormalJob)
                .map(app -> toAppDto(app, getJobId(app.getName()), getNormalJobEvn(app.getName())))
                .filter(appDto -> Objects.isNull(envEnum) || envEnum == appDto.getEnv())
                .collect(Collectors.toList());
    }

    @Override
    public ClusterAppDto queryAppId(String appId) {
        ClusterApp clusterApp = yarnApiAgent.queryAppId(getYarnServiceUrl(), appId);
        return toAppDto(clusterApp, getJobId(clusterApp.getName()), getNormalJobEvn(clusterApp.getName()));
    }

    @Override
    public Boolean killClusterApp(String appId) {
        return yarnApiAgent.setClusterAppState(getYarnServiceUrl(), appId, "KILLED");
    }

    private String getYarnServiceUrl() {
        ConfigDto configDto = systemConfigService.getSystemConfigByKey("htool-config");
        checkState(Objects.nonNull(configDto) && !CollectionUtils.isEmpty(configDto.getValueOne()), "Yarn地址配置未配置，请在系统配置中配置");
        ConfigValueDto valueDto = configDto.getValueOne().get("yarn.addr");
        checkState(Objects.nonNull(valueDto) && StringUtils.isNotBlank(valueDto.getConfigValue()), "Yarn地址配置未配置，请在系统配置中配置");
        return valueDto.getConfigValue();
    }

    private ClusterMetricsDto toMetricsDto(ClusterMetrics clusterMetrics) {
        ClusterMetricsDto dto = new ClusterMetricsDto();
        dto.setSubmittedJobs(clusterMetrics.getAppsSubmitted());
        dto.setCompletedJobs(clusterMetrics.getAppsCompleted());
        dto.setPendingJobs(clusterMetrics.getAppsPending());
        dto.setRunningJobs(clusterMetrics.getAppsRunning());
        dto.setFailedJobs(clusterMetrics.getAppsFailed());
        dto.setKilledJobs(clusterMetrics.getAppsKilled());
        dto.setAllocatedMem(clusterMetrics.getAllocatedMB());
        dto.setReservedMem(clusterMetrics.getReservedMB());
        dto.setAvailableMem(clusterMetrics.getAvailableMB());
        dto.setTotalMem(clusterMetrics.getTotalMB());
        dto.setAvailableVCores(clusterMetrics.getAvailableVirtualCores());
        dto.setAllocatedVCores(clusterMetrics.getAllocatedVirtualCores());
        dto.setReservedVCores(clusterMetrics.getReservedVirtualCores());
        dto.setTotalVCores(clusterMetrics.getTotalVirtualCores());
        return dto;
    }

    private ClusterAppDto toAppDto(ClusterApp app, Long jobId, EnvEnum envEnum) {
        ClusterAppDto dto = new ClusterAppDto();
        dto.setAppId(app.getId());
        dto.setJobId(jobId);
        dto.setEnv(envEnum);
        dto.setUser(app.getUser());
        dto.setAppName(app.getName());
        dto.setQueue(app.getQueue());
        dto.setState(app.getState());
        dto.setFinalStatus(app.getFinalStatus());
        dto.setProgress(app.getProgress());
        dto.setClusterId(app.getClusterId());
        dto.setApplicationType(app.getApplicationType());
        dto.setApplicationTags(app.getApplicationTags());
        dto.setElapsedTime(app.getElapsedTime());
        dto.setMemorySeconds(app.getMemorySeconds());
        dto.setVcoreSeconds(app.getVcoreSeconds());
        dto.setAmContainerLogs(app.getAmContainerLogs());

        Long startedTime = app.getStartedTime();
        if (Objects.nonNull(startedTime)) {
            dto.setStartedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(startedTime), ZoneId.systemDefault()));
        }
        Long finishedTime = app.getFinishedTime();
        if (Objects.nonNull(finishedTime)) {
            dto.setFinishedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(finishedTime), ZoneId.systemDefault()));
        }
        dto.setAllocatedVCores(app.getAllocatedVCores());
        dto.setAllocatedMem(app.getAllocatedMB());
        return dto;
    }

    private Long getJobId(String appName) {
        if (StringUtils.isEmpty(appName)) return null;

        try {
            String jobIdStr = appName.substring(appName.lastIndexOf("-") + 1);
            return Long.parseLong(jobIdStr);
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isNormalJob(ClusterApp app) {
        String appName = app.getName();
        if (StringUtils.isEmpty(appName)) return false;

        return appName.indexOf("-s-") > 0 || appName.indexOf("-p-") > 0;
    }

    private EnvEnum getNormalJobEvn(String appName) {
        if (StringUtils.isEmpty(appName)) return null;
        if (appName.indexOf("-s-") > 0) return EnvEnum.stag;
        if (appName.indexOf("-p-") > 0) return EnvEnum.prod;
        return null;
    }
}
