package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevTableInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_table_info")
    public static final DevTableInfo devTableInfo = new DevTableInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.id")
    public static final SqlColumn<Long> id = devTableInfo.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.del")
    public static final SqlColumn<Integer> del = devTableInfo.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.creator")
    public static final SqlColumn<String> creator = devTableInfo.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.create_time")
    public static final SqlColumn<Date> createTime = devTableInfo.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.editor")
    public static final SqlColumn<String> editor = devTableInfo.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.edit_time")
    public static final SqlColumn<Date> editTime = devTableInfo.editTime;

    /**
     * Database Column Remarks:
     *   表名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.table_name")
    public static final SqlColumn<String> tableName = devTableInfo.tableName;

    /**
     * Database Column Remarks:
     *   同步至hive的表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.hive_table_name")
    public static final SqlColumn<String> hiveTableName = devTableInfo.hiveTableName;

    /**
     * Database Column Remarks:
     *   文件夹ID,null表示最外层文件夹
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_table_info.folder_id")
    public static final SqlColumn<Long> folderId = devTableInfo.folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_table_info")
    public static final class DevTableInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> tableName = column("table_name", JDBCType.VARCHAR);

        public final SqlColumn<String> hiveTableName = column("hive_table_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public DevTableInfo() {
            super("dev_table_info");
        }
    }
}