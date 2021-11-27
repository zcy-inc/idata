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
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceCustomizeDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceDynamicSqlSupport.JOB_DEPENDENCE;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-16 14:37
 **/
@Repository
public class JobDependenceRepoImpl implements JobDependenceRepo {

    private final JobDependenceDao jobDependenceDao;
    private final JobDependenceCustomizeDao jobDependenceCustomizeDao;

    @Autowired
    public JobDependenceRepoImpl(JobDependenceDao jobDependenceDao,
                                 JobDependenceCustomizeDao jobDependenceCustomizeDao) {
        this.jobDependenceDao = jobDependenceDao;
        this.jobDependenceCustomizeDao = jobDependenceCustomizeDao;
    }

    @Override
    public List<JobDependence> queryPrevJob(Long jobId, String environment) {
        return jobDependenceDao.select(dsl -> dsl.where(JOB_DEPENDENCE.jobId, isEqualTo(jobId),
                and(JOB_DEPENDENCE.environment, isEqualTo(environment)),
                and(JOB_DEPENDENCE.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobDependence> queryPostJob(Long jobId, String environment) {
        return jobDependenceDao.select(dsl -> dsl.where(JOB_DEPENDENCE.prevJobId, isEqualTo(jobId),
                and(JOB_DEPENDENCE.environment, isEqualTo(environment)),
                and(JOB_DEPENDENCE.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobDependence> queryPostJob(Long jobId) {
        return jobDependenceDao.select(dsl -> dsl.where(JOB_DEPENDENCE.prevJobId, isEqualTo(jobId),
                and(JOB_DEPENDENCE.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Boolean addDependence(List<JobDependence> dependenceList) {
        return jobDependenceCustomizeDao.insertMultiple(dependenceList) > 0;
    }

    @Override
    public Boolean deleteDependence(Long jobId, String environment) {
        return jobDependenceDao.delete(dsl -> dsl.where(JOB_DEPENDENCE.jobId, isEqualTo(jobId),
                and(JOB_DEPENDENCE.environment, isEqualTo(environment)))) > 0;
    }
}
