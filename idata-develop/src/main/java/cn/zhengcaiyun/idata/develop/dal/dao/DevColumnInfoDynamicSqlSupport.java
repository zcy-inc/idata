package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevColumnInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_column_info")
    public static final DevColumnInfo devColumnInfo = new DevColumnInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.id")
    public static final SqlColumn<Long> id = devColumnInfo.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.del")
    public static final SqlColumn<Integer> del = devColumnInfo.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.creator")
    public static final SqlColumn<String> creator = devColumnInfo.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.create_time")
    public static final SqlColumn<Date> createTime = devColumnInfo.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.editor")
    public static final SqlColumn<String> editor = devColumnInfo.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.edit_time")
    public static final SqlColumn<Date> editTime = devColumnInfo.editTime;

    /**
     * Database Column Remarks:
     *   字段名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.column_name")
    public static final SqlColumn<String> columnName = devColumnInfo.columnName;

    /**
     * Database Column Remarks:
     *   所属表ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.table_id")
    public static final SqlColumn<Long> tableId = devColumnInfo.tableId;

    /**
     * Database Column Remarks:
     *   字段顺序(从0开始)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_column_info.column_index")
    public static final SqlColumn<Integer> columnIndex = devColumnInfo.columnIndex;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_column_info")
    public static final class DevColumnInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> columnName = column("column_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> tableId = column("table_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> columnIndex = column("column_index", JDBCType.INTEGER);

        public DevColumnInfo() {
            super("dev_column_info");
        }
    }
}