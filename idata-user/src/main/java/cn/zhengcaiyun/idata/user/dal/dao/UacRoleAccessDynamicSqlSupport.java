package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacRoleAccessDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_role_access")
    public static final UacRoleAccess uacRoleAccess = new UacRoleAccess();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.id")
    public static final SqlColumn<Long> id = uacRoleAccess.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.del")
    public static final SqlColumn<Integer> del = uacRoleAccess.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.creator")
    public static final SqlColumn<String> creator = uacRoleAccess.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.create_time")
    public static final SqlColumn<Date> createTime = uacRoleAccess.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.editor")
    public static final SqlColumn<String> editor = uacRoleAccess.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.edit_time")
    public static final SqlColumn<Date> editTime = uacRoleAccess.editTime;

    /**
     * Database Column Remarks:
     *   角色编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.role_code")
    public static final SqlColumn<String> roleCode = uacRoleAccess.roleCode;

    /**
     * Database Column Remarks:
     *   权限编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_code")
    public static final SqlColumn<String> accessCode = uacRoleAccess.accessCode;

    /**
     * Database Column Remarks:
     *   权限类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_type")
    public static final SqlColumn<String> accessType = uacRoleAccess.accessType;

    /**
     * Database Column Remarks:
     *   权限对应的ID或编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_key")
    public static final SqlColumn<String> accessKey = uacRoleAccess.accessKey;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_role_access")
    public static final class UacRoleAccess extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> roleCode = column("role_code", JDBCType.VARCHAR);

        public final SqlColumn<String> accessCode = column("access_code", JDBCType.VARCHAR);

        public final SqlColumn<String> accessType = column("access_type", JDBCType.VARCHAR);

        public final SqlColumn<String> accessKey = column("access_key", JDBCType.VARCHAR);

        public UacRoleAccess() {
            super("uac_role_access");
        }
    }
}