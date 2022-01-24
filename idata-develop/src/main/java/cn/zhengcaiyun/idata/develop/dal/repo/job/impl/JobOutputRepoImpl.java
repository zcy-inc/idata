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
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobOutputRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputDynamicSqlSupport.JOB_OUTPUT;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-18 15:14
 **/
@Repository
public class JobOutputRepoImpl implements JobOutputRepo {

    private final JobOutputDao jobOutputDao;

    @Autowired
    public JobOutputRepoImpl(JobOutputDao jobOutputDao) {
        this.jobOutputDao = jobOutputDao;
    }


    @Override
    public Long save(JobOutput output) {
        jobOutputDao.insertSelective(output);
        return output.getId();
    }

    @Override
    public Optional<JobOutput> query(Long jobId, String environment) {
        List<JobOutput> outputs = jobOutputDao.select(dsl -> dsl.where(JOB_OUTPUT.jobId, isEqualTo(jobId),
                and(JOB_OUTPUT.environment, isEqualTo(environment)),
                and(JOB_OUTPUT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
        return Optional.ofNullable(outputs.size() > 0 ? outputs.get(0) : null);
    }

    @Override
    public Boolean delete(Long jobId, String environment) {
        return jobOutputDao.delete(dsl -> dsl.where(JOB_OUTPUT.jobId, isEqualTo(jobId),
                and(JOB_OUTPUT.environment, isEqualTo(environment)))) > 0;
    }
}
