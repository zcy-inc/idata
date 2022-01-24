package cn.zhengcaiyun.idata.portal.model.request.ops;

public class JobHistoryGanttRequest {

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 数仓分层code
     */
    private String layerCode;

    /**
     * DAG
     */
    private Long dagId;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }

    public Long getDagId() {
        return dagId;
    }

    public void setDagId(Long dagId) {
        this.dagId = dagId;
    }
}
