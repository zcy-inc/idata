package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:11
 **/
public class Between extends BaseCondition {

    private Between(BaseColumn column, Long... params) {
        super(column, params);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " between " + getFirstParam() + " and " + getSecondParam() + connectNextCond();
    }

    public static Between of(BaseColumn column, Long... params) {
        return new Between(column, params);
    }

}
