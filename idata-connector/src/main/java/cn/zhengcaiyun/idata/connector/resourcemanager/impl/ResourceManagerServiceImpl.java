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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        return null;
    }

    @Override
    public List<ClusterAppDto> fetchRunningClusterApps() {
        QueryClusterAppParam param = new QueryClusterAppParam();
        param.setStates("RUNNING");
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return null;
    }

    @Override
    public List<ClusterAppDto> fetchClusterApps(LocalDateTime startedTimeBegin, LocalDateTime startedTimeEnd) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        // todo set time
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return null;
    }

    @Override
    public List<ClusterAppDto> fetchFinishedClusterApps(LocalDateTime finishedTimeBegin, LocalDateTime finishedTimeEnd) {
        QueryClusterAppParam param = new QueryClusterAppParam();
        // todo set time and state
        List<ClusterApp> clusterApps = yarnApiAgent.fetchClusterApps(getYarnServiceUrl(), param);
        return null;
    }

    @Override
    public Boolean killClusterApp(String appId) {
        return yarnApiAgent.setClusterAppState(getYarnServiceUrl(), appId, "KILLED");
    }

    private String getYarnServiceUrl() {
        return "";
    }
}
