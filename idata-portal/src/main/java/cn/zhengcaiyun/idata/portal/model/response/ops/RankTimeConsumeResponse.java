package cn.zhengcaiyun.idata.portal.model.response.ops;

import java.util.Date;

public class RankTimeConsumeResponse {

    /**
     * job id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 作业持续时间(min)
     */
    private String duration;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date finishTime;

    /**
     * 平均内存
     */
    private Double avgDuration;

    /**
     * 平均执行时长(min)
     */
    private String avgDurationStr;

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
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setAvgDurationStr(String avgDurationStr) {
        this.avgDurationStr = avgDurationStr;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public String getAvgDurationStr() {
        if (avgDuration != null) {
            long secondsTotal = avgDuration.longValue() / 1000;
            long minutes = secondsTotal / 60;
            long seconds = secondsTotal % 60;
            return minutes + ":" + seconds;
        }
        return null;
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

