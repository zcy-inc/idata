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
     *   来源为kafka数据类型的topic
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_topic")
    private String srcTopic;

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
     *   数据来源-分片数量（并行度）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_sharding_num")
    private Integer srcShardingNum;

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
     *   目标为kafka的数据类的topic
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_topic")
    private String destTopic;

    /**
     * Database Column Remarks:
     *   数据去向-目标表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table")
    private String destTable;

    /**
     * Database Column Remarks:
     *   数据去向-写入模式，init: 新建表，override: 覆盖表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_write_mode")
    private String destWriteMode;

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
     *   目标库中间件的内置属性
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_properties")
    private String destProperties;

    /**
     * Database Column Remarks:
     *   多分片写入（并行度）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_sharding_num")
    private Integer destShardingNum;

    /**
     * Database Column Remarks:
     *   单次批量写入数据条数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_bulk_num")
    private Long destBulkNum;

    /**
     * Database Column Remarks:
     *   作业内容hash
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    private String contentHash;

    /**
     * Database Column Remarks:
     *   DI作业增量模式的mergeSql
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.merge_sql")
    private String mergeSql;

    /**
     * Database Column Remarks:
     *   脚本模式，作用同可视化src_columns
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_select_columns")
    private String scriptSelectColumns;

    /**
     * Database Column Remarks:
     *   脚本模式，同可视化src_columns中primaryKey字段
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_key_columns")
    private String scriptKeyColumns;

    /**
     * Database Column Remarks:
     *   merge_sql的参数，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql_param")
    private String scriptMergeSqlParam;

    /**
     * Database Column Remarks:
     *   配置模式，1：可视化模式，2：脚本模式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.config_mode")
    private Integer configMode;

    /**
     * Database Column Remarks:
     *   数据来源-来源表
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
     *   补充迁移旧版Idata的di_query字段。用于复杂sql或函数sql，解决除了单表的简单列映射之外的场景。
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_query")
    private String srcQuery;

    /**
     * Database Column Remarks:
     *   数据去向-字段信息，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    private String destColumns;

    /**
     * Database Column Remarks:
     *   脚本模式，同可视化merge_sql
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql")
    private String scriptMergeSql;

    /**
     * Database Column Remarks:
     *   脚本模式，同可视化src_query
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_query")
    private String scriptQuery;

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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_topic")
    public String getSrcTopic() {
        return srcTopic;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_topic")
    public void setSrcTopic(String srcTopic) {
        this.srcTopic = srcTopic;
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_sharding_num")
    public Integer getSrcShardingNum() {
        return srcShardingNum;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_sharding_num")
    public void setSrcShardingNum(Integer srcShardingNum) {
        this.srcShardingNum = srcShardingNum;
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_topic")
    public String getDestTopic() {
        return destTopic;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_topic")
    public void setDestTopic(String destTopic) {
        this.destTopic = destTopic;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table")
    public String getDestTable() {
        return destTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table")
    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_write_mode")
    public String getDestWriteMode() {
        return destWriteMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_write_mode")
    public void setDestWriteMode(String destWriteMode) {
        this.destWriteMode = destWriteMode;
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_properties")
    public String getDestProperties() {
        return destProperties;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_properties")
    public void setDestProperties(String destProperties) {
        this.destProperties = destProperties;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_sharding_num")
    public Integer getDestShardingNum() {
        return destShardingNum;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_sharding_num")
    public void setDestShardingNum(Integer destShardingNum) {
        this.destShardingNum = destShardingNum;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_bulk_num")
    public Long getDestBulkNum() {
        return destBulkNum;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_bulk_num")
    public void setDestBulkNum(Long destBulkNum) {
        this.destBulkNum = destBulkNum;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    public String getContentHash() {
        return contentHash;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.merge_sql")
    public String getMergeSql() {
        return mergeSql;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.merge_sql")
    public void setMergeSql(String mergeSql) {
        this.mergeSql = mergeSql;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_select_columns")
    public String getScriptSelectColumns() {
        return scriptSelectColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_select_columns")
    public void setScriptSelectColumns(String scriptSelectColumns) {
        this.scriptSelectColumns = scriptSelectColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_key_columns")
    public String getScriptKeyColumns() {
        return scriptKeyColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_key_columns")
    public void setScriptKeyColumns(String scriptKeyColumns) {
        this.scriptKeyColumns = scriptKeyColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql_param")
    public String getScriptMergeSqlParam() {
        return scriptMergeSqlParam;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql_param")
    public void setScriptMergeSqlParam(String scriptMergeSqlParam) {
        this.scriptMergeSqlParam = scriptMergeSqlParam;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.config_mode")
    public Integer getConfigMode() {
        return configMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.config_mode")
    public void setConfigMode(Integer configMode) {
        this.configMode = configMode;
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_query")
    public String getSrcQuery() {
        return srcQuery;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_query")
    public void setSrcQuery(String srcQuery) {
        this.srcQuery = srcQuery;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    public String getDestColumns() {
        return destColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    public void setDestColumns(String destColumns) {
        this.destColumns = destColumns;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql")
    public String getScriptMergeSql() {
        return scriptMergeSql;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_merge_sql")
    public void setScriptMergeSql(String scriptMergeSql) {
        this.scriptMergeSql = scriptMergeSql;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_query")
    public String getScriptQuery() {
        return scriptQuery;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.script_query")
    public void setScriptQuery(String scriptQuery) {
        this.scriptQuery = scriptQuery;
    }
}