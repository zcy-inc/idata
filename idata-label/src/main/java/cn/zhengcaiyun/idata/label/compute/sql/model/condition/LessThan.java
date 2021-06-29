package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:07
 **/
public class LessThan<T> extends BaseCondition<T> {

    private LessThan(BaseColumn column, T param) {
        super(column, param);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " < " + getParam() + connectNextCond();
    }

    public static <T> LessThan<T> of(BaseColumn column, T param) {
        return new LessThan(column, param);
    }

}
