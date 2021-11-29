package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevJobUdfDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    public static final DevJobUdf devJobUdf = new DevJobUdf();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.id")
    public static final SqlColumn<Long> id = devJobUdf.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.del")
    public static final SqlColumn<Integer> del = devJobUdf.del;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.creator")
    public static final SqlColumn<String> creator = devJobUdf.creator;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.editor")
    public static final SqlColumn<String> editor = devJobUdf.editor;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.create_time")
    public static final SqlColumn<Date> createTime = devJobUdf.createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.edit_time")
    public static final SqlColumn<Date> editTime = devJobUdf.editTime;

    /**
     * Database Column Remarks:
     *   '函数名称'
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.udf_name")
    public static final SqlColumn<String> udfName = devJobUdf.udfName;

    /**
     * Database Column Remarks:
     *   函数类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.udf_type")
    public static final SqlColumn<String> udfType = devJobUdf.udfType;

    /**
     * Database Column Remarks:
     *   文件名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.file_name")
    public static final SqlColumn<String> fileName = devJobUdf.fileName;

    /**
     * Database Column Remarks:
     *   hdfs文件路径
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.hdfs_path")
    public static final SqlColumn<String> hdfsPath = devJobUdf.hdfsPath;

    /**
     * Database Column Remarks:
     *   返回类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.return_type")
    public static final SqlColumn<String> returnType = devJobUdf.returnType;

    /**
     * Database Column Remarks:
     *   返回值
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.return_sample")
    public static final SqlColumn<String> returnSample = devJobUdf.returnSample;

    /**
     * Database Column Remarks:
     *   目标文件夹
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.folder_id")
    public static final SqlColumn<Long> folderId = devJobUdf.folderId;

    /**
     * Database Column Remarks:
     *   描述
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.description")
    public static final SqlColumn<String> description = devJobUdf.description;

    /**
     * Database Column Remarks:
     *   命令格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.command_format")
    public static final SqlColumn<String> commandFormat = devJobUdf.commandFormat;

    /**
     * Database Column Remarks:
     *   示例
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_udf.udf_sample")
    public static final SqlColumn<String> udfSample = devJobUdf.udfSample;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    public static final class DevJobUdf extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.BIT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> udfName = column("udf_name", JDBCType.VARCHAR);

        public final SqlColumn<String> udfType = column("udf_type", JDBCType.VARCHAR);

        public final SqlColumn<String> fileName = column("file_name", JDBCType.VARCHAR);

        public final SqlColumn<String> hdfsPath = column("hdfs_path", JDBCType.VARCHAR);

        public final SqlColumn<String> returnType = column("return_type", JDBCType.VARCHAR);

        public final SqlColumn<String> returnSample = column("return_sample", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public final SqlColumn<String> commandFormat = column("command_format", JDBCType.VARCHAR);

        public final SqlColumn<String> udfSample = column("udf_sample", JDBCType.VARCHAR);

        public DevJobUdf() {
            super("dev_job_udf");
        }
    }
}