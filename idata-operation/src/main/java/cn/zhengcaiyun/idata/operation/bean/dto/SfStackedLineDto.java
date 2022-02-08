package cn.zhengcaiyun.idata.operation.bean.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * succeess/fail 折叠折线图
 */
public class SfStackedLineDto {

    // x轴
    List<String> xAxis = new ArrayList<>();

    // y轴-success
    List<String> yAxisSuccessList = new ArrayList<>();

    // y轴-fail
    List<String> yAxisFailList = new ArrayList<>();

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public List<String> getyAxisSuccessList() {
        return yAxisSuccessList;
    }

    public void setyAxisSuccessList(List<String> yAxisSuccessList) {
        this.yAxisSuccessList = yAxisSuccessList;
    }

    public List<String> getyAxisFailList() {
        return yAxisFailList;
    }

    public void setyAxisFailList(List<String> yAxisFailList) {
        this.yAxisFailList = yAxisFailList;
    }
}
