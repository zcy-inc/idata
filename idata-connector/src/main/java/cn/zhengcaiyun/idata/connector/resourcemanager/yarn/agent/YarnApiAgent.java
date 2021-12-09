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

package cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent;


import cn.zhengcaiyun.idata.commons.exception.RemoteServiceException;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.param.QueryClusterAppParam;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.result.ClusterAppListResult;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.result.ClusterMetricsResult;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterApp;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterAppState;
import cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean.ClusterMetrics;
import cn.zhengcaiyun.idata.core.http.HttpClientUtil;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 15:14
 **/
@Component
public class YarnApiAgent {

    public ClusterMetrics fetchClusterMetrics(String yarnBaseUrl) {
        String reqUrl = yarnBaseUrl + "/ws/v1/cluster/metrics";
        HttpInput httpInput = new HttpInput().setServerName("Yarn-api-service")
                .setUri(reqUrl).setMethod("GET");
        try {
            ClusterMetricsResult result = HttpClientUtil.executeHttpRequest(httpInput, new TypeReference<ClusterMetricsResult>() {
            });
            return result.getClusterMetrics();
        } catch (Exception ex) {
            throw new RemoteServiceException("调用Yarn服务失败", ex);
        }
    }

    public List<ClusterApp> fetchClusterApps(String yarnBaseUrl, QueryClusterAppParam param) {
        String reqUrl = yarnBaseUrl + "/ws/v1/cluster/apps";
        try {
            Map<String, String> paramMap = buildQueryClusterAppParam(param);
            HttpInput httpInput = new HttpInput().setServerName("Yarn-api-service")
                    .setUri(reqUrl).setMethod("GET")
                    .setQueryParamMap(paramMap);
            ClusterAppListResult result = HttpClientUtil.executeHttpRequest(httpInput, new TypeReference<ClusterAppListResult>() {
            });
            return result.getApps().getApp();
        } catch (Exception ex) {
            throw new RemoteServiceException("调用Yarn服务失败", ex);
        }
    }

    public Boolean setClusterAppState(String yarnBaseUrl, String appId, String state) {
        String reqUrl = String.format("%s/ws/v1/cluster/apps/%s/state", yarnBaseUrl, appId);
        try {
            HttpInput httpInput = new HttpInput().setServerName("Yarn-api-service")
                    .setUri(reqUrl).setMethod("PUT")
                    .setObjectBody(new ClusterAppState(state));
            ClusterAppState result = HttpClientUtil.executeHttpRequest(httpInput, new TypeReference<ClusterAppState>() {
            });
            return Boolean.TRUE;
        } catch (Exception ex) {
            throw new RemoteServiceException("调用Yarn服务失败", ex);
        }
    }

    private Map<String, String> buildQueryClusterAppParam(QueryClusterAppParam param) throws IllegalAccessException {
        Map<String, String> paramMap = Maps.newHashMap();
        for (Field field : param.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(param);
            if (Objects.nonNull(value)) {
                paramMap.put(field.getName(), value.toString());
            }
        }
        return paramMap;
    }
}
