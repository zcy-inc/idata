package cn.zhengcaiyun.idata.connector.util.model;

import java.util.List;
import java.util.Objects;

public class ColunmObj {
    private String curName;
    private Integer curIndex;
    private String parentName;
    private Integer parentIndex;

    public ColunmObj(String curName, Integer curIndex, String parentName, Integer parentIndex) {
        this.curName = curName;
        this.curIndex = curIndex;
        this.parentName = parentName;
        this.parentIndex = parentIndex;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public Integer getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(Integer curIndex) {
        this.curIndex = curIndex;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColunmObj colunmObj = (ColunmObj) o;
        return Objects.equals(curName, colunmObj.curName) && Objects.equals(curIndex, colunmObj.curIndex) && Objects.equals(parentName, colunmObj.parentName) && Objects.equals(parentIndex, colunmObj.parentIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curName, curIndex, parentName, parentIndex);
    }
}
