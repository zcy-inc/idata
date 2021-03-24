package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacUserRoleDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user_role")
    public static final UacUserRole uacUserRole = new UacUserRole();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.id")
    public static final SqlColumn<Long> id = uacUserRole.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.del")
    public static final SqlColumn<Integer> del = uacUserRole.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.creator")
    public static final SqlColumn<String> creator = uacUserRole.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.create_time")
    public static final SqlColumn<Date> createTime = uacUserRole.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.editor")
    public static final SqlColumn<String> editor = uacUserRole.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.edit_time")
    public static final SqlColumn<Date> editTime = uacUserRole.editTime;

    /**
     * Database Column Remarks:
     *   用户ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.user_id")
    public static final SqlColumn<Long> userId = uacUserRole.userId;

    /**
     * Database Column Remarks:
     *   角色编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_role.role_code")
    public static final SqlColumn<String> roleCode = uacUserRole.roleCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user_role")
    public static final class UacUserRole extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<String> roleCode = column("role_code", JDBCType.VARCHAR);

        public UacUserRole() {
            super("uac_user_role");
        }
    }
}