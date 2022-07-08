package cn.zhengcaiyun.idata.dqc.model.enums;

/**
 * author:zheng
 * Date:2022/6/23
 */
public enum MonitorTemplateEnum {
    TABLE_ROW("表行数","table_row"),
    TABLE_OUTPUT_TIME("表产出时间",  "table_output_time"),
    TABLE_PK_UNIQUE("表主键唯一",  "table_pk_unique"),
    FIELD_ENUM_CONTENT("字段枚举内容",  "field_enum_content"),
    FIELD_ENUM_COUNT("字段枚举数量",  "field_enum_count" ),
    FIELD_DATA_RANGE("字段数值范围",  "field_data_range" ),
    FIELD_NOT_NULL("字段值不为空",  "field_not_null");

    private String desc;
    private String value;

    MonitorTemplateEnum(String desc,String value) {
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
