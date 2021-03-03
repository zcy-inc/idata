package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SysFeatureDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    public static final SysFeature sysFeature = new SysFeature();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.id")
    public static final SqlColumn<Long> id = sysFeature.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,其他:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.del")
    public static final SqlColumn<Short> del = sysFeature.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.create_time")
    public static final SqlColumn<Date> createTime = sysFeature.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.edit_time")
    public static final SqlColumn<Date> editTime = sysFeature.editTime;

    /**
     * Database Column Remarks:
     *   功能编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.feature_code")
    public static final SqlColumn<String> featureCode = sysFeature.featureCode;

    /**
     * Database Column Remarks:
     *   功能名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.feature_name")
    public static final SqlColumn<String> featureName = sysFeature.featureName;

    /**
     * Database Column Remarks:
     *   功能类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.feature_type")
    public static final SqlColumn<String> featureType = sysFeature.featureType;

    /**
     * Database Column Remarks:
     *   上级功能编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_feature.parent_code")
    public static final SqlColumn<String> parentCode = sysFeature.parentCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    public static final class SysFeature extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Short> del = column("del", JDBCType.SMALLINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> featureCode = column("feature_code", JDBCType.VARCHAR);

        public final SqlColumn<String> featureName = column("feature_name", JDBCType.VARCHAR);

        public final SqlColumn<String> featureType = column("feature_type", JDBCType.VARCHAR);

        public final SqlColumn<String> parentCode = column("parent_code", JDBCType.VARCHAR);

        public SysFeature() {
            super("sys_feature");
        }
    }
}