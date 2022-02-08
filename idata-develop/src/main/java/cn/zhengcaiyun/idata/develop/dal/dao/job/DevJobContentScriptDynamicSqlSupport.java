package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobContentScriptDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_script")
    public static final DevJobContentScript devJobContentScript = new DevJobContentScript();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.id")
    public static final SqlColumn<Long> id = devJobContentScript.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.del")
    public static final SqlColumn<Integer> del = devJobContentScript.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.creator")
    public static final SqlColumn<String> creator = devJobContentScript.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.create_time")
    public static final SqlColumn<Date> createTime = devJobContentScript.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.editor")
    public static final SqlColumn<String> editor = devJobContentScript.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.edit_time")
    public static final SqlColumn<Date> editTime = devJobContentScript.editTime;

    /**
     * Database Column Remarks:
     *   作业ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.job_id")
    public static final SqlColumn<Long> jobId = devJobContentScript.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.editable")
    public static final SqlColumn<Integer> editable = devJobContentScript.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.version")
    public static final SqlColumn<Integer> version = devJobContentScript.version;

    /**
     * Database Column Remarks:
     *   执行参数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.script_arguments")
    public static final SqlColumn<List> scriptArguments = devJobContentScript.scriptArguments;

    /**
     * Database Column Remarks:
     *   脚本资源内容
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_script.source_resource")
    public static final SqlColumn<String> sourceResource = devJobContentScript.sourceResource;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_script")
    public static final class DevJobContentScript extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> editable = column("editable", JDBCType.TINYINT);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<List> scriptArguments = column("script_arguments", JDBCType.VARCHAR, "cn.zhengcaiyun.idata.develop.dal.JsonColumnHandler");

        public final SqlColumn<String> sourceResource = column("source_resource", JDBCType.LONGVARCHAR);

        public DevJobContentScript() {
            super("dev_job_content_script");
        }
    }
}