package cn.zhengcaiyun.idata.label.compute.sql.model.function;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:27
 **/
public abstract class BaseFunction implements BaseColumn {
    protected final BaseColumn column;
    protected final String alias;

    protected BaseFunction(BaseColumn column, String alias) {
        checkNotNull(column, "函数列不能为空.");
        this.column = column;
        this.alias = alias;
    }
}
