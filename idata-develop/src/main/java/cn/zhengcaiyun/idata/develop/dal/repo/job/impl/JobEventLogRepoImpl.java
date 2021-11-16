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

import cn.zhengcaiyun.idata.develop.dal.dao.job.JobEventLogDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobEventLog;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobEventLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobEventLogDynamicSqlSupport.JOB_EVENT_LOG;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-16 11:40
 **/
@Repository
public class JobEventLogRepoImpl implements JobEventLogRepo {

    private final JobEventLogDao jobEventLogDao;

    @Autowired
    public JobEventLogRepoImpl(JobEventLogDao jobEventLogDao) {
        this.jobEventLogDao = jobEventLogDao;
    }

    @Override
    public Long create(JobEventLog eventLog) {
        jobEventLogDao.insertSelective(eventLog);
        return eventLog.getId();
    }

    @Override
    public Boolean update(JobEventLog eventLog) {
        return jobEventLogDao.updateByPrimaryKeySelective(eventLog) > 0;
    }

    @Override
    public Optional<JobEventLog> query(Long id) {
        Optional<JobEventLog> optional = jobEventLogDao.selectByPrimaryKey(id);
        if (optional.isPresent() && DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public List<JobEventLog> query(Long jobId, String event, Integer status) {
        return jobEventLogDao.select(dsl -> dsl.where(JOB_EVENT_LOG.jobId, isEqualTo(jobId),
                and(JOB_EVENT_LOG.jobEvent, isEqualTo(event)),
                and(JOB_EVENT_LOG.handleStatus, isEqualTo(status)),
                and(JOB_EVENT_LOG.del, isEqualTo(DEL_NO.val))));
    }

    @Override
    public List<JobEventLog> query(Long jobId, Integer status) {
        return jobEventLogDao.select(dsl -> dsl.where(JOB_EVENT_LOG.jobId, isEqualTo(jobId),
                and(JOB_EVENT_LOG.handleStatus, isEqualTo(status)),
                and(JOB_EVENT_LOG.del, isEqualTo(DEL_NO.val))));
    }
}
