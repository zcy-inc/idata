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
