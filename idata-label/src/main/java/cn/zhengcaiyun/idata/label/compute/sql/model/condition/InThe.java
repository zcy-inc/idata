package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 11:57
 **/
public class InThe<T> extends BaseCondition<T> {
    private InThe(BaseColumn column, T... params) {
        super(column, params);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String renderSql() {
        return getColumnName() + " " + getParams().stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(",", "in (", ")")) + connectNextCond();
    }

    public static <T> InThe<T> of(BaseColumn column, T... params) {
        return new InThe(column, params);
    }

}
