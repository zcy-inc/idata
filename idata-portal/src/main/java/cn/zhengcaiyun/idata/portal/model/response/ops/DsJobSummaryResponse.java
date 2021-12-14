package cn.zhengcaiyun.idata.portal.model.response.ops;

public class DsJobSummaryResponse {

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
     * 任务实例id
     */
    private Long taskId;

    /**
     * 环境
     */
    private String environment;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
