package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    public static final JobInfo jobInfo = new JobInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.id")
    public static final SqlColumn<Long> id = jobInfo.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.del")
    public static final SqlColumn<Integer> del = jobInfo.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.creator")
    public static final SqlColumn<String> creator = jobInfo.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.create_time")
    public static final SqlColumn<Date> createTime = jobInfo.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.editor")
    public static final SqlColumn<String> editor = jobInfo.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.edit_time")
    public static final SqlColumn<Date> editTime = jobInfo.editTime;

    /**
     * Database Column Remarks:
     *   作业名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.name")
    public static final SqlColumn<String> name = jobInfo.name;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_type")
    public static final SqlColumn<String> jobType = jobInfo.jobType;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.dw_layer_code")
    public static final SqlColumn<String> dwLayerCode = jobInfo.dwLayerCode;

    /**
     * Database Column Remarks:
     *   状态，1启用，0停用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.status")
    public static final SqlColumn<Integer> status = jobInfo.status;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.remark")
    public static final SqlColumn<String> remark = jobInfo.remark;

    /**
     * Database Column Remarks:
     *   文件夹id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.folder_id")
    public static final SqlColumn<Long> folderId = jobInfo.folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    public static final class JobInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> jobType = column("job_type", JDBCType.VARCHAR);

        public final SqlColumn<String> dwLayerCode = column("dw_layer_code", JDBCType.VARCHAR);

        public final SqlColumn<Integer> status = column("status", JDBCType.INTEGER);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public JobInfo() {
            super("dev_job_info");
        }
    }
}