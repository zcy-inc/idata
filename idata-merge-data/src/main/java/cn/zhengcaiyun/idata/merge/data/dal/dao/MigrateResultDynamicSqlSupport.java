package cn.zhengcaiyun.idata.merge.data.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class MigrateResultDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    public static final MigrateResult migrateResult = new MigrateResult();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.id")
    public static final SqlColumn<Long> id = migrateResult.id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.create_time")
    public static final SqlColumn<Date> createTime = migrateResult.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.edit_time")
    public static final SqlColumn<Date> editTime = migrateResult.editTime;

    /**
     * Database Column Remarks:
     *   迁移操作类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.migrate_type")
    public static final SqlColumn<String> migrateType = migrateResult.migrateType;

    /**
     * Database Column Remarks:
     *   原因
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.reason")
    public static final SqlColumn<String> reason = migrateResult.reason;

    /**
     * Database Column Remarks:
     *   具体数据
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.data")
    public static final SqlColumn<String> data = migrateResult.data;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    public static final class MigrateResult extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> migrateType = column("migrate_type", JDBCType.VARCHAR);

        public final SqlColumn<String> reason = column("reason", JDBCType.VARCHAR);

        public final SqlColumn<String> data = column("data", JDBCType.LONGVARCHAR);

        public MigrateResult() {
            super("oth_migrate_result");
        }
    }
}