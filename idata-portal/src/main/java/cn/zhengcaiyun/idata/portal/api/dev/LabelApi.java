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
package cn.zhengcaiyun.idata.portal.api.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.dto.dev.label.LabelDefineDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 20:05
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class LabelApi {

    @GetMapping("label/{labelDefineId}")
    public RestResult<LabelDefineDto> findById(@PathVariable("labelDefineId") Long labelDefineId) {
        return RestResult.success();
    }

    @GetMapping("labels")
    public RestResult<List<LabelDefineDto>> findLabels(@RequestParam(value = "labelTag", required = false) String labelTag) {
        return RestResult.success();
    }

    @PostMapping("label")
    public RestResult<LabelDefineDto> addLabel(@RequestBody LabelDefineDto labelDefineDto,
                                               HttpServletRequest request) {
        return RestResult.success();
    }

    @PutMapping("label")
    public RestResult<LabelDefineDto> updateLabel(@RequestBody LabelDefineDto labelDefineDto,
                                                  HttpServletRequest request) {
        return RestResult.success();
    }

    @DeleteMapping("label/{labelDefineId}")
    public RestResult deleteLabel(@PathVariable("labelDefineId") Long labelDefineId,
                                  HttpServletRequest request) {
        return RestResult.success();
    }
}
