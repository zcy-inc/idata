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

package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IDagIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:40
 **/
@Component
public class WorkflowIntegrator implements IDagIntegrator {

    public static final String ENTITY_TYPE = "workflow";

    private final DSEntityMappingRepo dsEntityMappingRepo;

    @Autowired
    public WorkflowIntegrator(DSEntityMappingRepo dsEntityMappingRepo) {
        this.dsEntityMappingRepo = dsEntityMappingRepo;
    }

    @Override
    public void create(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // todo 创建空工作流和定时器，状态为下线

        // todo 保存DAG和workflow关系

    }

    @Override
    public void update(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // 获取工作流code

        // 更新工作流
    }

    @Override
    public void delete(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // 获取工作流code
    }

    @Override
    public void online(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // 获取工作流code
    }

    @Override
    public void offline(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // 获取工作流code
    }

    @Override
    public void updateSchedule(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        // 获取工作流code

        // 更新工作流定时
    }

    @Override
    public void run(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 暂不实现
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) throws ExternalIntegrationException {
    }

}
