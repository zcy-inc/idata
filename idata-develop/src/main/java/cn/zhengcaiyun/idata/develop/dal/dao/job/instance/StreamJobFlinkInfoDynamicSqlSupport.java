package cn.zhengcaiyun.idata.develop.dal.dao.job.instance;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class StreamJobFlinkInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    public static final StreamJobFlinkInfo STREAM_JOB_FLINK_INFO = new StreamJobFlinkInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.id")
    public static final SqlColumn<Long> id = STREAM_JOB_FLINK_INFO.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.del")
    public static final SqlColumn<Integer> del = STREAM_JOB_FLINK_INFO.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.create_time")
    public static final SqlColumn<Date> createTime = STREAM_JOB_FLINK_INFO.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.edit_time")
    public static final SqlColumn<Date> editTime = STREAM_JOB_FLINK_INFO.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.job_id")
    public static final SqlColumn<Long> jobId = STREAM_JOB_FLINK_INFO.jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.environment")
    public static final SqlColumn<String> environment = STREAM_JOB_FLINK_INFO.environment;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.secondary_id")
    public static final SqlColumn<String> secondaryId = STREAM_JOB_FLINK_INFO.secondaryId;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_flink_info.flink_job_id")
    public static final SqlColumn<String> flinkJobId = STREAM_JOB_FLINK_INFO.flinkJobId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    public static final class StreamJobFlinkInfo extends AliasableSqlTable<StreamJobFlinkInfo> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<String> secondaryId = column("secondary_id", JDBCType.VARCHAR);

        public final SqlColumn<String> flinkJobId = column("flink_job_id", JDBCType.VARCHAR);

        public StreamJobFlinkInfo() {
            super("dev_stream_job_flink_info", StreamJobFlinkInfo::new);
        }
    }
}