package cn.zhengcaiyun.idata.operation.bean.dto;

public class JobDsOverviewDto {

    /**
     * 等待运行
     */
    private Integer waiting = 0;

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

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }
}
