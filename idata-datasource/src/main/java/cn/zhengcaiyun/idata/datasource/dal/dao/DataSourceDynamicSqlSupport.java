package cn.zhengcaiyun.idata.datasource.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DataSourceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source")
    public static final DataSource dataSource = new DataSource();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.id")
    public static final SqlColumn<Long> id = dataSource.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.del")
    public static final SqlColumn<Integer> del = dataSource.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.creator")
    public static final SqlColumn<String> creator = dataSource.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.create_time")
    public static final SqlColumn<Date> createTime = dataSource.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.editor")
    public static final SqlColumn<String> editor = dataSource.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.edit_time")
    public static final SqlColumn<Date> editTime = dataSource.editTime;

    /**
     * Database Column Remarks:
     *   数据源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.type")
    public static final SqlColumn<String> type = dataSource.type;

    /**
     * Database Column Remarks:
     *   数据源名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.name")
    public static final SqlColumn<String> name = dataSource.name;

    /**
     * Database Column Remarks:
     *   环境，多个环境用,号分隔
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.environments")
    public static final SqlColumn<String> environments = dataSource.environments;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.remark")
    public static final SqlColumn<String> remark = dataSource.remark;

    /**
     * Database Column Remarks:
     *   数据库配置，json字符串
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: das_data_source.db_configs")
    public static final SqlColumn<String> dbConfigs = dataSource.dbConfigs;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source")
    public static final class DataSource extends SqlTable {
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

        public final SqlColumn<String> dbConfigs = column("db_configs", JDBCType.VARCHAR);

        public DataSource() {
            super("das_data_source");
        }
    }
}