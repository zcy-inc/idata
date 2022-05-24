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
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSparkDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSpark;
import cn.zhengcaiyun.idata.develop.dal.repo.job.SparkJobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSparkDynamicSqlSupport.devJobContentSpark;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-11-24 下午7:07
 */
@Repository
public class SparkJobRepoImpl implements SparkJobRepo {

    @Autowired
    private DevJobContentSparkDao devJobContentSparkDao;

    @Override
    public DevJobContentSpark query(Long jobId, Integer version) {
        return devJobContentSparkDao.selectOne(c ->
                        c.where(devJobContentSpark.del, isEqualTo(DeleteEnum.DEL_NO.val),
                                and(devJobContentSpark.jobId, isEqualTo(jobId)),
                                and(devJobContentSpark.version, isEqualTo(version))))
                .orElse(null);
    }

    @Override
    public List<DevJobContentSpark> queryList(Long jobId) {
        return devJobContentSparkDao.select(c ->
                c.where(devJobContentSpark.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentSpark.jobId, isEqualTo(jobId))));
    }

    @Override
    public boolean add(DevJobContentSpark jobContentSpark) {
        devJobContentSparkDao.insertSelective(jobContentSpark);
        return true;
    }

    @Override
    public boolean update(DevJobContentSpark jobContentSpark) {
        devJobContentSparkDao.updateByPrimaryKeySelective(jobContentSpark);
        return true;
    }

    @Override
    public Integer newVersion(Long jobId) {
        DevJobContentSpark jobContentSpark = devJobContentSparkDao.selectOne(c -> c.where(devJobContentSpark.jobId, isEqualTo(jobId))
                .orderBy(devJobContentSpark.version.descending()).limit(1)).orElse(null);
        return jobContentSpark != null ? jobContentSpark.getVersion() + 1 : 1;
    }

    @Override
    public Boolean updateEditable(Long id, EditableEnum editable, String operator) {
        devJobContentSparkDao.update(dsl -> dsl.set(devJobContentSpark.editable).equalTo(editable.val)
                .set(devJobContentSpark.editor).equalTo(operator)
                .where(devJobContentSpark.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Optional<DevJobContentSpark> queryLatest(Long jobId) {
        return devJobContentSparkDao.selectOne(dsl -> dsl.where(devJobContentSpark.jobId, isEqualTo(jobId),
                        and(devJobContentSpark.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(devJobContentSpark.version.descending())
                .limit(1));
    }

    @Override
    public List<DevJobContentSpark> queryList(List<Long> ids) {
        return devJobContentSparkDao.select(c ->
                c.where(devJobContentSpark.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentSpark.id, isIn(ids))));
    }
}
