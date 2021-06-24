package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:01
 **/
public class Max extends BaseFunction {

    private Max(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "max(" + super.column.renderSql() + ")";
    }

    public static Max of(BaseColumn column, String alias) {
        return new Max(column, alias);
    }

    public static Max of(BaseColumn column) {
        return new Max(column, null);
    }
}
