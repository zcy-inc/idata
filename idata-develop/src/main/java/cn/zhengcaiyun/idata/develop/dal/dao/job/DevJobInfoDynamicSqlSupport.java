package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    public static final DevJobInfo devJobInfo = new DevJobInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.id")
    public static final SqlColumn<Long> id = devJobInfo.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.del")
    public static final SqlColumn<Integer> del = devJobInfo.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.creator")
    public static final SqlColumn<String> creator = devJobInfo.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.create_time")
    public static final SqlColumn<Date> createTime = devJobInfo.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.editor")
    public static final SqlColumn<String> editor = devJobInfo.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.edit_time")
    public static final SqlColumn<Date> editTime = devJobInfo.editTime;

    /**
     * Database Column Remarks:
     *   作业名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.name")
    public static final SqlColumn<String> name = devJobInfo.name;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_type")
    public static final SqlColumn<String> jobType = devJobInfo.jobType;

    /**
     * Database Column Remarks:
     *   作业使用语言
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_language")
    public static final SqlColumn<String> jobLanguage = devJobInfo.jobLanguage;

    /**
     * Database Column Remarks:
     *   作业执行引擎
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.job_execution_engine")
    public static final SqlColumn<String> jobExecutionEngine = devJobInfo.jobExecutionEngine;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.dw_layer_code")
    public static final SqlColumn<String> dwLayerCode = devJobInfo.dwLayerCode;

    /**
     * Database Column Remarks:
     *   状态，1启用，0停用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.status")
    public static final SqlColumn<Integer> status = devJobInfo.status;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.remark")
    public static final SqlColumn<String> remark = devJobInfo.remark;

    /**
     * Database Column Remarks:
     *   文件夹id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_info.folder_id")
    public static final SqlColumn<Long> folderId = devJobInfo.folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    public static final class DevJobInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> jobType = column("job_type", JDBCType.VARCHAR);

        public final SqlColumn<String> jobLanguage = column("job_language", JDBCType.VARCHAR);

        public final SqlColumn<String> jobExecutionEngine = column("job_execution_engine", JDBCType.VARCHAR);

        public final SqlColumn<String> dwLayerCode = column("dw_layer_code", JDBCType.VARCHAR);

        public final SqlColumn<Integer> status = column("status", JDBCType.INTEGER);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public DevJobInfo() {
            super("dev_job_info");
        }
    }
}