package cn.zhengcaiyun.idata.label.compute.query.dto;


import cn.zhengcaiyun.idata.label.compute.query.enums.DataTypeEnum;

/**
 * @author shiyin(沐泽)
 * @date 2020/6/15 15:53
 */
public class ColumnDto {
    private String columnName;
    private String columnType;
    private DataTypeEnum dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public DataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }
}
