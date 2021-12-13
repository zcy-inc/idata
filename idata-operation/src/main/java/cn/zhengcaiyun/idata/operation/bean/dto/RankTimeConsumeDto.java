package cn.zhengcaiyun.idata.operation.bean.dto;

import cn.hutool.core.util.NumberUtil;

import java.util.Date;

public class RankTimeConsumeDto {

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 应用运行id
     */
    private String applicationId;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 平均执行时长
     */
    private Double avgDuration;

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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        this.avgDuration = avgDuration;
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


}
