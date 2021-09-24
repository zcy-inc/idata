package cn.zhengcaiyun.idata.develop.dal.model.job;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_content_di
 */
public class DIJobContent {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.job_id")
    private Long jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editable")
    private Integer editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.version")
    private Integer version;

    /**
     * Database Column Remarks:
     *   数据来源-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_type")
    private String srcDataSourceType;

    /**
     * Database Column Remarks:
     *   数据来源-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_id")
    private Long srcDataSourceId;

    /**
     * Database Column Remarks:
     *   数据来源-读取模式，all：全量，incremental：增量
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_mode")
    private String srcReadMode;

    /**
     * Database Column Remarks:
     *   数据来源-过滤条件
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_filter")
    private String srcReadFilter;

    /**
     * Database Column Remarks:
     *   数据来源-切分键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_shard_key")
    private String srcReadShardKey;

    /**
     * Database Column Remarks:
     *   数据去向-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_type")
    private String destDataSourceType;

    /**
     * Database Column Remarks:
     *   数据去向-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_id")
    private Long destDataSourceId;

    /**
     * Database Column Remarks:
     *   数据去向-数仓表id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table_id")
    private Long destTableId;

    /**
     * Database Column Remarks:
     *   数据去向-写入模式，override，upsert
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_write_mode")
    private String srcWriteMode;

    /**
     * Database Column Remarks:
     *   数据去向-写入前语句
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_before_write")
    private String destBeforeWrite;

    /**
     * Database Column Remarks:
     *   数据去向-写入后语句
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_after_write")
    private String destAfterWrite;

    /**
     * Database Column Remarks:
     *   作业内容hash
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    private String contentHash;

    /**
     * Database Column Remarks:
     *   数据来源-表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_tables")
    private String srcTables;

    /**
     * Database Column Remarks:
     *   数据来源-字段信息，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_columns")
    private String srcColumns;

    /**
     * Database Column Remarks:
     *   数据去向-字段信息，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    private String destColumns;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.job_id")
    public Long getJobId() {
        return jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editable")
    public Integer getEditable() {
        return editable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editable")
    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.version")
    public Integer getVersion() {
        return version;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_type")
    public String getSrcDataSourceType() {
        return srcDataSourceType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_type")
    public void setSrcDataSourceType(String srcDataSourceType) {
        this.srcDataSourceType = srcDataSourceType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_id")
    public Long getSrcDataSourceId() {
        return srcDataSourceId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_id")
    public void setSrcDataSourceId(Long srcDataSourceId) {
        this.srcDataSourceId = srcDataSourceId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_mode")
    public String getSrcReadMode() {
        return srcReadMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_mode")
    public void setSrcReadMode(String srcReadMode) {
        this.srcReadMode = srcReadMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_filter")
    public String getSrcReadFilter() {
        return srcReadFilter;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_filter")
    public void setSrcReadFilter(String srcReadFilter) {
        this.srcReadFilter = srcReadFilter;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_shard_key")
    public String getSrcReadShardKey() {
        return srcReadShardKey;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_shard_key")
    public void setSrcReadShardKey(String srcReadShardKey) {
        this.srcReadShardKey = srcReadShardKey;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_type")
    public String getDestDataSourceType() {
        return destDataSourceType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_type")
    public void setDestDataSourceType(String destDataSourceType) {
        this.destDataSourceType = destDataSourceType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_id")
    public Long getDestDataSourceId() {
        return destDataSourceId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_id")
    public void setDestDataSourceId(Long destDataSourceId) {
        this.destDataSourceId = destDataSourceId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table_id")
    public Long getDestTableId() {
        return destTableId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table_id")
    public void setDestTableId(Long destTableId) {
        this.destTableId = destTableId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_write_mode")
    public String getSrcWriteMode() {
        return srcWriteMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_write_mode")
    public void setSrcWriteMode(String srcWriteMode) {
        this.srcWriteMode = srcWriteMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_before_write")
    public String getDestBeforeWrite() {
        return destBeforeWrite;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_before_write")
    public void setDestBeforeWrite(String destBeforeWrite) {
        this.destBeforeWrite = destBeforeWrite;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_after_write")
    public String getDestAfterWrite() {
        return destAfterWrite;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_after_write")
    public void setDestAfterWrite(String destAfterWrite) {
        this.destAfterWrite = destAfterWrite;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    public String getContentHash() {
        return contentHash;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_tables")
    public String getSrcTables() {
        return srcTables;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_tables")
    public void setSrcTables(String srcTables) {
        this.srcTables = srcTables;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_columns")
    public String getSrcColumns() {
        return srcColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_columns")
    public void setSrcColumns(String srcColumns) {
        this.srcColumns = srcColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    public String getDestColumns() {
        return destColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    public void setDestColumns(String destColumns) {
        this.destColumns = destColumns;
    }
}