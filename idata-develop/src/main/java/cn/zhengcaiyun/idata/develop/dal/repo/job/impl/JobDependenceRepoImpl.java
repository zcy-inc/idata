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

import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-16 14:37
 **/
@Repository
public class JobDependenceRepoImpl implements JobDependenceRepo {

    private final JobDependenceRepo jobDependenceRepo;

    @Autowired
    public JobDependenceRepoImpl(JobDependenceRepo jobDependenceRepo) {
        this.jobDependenceRepo = jobDependenceRepo;
    }

    @Override
    public List<JobDependence> queryPrevJob(Long jobId) {
        return null;
    }

    @Override
    public List<JobDependence> queryPostJob(Long jobId) {
        return null;
    }

    @Override
    public Boolean addDependence(List<JobDependence> dependenceList) {
        return null;
    }

    @Override
    public Boolean deleteDependence(Long jobId) {
        return null;
    }
}
