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

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:55
 **/
public interface JobPublishRecordRepo {

    Page<JobPublishRecord> paging(JobPublishRecordCondition condition, PageParam pageParam);

    List<JobPublishRecord> queryList(JobPublishRecordCondition condition, long limit, long offset);

    long count(JobPublishRecordCondition condition);

    List<JobPublishRecord> queryList(JobPublishRecordCondition condition);

    Long save(JobPublishRecord record);

    Boolean update(JobPublishRecord record);

    Optional<JobPublishRecord> query(Long jobId, Integer version, String environment);

    Optional<JobPublishRecord> query(Long id);

    List<JobPublishRecord> queryList(Long jobId);

    Boolean submit(JobPublishRecord record, String operator);

    Boolean publish(JobPublishRecord record, String operator);
}
