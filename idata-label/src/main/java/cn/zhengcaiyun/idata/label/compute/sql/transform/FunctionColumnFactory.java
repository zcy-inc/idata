package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.function.*;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 11:02
 **/
public class FunctionColumnFactory {
    public static Optional<BaseColumn> createFunctionColumn(String function, BaseColumn column, String aliasName) {
        BaseFunction baseFunction = null;
        if ("AGGREGATOR_SUM:ENUM_VALUE".equals(function)) {
            baseFunction = Sum.of(column, aliasName);
        } else if ("AGGREGATOR_AVG:ENUM_VALUE".equals(function)) {
            baseFunction = Avg.of(column, aliasName);
        } else if ("AGGREGATOR_MAX:ENUM_VALUE".equals(function)) {
            baseFunction = Max.of(column, aliasName);
        } else if ("AGGREGATOR_MIN:ENUM_VALUE".equals(function)) {
            baseFunction = Min.of(column, aliasName);
        } else if ("AGGREGATOR_CNT:ENUM_VALUE".equals(function)) {
            baseFunction = Count.of(column, aliasName);
        } else if ("AGGREGATOR_CNTD:ENUM_VALUE".equals(function)) {
            baseFunction = CountDistinct.of(column, aliasName);
        }
        return Optional.ofNullable(baseFunction);
    }
}
