package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:07
 **/
public class LessThanOrEqualTo<T> extends BaseCondition<T> {

    private LessThanOrEqualTo(BaseColumn column, T param) {
        super(column, param);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " <= " + getParam() + connectNextCond();
    }

    public static <T> LessThanOrEqualTo<T> of(BaseColumn column, T param) {
        return new LessThanOrEqualTo(column, param);
    }

}
