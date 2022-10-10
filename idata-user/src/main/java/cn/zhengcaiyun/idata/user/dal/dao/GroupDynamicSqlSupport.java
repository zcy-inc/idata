package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class GroupDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_group")
    public static final Group group = new Group();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.id")
    public static final SqlColumn<Long> id = group.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.del")
    public static final SqlColumn<Integer> del = group.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.creator")
    public static final SqlColumn<String> creator = group.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.create_time")
    public static final SqlColumn<Date> createTime = group.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.editor")
    public static final SqlColumn<String> editor = group.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.edit_time")
    public static final SqlColumn<Date> editTime = group.editTime;

    /**
     * Database Column Remarks:
     *   组名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.name")
    public static final SqlColumn<String> name = group.name;

    /**
     * Database Column Remarks:
     *   组负责人
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.owner")
    public static final SqlColumn<String> owner = group.owner;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group.remark")
    public static final SqlColumn<String> remark = group.remark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_group")
    public static final class Group extends AliasableSqlTable<Group> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> owner = column("owner", JDBCType.VARCHAR);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public Group() {
            super("uac_group", Group::new);
        }
    }
}