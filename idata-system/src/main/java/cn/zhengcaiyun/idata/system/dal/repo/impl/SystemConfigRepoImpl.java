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
package cn.zhengcaiyun.idata.system.dal.repo.impl;

import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.repo.SystemConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-11-04 下午3:08
 */

@Service
public class SystemConfigRepoImpl implements SystemConfigRepo {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public List<SysConfig> getConfigsByType(String configType) {
        return sysConfigDao.select(c -> c.where(sysConfig.del, isNotEqualTo(1),
                and(sysConfig.type, isEqualTo(configType))));
    }

    @Override
    public SysConfig getConfigById(Long configId) {
        return sysConfigDao.selectOne(c -> c.where(sysConfig.del, isNotEqualTo(1),
                and(sysConfig.id, isEqualTo(configId)))).orElse(null);
    }

    @Override
    public boolean updateById(SysConfig config) {
        return sysConfigDao.update(c ->
                c.set(del).equalToWhenPresent(config::getDel)
                .set(editor).equalToWhenPresent(config::getEditor)
                .set(editTime).equalToWhenPresent(config::getEditTime)
                .set(keyOne).equalToWhenPresent(config::getKeyOne)
                .set(valueOne).equalToWhenPresent(config::getValueOne)
                .set(type).equalToWhenPresent(config::getType)
                .where(id, isEqualTo(config::getId))
        ) > 0;
    }
}
