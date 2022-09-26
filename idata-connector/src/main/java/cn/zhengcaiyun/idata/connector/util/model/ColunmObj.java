package cn.zhengcaiyun.idata.connector.util.model;

import java.util.List;

public class ColunmObj {
    private String colName;
    private List<String> relyList;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public List<String> getRelyList() {
        return relyList;
    }

    public void setRelyList(List<String> relyList) {
        this.relyList = relyList;
    }

    @Override
    public String toString() {
        return "ColunmObj{" +
                "colName='" + colName + '\'' +
                ", relyList=" + relyList +
                '}';
    }
}
