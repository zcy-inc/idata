package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:07
 **/
public class GreaterThanOrEqualTo<T> extends BaseCondition<T> {

    private GreaterThanOrEqualTo(BaseColumn column, T... params) {
        super(column, params);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " >= " + getFirstParam().toString() + connectNextCond();
    }

    public static <T> GreaterThanOrEqualTo<T> of(BaseColumn column, T... params) {
        return new GreaterThanOrEqualTo(column, params);
    }

}
