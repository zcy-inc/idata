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

package cn.zhengcaiyun.idata.develop.dal.repo.integration.impl;

import cn.zhengcaiyun.idata.develop.dal.dao.integration.DSDependenceNodeDao;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependenceNode;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.integration.DSDependenceNodeDynamicSqlSupport.DS_DEPENDENCE_NODE;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-15 14:18
 **/
@Repository
public class DSDependenceNodeRepoImpl implements DSDependenceNodeRepo {

    private final DSDependenceNodeDao dsDependenceNodeDao;

    @Autowired
    public DSDependenceNodeRepoImpl(DSDependenceNodeDao dsDependenceNodeDao) {
        this.dsDependenceNodeDao = dsDependenceNodeDao;
    }


    @Override
    public Long create(DSDependenceNode dependenceNode) {
        dsDependenceNodeDao.insertSelective(dependenceNode);
        return dependenceNode.getId();
    }

    @Override
    public Boolean delete(Long id) {
        dsDependenceNodeDao.deleteByPrimaryKey(id);
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<Long> ids) {
        dsDependenceNodeDao.delete(dsl -> dsl.where(DS_DEPENDENCE_NODE.id, isIn(ids)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteByDependenceNode(Long dependenceNodeCode) {
        dsDependenceNodeDao.delete(dsl -> dsl.where(DS_DEPENDENCE_NODE.dependenceNodeCode, isEqualTo(dependenceNodeCode)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteByTaskRelation(Long taskCode, Long prevTaskCode) {
        dsDependenceNodeDao.delete(dsl -> dsl.where(DS_DEPENDENCE_NODE.taskCode, isEqualTo(taskCode),
                and(DS_DEPENDENCE_NODE.prevTaskCode, isEqualTo(prevTaskCode))));
        return Boolean.TRUE;
    }

    @Override
    public List<DSDependenceNode> queryDependenceNodeInWorkflow(Long workflowCode, Long prevTaskCode) {
        return dsDependenceNodeDao.select(dsl -> dsl.where(DS_DEPENDENCE_NODE.workflowCode, isEqualTo(workflowCode),
                and(DS_DEPENDENCE_NODE.prevTaskCode, isEqualTo(prevTaskCode))));
    }

    @Override
    public List<DSDependenceNode> queryDepNodeByPrevTask(Long prevTaskCode) {
        return dsDependenceNodeDao.select(dsl -> dsl.where(DS_DEPENDENCE_NODE.prevTaskCode, isEqualTo(prevTaskCode)));
    }

    @Override
    public List<DSDependenceNode> queryDependenceNodeInWorkflow(Long taskCode, Long workflowCode, Long prevTaskCode) {
        return dsDependenceNodeDao.select(dsl -> dsl.where(DS_DEPENDENCE_NODE.workflowCode, isEqualTo(workflowCode),
                and(DS_DEPENDENCE_NODE.prevTaskCode, isEqualTo(prevTaskCode)),
                and(DS_DEPENDENCE_NODE.taskCode, isEqualTo(taskCode))));
    }

    @Override
    public List<DSDependenceNode> queryByDependenceNode(Long dependenceNodeCode) {
        return dsDependenceNodeDao.select(dsl -> dsl.where(DS_DEPENDENCE_NODE.dependenceNodeCode, isEqualTo(dependenceNodeCode)));
    }
}
