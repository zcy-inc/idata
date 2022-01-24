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

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceFileCondition;
import cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceFileDao;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSourceFile;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceFileDynamicSqlSupport.dataSourceFile;
import static java.util.Objects.nonNull;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:36
 **/
@Repository
public class DataSourceFileRepoImpl implements DataSourceFileRepo {

    private final DataSourceFileDao dataSourceFileDao;

    @Autowired
    public DataSourceFileRepoImpl(DataSourceFileDao dataSourceFileDao) {
        this.dataSourceFileDao = dataSourceFileDao;
    }

    @Override
    public Page<DataSourceFile> pagingDataSourceFile(DataSourceFileCondition condition, PageParam pageParam) {
        long total = countDataSourceFile(condition);
        List<DataSourceFile> dataSourceFiles = null;
        if (total > 0) {
            dataSourceFiles = queryDataSourceFile(condition, pageParam.getLimit(), pageParam.getOffset());
        }
        return Page.newOne(dataSourceFiles, total);
    }

    @Override
    public List<DataSourceFile> queryDataSourceFile(DataSourceFileCondition condition, long limit, long offset) {
        String type = nonNull(condition.getType()) ? condition.getType().name() : null;
        String env = nonNull(condition.getEnv()) ? condition.getEnv().name() : null;
        return dataSourceFileDao.select(dsl -> dsl.where(
                                dataSourceFile.type, isEqualToWhenPresent(type),
                                and(dataSourceFile.environments, isLikeWhenPresent(env).map(MybatisHelper::appendWildCards)),
                                and(dataSourceFile.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                                and(dataSourceFile.del, isEqualTo(DEL_NO.val))
                        ).orderBy(dataSourceFile.id.descending())
                        .limit(limit).offset(offset)
        );
    }

    @Override
    public long countDataSourceFile(DataSourceFileCondition condition) {
        String type = nonNull(condition.getType()) ? condition.getType().name() : null;
        String env = nonNull(condition.getEnv()) ? condition.getEnv().name() : null;
        return dataSourceFileDao.count(dsl -> dsl.where(
                dataSourceFile.type, isEqualToWhenPresent(type),
                and(dataSourceFile.environments, isLikeWhenPresent(env).map(MybatisHelper::appendWildCards)),
                and(dataSourceFile.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                and(dataSourceFile.del, isEqualTo(DEL_NO.val))
        ));
    }

    @Override
    public Optional<DataSourceFile> queryDataSourceFile(Long id) {
        return dataSourceFileDao.selectByPrimaryKey(id);
    }

    @Override
    public Long createDataSourceFile(DataSourceFile file) {
        int ret = dataSourceFileDao.insertSelective(file);
        if (ret <= 0) return null;
        return file.getId();
    }
}
