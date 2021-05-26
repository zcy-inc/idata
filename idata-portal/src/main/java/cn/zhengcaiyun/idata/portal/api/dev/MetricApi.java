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

/**
 * @author caizhedong
 * @date 2021-05-26 21:34
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class MetricApi {

    @GetMapping("metric/{metricId}")
    public RestResult<LabelDefineDto> findById(@PathVariable("metricId") Long metricId) {
        return RestResult.success();
    }

    @PostMapping("metric")
    public RestResult<LabelDefineDto> addOrUpdateMetric(LabelDefineDto labelDefineDto,
                                                           String creator) {
        return RestResult.success();
    }

    @DeleteMapping("dimension/{metricId}")
    public RestResult deleteMetric(@PathVariable("metricId") Long metricId,
                                      String editor) {
        return RestResult.success();
    }
}
