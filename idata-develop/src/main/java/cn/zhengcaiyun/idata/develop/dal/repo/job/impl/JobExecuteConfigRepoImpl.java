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
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDynamicSqlSupport.jobExecuteConfig;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-27 16:14
 **/
@Repository
public class JobExecuteConfigRepoImpl implements JobExecuteConfigRepo {

    private final JobExecuteConfigDao jobExecuteConfigDao;

    @Autowired
    public JobExecuteConfigRepoImpl(JobExecuteConfigDao jobExecuteConfigDao) {
        this.jobExecuteConfigDao = jobExecuteConfigDao;
    }

    @Override
    public Long save(JobExecuteConfig config) {
        int ret = jobExecuteConfigDao.insertSelective(config);
        if (ret <= 0) return null;
        return config.getId();
    }

    @Override
    public Boolean update(JobExecuteConfig config) {
        jobExecuteConfigDao.updateByPrimaryKeySelective(config);
        return Boolean.TRUE;
    }

    @Override
    public Boolean switchRunningState(Long id, RunningStateEnum runningState, String operator) {
        jobExecuteConfigDao.update(dsl -> dsl.set(jobExecuteConfig.runningState).equalTo(runningState.val)
                .set(jobExecuteConfig.editor).equalTo(operator)
                .where(jobExecuteConfig.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Optional<JobExecuteConfig> query(Long jobId, String environment) {
        return jobExecuteConfigDao.selectOne(dsl -> dsl.where(jobExecuteConfig.jobId, isEqualTo(jobId),
                and(jobExecuteConfig.environment, isEqualTo(environment)),
                and(jobExecuteConfig.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobExecuteConfig> queryList(Long jobId, JobExecuteConfigCondition condition) {
        return jobExecuteConfigDao.select(dsl -> dsl.where(jobExecuteConfig.jobId, isEqualTo(jobId),
                and(jobExecuteConfig.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(jobExecuteConfig.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Long countDagJob(Long dagId) {
        return jobExecuteConfigDao.count(dsl -> dsl.where(jobExecuteConfig.schDagId, isEqualTo(dagId),
                and(jobExecuteConfig.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobExecuteConfig> queryDagJobList(Long dagId, JobExecuteConfigCondition condition) {
        return jobExecuteConfigDao.select(dsl -> dsl.where(jobExecuteConfig.schDagId, isEqualTo(dagId),
                and(jobExecuteConfig.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(jobExecuteConfig.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobExecuteConfig> queryList(JobExecuteConfigCondition condition) {
        return jobExecuteConfigDao.select(dsl -> dsl.where(jobExecuteConfig.environment, isEqualToWhenPresent(condition.getEnvironment()),
                and(jobExecuteConfig.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

}
