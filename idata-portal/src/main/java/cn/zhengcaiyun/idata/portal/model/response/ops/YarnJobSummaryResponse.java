package cn.zhengcaiyun.idata.portal.model.response.ops;

public class YarnJobSummaryResponse {

    private Long JobId;

    /**
     *  任务名称
     */
    private String jobName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     *   application master container url地址
     */
    private String amContainerLogsUrl;

    public Long getJobId() {
        return JobId;
    }

    public void setJobId(Long jobId) {
        JobId = jobId;
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

    public String getAmContainerLogsUrl() {
        return amContainerLogsUrl;
    }

    public void setAmContainerLogsUrl(String amContainerLogsUrl) {
        this.amContainerLogsUrl = amContainerLogsUrl;
    }
}
