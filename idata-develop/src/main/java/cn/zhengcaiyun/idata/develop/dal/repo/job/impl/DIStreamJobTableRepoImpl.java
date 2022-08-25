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

import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableCustomizeDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobTable;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobTableRepo;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableDynamicSqlSupport.DI_STREAM_JOB_TABLE;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-16 17:44
 **/
@Repository
public class DIStreamJobTableRepoImpl implements DIStreamJobTableRepo {

    private final DIStreamJobTableDao streamJobTableDao;
    private final DIStreamJobTableCustomizeDao streamJobTableCustomizeDao;

    @Autowired
    public DIStreamJobTableRepoImpl(DIStreamJobTableDao streamJobTableDao,
                                    DIStreamJobTableCustomizeDao streamJobTableCustomizeDao) {
        this.streamJobTableDao = streamJobTableDao;
        this.streamJobTableCustomizeDao = streamJobTableCustomizeDao;
    }

    @Override
    public void save(List<DIStreamJobTable> contents) {
//        streamJobTableCustomizeDao.insertMultiple(contents);
    }

    @Override
    public int delete(Long jobId, Integer version) {
        return streamJobTableDao.delete(dsl -> dsl.where(DI_STREAM_JOB_TABLE.jobId, isEqualTo(jobId),
                and(DI_STREAM_JOB_TABLE.jobContentVersion, isEqualTo(version))));
    }

    @Override
    public List<DIStreamJobTable> query(Long jobId, Integer version) {
        return streamJobTableDao.select(dsl -> dsl.where(DI_STREAM_JOB_TABLE.jobId, isEqualTo(jobId),
                and(DI_STREAM_JOB_TABLE.jobContentVersion, isEqualTo(version))));
    }

    @Override
    public Set<Long> queryJobIdBySrcTable(String srcTable) {
        List<DIStreamJobTable> streamJobTables = streamJobTableDao.select(dsl -> dsl.where(DI_STREAM_JOB_TABLE.srcTable, isEqualTo(srcTable)));
        if (CollectionUtils.isEmpty(streamJobTables)) {
            return Sets.newHashSet();
        }
        return streamJobTables.stream()
                .map(DIStreamJobTable::getJobId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> queryJobIdByShardingTable(String srcBaseTable) {
        List<DIStreamJobTable> streamJobTables = streamJobTableDao.select(dsl -> dsl.where(DI_STREAM_JOB_TABLE.srcTable, isLike(srcBaseTable).map(MybatisHelper::appendRightWildCards)));
        if (CollectionUtils.isEmpty(streamJobTables)) {
            return Sets.newHashSet();
        }
        return streamJobTables.stream()
                .map(DIStreamJobTable::getJobId)
                .collect(Collectors.toSet());
    }
}
