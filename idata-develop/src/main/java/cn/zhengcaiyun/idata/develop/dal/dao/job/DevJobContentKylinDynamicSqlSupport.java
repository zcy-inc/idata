package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobContentKylinDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    public static final DevJobContentKylin devJobContentKylin = new DevJobContentKylin();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.id")
    public static final SqlColumn<Long> id = devJobContentKylin.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.del")
    public static final SqlColumn<Integer> del = devJobContentKylin.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.creator")
    public static final SqlColumn<String> creator = devJobContentKylin.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.create_time")
    public static final SqlColumn<Date> createTime = devJobContentKylin.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.editor")
    public static final SqlColumn<String> editor = devJobContentKylin.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.edit_time")
    public static final SqlColumn<Date> editTime = devJobContentKylin.editTime;

    /**
     * Database Column Remarks:
     *   作业ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.job_id")
    public static final SqlColumn<Long> jobId = devJobContentKylin.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.editable")
    public static final SqlColumn<Integer> editable = devJobContentKylin.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.version")
    public static final SqlColumn<Integer> version = devJobContentKylin.version;

    /**
     * Database Column Remarks:
     *   CUBE名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.cube_name")
    public static final SqlColumn<String> cubeName = devJobContentKylin.cubeName;

    /**
     * Database Column Remarks:
     *   CUBE构建类型(BUILD/MERGE/REFRESH)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.build_type")
    public static final SqlColumn<String> buildType = devJobContentKylin.buildType;

    /**
     * Database Column Remarks:
     *   数据开始时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.start_time")
    public static final SqlColumn<Date> startTime = devJobContentKylin.startTime;

    /**
     * Database Column Remarks:
     *   数据结束时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_kylin.end_time")
    public static final SqlColumn<Date> endTime = devJobContentKylin.endTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    public static final class DevJobContentKylin extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> editable = column("editable", JDBCType.TINYINT);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<String> cubeName = column("cube_name", JDBCType.VARCHAR);

        public final SqlColumn<String> buildType = column("build_type", JDBCType.VARCHAR);

        public final SqlColumn<Date> startTime = column("start_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> endTime = column("end_time", JDBCType.TIMESTAMP);

        public DevJobContentKylin() {
            super("dev_job_content_kylin");
        }
    }
}