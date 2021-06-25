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
package cn.zhengcaiyun.idata.portal.controller.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-26 21:34
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class MetricController {

    @GetMapping("metric")
    public RestResult<MeasureDto> findById(@RequestParam("metricCode") String metricCode) {
        return RestResult.success();
    }

    @GetMapping("measures")
    public RestResult<List<MeasureDto>> getMeasures(@RequestParam("labelTag") String labelTag,
                                                    @RequestParam(value = "labelCode", required = false) String labelCode) {
        return RestResult.success();
    }

    @PostMapping("metric")
    public RestResult<MeasureDto> addOrUpdateMetric(@RequestBody MeasureDto dimension,
                                                    HttpServletRequest request) {
        return RestResult.success();
    }

    @PostMapping("metric/disableMetric")
    public RestResult disableMetric(@RequestParam("metricCode") String metricCode,
                                   HttpServletRequest request) {
        return RestResult.success();
    }

    @DeleteMapping("metric")
    public RestResult deleteMetric(@RequestParam("metricCode") String metricCode,
                                   HttpServletRequest request) {
        return RestResult.success();
    }
}
