package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:03
 **/
public class Min extends BaseFunction {

    private Min(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "min(" + super.column.renderSql() + ")";
    }

    public static Min of(BaseColumn column, String alias) {
        return new Min(column, alias);
    }

    public static Min of(BaseColumn column) {
        return new Min(column, null);
    }
}
