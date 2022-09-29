package cn.zhengcaiyun.idata.develop.dal.model.job;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_info
 */
public class DevJobInfo {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   作业名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.name")
    private String name;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_type")
    private String jobType;

    /**
     * Database Column Remarks:
     *   作业使用语言
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_language")
    private String jobLanguage;

    /**
     * Database Column Remarks:
     *   作业执行引擎
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_execution_engine")
    private String jobExecutionEngine;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.dw_layer_code")
    private String dwLayerCode;

    /**
     * Database Column Remarks:
     *   状态，1启用，0停用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.status")
    private Integer status;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.remark")
    private String remark;

    /**
     * Database Column Remarks:
     *   文件夹id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.folder_id")
    private Long folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.name")
    public void setName(String name) {
        this.name = name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_type")
    public String getJobType() {
        return jobType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_type")
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_language")
    public String getJobLanguage() {
        return jobLanguage;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_language")
    public void setJobLanguage(String jobLanguage) {
        this.jobLanguage = jobLanguage;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_execution_engine")
    public String getJobExecutionEngine() {
        return jobExecutionEngine;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_execution_engine")
    public void setJobExecutionEngine(String jobExecutionEngine) {
        this.jobExecutionEngine = jobExecutionEngine;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.dw_layer_code")
    public String getDwLayerCode() {
        return dwLayerCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.dw_layer_code")
    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.status")
    public Integer getStatus() {
        return status;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.remark")
    public String getRemark() {
        return remark;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.folder_id")
    public Long getFolderId() {
        return folderId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.folder_id")
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}