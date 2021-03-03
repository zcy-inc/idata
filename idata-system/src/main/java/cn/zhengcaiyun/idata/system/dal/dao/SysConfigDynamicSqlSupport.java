package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
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
     *   是否删除(1:是,其他:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.del")
    public static final SqlColumn<Short> del = sysConfig.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_config.create_time")
    public static final SqlColumn<Date> createTime = sysConfig.createTime;

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
    public static final SqlColumn<String> valueOne = sysConfig.valueOne;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    public static final class SysConfig extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Short> del = column("del", JDBCType.SMALLINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> keyOne = column("key_one", JDBCType.VARCHAR);

        public final SqlColumn<String> valueOne = column("value_one", JDBCType.VARCHAR);

        public SysConfig() {
            super("sys_config");
        }
    }
}