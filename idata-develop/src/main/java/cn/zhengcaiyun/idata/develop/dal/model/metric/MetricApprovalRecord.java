package cn.zhengcaiyun.idata.develop.dal.model.metric;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_metric_approval_record
 */
public class MetricApprovalRecord {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建/提交者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建/提交时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   数据指标id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_id")
    private String metricId;

    /**
     * Database Column Remarks:
     *   数据指标名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_name")
    private String metricName;

    /**
     * Database Column Remarks:
     *   数据指标类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_tag")
    private String metricTag;

    /**
     * Database Column Remarks:
     *   数据域
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_domain")
    private String bizDomain;

    /**
     * Database Column Remarks:
     *   业务过程
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_process")
    private String bizProcess;

    /**
     * Database Column Remarks:
     *   审批状态，1：待审批，2：已审批，3：已撤回，4：已驳回
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approval_status")
    private Integer approvalStatus;

    /**
     * Database Column Remarks:
     *   提交备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.submit_remark")
    private String submitRemark;

    /**
     * Database Column Remarks:
     *   审批人
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_operator")
    private String approveOperator;

    /**
     * Database Column Remarks:
     *   审批时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_time")
    private Date approveTime;

    /**
     * Database Column Remarks:
     *   审批备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_remark")
    private String approveRemark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_id")
    public String getMetricId() {
        return metricId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_id")
    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_name")
    public String getMetricName() {
        return metricName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_name")
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_tag")
    public String getMetricTag() {
        return metricTag;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_tag")
    public void setMetricTag(String metricTag) {
        this.metricTag = metricTag;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_domain")
    public String getBizDomain() {
        return bizDomain;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_domain")
    public void setBizDomain(String bizDomain) {
        this.bizDomain = bizDomain;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_process")
    public String getBizProcess() {
        return bizProcess;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_process")
    public void setBizProcess(String bizProcess) {
        this.bizProcess = bizProcess;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approval_status")
    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approval_status")
    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.submit_remark")
    public String getSubmitRemark() {
        return submitRemark;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.submit_remark")
    public void setSubmitRemark(String submitRemark) {
        this.submitRemark = submitRemark;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_operator")
    public String getApproveOperator() {
        return approveOperator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_operator")
    public void setApproveOperator(String approveOperator) {
        this.approveOperator = approveOperator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_time")
    public Date getApproveTime() {
        return approveTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_time")
    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_remark")
    public String getApproveRemark() {
        return approveRemark;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_remark")
    public void setApproveRemark(String approveRemark) {
        this.approveRemark = approveRemark;
    }
}