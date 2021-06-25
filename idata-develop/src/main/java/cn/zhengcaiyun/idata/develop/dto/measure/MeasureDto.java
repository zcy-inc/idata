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
package cn.zhengcaiyun.idata.develop.dto.measure;

import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-18 17:47
 */

public class MeasureDto extends LabelDefineDto {
    private List<LabelDto> measureLabels;
    private MeasureDto autoMetric;
    private List<MeasureDto> modifiers;

    // GaS
    public List<LabelDto> getMeasureLabels() {
        return measureLabels;
    }

    public void setMeasureLabels(List<LabelDto> measureLabels) {
        this.measureLabels = measureLabels;
    }
}
