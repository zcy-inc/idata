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
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.DIStreamJobContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * stream-di-job-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-18 14:06
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/stream/di")
public class DIStreamJobContentController {

    private final DIStreamJobContentService streamJobContentService;

    @Autowired
    public DIStreamJobContentController(DIStreamJobContentService streamJobContentService) {
        this.streamJobContentService = streamJobContentService;
    }

    /**
     * 保存实时抽数作业内容
     *
     * @param jobId      作业id
     * @param contentDto 作业内容
     * @return
     */
    @PostMapping("/contents")
    public RestResult<DIStreamJobContentDto> saveContent(@PathVariable("jobId") Long jobId,
                                                         @RequestBody DIStreamJobContentDto contentDto) {
        Integer version = streamJobContentService.save(jobId, contentDto, OperatorContext.getCurrentOperator());
        return RestResult.success(streamJobContentService.get(jobId, version));
    }

    /**
     * 获取实时抽数作业内容
     *
     * @param jobId   作业id
     * @param version 作业版本号
     * @return
     */
    @GetMapping("/contents/{version}")
    public RestResult<DIStreamJobContentDto> getContent(@PathVariable("jobId") Long jobId,
                                                        @PathVariable("version") Integer version) {
        return RestResult.success(streamJobContentService.get(jobId, version));
    }
}
