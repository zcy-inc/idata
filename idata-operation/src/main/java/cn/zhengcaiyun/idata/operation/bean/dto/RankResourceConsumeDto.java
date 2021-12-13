package cn.zhengcaiyun.idata.operation.bean.dto;

import java.util.Date;

public class RankResourceConsumeDto {

    /**
     * job id
     */
    private Long jobId;

    /**
     * 应用运行id
     */
    private String applicationId;

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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}

