package cn.zhengcaiyun.idata.connector.clients.hive.model;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MetadataInfo {

    private String dbName;

    private String tableName;

    private String tableComment;

    private List<ColumnInfo> columnList = new ArrayList<>();

    private List<ColumnInfo> partitionColumnList = new ArrayList<>();

    /**
     * 是否是分区表
     * @return
     */
    public boolean isPartitionTable() {
        return !CollectionUtils.isEmpty(partitionColumnList);
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<ColumnInfo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnInfo> columnList) {
        this.columnList = columnList;
    }

    public List<ColumnInfo> getPartitionColumnList() {
        return partitionColumnList;
    }

    public void setPartitionColumnList(List<ColumnInfo> partitionColumnList) {
        this.partitionColumnList = partitionColumnList;
    }

    public static class ColumnInfo {

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
}
