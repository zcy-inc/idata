package cn.zhengcaiyun.idata.connector.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SparkSqlTool {

    public static List<String> parseFromTable(String sql) {
        // 替换动态参数，避免影响SQL解析
        String replacedSql = SqlDynamicParamTool.replaceDynamicParam(sql, null, (extParam) -> "dummy_param");
        String[] multiSqlArray = SparkSqlUtil.splitToMultiSql(replacedSql);
        List<String> totalFromTables = Lists.newArrayList();
        for (String singleSql : multiSqlArray) {
            try {
                List<String> subFromTables = SparkSqlUtil.getFromTables(singleSql.trim(), null);
                if (CollectionUtils.isNotEmpty(subFromTables)) {
                    totalFromTables.addAll(subFromTables);
                }
            } catch (Exception ex) {
            }
        }
        return totalFromTables;
    }

    public static List<String> parseAndFilterFromTable(String sql) {
        List<String> totalFromTables = parseFromTable(sql);
        List<String> finalTables = totalFromTables.stream()
                .filter(tempTable -> tempTable.contains("."))
                .distinct()
                .collect(Collectors.toList());
        return finalTables;
    }
}
