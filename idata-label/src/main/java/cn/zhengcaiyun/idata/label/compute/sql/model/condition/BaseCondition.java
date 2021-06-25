package cn.zhengcaiyun.idata.label.compute.sql.model.condition;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.ModelRender;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:29
 **/
public abstract class BaseCondition<T> implements ModelRender {
    protected final BaseColumn column;
    protected final List<T> params;
    protected String connector;
    protected BaseCondition nextCondition;

    protected BaseCondition(BaseColumn column, T... params) {
        checkNotNull(column, "where条件列不能为空.");
        checkArgument(params != null && params.length > 0, "where条件参数不能为空.");
        this.column = column;
        this.params = Lists.newArrayList(params);
    }

    protected String getColumnName() {
        return this.column.renderSql();
    }

    protected T getFirstParam() {
        return this.params.get(0);
    }

    protected List<T> getParams() {
        return this.params;
    }

    protected T getSecondParam() {
        return this.params.get(1);
    }

    public BaseCondition and(BaseCondition condition) {
        connector = "and";
        nextCondition = condition;
        return getThis();
    }

    protected String connectNextCond() {
        if (Objects.isNull(nextCondition) || StringUtils.isEmpty(connector)) {
            return "";
        }
        return " " + connector + " " + nextCondition.renderSql();
    }

    public boolean hasNextCondition() {
        return !Objects.isNull(nextCondition);
    }

    public BaseCondition nextCondition() {
        return nextCondition;
    }

    protected abstract BaseCondition getThis();
}
