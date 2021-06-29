package cn.zhengcaiyun.idata.label.compute.sql.model;

import cn.zhengcaiyun.idata.label.compute.sql.model.condition.*;
import cn.zhengcaiyun.idata.label.compute.sql.transform.ConditionFactory;

import java.util.Optional;

/**
 * @description: 参考mybatis dynamic sql，BasicColumn
 * @author: yangjianhua
 * @create: 2021-06-24 15:24
 **/
public interface BaseColumn extends ModelRender {
    Optional<String> alias();

    default String renderSqlWithAlias() {
        String renderName = renderSql();
        return alias().map(a -> renderName + " as " + a)
                .orElse(renderName);
    }

    default <T> BaseCondition<T> between(T littler, T bigger) {
        return Between.of(this, littler, bigger);
    }

    default <T> BaseCondition<T> equalTo(T param) {
        return EqualTo.of(this, param);
    }

    default <T> BaseCondition<T> greaterThan(T param) {
        return GreaterThan.of(this, param);
    }

    default <T> BaseCondition<T> greaterThanOrEqualTo(T param) {
        return GreaterThanOrEqualTo.of(this, param);
    }

    default <T> BaseCondition<T> lessThan(T param) {
        return LessThan.of(this, param);
    }

    default <T> BaseCondition<T> lessThanOrEqualTo(T param) {
        return LessThanOrEqualTo.of(this, param);
    }

    default <T> BaseCondition<T> inThe(T... params) {
        return InThe.of(this, params);
    }

    default <T> Optional<BaseCondition<T>> withCondition(String condition, T... params) {
        return ConditionFactory.createCondition(condition, this, params);
    }
}
