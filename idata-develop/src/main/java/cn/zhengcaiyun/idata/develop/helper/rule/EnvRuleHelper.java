package cn.zhengcaiyun.idata.develop.helper.rule;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanqian
 * @date 2022/3/21 10:40 AM
 * @description 环境规则
 */
public class EnvRuleHelper {

    /**
     * 根据环境和数据源类型定位真正的表
     * @param dataSourceName
     * @param rawTable
     * @return
     */
    public static String handlerDbTableName(String dataSourceName, String rawTable, String env) {
        if (StringUtils.equalsIgnoreCase(env, EnvEnum.prod.name())) {
            return rawTable;
        }

        if (StringUtils.equalsIgnoreCase(dataSourceName, DataSourceTypeEnum.doris.name())) {
            return env + "_" + rawTable;
        }
        if (StringUtils.equalsIgnoreCase(dataSourceName, DataSourceTypeEnum.hive.name())) {
            return env + "_" + rawTable;
        }
        return rawTable;
    }

    /**
     * 根据环境和数据源类型定位真正的表
     * @param dataSourceTypeEnum
     * @param rawTable
     * @return
     */
    public static String handlerDbTableName(DataSourceTypeEnum dataSourceTypeEnum, String rawTable, String env) {
        return handlerDbTableName(dataSourceTypeEnum.name(), rawTable, env);
    }


}
