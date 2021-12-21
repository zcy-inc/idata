package cn.zhengcaiyun.idata.portal.model.request.ops;

import org.springframework.web.bind.annotation.RequestParam;

public class JobHistoryRequest {

    /**
     * 开始日期
     */
    private String startDateBegin;

    /**
     * 开始日期
     */
    private String startDateEnd;

    /**
     * 结束日期
     */
    private String finishDateBegin;

    /**
     * 结束日期
     */
    private String finishDateEnd;

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务状态：1:队列中 2：运行中 6：失败 7：成功 -1：其他
     */
    private Integer jobStatus;

    public String getStartDateBegin() {
        return startDateBegin;
    }

    public void setStartDateBegin(String startDateBegin) {
        this.startDateBegin = startDateBegin;
    }

    public String getStartDateEnd() {
        return startDateEnd;
    }

    public void setStartDateEnd(String startDateEnd) {
        this.startDateEnd = startDateEnd;
    }

    public String getFinishDateBegin() {
        return finishDateBegin;
    }

    public void setFinishDateBegin(String finishDateBegin) {
        this.finishDateBegin = finishDateBegin;
    }

    public String getFinishDateEnd() {
        return finishDateEnd;
    }

    public void setFinishDateEnd(String finishDateEnd) {
        this.finishDateEnd = finishDateEnd;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }
}
