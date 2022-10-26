package cn.zhengcaiyun.idata.connector.util.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableSib {
    private SqlTypeEnum sqlType;
    private List<TableObj> inputTables = new ArrayList<>();
    private TableObj outputTable;
    private Set<ColunmObj> colList = new HashSet<>();

    public List<TableObj> getInputTables() {
        return inputTables;
    }

    public void setInputTables(List<TableObj> inputTables) {
        this.inputTables = inputTables;
    }

    public TableObj getOutputTable() {
        return outputTable;
    }

    public void setOutputTable(TableObj outputTable) {
        this.outputTable = outputTable;
    }

    public SqlTypeEnum getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlTypeEnum sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public String toString() {
        return "TableSib{" +
                "sqlType=" + sqlType +
                ", inputTables=" + inputTables +
                ", outputTable=" + outputTable +
                '}';
    }

    public Set<ColunmObj> getColList() {
        return colList;
    }

    public void setColList(Set<ColunmObj> colList) {
        this.colList = colList;
    }
}
