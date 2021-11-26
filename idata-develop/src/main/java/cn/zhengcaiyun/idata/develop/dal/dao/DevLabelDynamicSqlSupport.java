package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevLabelDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    public static final DevLabel devLabel = new DevLabel();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.id")
    public static final SqlColumn<Long> id = devLabel.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.del")
    public static final SqlColumn<Integer> del = devLabel.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.creator")
    public static final SqlColumn<String> creator = devLabel.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.create_time")
    public static final SqlColumn<Date> createTime = devLabel.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.editor")
    public static final SqlColumn<String> editor = devLabel.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.edit_time")
    public static final SqlColumn<Date> editTime = devLabel.editTime;

    /**
     * Database Column Remarks:
     *   标签唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.label_code")
    public static final SqlColumn<String> labelCode = devLabel.labelCode;

    /**
     * Database Column Remarks:
     *   打标主体表ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.table_id")
    public static final SqlColumn<Long> tableId = devLabel.tableId;

    /**
     * Database Column Remarks:
     *   打标主体字段名
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.column_name")
    public static final SqlColumn<String> columnName = devLabel.columnName;

    /**
     * Database Column Remarks:
     *   标签参数值
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.label_param_value")
    public static final SqlColumn<String> labelParamValue = devLabel.labelParamValue;

    /**
     * Database Column Remarks:
     *   是否隐藏不展示
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label.hidden")
    public static final SqlColumn<Integer> hidden = devLabel.hidden;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    public static final class DevLabel extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> labelCode = column("label_code", JDBCType.VARCHAR);

        public final SqlColumn<Long> tableId = column("table_id", JDBCType.BIGINT);

        public final SqlColumn<String> columnName = column("column_name", JDBCType.VARCHAR);

        public final SqlColumn<String> labelParamValue = column("label_param_value", JDBCType.VARCHAR);

        public final SqlColumn<Integer> hidden = column("hidden", JDBCType.TINYINT);

        public DevLabel() {
            super("dev_label");
        }
    }
}