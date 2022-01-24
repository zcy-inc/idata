package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import java.util.Map;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SysConfigDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    public static final SysConfig sysConfig = new SysConfig();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.id")
    public static final SqlColumn<Long> id = sysConfig.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.del")
    public static final SqlColumn<Integer> del = sysConfig.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.creator")
    public static final SqlColumn<String> creator = sysConfig.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.create_time")
    public static final SqlColumn<Date> createTime = sysConfig.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.editor")
    public static final SqlColumn<String> editor = sysConfig.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.edit_time")
    public static final SqlColumn<Date> editTime = sysConfig.editTime;

    /**
     * Database Column Remarks:
     *   系统配置键1
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.key_one")
    public static final SqlColumn<String> keyOne = sysConfig.keyOne;

    /**
     * Database Column Remarks:
     *   系统配置值1
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.value_one")
    public static final SqlColumn<Map> valueOne = sysConfig.valueOne;

    /**
     * Database Column Remarks:
     *   配置类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.type")
    public static final SqlColumn<String> type = sysConfig.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    public static final class SysConfig extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> keyOne = column("key_one", JDBCType.VARCHAR);

        public final SqlColumn<Map> valueOne = column("value_one", JDBCType.VARCHAR, "cn.zhengcaiyun.idata.system.dal.JsonColumnHandler");

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public SysConfig() {
            super("sys_config");
        }
    }
}