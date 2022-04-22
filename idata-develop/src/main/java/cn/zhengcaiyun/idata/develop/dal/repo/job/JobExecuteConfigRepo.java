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

package cn.zhengcaiyun.idata.develop.dal.repo.job;

import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-27 16:14
 **/
public interface JobExecuteConfigRepo {

    Long save(JobExecuteConfig config);

    Boolean update(JobExecuteConfig config);

    Boolean switchRunningState(Long id, RunningStateEnum runningState, String operator);

    Optional<JobExecuteConfig> query(Long jobId, String environment);

    List<JobExecuteConfig> queryList(Long jobId, JobExecuteConfigCondition condition);

    Long countDagJob(Long dagId);

    List<JobExecuteConfig> queryDagJobList(Long dagId, JobExecuteConfigCondition condition);

    List<JobExecuteConfig> queryList(JobExecuteConfigCondition condition);

    List<JobExecuteConfig> queryList(List<Long> jobIds, String environment);
}
