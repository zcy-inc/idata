package cn.zhengcaiyun.idata.dqc.utils;

import cn.zhengcaiyun.idata.dqc.model.common.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleUtils {
    public static String DW_DUTY = "数仓值班";

    /**
     * 校验分区表达式是否正确
     * @param partitionExpr
     * @return
     */
    public static boolean checkPartitionExpr(String partitionExpr){
        if(StringUtils.isEmpty(partitionExpr)){
            return false;
        }
        String[] exprs = partitionExpr.split(",");
        for(String expr:exprs){
            String[] arr = expr.split("=");
            if(arr.length != 2){
                return false;
            }

            try {
                System.out.println(ParameterUtils.dateTemplateParse(arr[1],new Date()));
                return true;
            }catch (IllegalArgumentException e){
                return false;
            }
        }

        return false;
    }

    public static String replaceSql(String sql,String tableName,String fieldName, Date date) {
        if(StringUtils.isEmpty(sql)){
            return sql;
        }
        if(StringUtils.isNotEmpty(tableName)){
            sql = sql.replaceAll("\\$\\{tableName\\}",tableName);
        }
        if(StringUtils.isNotEmpty(fieldName)){
            sql = sql.replaceAll("\\$\\{fieldName\\}",fieldName);
        }

        sql = ParameterUtils.dateTemplateParse(sql, date);

        return sql;
    }

    /**
     * 获取${}的占位符
     *
     * @param content
     * @return
     */
    public static Set<String> getDatePlaceholder(String content) {
        if(StringUtils.isEmpty(content)){
            return new HashSet<>();
        }
        content = content.replaceAll("\\$\\{tableName\\}","").replaceAll("\\$\\{fieldName\\}","");

        Pattern pattern = Pattern.compile(ParameterUtils.DATE_PARSE_PATTERN);
        Matcher matcher = pattern.matcher(content);

        Set<String> set = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group(1);
            if (Pattern.matches(ParameterUtils.DATE_START_PATTERN, key)) {
                continue;
            }
            set.add("${"+key+"}");
        }
        return set;
    }

    /**
     * 校验分区表达式个数
     *
     * @param content
     */
    public static void checkSql(String content) {
        Set<String> set = getDatePlaceholder(content);

        if (set.size() > 1) {
            throw new BizException("分区表达式占位符只允许一种类型");
        }
    }

    public static String[] getAlarmTypes(int alarmLevel) {
        if (alarmLevel == 3) {
            return new String[]{"dingding", "message", "phone"};
        } else if (alarmLevel == 2) {
            return new String[]{"dingding", "message"};
        } else {
            return new String[]{"dingding"};
        }
    }

    public static void main(String[] args) {
        System.out.println(RuleUtils.checkPartitionExpr("pt=${yy-ga9-7}"));
    }
}
