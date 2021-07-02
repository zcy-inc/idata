package cn.zhengcaiyun.idata.label.dto.label.rule;

import java.util.List;
import java.util.Objects;

/**
 * @description: 规则定义dto，包含规则间关系
 * @author: yangjianhua
 * @create: 2021-06-23 10:19
 **/
public class LabelRuleDefDto {
    /**
     * and/or
     */
    private String connector;
    /**
     * 规则列表
     */
    private List<LabelRuleDto> rules;

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public List<LabelRuleDto> getRules() {
        return rules;
    }

    public void setRules(List<LabelRuleDto> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelRuleDefDto that = (LabelRuleDefDto) o;
        return Objects.equals(connector, that.connector) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connector, rules);
    }

    @Override
    public String toString() {
        return "LabelRuleDefDto{" +
                "connector='" + connector + '\'' +
                ", rules=" + rules +
                '}';
    }
}
