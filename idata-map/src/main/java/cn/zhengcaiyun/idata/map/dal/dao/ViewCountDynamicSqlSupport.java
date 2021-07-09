package cn.zhengcaiyun.idata.map.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ViewCountDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    public static final ViewCount VIEW_COUNT = new ViewCount();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.id")
    public static final SqlColumn<Long> id = VIEW_COUNT.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.del")
    public static final SqlColumn<Integer> del = VIEW_COUNT.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.creator")
    public static final SqlColumn<String> creator = VIEW_COUNT.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.create_time")
    public static final SqlColumn<Date> createTime = VIEW_COUNT.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.editor")
    public static final SqlColumn<String> editor = VIEW_COUNT.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.edit_time")
    public static final SqlColumn<Date> editTime = VIEW_COUNT.editTime;

    /**
     * Database Column Remarks:
     *   实体记录唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_code")
    public static final SqlColumn<String> entityCode = VIEW_COUNT.entityCode;

    /**
     * Database Column Remarks:
     *   实体记录数据源：数仓表（table） or 数据指标（indicator）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_source")
    public static final SqlColumn<String> entitySource = VIEW_COUNT.entitySource;

    /**
     * Database Column Remarks:
     *   用户编号，0表示全局所有用户的统计数据
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.user_id")
    public static final SqlColumn<Long> userId = VIEW_COUNT.userId;

    /**
     * Database Column Remarks:
     *   浏览次数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.view_count")
    public static final SqlColumn<Long> viewCount = VIEW_COUNT.viewCount;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    public static final class ViewCount extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> entityCode = column("entity_code", JDBCType.VARCHAR);

        public final SqlColumn<String> entitySource = column("entity_source", JDBCType.VARCHAR);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Long> viewCount = column("view_count", JDBCType.BIGINT);

        public ViewCount() {
            super("map_view_count");
        }
    }
}