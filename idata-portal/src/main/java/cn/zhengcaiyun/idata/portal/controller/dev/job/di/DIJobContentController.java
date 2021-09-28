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

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * job-di-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:46
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/di")
public class DIJobContentController {

    /**
     * 保存作业内容
     *
     * @param jobId      作业id
     * @param contentDto 作业内容信息
     * @return
     */
    @PostMapping("/contents")
    public RestResult<DIJobContentDto> saveContent(@PathVariable("jobId") Long jobId,
                                                   @RequestBody DIJobContentDto contentDto) {
        return RestResult.success();
    }

    /**
     * 获取作业内容
     *
     * @param jobId   作业id
     * @param version 作业版本号
     * @return
     */
    @GetMapping("/contents/{version}")
    public RestResult<DIJobContentDto> getContent(@PathVariable("jobId") Long jobId,
                                                  @PathVariable("version") Integer version) {
        return RestResult.success();
    }

    /**
     * 获取作业内容版本
     *
     * @param jobId 作业id
     * @return
     */
    @GetMapping("/versions")
    public RestResult<List<JobContentVersionDto>> getJobContentVersion(@PathVariable("jobId") Long jobId) {
        return RestResult.success();
    }

    /**
     * 提交作业
     *
     * @param jobId   作业id
     * @param version 作业内容版本号
     * @param env     提交环境
     * @return
     */
    @PostMapping("/contents/{version}/submit/{env}")
    public RestResult<DIJobContentDto> submitJob(@PathVariable("jobId") Long jobId,
                                                 @PathVariable("version") Long version,
                                                 @PathVariable("env") String env) {
        return RestResult.success();
    }

}