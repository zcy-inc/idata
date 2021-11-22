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
package cn.zhengcaiyun.idata.portal.controller.dev.job.script;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobDto;
import cn.zhengcaiyun.idata.develop.service.job.ScriptJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author caizhedong
 * @date 2021-11-22 下午2:51
 */

@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/script")
public class ScriptJobController {

    @Autowired
    private ScriptJobService scriptJobService;

    @GetMapping("/contents/{version}")
    public RestResult<ScriptJobDto> find(@PathVariable Long jobId,
                                         @PathVariable Integer version) {
        return RestResult.success(scriptJobService.find(jobId, version));
    }

    @PostMapping("/contents")
    public RestResult<ScriptJobDto> save(@PathVariable Long jobId,
                                        @RequestBody ScriptJobDto scriptJobDto) {
        return RestResult.success(scriptJobService.save(scriptJobDto, OperatorContext.getCurrentOperator().getNickname()));
    }
}
