package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobOutputDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    public static final JobOutput JOB_OUTPUT = new JobOutput();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.id")
    public static final SqlColumn<Long> id = JOB_OUTPUT.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.del")
    public static final SqlColumn<Integer> del = JOB_OUTPUT.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.creator")
    public static final SqlColumn<String> creator = JOB_OUTPUT.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.create_time")
    public static final SqlColumn<Date> createTime = JOB_OUTPUT.createTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.job_id")
    public static final SqlColumn<Long> jobId = JOB_OUTPUT.jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.environment")
    public static final SqlColumn<String> environment = JOB_OUTPUT.environment;

    /**
     * Database Column Remarks:
     *   数据去向-数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_data_source_type")
    public static final SqlColumn<String> destDataSourceType = JOB_OUTPUT.destDataSourceType;

    /**
     * Database Column Remarks:
     *   数据去向-数据源id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_data_source_id")
    public static final SqlColumn<Long> destDataSourceId = JOB_OUTPUT.destDataSourceId;

    /**
     * Database Column Remarks:
     *   数据去向-目标表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_table")
    public static final SqlColumn<String> destTable = JOB_OUTPUT.destTable;

    /**
     * Database Column Remarks:
     *   数据去向-写入模式，overwrite，upsert
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.dest_write_mode")
    public static final SqlColumn<String> destWriteMode = JOB_OUTPUT.destWriteMode;

    /**
     * Database Column Remarks:
     *   数据来源表主键(写入模式为upsert时必填)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_output.job_target_table_pk")
    public static final SqlColumn<String> jobTargetTablePk = JOB_OUTPUT.jobTargetTablePk;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    public static final class JobOutput extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<String> destDataSourceType = column("dest_data_source_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> destDataSourceId = column("dest_data_source_id", JDBCType.BIGINT);

        public final SqlColumn<String> destTable = column("dest_table", JDBCType.VARCHAR);

        public final SqlColumn<String> destWriteMode = column("dest_write_mode", JDBCType.VARCHAR);

        public final SqlColumn<String> jobTargetTablePk = column("job_target_table_pk", JDBCType.VARCHAR);

        public JobOutput() {
            super("dev_job_output");
        }
    }
}