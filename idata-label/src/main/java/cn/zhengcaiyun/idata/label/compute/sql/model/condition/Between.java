package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:11
 **/
public class Between<T> extends BaseCondition<T> {

    private Between(BaseColumn column, T littler, T bigger) {
        super(column, littler, bigger);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " between " + getParam() + " and " + getAnotherParam() + connectNextCond();
    }

    public static <T> Between<T> of(BaseColumn column, T littler, T bigger) {
        return new Between(column, littler, bigger);
    }

}
