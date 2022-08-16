package cn.zhengcaiyun.idata.dqc.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * author:zheng
 * Date:2022/6/23
 */
public enum MonitorTemplateEnum {
    TABLE_ROW("表行数", "table_row", 1L),
    TABLE_OUTPUT_TIME("表产出时间", "table_output_time", 2L),
    FIELD_UNIQUE("值唯一", "field_unique", 3L),
    FIELD_ENUM_CONTENT("字段枚举内容", "field_enum_content", 4L),
    FIELD_ENUM_COUNT("字段枚举数量", "field_enum_count", 5L),
    FIELD_DATA_RANGE("字段数值范围", "field_data_range", 6L),
    FIELD_NOT_NULL("字段值不为空", "field_not_null", 7L);

    private String desc;
    private String value;
    private Long id;

    private static final Map<Long, MonitorTemplateEnum> MAP = new HashMap<>();

    static {
        for (MonitorTemplateEnum templateEnum : values()) {
            MAP.put(templateEnum.getId(), templateEnum);
        }
    }

    MonitorTemplateEnum(String desc, String value, Long id) {
        this.desc = desc;
        this.value = value;
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static MonitorTemplateEnum getEnum(Long id) {
        return MAP.get(id);
    }

}
