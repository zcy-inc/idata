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
package cn.zhengcaiyun.idata.portal.controller.open;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.util.SparkSqlUtil;
import cn.zhengcaiyun.idata.portal.model.request.job.SqlParserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping(path = "/p0/dev/spark")
public class OpenSparkController {

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

    @PostMapping("insertErase")
    public RestResult<Map<String, String>> insertErase(@RequestBody SqlParserRequest sql) {
        checkArgument(sql.getSql() != null, "sql不能为空");
        Map<String, String> result = SparkSqlUtil.insertErase(sql.getSql());
        if (result.get("insertTable") != null) {
            result.put("insertErase", "true");
        } else {
            result.put("insertErase", "false");
            result.put("isHasPt", "false");
        }
        return RestResult.success(result);
    }
}
