package cn.zhengcaiyun.idata.dqc.utils;

import cn.zhengcaiyun.idata.dqc.model.common.BizException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleUtils {

    /**
     * 获取${}的占位符
     *
     * @param content
     * @return
     */
    public static Set<String> getPlaceholder(String content) {
        Pattern pattern = Pattern.compile(ParameterUtils.DATE_PARSE_PATTERN);
        Matcher matcher = pattern.matcher(content);

        Set<String> set = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group(1);
            if (Pattern.matches(ParameterUtils.DATE_START_PATTERN, key)) {
                continue;
            }
            set.add(key);
        }
        return set;
    }

    /**
     * 校验分区表达式个数
     *
     * @param content
     */
    public static void checkSql(String content) {
        Set<String> set = getPlaceholder(content);
        //移除系统参数的校验
        set.remove("tableName");
        set.remove("fieldName");

        if (set.size() > 1) {
            throw new BizException("分区表达式占位符只允许一种类型");
        }
    }

    public static String[] getAlarmTypes(int alarmLevel) {
        if (alarmLevel == 3) {
            return new String[]{"dingding", "meaasge", "phone"};
        } else if (alarmLevel == 2) {
            return new String[]{"dingding", "meaasge"};
        } else {
            return new String[]{"dingding"};
        }
    }
}
