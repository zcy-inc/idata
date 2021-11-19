package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DIJobContentDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    public static final DIJobContent DIJobContent = new DIJobContent();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.id")
    public static final SqlColumn<Long> id = DIJobContent.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.del")
    public static final SqlColumn<Integer> del = DIJobContent.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.creator")
    public static final SqlColumn<String> creator = DIJobContent.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.create_time")
    public static final SqlColumn<Date> createTime = DIJobContent.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editor")
    public static final SqlColumn<String> editor = DIJobContent.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.edit_time")
    public static final SqlColumn<Date> editTime = DIJobContent.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.job_id")
    public static final SqlColumn<Long> jobId = DIJobContent.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.editable")
    public static final SqlColumn<Integer> editable = DIJobContent.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.version")
    public static final SqlColumn<Integer> version = DIJobContent.version;

    /**
     * Database Column Remarks:
     *   数据来源-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_type")
    public static final SqlColumn<String> srcDataSourceType = DIJobContent.srcDataSourceType;

    /**
     * Database Column Remarks:
     *   数据来源-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_data_source_id")
    public static final SqlColumn<Long> srcDataSourceId = DIJobContent.srcDataSourceId;

    /**
     * Database Column Remarks:
     *   数据来源-读取模式，all：全量，incremental：增量
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_mode")
    public static final SqlColumn<String> srcReadMode = DIJobContent.srcReadMode;

    /**
     * Database Column Remarks:
     *   数据来源-过滤条件
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_filter")
    public static final SqlColumn<String> srcReadFilter = DIJobContent.srcReadFilter;

    /**
     * Database Column Remarks:
     *   数据来源-切分键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_read_shard_key")
    public static final SqlColumn<String> srcReadShardKey = DIJobContent.srcReadShardKey;

    /**
     * Database Column Remarks:
     *   数据去向-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_type")
    public static final SqlColumn<String> destDataSourceType = DIJobContent.destDataSourceType;

    /**
     * Database Column Remarks:
     *   数据去向-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_data_source_id")
    public static final SqlColumn<Long> destDataSourceId = DIJobContent.destDataSourceId;

    /**
     * Database Column Remarks:
     *   数据去向-目标表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_table")
    public static final SqlColumn<String> destTable = DIJobContent.destTable;

    /**
     * Database Column Remarks:
     *   数据去向-写入模式，init: 新建表，override: 覆盖表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_write_mode")
    public static final SqlColumn<String> destWriteMode = DIJobContent.destWriteMode;

    /**
     * Database Column Remarks:
     *   数据去向-写入前语句
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_before_write")
    public static final SqlColumn<String> destBeforeWrite = DIJobContent.destBeforeWrite;

    /**
     * Database Column Remarks:
     *   数据去向-写入后语句
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_after_write")
    public static final SqlColumn<String> destAfterWrite = DIJobContent.destAfterWrite;

    /**
     * Database Column Remarks:
     *   作业内容hash
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.content_hash")
    public static final SqlColumn<String> contentHash = DIJobContent.contentHash;

    /**
     * Database Column Remarks:
     *   数据来源-来源表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_tables")
    public static final SqlColumn<String> srcTables = DIJobContent.srcTables;

    /**
     * Database Column Remarks:
     *   数据来源-字段信息，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.src_columns")
    public static final SqlColumn<String> srcColumns = DIJobContent.srcColumns;

    /**
     * Database Column Remarks:
     *   数据去向-字段信息，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di.dest_columns")
    public static final SqlColumn<String> destColumns = DIJobContent.destColumns;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    public static final class DIJobContent extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> editable = column("editable", JDBCType.TINYINT);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<String> srcDataSourceType = column("src_data_source_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> srcDataSourceId = column("src_data_source_id", JDBCType.BIGINT);

        public final SqlColumn<String> srcReadMode = column("src_read_mode", JDBCType.VARCHAR);

        public final SqlColumn<String> srcReadFilter = column("src_read_filter", JDBCType.VARCHAR);

        public final SqlColumn<String> srcReadShardKey = column("src_read_shard_key", JDBCType.VARCHAR);

        public final SqlColumn<String> destDataSourceType = column("dest_data_source_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> destDataSourceId = column("dest_data_source_id", JDBCType.BIGINT);

        public final SqlColumn<String> destTable = column("dest_table", JDBCType.VARCHAR);

        public final SqlColumn<String> destWriteMode = column("dest_write_mode", JDBCType.VARCHAR);

        public final SqlColumn<String> destBeforeWrite = column("dest_before_write", JDBCType.VARCHAR);

        public final SqlColumn<String> destAfterWrite = column("dest_after_write", JDBCType.VARCHAR);

        public final SqlColumn<String> contentHash = column("content_hash", JDBCType.VARCHAR);

        public final SqlColumn<String> srcTables = column("src_tables", JDBCType.LONGVARCHAR);

        public final SqlColumn<String> srcColumns = column("src_columns", JDBCType.LONGVARCHAR);

        public final SqlColumn<String> destColumns = column("dest_columns", JDBCType.LONGVARCHAR);

        public DIJobContent() {
            super("dev_job_content_di");
        }
    }
}