package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class WorkspaceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    public static final Workspace workspace = new Workspace();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.id")
    public static final SqlColumn<Long> id = workspace.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.del")
    public static final SqlColumn<Integer> del = workspace.del;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.creator")
    public static final SqlColumn<String> creator = workspace.creator;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.editor")
    public static final SqlColumn<String> editor = workspace.editor;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.create_time")
    public static final SqlColumn<Date> createTime = workspace.createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.edit_time")
    public static final SqlColumn<Date> editTime = workspace.editTime;

    /**
     * Database Column Remarks:
     *   工作空间名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.name")
    public static final SqlColumn<String> name = workspace.name;

    /**
     * Database Column Remarks:
     *   工作空间code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.code")
    public static final SqlColumn<String> code = workspace.code;

    /**
     * Database Column Remarks:
     *   工作空间url path前缀
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: workspace.url_path")
    public static final SqlColumn<String> urlPath = workspace.urlPath;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    public static final class Workspace extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.BIT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> code = column("code", JDBCType.VARCHAR);

        public final SqlColumn<String> urlPath = column("url_path", JDBCType.VARCHAR);

        public Workspace() {
            super("workspace");
        }
    }
}