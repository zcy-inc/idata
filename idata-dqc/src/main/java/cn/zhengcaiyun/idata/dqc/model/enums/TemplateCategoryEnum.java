package cn.zhengcaiyun.idata.dqc.model.enums;

public enum TemplateCategoryEnum {
    INTEGRITY("完整性","integrity"),
    ACCURACY("准确性","accuracy"),
    TIMELY("时效性",  "timely");
    private String desc;
    private String value;
    TemplateCategoryEnum(String desc, String value) {
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