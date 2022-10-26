package cn.zhengcaiyun.idata.connector.util.model;

import java.util.List;

public class TableObj {
    private String db;
    private String tableName;
    private List<ColunmObj> colList;

    public TableObj(String db,String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColunmObj> getColList() {
        return colList;
    }

    public void setColList(List<ColunmObj> colList) {
        this.colList = colList;
    }


    @Override
    public String toString() {
        return "TableObj{" +
                "tableName='" + tableName + '\'' +
                ", colList=" + colList +
                '}';
    }
}
