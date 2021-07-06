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
package cn.zhengcaiyun.idata.develop.dto.label;

import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-25 17:03
 */

public class SpecialAttributeDto {
    @ApiModelProperty(value = "AGGREGATOR_SUM:ENUM_VALUE | AGGREGATOR_AVG:ENUM_VALUE | AGGREGATOR_MAX:ENUM_VALUE | " +
            "AGGREGATOR_MIN:ENUM_VALUE | AGGREGATOR_CNT:ENUM_VALUE | AGGREGATOR_CNTD:ENUM_VALUE")
    private String aggregatorCode;
    private Boolean degradeDim;
    private String atomicMetricCode;
    private String atomicMetricName;
    private String complexMetricFormula;
    private List<ModifierDto> modifiers;

    // GaS
    public String getAggregatorCode() {
        return aggregatorCode;
    }

    public void setAggregatorCode(String aggregatorCode) {
        this.aggregatorCode = aggregatorCode;
    }

    public Boolean getDegradeDim() {
        return degradeDim;
    }

    public void setDegradeDim(Boolean degradeDim) {
        this.degradeDim = degradeDim;
    }

    public String getAtomicMetricCode() {
        return atomicMetricCode;
    }

    public void setAtomicMetricCode(String atomicMetricCode) {
        this.atomicMetricCode = atomicMetricCode;
    }

    public String getAtomicMetricName() {
        return atomicMetricName;
    }

    public void setAtomicMetricName(String atomicMetricName) {
        this.atomicMetricName = atomicMetricName;
    }

    public String getComplexMetricFormula() {
        return complexMetricFormula;
    }

    public void setComplexMetricFormula(String complexMetricFormula) {
        this.complexMetricFormula = complexMetricFormula;
    }

    public List<ModifierDto> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<ModifierDto> modifiers) {
        this.modifiers = modifiers;
    }
}
