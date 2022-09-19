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
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySessionLogDto;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.service.job.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author caizhedong
 * @date 2021-11-23 下午8:15
 */

@RestController
@RequestMapping(path = "/p1/dev/jobs")
public class QueryController {

    @Autowired
    private QueryService queryService;
    @Autowired
    private LivyService livyService;

    @PostMapping("/runQuery")
    public RestResult<QueryStatementDto> runQuery(@RequestBody QueryDto queryDto) {
        return RestResult.success(queryService.runQuery(queryDto));
    }

    @GetMapping("/runQueryResult")
    public RestResult<QueryRunResultDto> runQueryResult(@RequestParam("sessionId") Integer sessionId,
                                                        @RequestParam("statementId") Integer statementId,
                                                        @RequestParam("sessionKind") String sessionKind,
                                                        @RequestParam(value = "from", required = false) Integer from,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        return RestResult.success(queryService.runQueryResult(sessionId, statementId, sessionKind, from, size));
    }

    @GetMapping("/autocompletionTipConfigs")
    public RestResult<AutocompletionTipDto> getAutocompletionTipConfigs(@RequestParam("autocompletionType") String autocompletionType) {
        return RestResult.success(queryService.getAutocompletionTipConfigs(autocompletionType));
    }

    @GetMapping("/batchesLog")
    public RestResult<LivySessionLogDto> getBatchesLog(@RequestParam("sessionId") Integer sessionId,
                                                       @RequestParam(value = "from", required = false) Integer from,
                                                       @RequestParam(value = "size", required = false) Integer size) {
        LivySessionLogDto sessionLogDto = livyService.getBatchesLog(sessionId, from, size);
        sessionLogDto.setState(livyService.getBatchesState(sessionId));
        return RestResult.success(sessionLogDto);
    }

    @PostMapping("/cancelQuery")
    public RestResult cancelQuery(@RequestBody QueryStatementDto queryStatementDto) {
        queryService.cancelStatement(queryStatementDto);
        return RestResult.success();
    }
}
