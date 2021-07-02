package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:00
 **/
public class Sum extends BaseFunction {

    private Sum(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "sum(" + super.column.renderSql() + ")";
    }

    public static Sum of(BaseColumn column, String alias) {
        return new Sum(column, alias);
    }

    public static Sum of(BaseColumn column) {
        return new Sum(column, null);
    }
}
