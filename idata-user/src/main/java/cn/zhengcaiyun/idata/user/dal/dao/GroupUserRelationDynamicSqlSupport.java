package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class GroupUserRelationDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_group_user_relation")
    public static final GroupUserRelation groupUserRelation = new GroupUserRelation();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.id")
    public static final SqlColumn<Long> id = groupUserRelation.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.del")
    public static final SqlColumn<Integer> del = groupUserRelation.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.creator")
    public static final SqlColumn<String> creator = groupUserRelation.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.create_time")
    public static final SqlColumn<Date> createTime = groupUserRelation.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.editor")
    public static final SqlColumn<String> editor = groupUserRelation.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.edit_time")
    public static final SqlColumn<Date> editTime = groupUserRelation.editTime;

    /**
     * Database Column Remarks:
     *   组id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.group_id")
    public static final SqlColumn<Long> groupId = groupUserRelation.groupId;

    /**
     * Database Column Remarks:
     *   用户id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_group_user_relation.user_id")
    public static final SqlColumn<Long> userId = groupUserRelation.userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_group_user_relation")
    public static final class GroupUserRelation extends AliasableSqlTable<GroupUserRelation> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> groupId = column("group_id", JDBCType.BIGINT);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public GroupUserRelation() {
            super("uac_group_user_relation", GroupUserRelation::new);
        }
    }
}