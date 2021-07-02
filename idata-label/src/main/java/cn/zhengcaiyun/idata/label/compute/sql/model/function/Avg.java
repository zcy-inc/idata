package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:00
 **/
public class Avg extends BaseFunction {

    private Avg(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "avg(" + super.column.renderSql() + ")";
    }

    public static Avg of(BaseColumn column, String alias) {
        return new Avg(column, alias);
    }

    public static Avg of(BaseColumn column) {
        return new Avg(column, null);
    }
}
