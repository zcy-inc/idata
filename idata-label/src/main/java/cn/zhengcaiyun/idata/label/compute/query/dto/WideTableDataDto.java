package cn.zhengcaiyun.idata.label.compute.query.dto;


import java.util.List;

/**
 * @author shiyin(沐泽)
 * @date 2020/6/16 10:42
 */
public class WideTableDataDto {
    private List<ColumnDto> meta;
    private List<List<String>> data;

    public List<ColumnDto> getMeta() {
        return meta;
    }

    public void setMeta(List<ColumnDto> meta) {
        this.meta = meta;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
