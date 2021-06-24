package cn.zhengcaiyun.idata.label.compute.sql.model;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:52
 **/
public class ColumnModel implements BaseColumn {
    private final String name;
    private final TableModel table;
    private final String alias;

    private ColumnModel(String name, TableModel table, String alias) {
        this.name = name;
        this.table = table;
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(this.alias);
    }

    @Override
    public String renderSql() {
        return table.getName() + "." + this.name;
    }

    public static ColumnModel of(String name, TableModel table) {
        return new ColumnModel(name, table, null);
    }

    public static ColumnModel of(String name, TableModel table, String alias) {
        return new ColumnModel(name, table, alias);
    }
}
