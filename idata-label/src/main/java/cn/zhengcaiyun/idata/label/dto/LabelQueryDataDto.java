package cn.zhengcaiyun.idata.label.dto;


import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;

import java.util.List;

/**
 * @description: 标签数据dto类
 * @author: yangjianhua
 * @create: 2021-06-21 15:30
 **/
public class LabelQueryDataDto {
    private String labelName;
    private String layerName;
    private List<ColumnDto> columns;
    private List<List<String>> data;

    public List<ColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDto> columns) {
        this.columns = columns;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
}
