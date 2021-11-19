package cn.zhengcaiyun.idata.connector.spi.hive.dto;

import java.util.ArrayList;
import java.util.List;

public class CompareInfoDTO {

    private Boolean existHiveTable;

    private String localTableName;

    private String hiveTableName;

    // 相比hive多的字段
    private List<BasicColumnInfo> moreList = new ArrayList<>();

    // 相比hive少的字段
    private List<BasicColumnInfo> lessList = new ArrayList<>();

    // 相比hive不同的字段
    private List<BasicColumnInfo> differentList = new ArrayList<>();

    public CompareInfoDTO(Boolean existHiveTable) {
        this.existHiveTable = existHiveTable;
    }

    public CompareInfoDTO() {
    }

    public Boolean getExistHiveTable() {
        return existHiveTable;
    }

    public void setExistHiveTable(Boolean existHiveTable) {
        this.existHiveTable = existHiveTable;
    }

    public List<BasicColumnInfo> getMoreList() {
        return moreList;
    }

    public void setMoreList(List<BasicColumnInfo> moreList) {
        this.moreList = moreList;
    }

    public List<BasicColumnInfo> getLessList() {
        return lessList;
    }

    public void setLessList(List<BasicColumnInfo> lessList) {
        this.lessList = lessList;
    }

    public List<BasicColumnInfo> getDifferentList() {
        return differentList;
    }

    public void setDifferentList(List<BasicColumnInfo> differentList) {
        this.differentList = differentList;
    }

    public String getLocalTableName() {
        return localTableName;
    }

    public void setLocalTableName(String localTableName) {
        this.localTableName = localTableName;
    }

    public String getHiveTableName() {
        return hiveTableName;
    }

    public void setHiveTableName(String hiveTableName) {
        this.hiveTableName = hiveTableName;
    }

    public static class BasicColumnInfo {
        private String columnName;

        private String columnType;

        private String columnDesc;

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

        public String getColumnDesc() {
            return columnDesc;
        }

        public void setColumnDesc(String columnDesc) {
            this.columnDesc = columnDesc;
        }
    }
}
