package cn.zhengcaiyun.idata.develop.dal.query;

import javax.annotation.Generated;
import java.util.Date;

public class JobOutputQuery {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.job_id")
    private Long jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.environment")
    private String environment;

    /**
     * Database Column Remarks:
     *   数据去向-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_data_source_type")
    private String destDataSourceType;

    /**
     * Database Column Remarks:
     *   数据去向-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_data_source_id")
    private Long destDataSourceId;

    /**
     * Database Column Remarks:
     *   数据去向-目标表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_table")
    private String destTable;

    /**
     * Database Column Remarks:
     *   数据去向-写入模式，overwrite，upsert
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_write_mode")
    private String destWriteMode;

    /**
     * Database Column Remarks:
     *   数据来源表主键(写入模式为upsert时必填)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.job_target_table_pk")
    private String jobTargetTablePk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getDestDataSourceType() {
        return destDataSourceType;
    }

    public void setDestDataSourceType(String destDataSourceType) {
        this.destDataSourceType = destDataSourceType;
    }

    public Long getDestDataSourceId() {
        return destDataSourceId;
    }

    public void setDestDataSourceId(Long destDataSourceId) {
        this.destDataSourceId = destDataSourceId;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public String getDestWriteMode() {
        return destWriteMode;
    }

    public void setDestWriteMode(String destWriteMode) {
        this.destWriteMode = destWriteMode;
    }

    public String getJobTargetTablePk() {
        return jobTargetTablePk;
    }

    public void setJobTargetTablePk(String jobTargetTablePk) {
        this.jobTargetTablePk = jobTargetTablePk;
    }
}
