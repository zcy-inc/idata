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

package cn.zhengcaiyun.idata.develop.dal.repo.dag;

import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGDependence;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 13:45
 **/
public interface DAGRepo {

    Long saveDAGInfo(DAGInfo info);

    Long saveDAGSchedule(DAGSchedule schedule);

    Long saveDAG(DAGInfo info, DAGSchedule schedule);

    Boolean updateDAGInfo(DAGInfo info);

    Boolean updateDAGSchedule(DAGSchedule schedule);

    Boolean updateDAG(DAGInfo info, DAGSchedule schedule);

    Optional<DAGInfo> queryDAGInfo(Long dagId);

    Optional<DAGSchedule> queryDAGSchedule(Long dagId);

    List<DAGInfo> queryDAGInfo();

    List<DAGInfo> queryDAGInfo(String name);

    Boolean deleteDAG(Long dagId, String operator);

    Long countDag(Long folderId);

    List<DAGInfo> queryDAGInfo(DAGInfoCondition condition);

    @Deprecated
    List<DAGDependence> queryDependence(Long dagId);

    @Deprecated
    List<DAGDependence> queryDependence(List<Long> dagIds);

    @Deprecated
    Boolean addDependence(List<DAGDependence> dependenceList);

    @Deprecated
    Boolean deleteDependence(Long dagId);
}
