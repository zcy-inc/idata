package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class AuthEntryDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    public static final AuthEntry authEntry = new AuthEntry();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.id")
    public static final SqlColumn<Long> id = authEntry.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.del")
    public static final SqlColumn<Integer> del = authEntry.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.creator")
    public static final SqlColumn<String> creator = authEntry.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.create_time")
    public static final SqlColumn<Date> createTime = authEntry.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.editor")
    public static final SqlColumn<String> editor = authEntry.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.edit_time")
    public static final SqlColumn<Date> editTime = authEntry.editTime;

    /**
     * Database Column Remarks:
     *   授权主体唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.subject_id")
    public static final SqlColumn<String> subjectId = authEntry.subjectId;

    /**
     * Database Column Remarks:
     *   授权主体类型，users：用户，groups：用户组，apps：应用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.subject_type")
    public static final SqlColumn<String> subjectType = authEntry.subjectType;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_auth_entry.remark")
    public static final SqlColumn<String> remark = authEntry.remark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    public static final class AuthEntry extends AliasableSqlTable<AuthEntry> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> subjectId = column("subject_id", JDBCType.VARCHAR);

        public final SqlColumn<String> subjectType = column("subject_type", JDBCType.VARCHAR);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public AuthEntry() {
            super("uac_auth_entry", AuthEntry::new);
        }
    }
}