package cn.zhengcaiyun.idata.connector.spi.hive.dto;

import java.util.ArrayList;
import java.util.List;

public class CompareInfoDTO {

    /**
     * 是否已同步过hive
     */
    private Boolean existHiveTable;

    private String localTableName;

    private String hiveTableName;

    // 相比hive多的字段
    private List<BasicColumnInfo> moreList = new ArrayList<>();

    // 相比hive少的字段
    private List<BasicColumnInfo> lessList = new ArrayList<>();

    // 相比hive不同的字段
    private List<ChangeColumnInfo> differentList = new ArrayList<>();

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

    public List<ChangeColumnInfo> getDifferentList() {
        return differentList;
    }

    public void setDifferentList(List<ChangeColumnInfo> differentList) {
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
        public BasicColumnInfo() {
        }

        public BasicColumnInfo(String columnName, String columnType, String columnComment) {
            this.columnName = columnName;
            this.columnType = columnType;
            this.columnComment = columnComment;
        }

        private String columnName;

        private String columnType;

        private String columnComment;

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

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }
    }

    public static class ChangeColumnInfo {

        private String columnName;

        private String columnType;

        private String columnComment;

        private String hiveColumnName;

        private String hiveColumnType;

        private String hiveColumnComment;

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

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }

        public String getHiveColumnName() {
            return hiveColumnName;
        }

        public void setHiveColumnName(String hiveColumnName) {
            this.hiveColumnName = hiveColumnName;
        }

        public String getHiveColumnType() {
            return hiveColumnType;
        }

        public void setHiveColumnType(String hiveColumnType) {
            this.hiveColumnType = hiveColumnType;
        }

        public String getHiveColumnComment() {
            return hiveColumnComment;
        }

        public void setHiveColumnComment(String hiveColumnComment) {
            this.hiveColumnComment = hiveColumnComment;
        }
    }
}
