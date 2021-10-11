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

package cn.zhengcaiyun.idata.datasource.dal.repo.impl;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceDao;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceDynamicSqlSupport.dataSource;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-15 16:47
 **/
@Repository
public class DataSourceRepoImpl implements DataSourceRepo {

    private final DataSourceDao dataSourceDao;

    @Autowired
    public DataSourceRepoImpl(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

    @Override
    public Page<DataSource> pagingDataSource(DataSourceCondition condition, PageParam pageParam) {
        long total = countDataSource(condition);
        List<DataSource> dataSources = null;
        if (total > 0) {
            dataSources = queryDataSource(condition, pageParam.getLimit(), pageParam.getOffset());
        }
        return Page.newOne(dataSources, total);
    }

    @Override
    public List<DataSource> queryDataSource(DataSourceCondition condition, long limit, long offset) {
        return dataSourceDao.select(dsl -> dsl.where(
                                dataSource.type, isEqualToWhenPresent(condition.getType()).map(DataSourceTypeEnum::name),
                                and(dataSource.environments, isLikeWhenPresent(condition.getEnv()).map(EnvEnum::name).map(MybatisHelper::appendWildCards)),
                                and(dataSource.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                                and(dataSource.del, isEqualTo(DEL_NO.val))
                        ).orderBy(dataSource.id.descending())
                        .limit(limit).offset(offset)
        );
    }

    @Override
    public long countDataSource(DataSourceCondition condition) {
        return dataSourceDao.count(dsl -> dsl.where(
                dataSource.type, isEqualToWhenPresent(condition.getType()).map(DataSourceTypeEnum::name),
                and(dataSource.environments, isLikeWhenPresent(condition.getEnv()).map(EnvEnum::name).map(MybatisHelper::appendWildCards)),
                and(dataSource.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                and(dataSource.del, isEqualTo(DEL_NO.val))
        ));
    }

    @Override
    public Optional<DataSource> queryDataSource(Long id) {
        return dataSourceDao.selectByPrimaryKey(id);
    }

    @Override
    public Long createDataSource(DataSource source) {
        int ret = dataSourceDao.insertSelective(source);
        if (ret <= 0) return null;
        return source.getId();
    }

    @Override
    public boolean updateDataSource(DataSource source) {
        int ret = dataSourceDao.updateByPrimaryKeySelective(source);
        return ret > 0;
    }

    @Override
    public boolean deleteDataSource(Long id, String operator) {
        int ret = dataSourceDao.update(dsl -> dsl.set(dataSource.del).equalTo(DEL_YES.val)
                .set(dataSource.editor).equalTo(operator)
                .where(dataSource.id, isEqualTo(id)));
        return ret > 0;
    }

    @Override
    public List<DataSource> queryDataSource(String name) {
        return dataSourceDao.select(dsl -> dsl.where(dataSource.name, isEqualTo(name),
                and(dataSource.del, isEqualTo(DEL_NO.val))));
    }
}
