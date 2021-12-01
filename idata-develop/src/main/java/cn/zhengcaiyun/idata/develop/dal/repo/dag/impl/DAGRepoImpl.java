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

package cn.zhengcaiyun.idata.develop.dal.repo.dag.impl;

import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGDependenceCustomizeDao;
import cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGDependenceDao;
import cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGInfoDao;
import cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGScheduleDao;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGDependence;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGDependenceDynamicSqlSupport.DAG_DEPENDENCE;
import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGInfoDynamicSqlSupport.dag_info;
import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGScheduleDynamicSqlSupport.dag_schedule;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 13:45
 **/
@Repository
public class DAGRepoImpl implements DAGRepo {

    private final DAGInfoDao dagInfoDao;
    private final DAGScheduleDao dagScheduleDao;
    private final DAGDependenceDao dagDependenceDao;
    private final DAGDependenceCustomizeDao dagDependenceCustomizeDao;

    @Autowired
    public DAGRepoImpl(DAGInfoDao dagInfoDao,
                       DAGScheduleDao dagScheduleDao,
                       DAGDependenceDao dagDependenceDao,
                       DAGDependenceCustomizeDao dagDependenceCustomizeDao) {
        this.dagInfoDao = dagInfoDao;
        this.dagScheduleDao = dagScheduleDao;
        this.dagDependenceDao = dagDependenceDao;
        this.dagDependenceCustomizeDao = dagDependenceCustomizeDao;
    }

    @Override
    public Long saveDAGInfo(DAGInfo info) {
        int ret = dagInfoDao.insertSelective(info);
        if (ret <= 0) return null;
        return info.getId();
    }

    @Override
    public Long saveDAGSchedule(DAGSchedule schedule) {
        int ret = dagScheduleDao.insertSelective(schedule);
        if (ret <= 0) return null;
        return schedule.getId();
    }

    @Override
    @Transactional
    public Long saveDAG(DAGInfo info, DAGSchedule schedule) {
        Long dagId = saveDAGInfo(info);
        schedule.setDagId(dagId);
        saveDAGSchedule(schedule);
        return dagId;
    }

    @Override
    public Boolean updateDAGInfo(DAGInfo info) {
        int ret = dagInfoDao.updateByPrimaryKeySelective(info);
        return ret > 0;
    }

    @Override
    public Boolean updateDAGSchedule(DAGSchedule schedule) {
        int ret = dagScheduleDao.updateByPrimaryKeySelective(schedule);
        return ret > 0;
    }

    @Override
    @Transactional
    public Boolean updateDAG(DAGInfo info, DAGSchedule schedule) {
        updateDAGInfo(info);
        updateDAGSchedule(schedule);
        return Boolean.TRUE;
    }

    @Override
    public Optional<DAGInfo> queryDAGInfo(Long dagId) {
        Optional<DAGInfo> optional = dagInfoDao.selectByPrimaryKey(dagId);
        if (optional.isEmpty()) return optional;

        if (DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public Optional<DAGSchedule> queryDAGSchedule(Long dagId) {
        return dagScheduleDao.selectOne(dsl -> dsl.where(dag_schedule.dagId, isEqualTo(dagId),
                and(dag_schedule.del, isEqualTo(DEL_NO.val))));
    }

    @Override
    public List<DAGInfo> queryDAGInfo() {
        return dagInfoDao.select(dsl -> dsl.where(dag_info.del, isEqualTo(DEL_NO.val)));
    }

    @Override
    public List<DAGInfo> queryDAGInfo(List<Long> ids) {
        return dagInfoDao.select(dsl -> dsl.where(dag_info.id, isIn(ids),
                and(dag_info.del, isEqualTo(DEL_NO.val))));
    }

    @Override
    public List<DAGInfo> queryDAGInfo(String name) {
        return dagInfoDao.select(dsl -> dsl.where(dag_info.del, isEqualTo(DEL_NO.val),
                and(dag_info.name, isEqualTo(name))));
    }

    @Override
    @Transactional
    public Boolean deleteDAG(Long dagId, String operator) {
        dagInfoDao.update(dsl -> dsl.set(dag_info.del).equalTo(DEL_YES.val)
                .set(dag_info.editor).equalTo(operator)
                .where(dag_info.id, isEqualTo(dagId)));

        dagScheduleDao.update(dsl -> dsl.set(dag_schedule.del).equalTo(DEL_YES.val)
                .set(dag_schedule.editor).equalTo(operator)
                .where(dag_schedule.dagId, isEqualTo(dagId)));
        return Boolean.TRUE;
    }

    @Override
    public Long countDag(Long folderId) {
        return dagInfoDao.count(dsl -> dsl.where(dag_info.del, isEqualTo(DEL_NO.val)));
    }

    @Override
    public List<DAGInfo> queryDAGInfo(DAGInfoCondition condition) {
        return dagInfoDao.select(dsl -> dsl.where(dag_info.del, isEqualTo(DEL_NO.val),
                and(dag_info.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(dag_info.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(dag_info.status, isEqualToWhenPresent(condition.getStatus()))));
    }

    @Override
    @Deprecated
    public List<DAGDependence> queryDependence(Long dagId) {
        return dagDependenceDao.select(dsl -> dsl.where(DAG_DEPENDENCE.dagId, isEqualTo(dagId)));
    }

    @Override
    @Deprecated
    public List<DAGDependence> queryDependence(List<Long> dagIds) {
        return dagDependenceDao.select(dsl -> dsl.where(DAG_DEPENDENCE.dagId, isIn(dagIds)));
    }

    @Override
    @Deprecated
    public Boolean addDependence(List<DAGDependence> dependenceList) {
        return dagDependenceCustomizeDao.insertMultiple(dependenceList) > 0;
    }

    @Override
    @Deprecated
    public Boolean deleteDependence(Long dagId) {
        return dagDependenceDao.delete(dsl -> dsl.where(DAG_DEPENDENCE.dagId, isEqualTo(dagId))) > 0;
    }
}
