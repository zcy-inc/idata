package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DAGScheduleDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    public static final DAGSchedule DAGSchedule = new DAGSchedule();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.id")
    public static final SqlColumn<Long> id = DAGSchedule.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.del")
    public static final SqlColumn<Integer> del = DAGSchedule.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.creator")
    public static final SqlColumn<String> creator = DAGSchedule.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.create_time")
    public static final SqlColumn<Date> createTime = DAGSchedule.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.editor")
    public static final SqlColumn<String> editor = DAGSchedule.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.edit_time")
    public static final SqlColumn<Date> editTime = DAGSchedule.editTime;

    /**
     * Database Column Remarks:
     *   dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.dag_id")
    public static final SqlColumn<Long> dagId = DAGSchedule.dagId;

    /**
     * Database Column Remarks:
     *   开始时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.begin_time")
    public static final SqlColumn<Date> beginTime = DAGSchedule.beginTime;

    /**
     * Database Column Remarks:
     *   结束时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.end_time")
    public static final SqlColumn<Date> endTime = DAGSchedule.endTime;

    /**
     * Database Column Remarks:
     *   周期范围，year, month, week, day, hour, minute
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.period_range")
    public static final SqlColumn<String> periodRange = DAGSchedule.periodRange;

    /**
     * Database Column Remarks:
     *   触发方式，interval: 时间间隔，point: 指定时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.trigger_mode")
    public static final SqlColumn<String> triggerMode = DAGSchedule.triggerMode;

    /**
     * Database Column Remarks:
     *   cron表达式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_schedule.cron_expression")
    public static final SqlColumn<String> cronExpression = DAGSchedule.cronExpression;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    public static final class DAGSchedule extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> dagId = column("dag_id", JDBCType.BIGINT);

        public final SqlColumn<Date> beginTime = column("begin_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> endTime = column("end_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> periodRange = column("period_range", JDBCType.VARCHAR);

        public final SqlColumn<String> triggerMode = column("trigger_mode", JDBCType.VARCHAR);

        public final SqlColumn<String> cronExpression = column("cron_expression", JDBCType.VARCHAR);

        public DAGSchedule() {
            super("dev_dag_schedule");
        }
    }
}