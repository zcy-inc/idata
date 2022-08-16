package cn.zhengcaiyun.idata.dqc.model.enums;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

/**
 * author:zheng
 * Date:2022/6/23
 */
public enum AlarmLevelEnum {
    SIMPLE("一般",1),
    IMPORTANT("重要",  2),
    SERIOUS("严重",  3);

    private String desc;
    private Integer value;

    private static final Map<Integer, AlarmLevelEnum> MAP = new HashMap<>();

    static {
        for (AlarmLevelEnum levelEnum : values()) {
            MAP.put(levelEnum.getValue(), levelEnum);
        }
    }

    AlarmLevelEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static String getDest(Integer value){
        return MAP.get(value).desc;
    }

}
