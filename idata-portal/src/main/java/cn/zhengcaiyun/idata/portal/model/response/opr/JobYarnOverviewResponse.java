package cn.zhengcaiyun.idata.portal.model.response.opr;

/**
 * yarn作业情况概览
 */
public class JobYarnOverviewResponse {

    /**
     *作业总数
     */
    private Integer total;

    /**
     * 运行中
     */
    private Integer waiting;

    /**
     * 运行中
     */
    private Integer running;

    /**
     * 失败数
     */
    private Integer failure;

    /**
     * 成功数
     */
    private Integer success;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getWaiting() {
        return waiting;
    }

    public void setWaiting(Integer waiting) {
        this.waiting = waiting;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
