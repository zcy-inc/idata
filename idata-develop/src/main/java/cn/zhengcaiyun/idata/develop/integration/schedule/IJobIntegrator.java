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

package cn.zhengcaiyun.idata.develop.integration.schedule;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.JobRunOverviewDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskCountDto;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:40
 **/
public interface IJobIntegrator {

    void create(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment);

    void update(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment);

    void delete(JobInfo jobInfo, String environment);

    void enableRunning(JobInfo jobInfo, String environment);

    void disableRunning(JobInfo jobInfo, String environment);

    void publish(JobInfo jobInfo, String environment);

    void bindDag(JobInfo jobInfo, Long dagId, String environment);

    void unBindDag(JobInfo jobInfo, Long dagId, String environment);

    void run(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, boolean runPost);

    void buildJobRelation(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, List<DagJobPair> addingPrevRelations, List<DagJobPair> removingPrevRelations);

    String queryLog(Long jobId, String environment, Integer jobInstanceId, Integer lineNum, Integer skipLineNum);

    List<JobRunOverviewDto> getJobLatestRecords(String environment, Integer limit);

    /**
     * 按任务状态分组统计
     * @param environment
     * @param startTime
     * @param endTime
     * @return
     */
    List<TaskCountDto> getTaskCountGroupState(String environment, Date startTime, Date endTime);
}
