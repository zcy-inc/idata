package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobContentSparkDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    public static final DevJobContentSpark devJobContentSpark = new DevJobContentSpark();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.id")
    public static final SqlColumn<Long> id = devJobContentSpark.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.del")
    public static final SqlColumn<Integer> del = devJobContentSpark.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.creator")
    public static final SqlColumn<String> creator = devJobContentSpark.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.create_time")
    public static final SqlColumn<Date> createTime = devJobContentSpark.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.editor")
    public static final SqlColumn<String> editor = devJobContentSpark.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.edit_time")
    public static final SqlColumn<Date> editTime = devJobContentSpark.editTime;

    /**
     * Database Column Remarks:
     *   作业ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.job_id")
    public static final SqlColumn<Long> jobId = devJobContentSpark.jobId;

    /**
     * Database Column Remarks:
     *   是否可编辑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.editable")
    public static final SqlColumn<Integer> editable = devJobContentSpark.editable;

    /**
     * Database Column Remarks:
     *   作业版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.version")
    public static final SqlColumn<Integer> version = devJobContentSpark.version;

    /**
     * Database Column Remarks:
     *   执行文件HDFS地址
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.resource_hdfs_path")
    public static final SqlColumn<String> resourceHdfsPath = devJobContentSpark.resourceHdfsPath;

    /**
     * Database Column Remarks:
     *   执行参数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.app_arguments")
    public static final SqlColumn<List> appArguments = devJobContentSpark.appArguments;

    /**
     * Database Column Remarks:
     *   执行类(JAR类型)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_spark.main_class")
    public static final SqlColumn<String> mainClass = devJobContentSpark.mainClass;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    public static final class DevJobContentSpark extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> editable = column("editable", JDBCType.TINYINT);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<String> resourceHdfsPath = column("resource_hdfs_path", JDBCType.VARCHAR);

        public final SqlColumn<List> appArguments = column("app_arguments", JDBCType.VARCHAR, "cn.zhengcaiyun.idata.develop.dal.JsonColumnHandler");

        public final SqlColumn<String> mainClass = column("main_class", JDBCType.VARCHAR);

        public DevJobContentSpark() {
            super("dev_job_content_spark");
        }
    }
}