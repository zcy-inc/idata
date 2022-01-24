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

import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dto.JobDependencyDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-16 14:36
 **/
public interface JobDependenceRepo {

    List<JobDependence> queryPrevJob(Long jobId, String environment);

    List<JobDependence> queryPrevJob(Long jobId);

    List<JobDependence> queryPostJob(Long jobId, String environment);

    List<JobDependence> queryPostJob(Long jobId);

    Boolean addDependence(List<JobDependence> dependenceList);

    Boolean deleteDependence(Long jobId, String environment);

    List<JobDependencyDto> queryJobs(String env);

    List<JobDependence> queryJobs();
}
