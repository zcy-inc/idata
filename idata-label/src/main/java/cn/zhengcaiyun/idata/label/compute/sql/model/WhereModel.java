package cn.zhengcaiyun.idata.label.compute.sql.model;

import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:48
 **/
public class WhereModel implements ModelRender {
    private final BaseCondition conditionChain;

    private WhereModel(BaseCondition conditionChain) {
        this.conditionChain = conditionChain;
    }

    @Override
    public String renderSql() {
        return "where " + conditionChain.renderSql();
    }

    public static WhereModel of(BaseCondition conditionChain) {
        return new WhereModel(conditionChain);
    }
}
