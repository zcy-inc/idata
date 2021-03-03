package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacUserDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    public static final UacUser uacUser = new UacUser();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.id")
    public static final SqlColumn<Long> id = uacUser.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,其他:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.del")
    public static final SqlColumn<Short> del = uacUser.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.creator")
    public static final SqlColumn<String> creator = uacUser.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.create_time")
    public static final SqlColumn<Date> createTime = uacUser.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.editor")
    public static final SqlColumn<String> editor = uacUser.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.edit_time")
    public static final SqlColumn<Date> editTime = uacUser.editTime;

    /**
     * Database Column Remarks:
     *   用户名
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.username")
    public static final SqlColumn<String> username = uacUser.username;

    /**
     * Database Column Remarks:
     *   是否系统管理员(0:否,1、2:是)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.sys_admin")
    public static final SqlColumn<Short> sysAdmin = uacUser.sysAdmin;

    /**
     * Database Column Remarks:
     *   认证方式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.auth_type")
    public static final SqlColumn<String> authType = uacUser.authType;

    /**
     * Database Column Remarks:
     *   注册密码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.password")
    public static final SqlColumn<String> password = uacUser.password;

    /**
     * Database Column Remarks:
     *   昵称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.nickname")
    public static final SqlColumn<String> nickname = uacUser.nickname;

    /**
     * Database Column Remarks:
     *   工号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.employee_id")
    public static final SqlColumn<String> employeeId = uacUser.employeeId;

    /**
     * Database Column Remarks:
     *   部门
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.department")
    public static final SqlColumn<String> department = uacUser.department;

    /**
     * Database Column Remarks:
     *   真实姓名
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.real_name")
    public static final SqlColumn<String> realName = uacUser.realName;

    /**
     * Database Column Remarks:
     *   头像信息
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.avatar")
    public static final SqlColumn<String> avatar = uacUser.avatar;

    /**
     * Database Column Remarks:
     *   电子邮箱
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.email")
    public static final SqlColumn<String> email = uacUser.email;

    /**
     * Database Column Remarks:
     *   手机号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user.mobile")
    public static final SqlColumn<String> mobile = uacUser.mobile;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    public static final class UacUser extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Short> del = column("del", JDBCType.SMALLINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> username = column("username", JDBCType.VARCHAR);

        public final SqlColumn<Short> sysAdmin = column("sys_admin", JDBCType.SMALLINT);

        public final SqlColumn<String> authType = column("auth_type", JDBCType.VARCHAR);

        public final SqlColumn<String> password = column("password", JDBCType.VARCHAR);

        public final SqlColumn<String> nickname = column("nickname", JDBCType.VARCHAR);

        public final SqlColumn<String> employeeId = column("employee_id", JDBCType.VARCHAR);

        public final SqlColumn<String> department = column("department", JDBCType.VARCHAR);

        public final SqlColumn<String> realName = column("real_name", JDBCType.VARCHAR);

        public final SqlColumn<String> avatar = column("avatar", JDBCType.VARCHAR);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);

        public final SqlColumn<String> mobile = column("mobile", JDBCType.VARCHAR);

        public UacUser() {
            super("uac_user");
        }
    }
}