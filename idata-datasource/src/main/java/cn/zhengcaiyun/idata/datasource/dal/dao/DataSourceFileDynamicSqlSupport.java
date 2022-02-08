package cn.zhengcaiyun.idata.datasource.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DataSourceFileDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    public static final DataSourceFile dataSourceFile = new DataSourceFile();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.id")
    public static final SqlColumn<Long> id = dataSourceFile.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.del")
    public static final SqlColumn<Integer> del = dataSourceFile.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.creator")
    public static final SqlColumn<String> creator = dataSourceFile.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.create_time")
    public static final SqlColumn<Date> createTime = dataSourceFile.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.editor")
    public static final SqlColumn<String> editor = dataSourceFile.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.edit_time")
    public static final SqlColumn<Date> editTime = dataSourceFile.editTime;

    /**
     * Database Column Remarks:
     *   数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.type")
    public static final SqlColumn<String> type = dataSourceFile.type;

    /**
     * Database Column Remarks:
     *   数据源名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.name")
    public static final SqlColumn<String> name = dataSourceFile.name;

    /**
     * Database Column Remarks:
     *   环境，多个环境用,号分隔
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.environments")
    public static final SqlColumn<String> environments = dataSourceFile.environments;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.remark")
    public static final SqlColumn<String> remark = dataSourceFile.remark;

    /**
     * Database Column Remarks:
     *   文件名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source_file.file_name")
    public static final SqlColumn<String> fileName = dataSourceFile.fileName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    public static final class DataSourceFile extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> environments = column("environments", JDBCType.VARCHAR);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public final SqlColumn<String> fileName = column("file_name", JDBCType.VARCHAR);

        public DataSourceFile() {
            super("das_data_source_file");
        }
    }
}