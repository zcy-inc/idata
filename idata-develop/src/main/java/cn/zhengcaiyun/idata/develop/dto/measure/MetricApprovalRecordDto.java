package cn.zhengcaiyun.idata.develop.dto.measure;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.metric.MetricApprovalRecord;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class MetricApprovalRecordDto extends BaseDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 数据指标id
     */
    private String metricId;

    /**
     * 数据指标名称
     */
    private String metricName;

    /**
     * 数据指标类型
     */
    private String metricTag;
    /**
     * 数据指标类型
     */
    private String metricTagName;

    /**
     * 数据域
     */
    private String bizDomain;
    /**
     * 数据域
     */
    private String bizDomainName;

    /**
     * 业务过程
     */
    private String bizProcess;
    /**
     * 业务过程
     */
    private String bizProcessName;

    /**
     * 审批状态，1：待审批，2：已审批，3：已撤回，4：已驳回
     */
    private Integer approvalStatus;
    /**
     * 审批状态
     */
    private String approvalStatusName;

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

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricTag() {
        return metricTag;
    }

    public void setMetricTag(String metricTag) {
        this.metricTag = metricTag;
    }

    public String getMetricTagName() {
        return metricTagName;
    }

    public void setMetricTagName(String metricTagName) {
        this.metricTagName = metricTagName;
    }

    public String getBizDomain() {
        return bizDomain;
    }

    public void setBizDomain(String bizDomain) {
        this.bizDomain = bizDomain;
    }

    public String getBizDomainName() {
        return bizDomainName;
    }

    public void setBizDomainName(String bizDomainName) {
        this.bizDomainName = bizDomainName;
    }

    public String getBizProcess() {
        return bizProcess;
    }

    public void setBizProcess(String bizProcess) {
        this.bizProcess = bizProcess;
    }

    public String getBizProcessName() {
        return bizProcessName;
    }

    public void setBizProcessName(String bizProcessName) {
        this.bizProcessName = bizProcessName;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
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

    public static MetricApprovalRecordDto from(MetricApprovalRecord record) {
        MetricApprovalRecordDto dto = new MetricApprovalRecordDto();
        BeanUtils.copyProperties(record, dto);

        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(record.getMetricTag())) {
            dto.setMetricTagName("原子指标");
        } else if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(record.getMetricTag())) {
            dto.setMetricTagName("派生指标");
        } else if (LabelTagEnum.COMPLEX_METRIC_LABEL.name().equals(record.getMetricTag())) {
            dto.setMetricTagName("复合指标");
        }
        return dto;
    }
}
