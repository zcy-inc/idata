package cn.zhengcaiyun.idata.user.dal.dao.auth;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class AuthPolicyDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    public static final AuthPolicy AUTH_POLICY = new AuthPolicy();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.id")
    public static final SqlColumn<Long> id = AUTH_POLICY.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.del")
    public static final SqlColumn<Integer> del = AUTH_POLICY.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.creator")
    public static final SqlColumn<String> creator = AUTH_POLICY.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.create_time")
    public static final SqlColumn<Date> createTime = AUTH_POLICY.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.editor")
    public static final SqlColumn<String> editor = AUTH_POLICY.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.edit_time")
    public static final SqlColumn<Date> editTime = AUTH_POLICY.editTime;

    /**
     * Database Column Remarks:
     *   授权记录id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.auth_record_id")
    public static final SqlColumn<Long> authRecordId = AUTH_POLICY.authRecordId;

    /**
     * Database Column Remarks:
     *   授权类型：allow：允许，deny：拒绝
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.effect_type")
    public static final SqlColumn<String> effectType = AUTH_POLICY.effectType;

    /**
     * Database Column Remarks:
     *   授权类型：read：读，write：写
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.action_type")
    public static final SqlColumn<String> actionType = AUTH_POLICY.actionType;

    /**
     * Database Column Remarks:
     *   资源类型：table：表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.resource_type")
    public static final SqlColumn<String> resourceType = AUTH_POLICY.resourceType;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_policy.remark")
    public static final SqlColumn<String> remark = AUTH_POLICY.remark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    public static final class AuthPolicy extends AliasableSqlTable<AuthPolicy> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> authRecordId = column("auth_record_id", JDBCType.BIGINT);

        public final SqlColumn<String> effectType = column("effect_type", JDBCType.VARCHAR);

        public final SqlColumn<String> actionType = column("action_type", JDBCType.VARCHAR);

        public final SqlColumn<String> resourceType = column("resource_type", JDBCType.VARCHAR);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public AuthPolicy() {
            super("uac_auth_policy", AuthPolicy::new);
        }
    }
}