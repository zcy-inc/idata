package cn.zhengcaiyun.idata.map.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class MapUserFavouriteDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_user_favourite")
    public static final MapUserFavourite mapUserFavourite = new MapUserFavourite();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.id")
    public static final SqlColumn<Long> id = mapUserFavourite.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.del")
    public static final SqlColumn<Integer> del = mapUserFavourite.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.creator")
    public static final SqlColumn<String> creator = mapUserFavourite.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.create_time")
    public static final SqlColumn<Date> createTime = mapUserFavourite.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.editor")
    public static final SqlColumn<String> editor = mapUserFavourite.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.edit_time")
    public static final SqlColumn<Date> editTime = mapUserFavourite.editTime;

    /**
     * Database Column Remarks:
     *   实体记录唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.entity_code")
    public static final SqlColumn<String> entityCode = mapUserFavourite.entityCode;

    /**
     * Database Column Remarks:
     *   实体记录数据源：数仓表（table） or 数据指标（indicator）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.entity_source")
    public static final SqlColumn<String> entitySource = mapUserFavourite.entitySource;

    /**
     * Database Column Remarks:
     *   用户编号，0表示全局所有用户的统计数据
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_user_favourite.user_id")
    public static final SqlColumn<Long> userId = mapUserFavourite.userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_user_favourite")
    public static final class MapUserFavourite extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> entityCode = column("entity_code", JDBCType.VARCHAR);

        public final SqlColumn<String> entitySource = column("entity_source", JDBCType.VARCHAR);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public MapUserFavourite() {
            super("map_user_favourite");
        }
    }
}