package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:03
 **/
public class Count extends BaseFunction {

    private Count(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "count(" + super.column.renderSql() + ")";
    }

    public static Count of(BaseColumn column, String alias) {
        return new Count(column, alias);
    }

    public static Count of(BaseColumn column) {
        return new Count(column, null);
    }
}
