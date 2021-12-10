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

import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.YarnApiAgent;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.param.QueryClusterAppParam;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterApp;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterMetrics;
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

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 15:17
 **/
@Service
public class ResourceManagerServiceImpl implements ResourceManagerService {

    private final YarnApiAgent yarnApiAgent;

    @Autowired
    public ResourceManagerServiceImpl(YarnApiAgent yarnApiAgent) {
        this.yarnApiAgent = yarnApiAgent;
    }


    @Override
    public ClusterMetricsDto fetchClusterMetrics() {
        ClusterMetrics clusterMetrics = yarnApiAgent.fetchClusterMetrics(getYarnServiceUrl());
        return toMetricsDto(clusterMetrics);
    }

    @Override
    public List<ClusterAppDto> fetchRunningClusterApps() {
        QueryClusterAppParam param = new QueryClusterAppParam();
        param.setStates("RUNNING");
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps);
    }

    @Override
    public List<ClusterAppDto> fetchClusterApps(LocalDateTime startedTimeBegin, LocalDateTime startedTimeEnd) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        if (Objects.nonNull(startedTimeBegin)) {
            param.setStartedTimeBegin(startedTimeBegin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        if (Objects.nonNull(startedTimeEnd)) {
            param.setStartedTimeEnd(startedTimeEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps);
    }

    @Override
    public List<ClusterAppDto> fetchFinishedClusterApps(LocalDateTime finishedTimeBegin, LocalDateTime finishedTimeEnd) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        if (Objects.nonNull(finishedTimeBegin)) {
            param.setFinishedTimeBegin(finishedTimeBegin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        if (Objects.nonNull(finishedTimeEnd)) {
            param.setFinishedTimeEnd(finishedTimeEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "");
        }
        param.setStates("FINISHED,FAILED,KILLED");
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return filterAndConvertToDto(clusterApps);
    }

    private List<ClusterAppDto> filterAndConvertToDto(List<ClusterApp> clusterApps) {
        if (CollectionUtils.isEmpty(clusterApps)) return Lists.newArrayList();

        return clusterApps.stream()
                .filter(this::isNormalJob)
                .map(this::toAppDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean killClusterApp(String appId) {
        return yarnApiAgent.setClusterAppState(getYarnServiceUrl(), appId, "KILLED");
    }

    private String getYarnServiceUrl() {
        return "";
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

    private ClusterAppDto toAppDto(ClusterApp app) {
        ClusterAppDto dto = new ClusterAppDto();
        dto.setAppId(app.getId());
        dto.setJobId(getJobId(app.getName()));
        dto.setUser(app.getUser());
        dto.setAppName(app.getName());
        dto.setQueue(app.getQueue());
        dto.setState(app.getState());
        dto.setFinalStatus(app.getFinalStatus());
        dto.setProgress(app.getProgress());
        dto.setClusterId(app.getClusterId());
        dto.setApplicationType(app.getApplicationType());
        dto.setApplicationTags(app.getApplicationTags());

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
}
