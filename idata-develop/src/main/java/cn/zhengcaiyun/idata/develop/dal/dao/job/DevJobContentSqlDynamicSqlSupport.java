package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobContentSqlDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    public static final DevJobContentSql devJobContentSql = new DevJobContentSql();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.id")
    public static final SqlColumn<Long> id = devJobContentSql.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.del")
    public static final SqlColumn<Integer> del = devJobContentSql.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.creator")
    public static final SqlColumn<String> creator = devJobContentSql.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.create_time")
    public static final SqlColumn<Date> createTime = devJobContentSql.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.editor")
    public static final SqlColumn<String> editor = devJobContentSql.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.edit_time")
    public static final SqlColumn<Date> editTime = devJobContentSql.editTime;

    /**
     * Database Column Remarks:
     *   作业ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.job_id")
    public static final SqlColumn<Long> jobId = devJobContentSql.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.editable")
    public static final SqlColumn<Integer> editable = devJobContentSql.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.version")
    public static final SqlColumn<Integer> version = devJobContentSql.version;

    /**
     * Database Column Remarks:
     *   UDF ID列表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.udf_ids")
    public static final SqlColumn<String> udfIds = devJobContentSql.udfIds;

    /**
     * Database Column Remarks:
     *   外部表配置
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.external_tables")
    public static final SqlColumn<String> externalTables = devJobContentSql.externalTables;

    /**
     * Database Column Remarks:
     *   扩展配置
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.extend_configs")
    public static final SqlColumn<String> extendConfigs = devJobContentSql.extendConfigs;

    /**
     * Database Column Remarks:
     *   数据来源SQL
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_sql.source_sql")
    public static final SqlColumn<String> sourceSql = devJobContentSql.sourceSql;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    public static final class DevJobContentSql extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> editable = column("editable", JDBCType.TINYINT);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<String> udfIds = column("udf_ids", JDBCType.VARCHAR);

        public final SqlColumn<String> externalTables = column("external_tables", JDBCType.VARCHAR);

        public final SqlColumn<String> extendConfigs = column("extend_configs", JDBCType.VARCHAR);

        public final SqlColumn<String> sourceSql = column("source_sql", JDBCType.LONGVARCHAR);

        public DevJobContentSql() {
            super("dev_job_content_sql");
        }
    }
}