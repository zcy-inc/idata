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
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.FlinkJobInfoDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.core.http.HttpClientUtil;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-23 15:52
 **/
@Component
public class FlinkJobManager {
    private final ResourceManagerService resourceManagerService;

    @Autowired
    public FlinkJobManager(ResourceManagerService resourceManagerService) {
        this.resourceManagerService = resourceManagerService;
    }

    public List<ClusterAppDto> fetchRunningFlinkApp(EnvEnum envEnum) {
        List<ClusterAppDto> clusterAppDtoList = resourceManagerService.fetchRunningClusterApps(envEnum);
        if (CollectionUtils.isEmpty(clusterAppDtoList)) {
            return Lists.newArrayList();
        }
        return clusterAppDtoList.stream()
                .filter(appDto -> appDto.getAppName().startsWith("FlinkCDC-") || appDto.getAppName().startsWith("FlinkSQL-"))
                .collect(Collectors.toList());
    }

    public List<ClusterAppDto> fetchFlinkApp(Long jobId, String env) {
        List<ClusterAppDto> clusterAppDtoList = resourceManagerService.fetchRunningClusterApps(null);
        if (CollectionUtils.isEmpty(clusterAppDtoList)) {
            return Lists.newArrayList();
        }
        return clusterAppDtoList.stream()
                .filter(appDto -> jobId.equals(appDto.getJobId()) && env.equals(appDto.getEnv().name()))
                .filter(appDto -> appDto.getAppName().startsWith("FlinkCDC-") || appDto.getAppName().startsWith("FlinkSQL-"))
                .collect(Collectors.toList());
    }

    public void stopFlinkApp(Long jobId, String env) {
        List<ClusterAppDto> appDtoList = fetchFlinkApp(jobId, env);
        for (ClusterAppDto appDto : appDtoList) {
            resourceManagerService.killClusterApp(appDto.getAppId());
        }
    }

    public String getFlinkAppUrl(ClusterAppDto appDto) {
        String trackingUrl = appDto.getTrackingUrl();
        if (StringUtils.isBlank(trackingUrl)) {
            return null;
        }

        String flinkAppUrl = trackingUrl + "jobs/overview";
        return flinkAppUrl;
    }

    public List<FlinkJobInfoDto> fetchFlinkJobInfo(String flinkAppUrl) {
        HttpInput httpInput = new HttpInput();
        httpInput.setServerName("Flink Job Manager");
        httpInput.setUri(flinkAppUrl);
        httpInput.setMethod("GET");

        JSONObject result = HttpClientUtil.executeHttpRequest(httpInput, new TypeReference<JSONObject>() {
        });
        JSONArray jobs = result.getJSONArray("jobs");
        List<FlinkJobInfoDto> jobInfoDtoList = Lists.newArrayList();
        for (int i = 0; i < jobs.size(); i++) {
            JSONObject jobObject = (JSONObject) jobs.get(i);
            String name = (String) jobObject.get("name");
            String jid = (String) jobObject.get("jid");
            String state = (String) jobObject.get("state");
            Long startTime = (Long) jobObject.get("start-time");
            Integer duration = (Integer) jobObject.get("duration");

            FlinkJobInfoDto dto = new FlinkJobInfoDto();
            dto.setName(name);
            dto.setJid(jid);
            dto.setState(state);
            dto.setStartTime(startTime);
            dto.setDuration(duration);
            jobInfoDtoList.add(dto);
        }
        return jobInfoDtoList;
    }
}
