package cn.zhengcaiyun.idata.label.compute.query.enums;

/**
 * @author shiyin(沐泽)
 * @date 2020/6/23 15:40
 */

public enum PrestoTypeEnum {

    booleanType("boolean", DataTypeEnum.String),
    tinyintType("tinyint", DataTypeEnum.Whole),
    smallintType("smallint", DataTypeEnum.Whole),
    integerType("integer", DataTypeEnum.Whole),
    bigintType("bigint", DataTypeEnum.Whole),
    realType("real", DataTypeEnum.Decimal),
    doubleType("double", DataTypeEnum.Decimal),
    decimalType("decimal", DataTypeEnum.Decimal),
    varcharType("varchar", DataTypeEnum.String),
    charType("char", DataTypeEnum.String),
    varbinaryType("varbinary", DataTypeEnum.String),
    jsonType("json", DataTypeEnum.String),
    dateType("date", DataTypeEnum.Date),
    timeType("time", DataTypeEnum.String),
    timeWithTimeZoneType("time with time zone", DataTypeEnum.String),
    timestampType("timestamp", DataTypeEnum.DateTime),
    timestampWithTimeZoneType("timestamp with time zone", DataTypeEnum.DateTime),
    arrayType("array", DataTypeEnum.String),
    mapType("map", DataTypeEnum.String),
    rowType("row", DataTypeEnum.String);

    private String prestoType;
    private DataTypeEnum dataType;

    PrestoTypeEnum(String prestoType, DataTypeEnum dataType) {
        this.prestoType = prestoType;
        this.dataType = dataType;
    }

    public static DataTypeEnum toDataType(String prestoType) {
        if (prestoType != null && prestoType.indexOf("(") > 0) {
            prestoType = prestoType.substring(0, prestoType.indexOf("("));
        }
        for (PrestoTypeEnum t: PrestoTypeEnum.values()) {
            if (t.prestoType.equalsIgnoreCase(prestoType)) {
                return t.dataType;
            }
        }
        return DataTypeEnum.String;
    }
}
