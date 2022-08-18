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

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableDynamicSqlSupport.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-16 19:23
 **/
@Mapper
public interface DIStreamJobTableCustomizeDao {
    BasicColumn[] selectList = BasicColumn.columnList(id, jobId, jobContentId, jobContentVersion, srcTable, destTable, sharding, forceInit, tableCdcProps);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<DIStreamJobTable> insertStatement);

    default int insertMultiple(DIStreamJobTable... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<DIStreamJobTable> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, DI_STREAM_JOB_TABLE, c ->
                c.map(jobContentId).toProperty("jobContentId")
                        .map(jobContentVersion).toProperty("jobContentVersion")
                        .map(srcTable).toProperty("srcTable")
                        .map(destTable).toProperty("destTable")
                        .map(sharding).toProperty("sharding")
                        .map(forceInit).toProperty("forceInit")
                        .map(tableCdcProps).toProperty("tableCdcProps")
        );
    }

}
