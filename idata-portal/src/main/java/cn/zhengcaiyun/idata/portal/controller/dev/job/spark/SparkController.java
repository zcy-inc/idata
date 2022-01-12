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
import cn.zhengcaiyun.idata.connector.util.SparkSqlUtil;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.SparkJobService;
import cn.zhengcaiyun.idata.portal.model.request.job.SqlParserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping(path = "/p1/dev/spark")
public class SparkController {

    /**
     * 从旧版idata迁移接口
     * @param request
     * @return
     */
    @PostMapping("/addDatabaseEnv")
    public RestResult<String> addDatabaseEnv(@RequestBody SqlParserRequest request) {
        checkArgument(request.getSql() != null, "sql不能为空");
        return RestResult.success(SparkSqlUtil.addDatabaseEnv(request.getSql(), request.getEnv()));
    }
}
