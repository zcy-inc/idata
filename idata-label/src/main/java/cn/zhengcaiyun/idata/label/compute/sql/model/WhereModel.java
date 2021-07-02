package cn.zhengcaiyun.idata.label.compute.sql.model;

import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:48
 **/
public class WhereModel implements ModelRender {
    private final BaseCondition firstConditionOfChain;

    private WhereModel(BaseCondition conditionChain) {
        this.firstConditionOfChain = conditionChain;
    }

    @Override
    public String renderSql() {
        return "where " + firstConditionOfChain.renderSql();
    }

    public static WhereModel of(BaseCondition conditionChain) {
        return new WhereModel(conditionChain);
    }

    public WhereModel and(BaseCondition condition) {
        BaseCondition lastCond = firstConditionOfChain;
        while (lastCond.hasNextCondition()) {
            lastCond = lastCond.nextCondition();
        }
        lastCond.and(condition);
        return this;
    }
}
