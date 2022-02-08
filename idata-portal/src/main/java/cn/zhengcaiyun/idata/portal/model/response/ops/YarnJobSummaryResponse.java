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

    /**
     * 业务方需要的日志地址逻辑
     * http://bigdata-master3.cai-inc.com:8088/cluster/app/application_1636461038777_141467     killed  failed finished
     * http://bigdata-master3.cai-inc.com:8088/proxy/application_1636461038777_145072/       running
     */
    private String businessLogsUrl;

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

    public String getBusinessLogsUrl() {
        return businessLogsUrl;
    }

    public void setBusinessLogsUrl(String businessLogsUrl) {
        this.businessLogsUrl = businessLogsUrl;
    }
}
