package cn.zhengcaiyun.idata.label.compute.metadata;

/**
 * @description: 标签主体dto，继承自维度dto
 * @author: yangjianhua
 * @create: 2021-06-29 11:48
 **/
public class ObjectMetadata extends DimensionMetadata {
    /**
     * 标签主体维度主表，用来关联查询主体名称
     */
    private String originTable;

    public String getOriginTable() {
        return originTable;
    }

    public void setOriginTable(String originTable) {
        this.originTable = originTable;
    }
}
