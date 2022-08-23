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
import cn.zhengcaiyun.idata.develop.manager.FlinkJobManager;
import cn.zhengcaiyun.idata.develop.manager.StreamJobFlinkInfoManager;
import cn.zhengcaiyun.idata.develop.manager.StreamJobInstanceManager;
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

    private final FlinkJobManager flinkJobManager;
    private final StreamJobInstanceManager streamJobInstanceManager;
    private final StreamJobFlinkInfoManager streamJobFlinkInfoManager;

    @Autowired
    public FlinkJobCommonServiceImpl(FlinkJobManager flinkJobManager,
                                     StreamJobInstanceManager streamJobInstanceManager,
                                     StreamJobFlinkInfoManager streamJobFlinkInfoManager) {
        this.flinkJobManager = flinkJobManager;
        this.streamJobInstanceManager = streamJobInstanceManager;
        this.streamJobFlinkInfoManager = streamJobFlinkInfoManager;
    }

    @Override
    public void fetchAndSetFlinkJobRunningInfo() {
        // 获取flink app信息
        List<ClusterAppDto> clusterAppDtoList = flinkJobManager.fetchFlinkApp();
        if (CollectionUtils.isEmpty(clusterAppDtoList)) {
            return;
        }

        for (ClusterAppDto appDto:clusterAppDtoList){
            // 获取 flink job 信息

            // 设置运行实例信息

            // 设置flink id 信息
        }



    }

}
