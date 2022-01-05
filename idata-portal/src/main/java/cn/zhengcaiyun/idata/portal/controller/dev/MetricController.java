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
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author caizhedong
 * @date 2021-05-26 21:34
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class MetricController {

    @Autowired
    private MetricService metricService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("metric")
    public RestResult<MeasureDto> findByCode(@RequestParam("metricCode") String metricCode) {
        return RestResult.success(metricService.findMetric(metricCode));
    }

    @GetMapping("metrics")
    public RestResult<List<MeasureDto>> getMetrics(@RequestParam("labelTag") String labelTag,
                                                    @RequestParam(value = "labelCode", required = false) String labelCode) {
        return RestResult.success(metricService.findMetrics(labelTag));
    }

    @GetMapping("metricsOrDimensions")
    public RestResult<List<MeasureDto>> getMetricsOrDimensions(@RequestParam("labelTag") String labelTag,
                                                               @RequestParam("labelCodes") List<String> labelCodes) {
        return RestResult.success(metricService.findMetricsOrDimensions(labelCodes, labelTag));
    }

    @GetMapping("metricSql")
    public RestResult<String> getMetricsSql(@RequestParam("metricCode") String metricCode) {
        return RestResult.success(metricService.getMetricSql(metricCode));
    }

    @PostMapping("metric")
    public RestResult<MeasureDto> addOrUpdateMetric(@RequestBody MeasureDto metric,
                                                    HttpServletRequest request) {
        MeasureDto echoMetric;
        if (isEmpty(metric.getLabelCode())) {
            echoMetric = metricService.create(metric, tokenService.getNickname(request));
        }
        else {
            echoMetric = metricService.edit(metric, tokenService.getNickname(request));
        }
        return RestResult.success(echoMetric);
    }

    @PostMapping("metric/disableOrAbleMetric")
    public RestResult disableOrAbleMetric(@RequestParam("metricCode") String metricCode,
                                          @RequestParam("labelTag") String labelTag,
                                          HttpServletRequest request) {
        return RestResult.success(metricService.disableOrAble(metricCode, labelTag, tokenService.getNickname(request)));
    }

    @DeleteMapping("metric")
    public RestResult deleteMetric(@RequestParam("metricCode") String metricCode,
                                   HttpServletRequest request) {
        return RestResult.success(metricService.delete(metricCode, tokenService.getNickname(request)));
    }
}
