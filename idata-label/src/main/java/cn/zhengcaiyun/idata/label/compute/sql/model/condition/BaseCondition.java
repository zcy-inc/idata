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
public abstract class BaseCondition implements ModelRender {
    protected final BaseColumn column;
    protected final List<Long> params;
    protected String connector;
    protected BaseCondition nextCondition;

    protected BaseCondition(BaseColumn column, Long... params) {
        checkNotNull(column, "column is null.");
        checkArgument(params != null && params.length > 0, "params is null.");
        this.column = column;
        this.params = Lists.newArrayList(params);
    }

    protected String getColumnName() {
        return this.column.renderSql();
    }

    protected Long getFirstParam() {
        return this.params.get(0);
    }

    protected Long getSecondParam() {
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

    protected abstract BaseCondition getThis();
}
