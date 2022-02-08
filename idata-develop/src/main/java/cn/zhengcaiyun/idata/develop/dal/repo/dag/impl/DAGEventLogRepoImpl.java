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

import cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGEventLogDao;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGEventLog;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGEventLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGEventLogDynamicSqlSupport.DAG_EVENT_LOG;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-10 14:36
 **/
@Repository
public class DAGEventLogRepoImpl implements DAGEventLogRepo {

    private final DAGEventLogDao dagEventLogDao;

    @Autowired
    public DAGEventLogRepoImpl(DAGEventLogDao dagEventLogDao) {
        this.dagEventLogDao = dagEventLogDao;
    }

    @Override
    public Long create(DAGEventLog eventLog) {
        int ret = dagEventLogDao.insertSelective(eventLog);
        if (ret <= 0) return null;
        return eventLog.getId();
    }

    @Override
    public Boolean update(DAGEventLog eventLog) {
        int ret = dagEventLogDao.updateByPrimaryKeySelective(eventLog);
        return ret > 0;
    }

    @Override
    public Optional<DAGEventLog> query(Long id) {
        Optional<DAGEventLog> optional = dagEventLogDao.selectByPrimaryKey(id);
        if (optional.isPresent() && DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public List<DAGEventLog> query(Long dagId, String event, Integer status) {
        return dagEventLogDao.select(dsl -> dsl.where(DAG_EVENT_LOG.dagId, isEqualTo(dagId),
                and(DAG_EVENT_LOG.dagEvent, isEqualTo(event)),
                and(DAG_EVENT_LOG.handleStatus, isEqualTo(status)),
                and(DAG_EVENT_LOG.del, isEqualTo(DEL_NO.val))));
    }

    @Override
    public List<DAGEventLog> query(Long dagId, Integer status) {
        return dagEventLogDao.select(dsl -> dsl.where(DAG_EVENT_LOG.dagId, isEqualTo(dagId),
                and(DAG_EVENT_LOG.handleStatus, isEqualTo(status)),
                and(DAG_EVENT_LOG.del, isEqualTo(DEL_NO.val))));
    }
}
