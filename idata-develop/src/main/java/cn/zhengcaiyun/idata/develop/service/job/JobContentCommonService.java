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
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
public interface JobContentCommonService {

    Boolean submit(Long jobId, Integer version, String env, String remark, Operator operator);

    List<JobContentVersionDto> getVersions(Long jobId);

    JobContentBaseDto getJobContent(Long jobId, Integer version, String jobType);

    List<JobContentBaseDto> getJobContents(Long jobId, String jobType);

    Map<Integer, String> getJobContentVersion(Long jobId, String jobType);

    /**
     * 是否绑定了UDF
     *
     * @param id
     * @return
     */
    Boolean ifBindUDF(Long id);
}
