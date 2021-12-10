package cn.zhengcaiyun.idata.operation.bean.dto;

public class JobStatisticDto {

    /**
     * 等待运行/队列中
     */
    private Integer ready = 0;

    /**
     * 运行中
     */
    private Integer running = 0;

    /**
     * 失败数
     */
    private Integer failure = 0;

    /**
     * 成功数
     */
    private Integer success = 0;

    /**
     * 其他
     */
    private Integer other = 0;

    public Integer getReady() {
        return ready;
    }

    public void setReady(Integer ready) {
        this.ready = ready;
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

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getSum() {
        return other + ready + success + failure + running;
    }

}
