package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DIStreamJobContentDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    public static final DIStreamJobContent DI_STREAM_JOB_CONTENT = new DIStreamJobContent();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.id")
    public static final SqlColumn<Long> id = DI_STREAM_JOB_CONTENT.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.del")
    public static final SqlColumn<Integer> del = DI_STREAM_JOB_CONTENT.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.creator")
    public static final SqlColumn<String> creator = DI_STREAM_JOB_CONTENT.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.create_time")
    public static final SqlColumn<Date> createTime = DI_STREAM_JOB_CONTENT.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.editor")
    public static final SqlColumn<String> editor = DI_STREAM_JOB_CONTENT.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.edit_time")
    public static final SqlColumn<Date> editTime = DI_STREAM_JOB_CONTENT.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.job_id")
    public static final SqlColumn<Long> jobId = DI_STREAM_JOB_CONTENT.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.editable")
    public static final SqlColumn<Integer> editable = DI_STREAM_JOB_CONTENT.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.version")
    public static final SqlColumn<Integer> version = DI_STREAM_JOB_CONTENT.version;

    /**
     * Database Column Remarks:
     *   数据来源-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.src_data_source_type")
    public static final SqlColumn<String> srcDataSourceType = DI_STREAM_JOB_CONTENT.srcDataSourceType;

    /**
     * Database Column Remarks:
     *   数据来源-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.src_data_source_id")
    public static final SqlColumn<Long> srcDataSourceId = DI_STREAM_JOB_CONTENT.srcDataSourceId;

    /**
     * Database Column Remarks:
     *   数据去向-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.dest_data_source_type")
    public static final SqlColumn<String> destDataSourceType = DI_STREAM_JOB_CONTENT.destDataSourceType;

    /**
     * Database Column Remarks:
     *   数据去向-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.dest_data_source_id")
    public static final SqlColumn<Long> destDataSourceId = DI_STREAM_JOB_CONTENT.destDataSourceId;

    /**
     * Database Column Remarks:
     *   数据来源-来源表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream.cdc_tables")
    public static final SqlColumn<String> cdcTables = DI_STREAM_JOB_CONTENT.cdcTables;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    public static final class DIStreamJobContent extends SqlTable {
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

        public final SqlColumn<String> destDataSourceType = column("dest_data_source_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> destDataSourceId = column("dest_data_source_id", JDBCType.BIGINT);

        public final SqlColumn<String> cdcTables = column("cdc_tables", JDBCType.LONGVARCHAR);

        public DIStreamJobContent() {
            super("dev_job_content_di_stream");
        }
    }
}