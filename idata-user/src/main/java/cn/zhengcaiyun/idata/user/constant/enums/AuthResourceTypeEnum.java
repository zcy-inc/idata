package cn.zhengcaiyun.idata.user.constant.enums;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

/**
 * 授权资源类型，tables：表
 */
public enum AuthResourceTypeEnum {
    tables;

    private static final Map<String, AuthResourceTypeEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(AuthResourceTypeEnum.values())
                .forEach(enumObj -> map.put(enumObj.name(), enumObj));
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }
}
