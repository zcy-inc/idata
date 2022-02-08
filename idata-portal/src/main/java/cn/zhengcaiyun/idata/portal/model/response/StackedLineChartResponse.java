package cn.zhengcaiyun.idata.portal.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆叠折线图-通用
 * @param <T>
 */
public class StackedLineChartResponse<T> {

    /**
     * x轴
     */
    List<String> xAxis = new ArrayList<>();

    /**
     * y轴
     */
    List<YAxis> yAxisList = new ArrayList<>();

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public List<YAxis> getyAxisList() {
        return yAxisList;
    }

    public void setyAxisList(List<YAxis> yAxisList) {
        this.yAxisList = yAxisList;
    }

    public static class YAxis {

        /**
         * y轴名称
         */
        private String name;

        /**
         * y轴值
         */
        private List<String> yAxis;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getyAxis() {
            return yAxis;
        }

        public void setyAxis(List<String> yAxis) {
            this.yAxis = yAxis;
        }
    }
}
