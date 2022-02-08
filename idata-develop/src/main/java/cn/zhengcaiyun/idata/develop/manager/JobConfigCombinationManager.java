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

package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobConfigCombination;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobOutputRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-19 11:13
 **/
@Component
public class JobConfigCombinationManager {

    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobOutputRepo jobOutputRepo;
    private final JobDependenceRepo jobDependenceRepo;

    @Autowired
    public JobConfigCombinationManager(JobExecuteConfigRepo jobExecuteConfigRepo,
                                       JobOutputRepo jobOutputRepo,
                                       JobDependenceRepo jobDependenceRepo) {
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobOutputRepo = jobOutputRepo;
        this.jobDependenceRepo = jobDependenceRepo;
    }

    public Optional<JobConfigCombination> getCombineConfig(Long jobId, String environment) {
        Optional<JobExecuteConfig> executeConfigOptional = jobExecuteConfigRepo.query(jobId, environment);
        if (executeConfigOptional.isEmpty())
            return Optional.empty();

        JobExecuteConfig existConfig = executeConfigOptional.get();
        List<JobDependence> dependenceList = jobDependenceRepo.queryPrevJob(jobId, environment);
        Optional<JobOutput> outputOptional = jobOutputRepo.query(jobId, environment);
        JobConfigCombination combination = new JobConfigCombination(existConfig, dependenceList, outputOptional);
        return Optional.of(combination);
    }
}
