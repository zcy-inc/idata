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
     * 最终状态
     */
    private String jobStatus;

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

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}
