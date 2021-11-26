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

package cn.zhengcaiyun.idata.portal.controller.dev.job.di;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;
import cn.zhengcaiyun.idata.develop.dto.job.SubmitJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * job-content-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:46
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}")
public class JobContentCommonController {

    private final JobContentCommonService jobContentCommonService;

    @Autowired
    public JobContentCommonController(JobContentCommonService jobContentCommonService) {
        this.jobContentCommonService = jobContentCommonService;
    }

    /**
     * 获取作业内容版本
     *
     * @param jobId 作业id
     * @return
     */
    @GetMapping("/versions")
    public RestResult<List<JobContentVersionDto>> getJobContentVersion(@PathVariable("jobId") Long jobId) {
        return RestResult.success(jobContentCommonService.getVersions(jobId));
    }

    /**
     * 提交作业
     *
     * @param jobId   作业id
     * @param version 作业内容版本号
     * @param env     提交环境
     * @return
     */
    @PostMapping("/versions/{version}/submit/{env}")
    public RestResult<DIJobContentContentDto> submitJob(@PathVariable("jobId") Long jobId,
                                                        @PathVariable("version") Integer version,
                                                        @PathVariable("env") String env,
                                                        @RequestBody SubmitJobDto submitJobDto) {
        return RestResult.success();
//        return RestResult.success(jobContentCommonService.submit(jobId, version, env, submitJobDto.getRemark(), OperatorContext.getCurrentOperator()));
    }

}
