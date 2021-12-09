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

package cn.zhengcaiyun.idata.connector.resourcemanager;

import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 15:15
 **/
public interface ResourceManagerService {

    /**
     * 查询集群统计信息
     *
     * @return
     */
    ClusterMetricsDto fetchClusterMetrics();

    /**
     * 获取正在运行的集群应用
     *
     * @return
     */
    List<ClusterAppDto> fetchRunningClusterApps();

    /**
     * 根据开始时间，获取集群应用
     *
     * @param startedTimeBegin
     * @param startedTimeEnd
     * @return
     */
    List<ClusterAppDto> fetchClusterApps(LocalDateTime startedTimeBegin, LocalDateTime startedTimeEnd);

    /**
     * 根据结束时间，获取已结束的集群应用
     *
     * @param finishedTimeBegin
     * @param finishedTimeEnd
     * @return
     */
    List<ClusterAppDto> fetchFinishedClusterApps(LocalDateTime finishedTimeBegin, LocalDateTime finishedTimeEnd);

    /**
     * kill集群应用
     *
     * @param appId
     * @return
     */
    Boolean killClusterApp(String appId);
}
