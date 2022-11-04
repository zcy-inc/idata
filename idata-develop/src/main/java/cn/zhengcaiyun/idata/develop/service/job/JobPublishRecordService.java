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
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dto.job.JobPublishRecordDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-29 14:34
 **/
public interface JobPublishRecordService {

    Page<JobPublishRecordDto> paging(JobPublishRecordCondition condition, PageParam pageParam);

    Boolean approve(Long id, String remark, Operator operator);

    Boolean approve(List<Long> ids, String remark, Operator operator);

    Boolean reject(Long id, String remark, Operator operator);

    Boolean reject(List<Long> ids, String remark, Operator operator);

    List<JobPublishRecord> findJobs(JobPublishRecordCondition condition);

    /**
     * 删除发布版本
     * @param id
     */
    boolean delete(Long id);
}
