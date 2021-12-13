package cn.zhengcaiyun.idata.develop.dto.job;

import javax.annotation.Generated;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_history
 */
public class JobHistoryDto {
    private Long id;

    private Date createTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    private Long jobId;

    /**
     * Database Column Remarks:
     *   作业开始时间
     */
    private Date startTime;

    /**
     * Database Column Remarks:
     *   作业结束时间
     */
    private Date finishTime;

    /**
     * Database Column Remarks:
     *   作业持续时间（ms）
     */
    private Long duration;

    /**
     * Database Column Remarks:
     *   作业最终状态
     */
    private String finalStatus;

    /**
     * Database Column Remarks:
     *   作业平均消耗cpu虚拟核数
     */
    private Double avgVcores;

    /**
     * Database Column Remarks:
     *   作业平均消耗内存（MB）
     */
    private Long avgMemory;

    /**
     * Database Column Remarks:
     *   yarn的application
     */
    private String applicationId;

    private Long avgDuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Long getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Long avgDuration) {
        this.avgDuration = avgDuration;
    }
}