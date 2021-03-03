package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacAccessDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    public static final UacAccess uacAccess = new UacAccess();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.id")
    public static final SqlColumn<Long> id = uacAccess.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,其他:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.del")
    public static final SqlColumn<Short> del = uacAccess.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.create_time")
    public static final SqlColumn<Date> createTime = uacAccess.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.edit_time")
    public static final SqlColumn<Date> editTime = uacAccess.editTime;

    /**
     * Database Column Remarks:
     *   权限编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.access_code")
    public static final SqlColumn<String> accessCode = uacAccess.accessCode;

    /**
     * Database Column Remarks:
     *   权限类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.access_type")
    public static final SqlColumn<String> accessType = uacAccess.accessType;

    /**
     * Database Column Remarks:
     *   权限对应的ID或编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_access.access_key")
    public static final SqlColumn<String> accessKey = uacAccess.accessKey;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    public static final class UacAccess extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Short> del = column("del", JDBCType.SMALLINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> accessCode = column("access_code", JDBCType.VARCHAR);

        public final SqlColumn<String> accessType = column("access_type", JDBCType.VARCHAR);

        public final SqlColumn<String> accessKey = column("access_key", JDBCType.VARCHAR);

        public UacAccess() {
            super("uac_access");
        }
    }
}