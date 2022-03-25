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
package cn.zhengcaiyun.idata.develop.dal.repo.job.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
import cn.zhengcaiyun.idata.develop.dal.repo.job.SqlJobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlDynamicSqlSupport.devJobContentSql;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author caizhedong
 * @date 2021-11-25 上午9:18
 */
@Service
public class SqlJobRepoImpl implements SqlJobRepo {

    @Autowired
    private DevJobContentSqlDao devJobContentSqlDao;

    @Override
    public DevJobContentSql query(Long jobId, Integer version) {
        return devJobContentSqlDao.selectOne(c ->
                c.where(devJobContentSql.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentSql.jobId, isEqualTo(jobId)),
                        and(devJobContentSql.version, isEqualTo(version))))
                .orElse(null);
    }

    @Override
    public List<DevJobContentSql> queryList(Long jobId) {
        return devJobContentSqlDao.select(c ->
                c.where(devJobContentSql.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentSql.jobId, isEqualTo(jobId))));
    }

    @Override
    public Optional<DevJobContentSql> queryLatest(Long jobId) {
        return devJobContentSqlDao.selectOne(dsl -> dsl.where(devJobContentSql.jobId, isEqualTo(jobId),
                        and(devJobContentSql.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(devJobContentSql.version.descending())
                .limit(1));
    }

    @Override
    public boolean add(DevJobContentSql jobContentSql) {
        devJobContentSqlDao.insertSelective(jobContentSql);
        return true;
    }

    @Override
    public boolean update(DevJobContentSql jobContentSql) {
        devJobContentSqlDao.updateByPrimaryKeySelective(jobContentSql);
        return true;
    }

    @Override
    public Integer newVersion(Long jobId) {
        DevJobContentSql jobContentSql = devJobContentSqlDao.selectOne(c -> c.where(devJobContentSql.jobId, isEqualTo(jobId))
                .orderBy(devJobContentSql.version.descending()).limit(1)).orElse(null);
        return jobContentSql != null ? jobContentSql.getVersion() + 1 : 1;
    }

    @Override
    public Boolean updateEditable(Long id, EditableEnum editable, String operator) {
        devJobContentSqlDao.update(dsl -> dsl.set(devJobContentSql.editable).equalTo(editable.val)
                .set(devJobContentSql.editor).equalTo(operator)
                .where(devJobContentSql.id, isEqualTo(id)));
        return Boolean.TRUE;
    }
}
