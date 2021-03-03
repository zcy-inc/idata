package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevFolderDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_folder")
    public static final DevFolder devFolder = new DevFolder();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.id")
    public static final SqlColumn<Long> id = devFolder.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,其他:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.del")
    public static final SqlColumn<Short> del = devFolder.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.creator")
    public static final SqlColumn<String> creator = devFolder.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.create_time")
    public static final SqlColumn<Date> createTime = devFolder.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.editor")
    public static final SqlColumn<String> editor = devFolder.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.edit_time")
    public static final SqlColumn<Date> editTime = devFolder.editTime;

    /**
     * Database Column Remarks:
     *   文件夹类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.folder_type")
    public static final SqlColumn<String> folderType = devFolder.folderType;

    /**
     * Database Column Remarks:
     *   文件夹名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.folder_name")
    public static final SqlColumn<String> folderName = devFolder.folderName;

    /**
     * Database Column Remarks:
     *   父文件夹ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_folder.parent_id")
    public static final SqlColumn<Long> parentId = devFolder.parentId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_folder")
    public static final class DevFolder extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Short> del = column("del", JDBCType.SMALLINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> folderType = column("folder_type", JDBCType.VARCHAR);

        public final SqlColumn<String> folderName = column("folder_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> parentId = column("parent_id", JDBCType.BIGINT);

        public DevFolder() {
            super("dev_folder");
        }
    }
}