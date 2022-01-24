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

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.service.job.JobExecuteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * job-config-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:52
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}")
public class JobExecuteConfigController {

    private final JobExecuteConfigService jobExecuteConfigService;

    @Autowired
    public JobExecuteConfigController(JobExecuteConfigService jobExecuteConfigService) {
        this.jobExecuteConfigService = jobExecuteConfigService;
    }

    /**
     * 保存配置
     *
     * @param jobId       作业id
     * @param environment 环境
     * @param dto         配置
     * @return
     */
    @PostMapping("/environments/{environment}/configs")
    public RestResult<JobConfigCombinationDto> saveJobConfig(@PathVariable("jobId") Long jobId,
                                                             @PathVariable("environment") String environment,
                                                             @RequestBody JobConfigCombinationDto dto) {
        return RestResult.success(jobExecuteConfigService.save(jobId, environment, dto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 获取配置
     *
     * @param jobId       作业id
     * @param environment 环境
     * @return
     */
    @GetMapping("/environments/{environment}/configs")
    public RestResult<JobConfigCombinationDto> getJobConfig(@PathVariable("jobId") Long jobId,
                                                            @PathVariable("environment") String environment) {
        return RestResult.success(jobExecuteConfigService.getCombineConfig(jobId, environment));
    }

}
