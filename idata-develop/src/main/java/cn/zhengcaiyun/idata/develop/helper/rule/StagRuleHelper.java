package cn.zhengcaiyun.idata.develop.helper.rule;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanqian
 * @date 2022/3/21 10:40 AM
 * @description
 */
public class StagRuleHelper {

    /**
     * 根据环境和数据源类型定位真正的表
     * @param dataSourceName
     * @param rawTable
     * @return
     */
    public static String handlerStagTable(String dataSourceName, String rawTable, String env) {
        if (!StringUtils.equals(env, "test")) {
            return rawTable;
        }

        if (StringUtils.equalsIgnoreCase(dataSourceName, "doris")) {
            return "stag_" + rawTable;
        }
        if (StringUtils.equalsIgnoreCase(dataSourceName, "hive")) {
            return "stag_" + rawTable;
        }
        return rawTable;
    }

    public static String handlerStagTable(DataSourceTypeEnum dataSourceTypeEnum, String rawTable, String env) {
        return handlerStagTable(dataSourceTypeEnum.name(), rawTable, env);
    }


}
