package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevForeignKeyDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    public static final DevForeignKey devForeignKey = new DevForeignKey();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.id")
    public static final SqlColumn<Long> id = devForeignKey.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.del")
    public static final SqlColumn<Integer> del = devForeignKey.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.creator")
    public static final SqlColumn<String> creator = devForeignKey.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.create_time")
    public static final SqlColumn<Date> createTime = devForeignKey.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.editor")
    public static final SqlColumn<String> editor = devForeignKey.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.edit_time")
    public static final SqlColumn<Date> editTime = devForeignKey.editTime;

    /**
     * Database Column Remarks:
     *   外键所属表ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.table_id")
    public static final SqlColumn<Long> tableId = devForeignKey.tableId;

    /**
     * Database Column Remarks:
     *   外键列名称(支持组合)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.column_names")
    public static final SqlColumn<String> columnNames = devForeignKey.columnNames;

    /**
     * Database Column Remarks:
     *   外键引用表ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.refer_table_id")
    public static final SqlColumn<Long> referTableId = devForeignKey.referTableId;

    /**
     * Database Column Remarks:
     *   外键引用列名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.refer_column_names")
    public static final SqlColumn<String> referColumnNames = devForeignKey.referColumnNames;

    /**
     * Database Column Remarks:
     *   ER联系类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_foreign_key.er_type")
    public static final SqlColumn<String> erType = devForeignKey.erType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    public static final class DevForeignKey extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> tableId = column("table_id", JDBCType.BIGINT);

        public final SqlColumn<String> columnNames = column("column_names", JDBCType.VARCHAR);

        public final SqlColumn<Long> referTableId = column("refer_table_id", JDBCType.BIGINT);

        public final SqlColumn<String> referColumnNames = column("refer_column_names", JDBCType.VARCHAR);

        public final SqlColumn<String> erType = column("er_type", JDBCType.VARCHAR);

        public DevForeignKey() {
            super("dev_foreign_key");
        }
    }
}