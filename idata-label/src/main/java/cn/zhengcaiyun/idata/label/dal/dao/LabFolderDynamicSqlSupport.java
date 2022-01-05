package cn.zhengcaiyun.idata.label.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class LabFolderDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    public static final LabFolder labFolder = new LabFolder();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.id")
    public static final SqlColumn<Long> id = labFolder.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.del")
    public static final SqlColumn<Integer> del = labFolder.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.creator")
    public static final SqlColumn<String> creator = labFolder.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.create_time")
    public static final SqlColumn<Date> createTime = labFolder.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.editor")
    public static final SqlColumn<String> editor = labFolder.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.edit_time")
    public static final SqlColumn<Date> editTime = labFolder.editTime;

    /**
     * Database Column Remarks:
     *   名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.name")
    public static final SqlColumn<String> name = labFolder.name;

    /**
     * Database Column Remarks:
     *   父文件夹编号，第一级文件夹父编号为0
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.parent_id")
    public static final SqlColumn<Long> parentId = labFolder.parentId;

    /**
     * Database Column Remarks:
     *   所属业务标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_folder.belong")
    public static final SqlColumn<String> belong = labFolder.belong;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    public static final class LabFolder extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<Long> parentId = column("parent_id", JDBCType.BIGINT);

        public final SqlColumn<String> belong = column("belong", JDBCType.VARCHAR);

        public LabFolder() {
            super("lab_folder");
        }
    }
}