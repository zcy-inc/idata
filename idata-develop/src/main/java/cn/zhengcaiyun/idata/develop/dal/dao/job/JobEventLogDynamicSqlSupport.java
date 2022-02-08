package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobEventLogDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_event_log")
    public static final JobEventLog JOB_EVENT_LOG = new JobEventLog();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.id")
    public static final SqlColumn<Long> id = JOB_EVENT_LOG.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.del")
    public static final SqlColumn<Integer> del = JOB_EVENT_LOG.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.creator")
    public static final SqlColumn<String> creator = JOB_EVENT_LOG.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.create_time")
    public static final SqlColumn<Date> createTime = JOB_EVENT_LOG.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.editor")
    public static final SqlColumn<String> editor = JOB_EVENT_LOG.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.edit_time")
    public static final SqlColumn<Date> editTime = JOB_EVENT_LOG.editTime;

    /**
     * Database Column Remarks:
     *   dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_id")
    public static final SqlColumn<Long> jobId = JOB_EVENT_LOG.jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.environment")
    public static final SqlColumn<String> environment = JOB_EVENT_LOG.environment;

    /**
     * Database Column Remarks:
     *   事件，created, updated, deleted ...
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.job_event")
    public static final SqlColumn<String> jobEvent = JOB_EVENT_LOG.jobEvent;

    /**
     * Database Column Remarks:
     *   事件信息，用于事件重放处理时使用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.event_info")
    public static final SqlColumn<String> eventInfo = JOB_EVENT_LOG.eventInfo;

    /**
     * Database Column Remarks:
     *   事件处理状态，0: 待处理，1: 处理成功，9：处理失败
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_status")
    public static final SqlColumn<Integer> handleStatus = JOB_EVENT_LOG.handleStatus;

    /**
     * Database Column Remarks:
     *   处理结果信息
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_event_log.handle_msg")
    public static final SqlColumn<String> handleMsg = JOB_EVENT_LOG.handleMsg;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_event_log")
    public static final class JobEventLog extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<String> jobEvent = column("job_event", JDBCType.VARCHAR);

        public final SqlColumn<String> eventInfo = column("event_info", JDBCType.VARCHAR);

        public final SqlColumn<Integer> handleStatus = column("handle_status", JDBCType.INTEGER);

        public final SqlColumn<String> handleMsg = column("handle_msg", JDBCType.VARCHAR);

        public JobEventLog() {
            super("dev_job_event_log");
        }
    }
}