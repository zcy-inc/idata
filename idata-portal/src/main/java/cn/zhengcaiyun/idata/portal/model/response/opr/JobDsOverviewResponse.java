package cn.zhengcaiyun.idata.portal.model.response.opr;

/**
 * DS调度任务情况概览
 */
public class JobDsOverviewResponse {

    /**
     *作业总数
     */
    private Integer total;

    /**
     * 等待运行
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

    /**
     * 其他
     */
    private Integer other;

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

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
