package cn.zhengcaiyun.idata.develop.dal.model.job;

import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_content_di_stream_table
 */
public class DIStreamJobTable {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_id")
    private Long jobId;

    /**
     * Database Column Remarks:
     *   作业内容id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_id")
    private Long jobContentId;

    /**
     * Database Column Remarks:
     *   作业内容版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_version")
    private Integer jobContentVersion;

    /**
     * Database Column Remarks:
     *   数据来源-表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.src_table")
    private String srcTable;

    /**
     * Database Column Remarks:
     *   数据去向-表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.dest_table")
    private String destTable;

    /**
     * Database Column Remarks:
     *   是否分表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.sharding")
    private Integer sharding;

    /**
     * Database Column Remarks:
     *   是否强制初始化，0：否，1：是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.force_init")
    private Integer forceInit;

    /**
     * Database Column Remarks:
     *   表cdc配置
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.table_cdc_props")
    private String tableCdcProps;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_id")
    public Long getJobId() {
        return jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_id")
    public Long getJobContentId() {
        return jobContentId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_id")
    public void setJobContentId(Long jobContentId) {
        this.jobContentId = jobContentId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_version")
    public Integer getJobContentVersion() {
        return jobContentVersion;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_version")
    public void setJobContentVersion(Integer jobContentVersion) {
        this.jobContentVersion = jobContentVersion;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.src_table")
    public String getSrcTable() {
        return srcTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.src_table")
    public void setSrcTable(String srcTable) {
        this.srcTable = srcTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.dest_table")
    public String getDestTable() {
        return destTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.dest_table")
    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.sharding")
    public Integer getSharding() {
        return sharding;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.sharding")
    public void setSharding(Integer sharding) {
        this.sharding = sharding;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.force_init")
    public Integer getForceInit() {
        return forceInit;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.force_init")
    public void setForceInit(Integer forceInit) {
        this.forceInit = forceInit;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.table_cdc_props")
    public String getTableCdcProps() {
        return tableCdcProps;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.table_cdc_props")
    public void setTableCdcProps(String tableCdcProps) {
        this.tableCdcProps = tableCdcProps;
    }
}