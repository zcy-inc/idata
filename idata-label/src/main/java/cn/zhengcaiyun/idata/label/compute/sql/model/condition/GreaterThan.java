package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:07
 **/
public class GreaterThan extends BaseCondition {

    private GreaterThan(BaseColumn column, Long... params) {
        super(column, params);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " > " + getFirstParam() + connectNextCond();
    }

    public static GreaterThan of(BaseColumn column, Long... params) {
        return new GreaterThan(column, params);
    }
}
