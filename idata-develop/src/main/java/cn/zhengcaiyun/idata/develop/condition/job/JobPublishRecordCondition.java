package cn.zhengcaiyun.idata.develop.condition.job;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobPublishRecordCondition {

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业内容id
     */
    private Long jobContentId;

    /**
     * 作业内容版本号
     */
    private Integer jobContentVersion;

    /**
     * 环境
     */
    private String environment;

    /**
     * 发布状态，1：待发布，2：已发布，4：已驳回，9：已归档
     */
    private Integer publishStatus;

    /**
     * 作业类型
     */
    private String jobTypeCode;

    /**
     * 数仓分层
     */
    private String dwLayerCode;

    /**
     * 提交人
     */
    private String submitOperator;

    /**
     * 作业id集合
     */
    private List<Long> jobIds;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobContentId() {
        return jobContentId;
    }

    public void setJobContentId(Long jobContentId) {
        this.jobContentId = jobContentId;
    }

    public Integer getJobContentVersion() {
        return jobContentVersion;
    }

    public void setJobContentVersion(Integer jobContentVersion) {
        this.jobContentVersion = jobContentVersion;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getJobTypeCode() {
        return jobTypeCode;
    }

    public void setJobTypeCode(String jobTypeCode) {
        this.jobTypeCode = jobTypeCode;
    }

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public String getSubmitOperator() {
        return submitOperator;
    }

    public void setSubmitOperator(String submitOperator) {
        this.submitOperator = submitOperator;
    }

    public List<Long> getJobIds() {
        return jobIds;
    }

    public void setJobIds(List<Long> jobIds) {
        this.jobIds = jobIds;
    }
}