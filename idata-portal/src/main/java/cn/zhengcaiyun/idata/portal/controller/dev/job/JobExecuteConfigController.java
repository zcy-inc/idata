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

package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.dto.job.JobExecuteConfigDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * job-config-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:52
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/execConfigs")
public class JobExecuteConfigController {

    /**
     * 创建作业运行配置
     *
     * @param jobId     作业id
     * @param configDto 配置
     * @return
     */
    @PostMapping()
    public RestResult<JobExecuteConfigDto> addJobExecuteConfig(@PathVariable("jobId") Long jobId,
                                                               @RequestBody JobExecuteConfigDto configDto) {
        return RestResult.success();
    }

    /**
     * 获取作业运行配置列表
     *
     * @param condition 条件
     * @return
     */
    @GetMapping()
    public RestResult<List<JobExecuteConfigDto>> getJobExecuteConfig(@PathVariable("jobId") Long jobId,
                                                                     JobExecuteConfigCondition condition) {
        return RestResult.success();
    }
}
