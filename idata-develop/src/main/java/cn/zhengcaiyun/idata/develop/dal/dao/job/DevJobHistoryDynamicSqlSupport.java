package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobHistoryDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    public static final DevJobHistory devJobHistory = new DevJobHistory();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.id")
    public static final SqlColumn<Long> id = devJobHistory.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.create_time")
    public static final SqlColumn<Date> createTime = devJobHistory.createTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.job_id")
    public static final SqlColumn<Long> jobId = devJobHistory.jobId;

    /**
     * Database Column Remarks:
     *   作业开始时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.start_time")
    public static final SqlColumn<Date> startTime = devJobHistory.startTime;

    /**
     * Database Column Remarks:
     *   作业结束时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.finish_time")
    public static final SqlColumn<Date> finishTime = devJobHistory.finishTime;

    /**
     * Database Column Remarks:
     *   作业持续时间（ms）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.duration")
    public static final SqlColumn<Long> duration = devJobHistory.duration;

    /**
     * Database Column Remarks:
     *   作业最终状态
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.final_status")
    public static final SqlColumn<String> finalStatus = devJobHistory.finalStatus;

    /**
     * Database Column Remarks:
     *   作业平均消耗cpu虚拟核数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.avg_vcores")
    public static final SqlColumn<Double> avgVcores = devJobHistory.avgVcores;

    /**
     * Database Column Remarks:
     *   作业平均消耗内存（MB）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.avg_memory")
    public static final SqlColumn<Long> avgMemory = devJobHistory.avgMemory;

    /**
     * Database Column Remarks:
     *   yarn的application
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.application_id")
    public static final SqlColumn<String> applicationId = devJobHistory.applicationId;

    /**
     * Database Column Remarks:
     *   启动应用的user
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.user")
    public static final SqlColumn<String> user = devJobHistory.user;

    /**
     * Database Column Remarks:
     *   application master container url地址
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_history.am_container_logs_url")
    public static final SqlColumn<String> amContainerLogsUrl = devJobHistory.amContainerLogsUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    public static final class DevJobHistory extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Date> startTime = column("start_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> finishTime = column("finish_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> duration = column("duration", JDBCType.BIGINT);

        public final SqlColumn<String> finalStatus = column("final_status", JDBCType.VARCHAR);

        public final SqlColumn<Double> avgVcores = column("avg_vcores", JDBCType.DOUBLE);

        public final SqlColumn<Long> avgMemory = column("avg_memory", JDBCType.BIGINT);

        public final SqlColumn<String> applicationId = column("application_id", JDBCType.VARCHAR);

        public final SqlColumn<String> user = column("user", JDBCType.VARCHAR);

        public final SqlColumn<String> amContainerLogsUrl = column("am_container_logs_url", JDBCType.VARCHAR);

        public DevJobHistory() {
            super("dev_job_history");
        }
    }
}