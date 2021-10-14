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

import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 16:56
 **/
public interface JobInfoRepo {

    Long saveJobInfo(JobInfo info);

    Boolean updateJobInfo(JobInfo info);

    Optional<JobInfo> queryJobInfo(Long id);

    Boolean deleteJobInfo(Long id, String operator);

    Boolean deleteJobAndSubInfo(JobInfo jobInfo, String operator);

    List<JobInfo> queryJobInfoByName(String name);

    Long countJobInfo(JobInfoCondition condition);

    List<JobInfo> queryJobInfo(JobInfoCondition condition);

    List<JobInfo> queryJobInfo(List<Long> ids);

    long count(JobInfoCondition condition);
}
