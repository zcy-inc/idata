package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class UacAppInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_info")
    public static final UacAppInfo uacAppInfo = new UacAppInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.id")
    public static final SqlColumn<Long> id = uacAppInfo.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.del")
    public static final SqlColumn<Integer> del = uacAppInfo.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.creator")
    public static final SqlColumn<String> creator = uacAppInfo.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.create_time")
    public static final SqlColumn<Date> createTime = uacAppInfo.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.editor")
    public static final SqlColumn<String> editor = uacAppInfo.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.edit_time")
    public static final SqlColumn<Date> editTime = uacAppInfo.editTime;

    /**
     * Database Column Remarks:
     *   应用名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.app_name")
    public static final SqlColumn<String> appName = uacAppInfo.appName;

    /**
     * Database Column Remarks:
     *   应用编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.app_key")
    public static final SqlColumn<String> appKey = uacAppInfo.appKey;

    /**
     * Database Column Remarks:
     *   应用秘钥
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.app_secret")
    public static final SqlColumn<String> appSecret = uacAppInfo.appSecret;

    /**
     * Database Column Remarks:
     *   应用描述
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_info.description")
    public static final SqlColumn<String> description = uacAppInfo.description;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_info")
    public static final class UacAppInfo extends AliasableSqlTable<UacAppInfo> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> appName = column("app_name", JDBCType.VARCHAR);

        public final SqlColumn<String> appKey = column("app_key", JDBCType.VARCHAR);

        public final SqlColumn<String> appSecret = column("app_secret", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public UacAppInfo() {
            super("uac_app_info", UacAppInfo::new);
        }
    }
}