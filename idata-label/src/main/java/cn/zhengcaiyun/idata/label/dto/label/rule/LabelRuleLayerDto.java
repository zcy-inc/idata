package cn.zhengcaiyun.idata.label.dto.label.rule;

import java.util.Objects;

/**
 * @description: 分层
 * @author: yangjianhua
 * @create: 2021-06-23 10:23
 **/
public class LabelRuleLayerDto {
    /**
     * 分层id
     */
    private Long layerId;
    /**
     * 标签名称
     */
    private String layerName;
    /**
     * 规则定义
     */
    private LabelRuleDefDto ruleDef;

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public LabelRuleDefDto getRuleDef() {
        return ruleDef;
    }

    public void setRuleDef(LabelRuleDefDto ruleDef) {
        this.ruleDef = ruleDef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelRuleLayerDto that = (LabelRuleLayerDto) o;
        return Objects.equals(layerId, that.layerId) && Objects.equals(layerName, that.layerName) && Objects.equals(ruleDef, that.ruleDef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerId, layerName, ruleDef);
    }

    @Override
    public String toString() {
        return "LabelRuleLayerDto{" +
                "layerId=" + layerId +
                ", layerName='" + layerName + '\'' +
                ", ruleDef=" + ruleDef +
                '}';
    }
}
