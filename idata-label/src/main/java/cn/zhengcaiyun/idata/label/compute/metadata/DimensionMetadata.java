package cn.zhengcaiyun.idata.label.compute.metadata;

/**
 * 维度信息
 *
 * @description: 数据来自指标系统
 * @author: yangjianhua
 * @create: 2021-06-25 09:51
 **/
public class DimensionMetadata {
    private String code;
    private String name;
    private String table;
    private String column;

    private String[] dimensionParams;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String[] getDimensionParams() {
        return dimensionParams;
    }

    public void setDimensionParams(String[] dimensionParams) {
        this.dimensionParams = dimensionParams;
    }
}
