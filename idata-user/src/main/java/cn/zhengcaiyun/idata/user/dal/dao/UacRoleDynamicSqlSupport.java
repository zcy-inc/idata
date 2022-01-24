package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacRoleDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_role")
    public static final UacRole uacRole = new UacRole();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.id")
    public static final SqlColumn<Long> id = uacRole.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.del")
    public static final SqlColumn<Integer> del = uacRole.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.creator")
    public static final SqlColumn<String> creator = uacRole.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.create_time")
    public static final SqlColumn<Date> createTime = uacRole.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.editor")
    public static final SqlColumn<String> editor = uacRole.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.edit_time")
    public static final SqlColumn<Date> editTime = uacRole.editTime;

    /**
     * Database Column Remarks:
     *   角色编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.role_code")
    public static final SqlColumn<String> roleCode = uacRole.roleCode;

    /**
     * Database Column Remarks:
     *   角色名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role.role_name")
    public static final SqlColumn<String> roleName = uacRole.roleName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_role")
    public static final class UacRole extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> roleCode = column("role_code", JDBCType.VARCHAR);

        public final SqlColumn<String> roleName = column("role_name", JDBCType.VARCHAR);

        public UacRole() {
            super("uac_role");
        }
    }
}