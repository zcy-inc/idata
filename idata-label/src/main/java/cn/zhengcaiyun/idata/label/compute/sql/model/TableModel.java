package cn.zhengcaiyun.idata.label.compute.sql.model;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:48
 **/
public class TableModel implements ModelRender {

    private final String name;

    private TableModel(String name) {
        checkArgument(isNotEmpty(name), "表名不能为空");
        this.name = name;
    }

    public static TableModel of(String name) {
        return new TableModel(name);
    }

    @Override
    public String renderSql() {
        return "from " + this.name;
    }

    public String getName() {
        return name;
    }
}
