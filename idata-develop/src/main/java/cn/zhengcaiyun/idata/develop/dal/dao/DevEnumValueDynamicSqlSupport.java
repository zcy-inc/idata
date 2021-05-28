package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevEnumValueDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    public static final DevEnumValue devEnumValue = new DevEnumValue();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.id")
    public static final SqlColumn<Long> id = devEnumValue.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.del")
    public static final SqlColumn<Integer> del = devEnumValue.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.creator")
    public static final SqlColumn<String> creator = devEnumValue.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.create_time")
    public static final SqlColumn<Date> createTime = devEnumValue.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.editor")
    public static final SqlColumn<String> editor = devEnumValue.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.edit_time")
    public static final SqlColumn<Date> editTime = devEnumValue.editTime;

    /**
     * Database Column Remarks:
     *   枚举标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.enum_code")
    public static final SqlColumn<String> enumCode = devEnumValue.enumCode;

    /**
     * Database Column Remarks:
     *   枚举值标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.value_code")
    public static final SqlColumn<String> valueCode = devEnumValue.valueCode;

    /**
     * Database Column Remarks:
     *   枚举值字面值
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.enum_value")
    public static final SqlColumn<String> enumValue = devEnumValue.enumValue;

    /**
     * Database Column Remarks:
     *   枚举值属性
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.enum_attributes")
    public static final SqlColumn<String> enumAttributes = devEnumValue.enumAttributes;

    /**
     * Database Column Remarks:
     *   上级枚举值标识,null表示最上级
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum_value.parent_code")
    public static final SqlColumn<String> parentCode = devEnumValue.parentCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    public static final class DevEnumValue extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> enumCode = column("enum_code", JDBCType.VARCHAR);

        public final SqlColumn<String> valueCode = column("value_code", JDBCType.VARCHAR);

        public final SqlColumn<String> enumValue = column("enum_value", JDBCType.VARCHAR);

        public final SqlColumn<String> enumAttributes = column("enum_attributes", JDBCType.VARCHAR);

        public final SqlColumn<String> parentCode = column("parent_code", JDBCType.VARCHAR);

        public DevEnumValue() {
            super("dev_enum_value");
        }
    }
}