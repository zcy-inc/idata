package cn.zhengcaiyun.idata.dqc.model.enums;

public enum MonitorObjEnum {
    FIELD("字段","field"),
    TABLE("表",  "table");
    private String desc;
    private String value;
    MonitorObjEnum(String desc, String value) {
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
