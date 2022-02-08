package cn.zhengcaiyun.idata.develop.dal.model.job;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_event_log
 */
public class JobEventLog {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_id")
    private Long jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.environment")
    private String environment;

    /**
     * Database Column Remarks:
     *   事件，created, updated, deleted ...
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_event")
    private String jobEvent;

    /**
     * Database Column Remarks:
     *   事件信息，用于事件重放处理时使用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.event_info")
    private String eventInfo;

    /**
     * Database Column Remarks:
     *   事件处理状态，0: 待处理，1: 处理成功，9：处理失败
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_status")
    private Integer handleStatus;

    /**
     * Database Column Remarks:
     *   处理结果信息
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_msg")
    private String handleMsg;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_id")
    public Long getJobId() {
        return jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.environment")
    public String getEnvironment() {
        return environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.environment")
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_event")
    public String getJobEvent() {
        return jobEvent;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_event")
    public void setJobEvent(String jobEvent) {
        this.jobEvent = jobEvent;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.event_info")
    public String getEventInfo() {
        return eventInfo;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.event_info")
    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_status")
    public Integer getHandleStatus() {
        return handleStatus;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_status")
    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_msg")
    public String getHandleMsg() {
        return handleMsg;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_msg")
    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }
}