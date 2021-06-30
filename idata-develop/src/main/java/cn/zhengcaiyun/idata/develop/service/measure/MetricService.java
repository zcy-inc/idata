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
package cn.zhengcaiyun.idata.develop.service.measure;

import cn.zhengcaiyun.idata.develop.dto.measure.MetricDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-22 10:34
 */

public interface MetricService {
    MetricDto findMetric(String metricCode);
    List<MeasureDto> findMetrics(String labelTag);
    List<MeasureDto> findMetricsOrDimensions(List<String> labelCodes, String labelTag);
    MeasureDto create(MeasureDto metric, String operator);
    MeasureDto edit(MeasureDto metric, String operator);
    MeasureDto disable(String metricCode, String operator);
    boolean delete(String metricCode, String operator);
}
