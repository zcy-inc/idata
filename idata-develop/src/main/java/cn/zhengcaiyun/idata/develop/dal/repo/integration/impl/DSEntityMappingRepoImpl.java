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

import cn.zhengcaiyun.idata.develop.dal.dao.integration.DSEntityMappingDao;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.integration.DSEntityMappingDynamicSqlSupport.DS_ENTITY_MAPPING;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-10 16:03
 **/
@Repository
public class DSEntityMappingRepoImpl implements DSEntityMappingRepo {

    private final DSEntityMappingDao dsEntityMappingDao;

    @Autowired
    public DSEntityMappingRepoImpl(DSEntityMappingDao dsEntityMappingDao) {
        this.dsEntityMappingDao = dsEntityMappingDao;
    }

    @Override
    public Long create(DSEntityMapping mapping) {
        dsEntityMappingDao.insertSelective(mapping);
        return mapping.getId();
    }

    @Override
    public Optional<Long> queryDsEntityCode(Long entityId, String dsEntityType, String environment) {
        Optional<DSEntityMapping> optional = dsEntityMappingDao.selectOne(dsl -> dsl.where(DS_ENTITY_MAPPING.entityId, isEqualTo(entityId),
                and(DS_ENTITY_MAPPING.dsEntityType, isEqualTo(dsEntityType)),
                and(DS_ENTITY_MAPPING.environment, isEqualTo(environment))));
        return Optional.ofNullable(optional.isPresent() ? optional.get().getDsEntityCode() : null);
    }

    @Override
    public Optional<Long> queryEntityId(Long dsEntityCode, String dsEntityType, String environment) {
        Optional<DSEntityMapping> optional = dsEntityMappingDao.selectOne(dsl -> dsl.where(DS_ENTITY_MAPPING.dsEntityCode, isEqualTo(dsEntityCode),
                and(DS_ENTITY_MAPPING.dsEntityType, isEqualTo(dsEntityType)),
                and(DS_ENTITY_MAPPING.environment, isEqualTo(environment))));
        return Optional.ofNullable(optional.isPresent() ? optional.get().getEntityId() : null);
    }
}
