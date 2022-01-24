package cn.zhengcaiyun.idata.develop.dal.dao.folder;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class CompositeFolderDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_composite_folder")
    public static final CompositeFolder compositeFolder = new CompositeFolder();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.id")
    public static final SqlColumn<Long> id = compositeFolder.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.del")
    public static final SqlColumn<Integer> del = compositeFolder.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.creator")
    public static final SqlColumn<String> creator = compositeFolder.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.create_time")
    public static final SqlColumn<Date> createTime = compositeFolder.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.editor")
    public static final SqlColumn<String> editor = compositeFolder.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.edit_time")
    public static final SqlColumn<Date> editTime = compositeFolder.editTime;

    /**
     * Database Column Remarks:
     *   名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.name")
    public static final SqlColumn<String> name = compositeFolder.name;

    /**
     * Database Column Remarks:
     *   功能型：FUNCTION，普通型：FOLDER
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.type")
    public static final SqlColumn<String> type = compositeFolder.type;

    /**
     * Database Column Remarks:
     *   文件夹所属业务功能：DESIGN, DESIGN.TABLE, DESIGN.LABEL, DESIGN.ENUM, DAG, DI, DEV, DEV.JOB
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.belong")
    public static final SqlColumn<String> belong = compositeFolder.belong;

    /**
     * Database Column Remarks:
     *   父文件夹编号，第一级文件夹父编号为0
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_composite_folder.parent_id")
    public static final SqlColumn<Long> parentId = compositeFolder.parentId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_composite_folder")
    public static final class CompositeFolder extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public final SqlColumn<String> belong = column("belong", JDBCType.VARCHAR);

        public final SqlColumn<Long> parentId = column("parent_id", JDBCType.BIGINT);

        public CompositeFolder() {
            super("dev_composite_folder");
        }
    }
}