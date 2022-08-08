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
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDynamicSqlSupport.DI_STREAM_JOB_CONTENT;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-06-24 14:10
 **/
@Repository
public class DIStreamJobContentRepoImpl implements DIStreamJobContentRepo {

    private final DIStreamJobContentDao diStreamJobContentDao;

    @Autowired
    public DIStreamJobContentRepoImpl(DIStreamJobContentDao diStreamJobContentDao) {
        this.diStreamJobContentDao = diStreamJobContentDao;
    }

    @Override
    public Optional<DIStreamJobContent> query(Long jobId, Integer version) {
        return diStreamJobContentDao.selectOne(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId),
                and(DI_STREAM_JOB_CONTENT.version, isEqualTo(version)),
                and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }
}
