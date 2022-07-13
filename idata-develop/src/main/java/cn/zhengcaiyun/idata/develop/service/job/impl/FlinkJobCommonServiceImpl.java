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

import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.core.http.HttpClientUtil;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import cn.zhengcaiyun.idata.develop.service.job.FlinkJobCommonService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-12 17:02
 **/
@Service
public class FlinkJobCommonServiceImpl implements FlinkJobCommonService {

    private final ResourceManagerService resourceManagerService;

    @Autowired
    public FlinkJobCommonServiceImpl(ResourceManagerService resourceManagerService) {
        this.resourceManagerService = resourceManagerService;
    }

    @Override
    public void fetchFlinkJobId() {
        List<ClusterAppDto> clusterAppDtoList = resourceManagerService.fetchRunningClusterApps(null);
        if (CollectionUtils.isEmpty(clusterAppDtoList)) {
            return;
        }

        for (ClusterAppDto appDto : clusterAppDtoList) {
            if (appDto.getAppName().startsWith("FlinkCDC-")) {
                processCDCJob(appDto);
            }
        }
    }

    private void processCDCJob(ClusterAppDto appDto) {
        String trackingUrl = appDto.getTrackingUrl();
        if (StringUtils.isBlank(trackingUrl)) {
            return;
        }

        String flinkJobUrl = trackingUrl + "jobs/overview";
        HttpInput httpInput = new HttpInput();
        httpInput.setServerName("Flink Job Manager");
        httpInput.setUri(flinkJobUrl);
        httpInput.setMethod("GET");

        JSONObject result = HttpClientUtil.executeHttpRequest(httpInput, new TypeReference<JSONObject>() {
        });
        JSONArray jobs = result.getJSONArray("jobs");
        for (int i = 0; i < jobs.size(); i++) {
            JSONObject jobObject = (JSONObject) jobs.get(i);
            String name = (String) jobObject.get("name");
            String jid = (String) jobObject.get("jid");
            Long startTime = (Long) jobObject.get("start-time");
            Integer duration = (Integer) jobObject.get("duration");
        }
    }
}
