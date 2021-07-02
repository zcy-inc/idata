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
package cn.zhengcaiyun.idata.label.dto.label.rule;

import java.util.List;
import java.util.Objects;

/**
 * @description: 规则dto
 * @author: yangjianhua
 * @create: 2021-06-23 09:51
 **/
public class LabelRuleDto {
    /**
     * 规则唯一标识
     */
    private Long ruleId;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 指标定义
     */
    private List<IndicatorDefDto> indicatorDefs;
    /**
     * 维度定义
     */
    private List<DimensionDefDto> dimensionDefs;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<IndicatorDefDto> getIndicatorDefs() {
        return indicatorDefs;
    }

    public void setIndicatorDefs(List<IndicatorDefDto> indicatorDefs) {
        this.indicatorDefs = indicatorDefs;
    }

    public List<DimensionDefDto> getDimensionDefs() {
        return dimensionDefs;
    }

    public void setDimensionDefs(List<DimensionDefDto> dimensionDefs) {
        this.dimensionDefs = dimensionDefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelRuleDto that = (LabelRuleDto) o;
        return Objects.equals(ruleId, that.ruleId) && Objects.equals(ruleName, that.ruleName) && Objects.equals(indicatorDefs, that.indicatorDefs) && Objects.equals(dimensionDefs, that.dimensionDefs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, ruleName, indicatorDefs, dimensionDefs);
    }

    @Override
    public String toString() {
        return "LabelRuleDto{" +
                "ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", indicatorDefs=" + indicatorDefs +
                ", dimensionDefs=" + dimensionDefs +
                '}';
    }
}
