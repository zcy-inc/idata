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

package cn.zhengcaiyun.idata.operation.service.impl;

import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.operation.bean.dto.ClusterAppMonitorDto;
import cn.zhengcaiyun.idata.operation.service.ClusterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-10 13:56
 **/
@Component
public class ClusterServiceImpl implements ClusterService {

    private final ResourceManagerService resourceManagerService;

    @Autowired
    public ClusterServiceImpl(ResourceManagerService resourceManagerService) {
        this.resourceManagerService = resourceManagerService;
    }

    @Override
    public List<ClusterAppMonitorDto> fetchClusterApp(String state) {
        List<ClusterAppDto> appDtoList = resourceManagerService.fetchRunningClusterApps(null);
        return appDtoList.stream().map(appDto -> {
            ClusterAppMonitorDto monitorDto = new ClusterAppMonitorDto();
            BeanUtils.copyProperties(appDto, monitorDto);
            return monitorDto;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean stopClusterApp(String appId) {
        return resourceManagerService.killClusterApp(appId);
    }
}
