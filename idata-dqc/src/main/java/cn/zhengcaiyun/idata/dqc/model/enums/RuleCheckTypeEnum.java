package cn.zhengcaiyun.idata.dqc.model.enums;

public enum RuleCheckTypeEnum {
    ABS("绝对值","abs"),
    PRE_PREIOD("上周期",  "pre_period上周期");
    private String desc;
    private String value;
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
}
