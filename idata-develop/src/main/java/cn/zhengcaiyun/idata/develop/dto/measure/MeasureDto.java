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

import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-18 17:47
 */

public class MeasureDto extends LabelDefineDto {
    private List<LabelDto> measureLabels;
    private MeasureDto atomicMetric;
    private List<ModifierDto> modifiers;
    private String enName;
    private String bizTypeValue;
    private String measureId;
    private String measureDefine;
    private Date measureDeadline;

    // GaS
    public List<LabelDto> getMeasureLabels() {
        return measureLabels;
    }

    public void setMeasureLabels(List<LabelDto> measureLabels) {
        this.measureLabels = measureLabels;
    }

    public MeasureDto getAtomicMetric() {
        return atomicMetric;
    }

    public void setAtomicMetric(MeasureDto atomicMetric) {
        this.atomicMetric = atomicMetric;
    }

    public List<ModifierDto> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<ModifierDto> modifiers) {
        this.modifiers = modifiers;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBizTypeValue() {
        return bizTypeValue;
    }

    public void setBizTypeValue(String bizTypeValue) {
        this.bizTypeValue = bizTypeValue;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getMeasureDefine() {
        return measureDefine;
    }

    public void setMeasureDefine(String measureDefine) {
        this.measureDefine = measureDefine;
    }

    public Date getMeasureDeadline() {
        return measureDeadline;
    }

    public void setMeasureDeadline(Date measureDeadline) {
        this.measureDeadline = measureDeadline;
    }
}
