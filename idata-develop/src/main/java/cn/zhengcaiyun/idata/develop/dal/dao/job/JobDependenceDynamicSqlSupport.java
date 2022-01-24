package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobDependenceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    public static final JobDependence JOB_DEPENDENCE = new JobDependence();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.id")
    public static final SqlColumn<Long> id = JOB_DEPENDENCE.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.del")
    public static final SqlColumn<Integer> del = JOB_DEPENDENCE.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.creator")
    public static final SqlColumn<String> creator = JOB_DEPENDENCE.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.create_time")
    public static final SqlColumn<Date> createTime = JOB_DEPENDENCE.createTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.job_id")
    public static final SqlColumn<Long> jobId = JOB_DEPENDENCE.jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.environment")
    public static final SqlColumn<String> environment = JOB_DEPENDENCE.environment;

    /**
     * Database Column Remarks:
     *   上游作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.prev_job_id")
    public static final SqlColumn<Long> prevJobId = JOB_DEPENDENCE.prevJobId;

    /**
     * Database Column Remarks:
     *   上游作业所属dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_dependence.prev_job_dag_id")
    public static final SqlColumn<Long> prevJobDagId = JOB_DEPENDENCE.prevJobDagId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    public static final class JobDependence extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<Long> prevJobId = column("prev_job_id", JDBCType.BIGINT);

        public final SqlColumn<Long> prevJobDagId = column("prev_job_dag_id", JDBCType.BIGINT);

        public JobDependence() {
            super("dev_job_dependence");
        }
    }
}