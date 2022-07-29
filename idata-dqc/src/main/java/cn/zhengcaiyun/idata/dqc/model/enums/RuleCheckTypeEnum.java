package cn.zhengcaiyun.idata.dqc.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum RuleCheckTypeEnum {
    FIX("固定值", "fix"),
    PRE_PREIOD("上周期", "pre_period");
    private String desc;
    private String value;

    private static final Map<String, RuleCheckTypeEnum> MAP = new HashMap<>();

    static {
        for (RuleCheckTypeEnum typeEnum : values()) {
            MAP.put(typeEnum.getValue(), typeEnum);
        }
    }

    RuleCheckTypeEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static RuleCheckTypeEnum getEnum(String value) {
        return MAP.get(value);
    }
}
