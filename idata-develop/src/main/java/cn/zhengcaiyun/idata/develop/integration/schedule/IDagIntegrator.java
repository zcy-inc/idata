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

package cn.zhengcaiyun.idata.develop.integration.schedule;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:39
 **/
public interface IDagIntegrator {

    void create(DAGInfo dagInfo, DAGSchedule dagSchedule);

    void update(DAGInfo dagInfo, DAGSchedule dagSchedule);

    void delete(Long dagId, String environment);

    void online(DAGInfo dagInfo);

    void offline(DAGInfo dagInfo);

    void updateSchedule(DAGInfo dagInfo, DAGSchedule dagSchedule);

    void run(DAGInfo dagInfo);

    @Deprecated
    void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds);

    @Deprecated
    void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId);

    @Deprecated
    void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds);

    @Deprecated
    void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId);
}
