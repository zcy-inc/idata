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

package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dto.job.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 17:06
 **/
public interface JobInfoService {

    Long addJob(JobInfoDto dto, Operator operator) throws IllegalAccessException;

    Boolean editJobInfo(JobInfoDto dto, Operator operator) throws IllegalAccessException;

    JobInfoDto getJobInfo(Long id);

    JobDetailsDto getJobDetails(Long jobId, Integer version, Boolean isDryRun);

    Boolean removeJob(Long id, Operator operator) throws IllegalAccessException;

    Boolean resumeJob(Long id, String environment, Operator operator);

    Boolean pauseJob(Long id, String environment, Operator operator);

    Boolean runJob(Long id, String environment, Operator operator);

    JobDryRunDto dryRunJob(Long jobId, Integer version);

    Boolean moveJob(List<Long> jobIds, Long destFolderId, Operator operator);

    /**
     * jobName模糊匹配
     *
     * @param searchName
     * @return
     */
    List<JobInfo> getJobListByName(String searchName);

    /**
     * 根据ids获取键值对
     *
     * @param accessIdSet
     * @return
     */
    Map<Long, String> getNameMapByIds(List<Long> accessIdSet);

    OverhangJobWrapperDto pagingOverhangJob(JobInfoCondition condition, PageParam pageParam);

    /**
     * 填充JobName
     *
     * @param list             填充的集合
     * @param klass            范型类型
     * @param jobIdFieldName   对应的jobId
     * @param jobNameFieldName 对应的jobName
     * @param <T>
     */
    <T> void fillJobName(List<T> list, Class klass, String jobIdFieldName, String jobNameFieldName) throws NoSuchFieldException;

    /**
     * 获取任务详情
     *
     * @param id
     * @param env
     * @return
     */
    JobInfoExecuteDetailDto getJobInfoExecuteDetail(Long id, String env);

    /**
     * 获取作业扩展信息
     *
     * @param jobIds
     * @return
     */
    List<JobExtInfoDto> getJobExtInfo(List<Long> jobIds);
}
