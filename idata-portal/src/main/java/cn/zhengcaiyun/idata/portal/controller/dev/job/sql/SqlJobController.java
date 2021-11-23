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
package cn.zhengcaiyun.idata.portal.controller.dev.job.sql;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlQueryDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlResultDto;
import cn.zhengcaiyun.idata.develop.service.job.SparkJobService;
import cn.zhengcaiyun.idata.develop.service.job.SqlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author caizhedong
 * @date 2021-11-22 下午3:12
 */

@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/sql")
public class SqlJobController {

    @Autowired
    private SqlJobService sqlJobService;

    @GetMapping("/contents/{version}")
    public RestResult<SqlJobDto> find(@PathVariable Long jobId,
                                      @PathVariable Integer version) {
        return RestResult.success(sqlJobService.find(jobId, version));
    }

    @PostMapping("/contents")
    public RestResult<SqlJobDto> save(@PathVariable Long jobId,
                                        @RequestBody SqlJobDto sqlJobDto) {
        return RestResult.success(sqlJobService.save(sqlJobDto, OperatorContext.getCurrentOperator().getNickname()));
    }

    @PostMapping("/runQuerySql")
    public RestResult<SqlResultDto> uploadFile(@PathVariable Long jobId,
                                               @RequestBody SqlQueryDto sqlQueryDto) {
        return RestResult.success(sqlJobService.runQuerySql(sqlQueryDto));
    }
}
