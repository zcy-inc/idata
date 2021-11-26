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
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentKylinDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentKylin;
import cn.zhengcaiyun.idata.develop.dal.repo.job.KylinJobRepo;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDynamicSqlSupport.DI_JOB_CONTENT;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentKylinDynamicSqlSupport.devJobContentKylin;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-11-22 下午5:36
 */

@Repository
public class KylinJobRepoImpl implements KylinJobRepo {

    @Autowired
    private DevJobContentKylinDao devJobContentKylinDao;

    @Override
    public DevJobContentKylin query(Long jobId, Integer version) {
        return devJobContentKylinDao.selectOne(c ->
                c.where(devJobContentKylin.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentKylin.jobId, isEqualTo(jobId)),
                        and(devJobContentKylin.version, isEqualTo(version))))
                .orElse(null);
    }

    @Override
    public List<DevJobContentKylin> queryList(Long jobId) {
        return devJobContentKylinDao.select(c ->
                c.where(devJobContentKylin.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentKylin.jobId, isEqualTo(jobId))));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean add(DevJobContentKylin jobContentKylin) {
        devJobContentKylinDao.insertSelective(jobContentKylin);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean update(DevJobContentKylin jobContentKylin) {
        devJobContentKylinDao.updateByPrimaryKeySelective(jobContentKylin);
        return true;
    }

    @Override
    public Integer newVersion(Long jobId) {
        DevJobContentKylin jobContentKylin = devJobContentKylinDao.selectOne(c -> c.where(devJobContentKylin.jobId, isEqualTo(jobId))
                .orderBy(devJobContentKylin.version.descending()).limit(1)).orElse(null);
        return jobContentKylin != null ? jobContentKylin.getVersion() + 1 : 1;
    }
}
