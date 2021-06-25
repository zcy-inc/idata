package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.*;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 14:19
 **/
public class ConditionFactory<T> {
    public static <T> Optional<BaseCondition<T>> createCondition(String condition, BaseColumn column, T... params) {
        BaseCondition<T> baseCondition = null;
        if ("Between".equals(condition)) {
            baseCondition = Between.of(column, params);
        } else if ("EqualTo".equals(condition)) {
            baseCondition = EqualTo.of(column, params);
        } else if ("GreaterThan".equals(condition)) {
            baseCondition = GreaterThan.of(column, params);
        } else if ("GreaterThanOrEqualTo".equals(condition)) {
            baseCondition = GreaterThanOrEqualTo.of(column, params);
        } else if ("InThe".equals(condition)) {
            baseCondition = InThe.of(column, params);
        } else if ("LessThan".equals(condition)) {
            baseCondition = LessThan.of(column, params);
        } else if ("LessThanOrEqualTo".equals(condition)) {
            baseCondition = LessThanOrEqualTo.of(column, params);
        }
        return Optional.ofNullable(baseCondition);
    }
}
