package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UacUserTokenDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user_token")
    public static final UacUserToken uacUserToken = new UacUserToken();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.id")
    public static final SqlColumn<Long> id = uacUserToken.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.del")
    public static final SqlColumn<Integer> del = uacUserToken.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.create_time")
    public static final SqlColumn<Date> createTime = uacUserToken.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.edit_time")
    public static final SqlColumn<Date> editTime = uacUserToken.editTime;

    /**
     * Database Column Remarks:
     *   用户ID
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.user_id")
    public static final SqlColumn<Long> userId = uacUserToken.userId;

    /**
     * Database Column Remarks:
     *   用户令牌
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_user_token.token")
    public static final SqlColumn<String> token = uacUserToken.token;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user_token")
    public static final class UacUserToken extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<String> token = column("token", JDBCType.VARCHAR);

        public UacUserToken() {
            super("uac_user_token");
        }
    }
}