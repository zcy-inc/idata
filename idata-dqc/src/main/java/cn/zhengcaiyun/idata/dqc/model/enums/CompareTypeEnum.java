package cn.zhengcaiyun.idata.dqc.model.enums;

/**
 * author:zheng
 * Date:2022/6/23
 */
public enum CompareTypeEnum {
    GREATER("大于",">"),
    GREATER_OR_EQUAL("大于等于",  ">="),
    EQUAL("等于",  "="),
    LESS("小于",  "<"),
    LESS_OR_EQUAL("小于等于",  "<="),
    NOT_EQUAL("不等于",  "<>"),
    UP("上浮",  "up"),
    DOWN("下降",  "down");

    private String desc;
    private String value;

    CompareTypeEnum(String desc, String value) {
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
}
