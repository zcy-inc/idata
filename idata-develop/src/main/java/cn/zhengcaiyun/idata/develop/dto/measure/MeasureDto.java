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
    private String bizProcessValue;
    private String measureId;
    private String measureDefine;
    private String metricDeadline;
    private String metricId;
    private String modifierId;
    private String domain;
    private String belongTblName;
    private String columnName;
    private String folderName;

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

    public String getBizProcessValue() {
        return bizProcessValue;
    }

    public void setBizProcessValue(String bizProcessValue) {
        this.bizProcessValue = bizProcessValue;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
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

    public String getMetricDeadline() {
        return metricDeadline;
    }

    public void setMetricDeadline(String metricDeadline) {
        this.metricDeadline = metricDeadline;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBelongTblName() {
        return belongTblName;
    }

    public void setBelongTblName(String belongTblName) {
        this.belongTblName = belongTblName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
