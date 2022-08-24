package cn.zhengcaiyun.idata.develop.dal.dao.job.instance;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class StreamJobInstanceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    public static final StreamJobInstance STREAM_JOB_INSTANCE = new StreamJobInstance();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.id")
    public static final SqlColumn<Long> id = STREAM_JOB_INSTANCE.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.del")
    public static final SqlColumn<Integer> del = STREAM_JOB_INSTANCE.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.creator")
    public static final SqlColumn<String> creator = STREAM_JOB_INSTANCE.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.create_time")
    public static final SqlColumn<Date> createTime = STREAM_JOB_INSTANCE.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.editor")
    public static final SqlColumn<String> editor = STREAM_JOB_INSTANCE.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.edit_time")
    public static final SqlColumn<Date> editTime = STREAM_JOB_INSTANCE.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.job_id")
    public static final SqlColumn<Long> jobId = STREAM_JOB_INSTANCE.jobId;

    /**
     * Database Column Remarks:
     *   作业name
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.job_name")
    public static final SqlColumn<String> jobName = STREAM_JOB_INSTANCE.jobName;

    /**
     * Database Column Remarks:
     *   作业内容id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.job_content_id")
    public static final SqlColumn<Long> jobContentId = STREAM_JOB_INSTANCE.jobContentId;

    /**
     * Database Column Remarks:
     *   作业内容版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.job_content_version")
    public static final SqlColumn<Integer> jobContentVersion = STREAM_JOB_INSTANCE.jobContentVersion;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.job_type_code")
    public static final SqlColumn<String> jobTypeCode = STREAM_JOB_INSTANCE.jobTypeCode;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.dw_layer_code")
    public static final SqlColumn<String> dwLayerCode = STREAM_JOB_INSTANCE.dwLayerCode;

    /**
     * Database Column Remarks:
     *   责任人
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.owner")
    public static final SqlColumn<String> owner = STREAM_JOB_INSTANCE.owner;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.environment")
    public static final SqlColumn<String> environment = STREAM_JOB_INSTANCE.environment;

    /**
     * Database Column Remarks:
     *   运行实例状态，0：待启动，1：启动中，2：运行中，6：启动失败，7：运行异常，8：已停止，9：已下线
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.status")
    public static final SqlColumn<Integer> status = STREAM_JOB_INSTANCE.status;

    /**
     * Database Column Remarks:
     *   运行开始时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.run_start_time")
    public static final SqlColumn<Date> runStartTime = STREAM_JOB_INSTANCE.runStartTime;

    /**
     * Database Column Remarks:
     *   外部链接
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.external_url")
    public static final SqlColumn<String> externalUrl = STREAM_JOB_INSTANCE.externalUrl;

    /**
     * Database Column Remarks:
     *   运行参数配置
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_stream_job_instance.run_params")
    public static final SqlColumn<String> runParams = STREAM_JOB_INSTANCE.runParams;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    public static final class StreamJobInstance extends AliasableSqlTable<StreamJobInstance> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> jobName = column("job_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> jobContentId = column("job_content_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> jobContentVersion = column("job_content_version", JDBCType.INTEGER);

        public final SqlColumn<String> jobTypeCode = column("job_type_code", JDBCType.VARCHAR);

        public final SqlColumn<String> dwLayerCode = column("dw_layer_code", JDBCType.VARCHAR);

        public final SqlColumn<String> owner = column("owner", JDBCType.VARCHAR);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<Integer> status = column("status", JDBCType.INTEGER);

        public final SqlColumn<Date> runStartTime = column("run_start_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> externalUrl = column("external_url", JDBCType.VARCHAR);

        public final SqlColumn<String> runParams = column("run_params", JDBCType.VARCHAR);

        public StreamJobInstance() {
            super("dev_stream_job_instance", StreamJobInstance::new);
        }
    }
}