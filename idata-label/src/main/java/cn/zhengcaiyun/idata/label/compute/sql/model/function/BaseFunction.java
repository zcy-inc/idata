package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:27
 **/
public abstract class BaseFunction implements BaseColumn {
    protected final BaseColumn column;
    protected final String alias;

    protected BaseFunction(BaseColumn column, String alias) {
        this.column = column;
        this.alias = alias;
    }
}
