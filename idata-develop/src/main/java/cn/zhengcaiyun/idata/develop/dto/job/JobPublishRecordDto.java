package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobPublishRecordDto {
    /**
     * 主键
     */
    private Long id;

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
     * 作业类型
     */
    private String jobTypeCode;

    /**
     * 数仓分层
     */
    private String dwLayerCode;

    /**
     * 环境
     */
    private String environment;

    /**
     * 发布状态，1：待发布，2：已发布，4：已驳回，9：已归档
     */
    private Integer publishStatus;

    /**
     * 提交备注
     */
    private String submitRemark;

    /**
     * 审批人
     */
    private String approveOperator;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 审批备注
     */
    private String approveRemark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSubmitRemark() {
        return submitRemark;
    }

    public void setSubmitRemark(String submitRemark) {
        this.submitRemark = submitRemark;
    }

    public String getApproveOperator() {
        return approveOperator;
    }

    public void setApproveOperator(String approveOperator) {
        this.approveOperator = approveOperator;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getApproveRemark() {
        return approveRemark;
    }

    public void setApproveRemark(String approveRemark) {
        this.approveRemark = approveRemark;
    }

    public static JobPublishRecordDto from(JobPublishRecord record) {
        JobPublishRecordDto dto = new JobPublishRecordDto();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }

    public JobPublishRecord toModel() {
        JobPublishRecord record = new JobPublishRecord();
        BeanUtils.copyProperties(this, record);
        return record;
    }
}