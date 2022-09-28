package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanqian
 * @date 2022/9/28 10:03 AM
 * @description 文件类型
 */
public enum TableStoredTypeEnum {
    orc,parquet;

    public static TableStoredTypeEnum myValueOf(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (TableStoredTypeEnum elem : TableStoredTypeEnum.values()) {
            if (StringUtils.equals(elem.name(), value)) {
                return elem;
            }
        }
        return null;
    }
}