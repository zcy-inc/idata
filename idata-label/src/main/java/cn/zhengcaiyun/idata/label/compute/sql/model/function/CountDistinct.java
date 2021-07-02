package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:04
 **/
public class CountDistinct extends BaseFunction {

    private CountDistinct(BaseColumn column, String alias) {
        super(column, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public String renderSql() {
        return "count(distinct " + super.column.renderSql() + ")";
    }

    public static CountDistinct of(BaseColumn column, String alias) {
        return new CountDistinct(column, alias);
    }

    public static CountDistinct of(BaseColumn column) {
        return new CountDistinct(column, null);
    }
}
