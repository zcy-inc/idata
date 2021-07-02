package cn.zhengcaiyun.idata.label.compute.sql.model;

import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:50
 **/
public class HavingModel implements ModelRender {
    private final BaseCondition conditionChain;

    private HavingModel(BaseCondition conditionChain) {
        this.conditionChain = conditionChain;
    }

    @Override
    public String renderSql() {
        return "having " + conditionChain.renderSql();
    }

    public static HavingModel of(BaseCondition conditionChain) {
        return new HavingModel(conditionChain);
    }
}
