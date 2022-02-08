package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobExecuteConfigDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    public static final JobExecuteConfig jobExecuteConfig = new JobExecuteConfig();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.id")
    public static final SqlColumn<Long> id = jobExecuteConfig.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.del")
    public static final SqlColumn<Integer> del = jobExecuteConfig.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.creator")
    public static final SqlColumn<String> creator = jobExecuteConfig.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.create_time")
    public static final SqlColumn<Date> createTime = jobExecuteConfig.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.editor")
    public static final SqlColumn<String> editor = jobExecuteConfig.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.edit_time")
    public static final SqlColumn<Date> editTime = jobExecuteConfig.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.job_id")
    public static final SqlColumn<Long> jobId = jobExecuteConfig.jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.environment")
    public static final SqlColumn<String> environment = jobExecuteConfig.environment;

    /**
     * Database Column Remarks:
     *   调度配置-dag编号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dag_id")
    public static final SqlColumn<Long> schDagId = jobExecuteConfig.schDagId;

    /**
     * Database Column Remarks:
     *   调度配置-重跑配置，always：皆可重跑，failed：失败后可重跑，never：皆不可重跑
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_rerun_mode")
    public static final SqlColumn<String> schRerunMode = jobExecuteConfig.schRerunMode;

    /**
     * Database Column Remarks:
     *   调度配置-超时时间，单位：秒
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out")
    public static final SqlColumn<Integer> schTimeOut = jobExecuteConfig.schTimeOut;

    /**
     * Database Column Remarks:
     *   调度配置-是否空跑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dry_run")
    public static final SqlColumn<Integer> schDryRun = jobExecuteConfig.schDryRun;

    /**
     * Database Column Remarks:
     *   运行配置-队列
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_queue")
    public static final SqlColumn<String> execQueue = jobExecuteConfig.execQueue;

    /**
     * Database Column Remarks:
     *   运行配置-告警等级
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_warn_level")
    public static final SqlColumn<String> execWarnLevel = jobExecuteConfig.execWarnLevel;

    /**
     * Database Column Remarks:
     *   调度配置-超时策略，alarm：超时告警，fail：超时失败，都有时用,号分隔
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out_strategy")
    public static final SqlColumn<String> schTimeOutStrategy = jobExecuteConfig.schTimeOutStrategy;

    /**
     * Database Column Remarks:
     *   调度配置-优先级，1：低，2：中，3：高
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_priority")
    public static final SqlColumn<Integer> schPriority = jobExecuteConfig.schPriority;

    /**
     * Database Column Remarks:
     *   运行配置-驱动器内存
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_driver_mem")
    public static final SqlColumn<Integer> execDriverMem = jobExecuteConfig.execDriverMem;

    /**
     * Database Column Remarks:
     *   运行配置-执行器内存
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_worker_mem")
    public static final SqlColumn<Integer> execWorkerMem = jobExecuteConfig.execWorkerMem;

    /**
     * Database Column Remarks:
     *   作业运行状态（环境级），0：暂停运行；1：恢复运行
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.running_state")
    public static final SqlColumn<Integer> runningState = jobExecuteConfig.runningState;

    /**
     * Database Column Remarks:
     *   执行引擎
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_engine")
    public static final SqlColumn<String> execEngine = jobExecuteConfig.execEngine;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    public static final class JobExecuteConfig extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<Long> schDagId = column("sch_dag_id", JDBCType.BIGINT);

        public final SqlColumn<String> schRerunMode = column("sch_rerun_mode", JDBCType.VARCHAR);

        public final SqlColumn<Integer> schTimeOut = column("sch_time_out", JDBCType.INTEGER);

        public final SqlColumn<Integer> schDryRun = column("sch_dry_run", JDBCType.TINYINT);

        public final SqlColumn<String> execQueue = column("exec_queue", JDBCType.VARCHAR);

        public final SqlColumn<String> execWarnLevel = column("exec_warn_level", JDBCType.VARCHAR);

        public final SqlColumn<String> schTimeOutStrategy = column("sch_time_out_strategy", JDBCType.VARCHAR);

        public final SqlColumn<Integer> schPriority = column("sch_priority", JDBCType.INTEGER);

        public final SqlColumn<Integer> execDriverMem = column("exec_driver_mem", JDBCType.INTEGER);

        public final SqlColumn<Integer> execWorkerMem = column("exec_worker_mem", JDBCType.INTEGER);

        public final SqlColumn<Integer> runningState = column("running_state", JDBCType.INTEGER);

        public final SqlColumn<String> execEngine = column("exec_engine", JDBCType.VARCHAR);

        public JobExecuteConfig() {
            super("dev_job_execute_config");
        }
    }
}