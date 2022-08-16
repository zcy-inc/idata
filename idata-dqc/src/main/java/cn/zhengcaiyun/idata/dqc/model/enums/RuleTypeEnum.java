package cn.zhengcaiyun.idata.dqc.model.enums;

/**
 * author:zheng
 * Date:2022/6/23
 */
public enum RuleTypeEnum {
    SYSTEM("内置规则","system"),
    TEMPLATE("模板规则",  "template"),
    CUSTOME("自定义",  "custom");

    private String desc;
    private String value;

    RuleTypeEnum(String desc, String value) {
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
