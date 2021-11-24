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
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentScriptDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentScript;
import cn.zhengcaiyun.idata.develop.dal.repo.job.ScriptJobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentScriptDynamicSqlSupport.devJobContentScript;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author caizhedong
 * @date 2021-11-24 下午7:08
 */

@Repository
public class ScriptJobRepoImpl implements ScriptJobRepo {

    @Autowired
    private DevJobContentScriptDao devJobContentScriptDao;

    @Override
    public DevJobContentScript query(Long jobId, Integer version) {
        return devJobContentScriptDao.selectOne(c ->
                c.where(devJobContentScript.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(devJobContentScript.jobId, isEqualTo(jobId)),
                        and(devJobContentScript.version, isEqualTo(version))))
                .orElse(null);
    }

    @Override
    public boolean add(DevJobContentScript jobContentScript) {
        devJobContentScriptDao.insertSelective(jobContentScript);
        return true;
    }

    @Override
    public boolean update(DevJobContentScript jobContentScript) {
        devJobContentScriptDao.updateByPrimaryKeySelective(jobContentScript);
        return true;
    }

    @Override
    public Integer newVersion(Long jobId) {
        DevJobContentScript jobContentScript = devJobContentScriptDao.selectOne(c -> c.where(devJobContentScript.jobId, isEqualTo(jobId))
                .orderBy(devJobContentScript.version.descending()).limit(1)).orElse(null);
        return jobContentScript != null ? jobContentScript.getVersion() + 1 : 1;
    }
}
