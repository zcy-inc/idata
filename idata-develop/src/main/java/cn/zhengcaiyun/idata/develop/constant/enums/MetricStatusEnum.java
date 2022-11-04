package cn.zhengcaiyun.idata.develop.constant.enums;

public enum MetricStatusEnum {
    ENABLE("", "已启动"),
    DISABLE("_DISABLE", "已停用"),
    DRAFT("_DRAFT", "草稿"),
    APPROVE("_APPROVE", "待审批"),
    ;

    private final String suffix;
    private final String desc;

    MetricStatusEnum(String suffix, String desc) {
        this.suffix = suffix;
        this.desc = desc;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getDesc() {
        return desc;
    }
}
