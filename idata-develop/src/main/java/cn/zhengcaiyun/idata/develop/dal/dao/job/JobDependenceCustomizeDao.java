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

package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceDynamicSqlSupport.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-18 15:19
 **/
@Mapper
public interface JobDependenceCustomizeDao {
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, jobId, environment, prevJobId, prevJobDagId);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<JobDependence> insertStatement);

    default int insertMultiple(JobDependence... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<JobDependence> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, JOB_DEPENDENCE, c ->
                c.map(del).toProperty("del")
                        .map(creator).toProperty("creator")
                        .map(jobId).toProperty("jobId")
                        .map(environment).toProperty("environment")
                        .map(prevJobId).toProperty("prevJobId")
                        .map(prevJobDagId).toProperty("prevJobDagId")
        );
    }
}
