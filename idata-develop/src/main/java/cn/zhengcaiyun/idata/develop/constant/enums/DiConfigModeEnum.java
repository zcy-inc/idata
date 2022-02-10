package cn.zhengcaiyun.idata.develop.constant.enums;

public enum DiConfigModeEnum {

    VISIBLE(1, "可视化"),
    SCRIPT(2, "脚本");

    public Integer value;
    public String description;

    DiConfigModeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static DiConfigModeEnum getByValue(Integer value) {
        for (DiConfigModeEnum elem : DiConfigModeEnum.values()) {
            if (elem.value.equals(value)) {
                return elem;
            }
        }
        return null;
    }

}
