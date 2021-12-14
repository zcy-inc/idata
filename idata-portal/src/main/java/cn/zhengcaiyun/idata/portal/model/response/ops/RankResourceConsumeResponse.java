package cn.zhengcaiyun.idata.portal.model.response.ops;

import javax.annotation.Generated;
import java.util.Date;

public class RankResourceConsumeResponse {

    /**
     * job id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     *   作业平均消耗cpu虚拟核数
     */
    private Double avgVcores;

    /**
     *   作业平均消耗内存（MB）
     */
    private Long avgMemory;

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
     *   application master container url地址
     */
    private String amContainerLogsUrl;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Double getAvgVcores() {
        return avgVcores;
    }

    public void setAvgVcores(Double avgVcores) {
        this.avgVcores = avgVcores;
    }

    public Long getAvgMemory() {
        return avgMemory;
    }

    public void setAvgMemory(Long avgMemory) {
        this.avgMemory = avgMemory;
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

    public String getAmContainerLogsUrl() {
        return amContainerLogsUrl;
    }

    public void setAmContainerLogsUrl(String amContainerLogsUrl) {
        this.amContainerLogsUrl = amContainerLogsUrl;
    }
}

