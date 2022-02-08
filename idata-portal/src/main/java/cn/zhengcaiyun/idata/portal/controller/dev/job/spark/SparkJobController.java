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
package cn.zhengcaiyun.idata.portal.controller.dev.job.spark;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.SparkJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author caizhedong
 * @date 2021-11-22 下午2:54
 */

@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/spark")
public class SparkJobController {

    @Autowired
    private SparkJobService sparkJobService;

    @GetMapping("/contents/{version}")
    public RestResult<SparkJobContentDto> find(@PathVariable Long jobId,
                                               @PathVariable Integer version) {
        return RestResult.success(sparkJobService.find(jobId, version));
    }

    @PostMapping("/contents")
    public RestResult<SparkJobContentDto> save(@PathVariable Long jobId,
                                               @RequestBody SparkJobContentDto sparkJobDto) throws IOException {
        return RestResult.success(sparkJobService.save(sparkJobDto, OperatorContext.getCurrentOperator().getNickname()));
    }

    @PostMapping("/uploadFile")
    public RestResult<String> uploadFile(@PathVariable Long jobId,
                                          @RequestParam("file") MultipartFile file) throws IOException {
        return RestResult.success(sparkJobService.uploadFile(file));
    }
}
