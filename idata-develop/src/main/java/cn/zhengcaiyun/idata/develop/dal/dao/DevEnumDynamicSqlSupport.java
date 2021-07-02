package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevEnumDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum")
    public static final DevEnum devEnum = new DevEnum();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.id")
    public static final SqlColumn<Long> id = devEnum.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.del")
    public static final SqlColumn<Integer> del = devEnum.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.creator")
    public static final SqlColumn<String> creator = devEnum.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.create_time")
    public static final SqlColumn<Date> createTime = devEnum.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.editor")
    public static final SqlColumn<String> editor = devEnum.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.edit_time")
    public static final SqlColumn<Date> editTime = devEnum.editTime;

    /**
     * Database Column Remarks:
     *   枚举标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.enum_code")
    public static final SqlColumn<String> enumCode = devEnum.enumCode;

    /**
     * Database Column Remarks:
     *   枚举名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.enum_name")
    public static final SqlColumn<String> enumName = devEnum.enumName;

    /**
     * Database Column Remarks:
     *   文件夹ID,null表示最外层文件夹
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_enum.folder_id")
    public static final SqlColumn<Long> folderId = devEnum.folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum")
    public static final class DevEnum extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> enumCode = column("enum_code", JDBCType.VARCHAR);

        public final SqlColumn<String> enumName = column("enum_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public DevEnum() {
            super("dev_enum");
        }
    }
}