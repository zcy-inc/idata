package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class AuthResourceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    public static final AuthResource authResource = new AuthResource();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.id")
    public static final SqlColumn<Long> id = authResource.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.del")
    public static final SqlColumn<Integer> del = authResource.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.creator")
    public static final SqlColumn<String> creator = authResource.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.create_time")
    public static final SqlColumn<Date> createTime = authResource.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.editor")
    public static final SqlColumn<String> editor = authResource.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.edit_time")
    public static final SqlColumn<Date> editTime = authResource.editTime;

    /**
     * Database Column Remarks:
     *   授权记录id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.auth_record_id")
    public static final SqlColumn<Long> authRecordId = authResource.authRecordId;

    /**
     * Database Column Remarks:
     *   授权策略记录id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.policy_record_id")
    public static final SqlColumn<Long> policyRecordId = authResource.policyRecordId;

    /**
     * Database Column Remarks:
     *   资源类型：table：表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.resource_type")
    public static final SqlColumn<String> resourceType = authResource.resourceType;

    /**
     * Database Column Remarks:
     *   资源
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_resource.resources")
    public static final SqlColumn<String> resources = authResource.resources;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    public static final class AuthResource extends AliasableSqlTable<AuthResource> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> authRecordId = column("auth_record_id", JDBCType.BIGINT);

        public final SqlColumn<Long> policyRecordId = column("policy_record_id", JDBCType.BIGINT);

        public final SqlColumn<String> resourceType = column("resource_type", JDBCType.VARCHAR);

        public final SqlColumn<String> resources = column("resources", JDBCType.LONGVARCHAR);

        public AuthResource() {
            super("uac_auth_resource", AuthResource::new);
        }
    }
}