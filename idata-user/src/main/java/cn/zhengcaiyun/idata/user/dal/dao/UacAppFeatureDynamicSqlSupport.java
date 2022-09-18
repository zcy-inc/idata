package cn.zhengcaiyun.idata.user.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class UacAppFeatureDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    public static final UacAppFeature uacAppFeature = new UacAppFeature();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.id")
    public static final SqlColumn<Long> id = uacAppFeature.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.del")
    public static final SqlColumn<Integer> del = uacAppFeature.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.creator")
    public static final SqlColumn<String> creator = uacAppFeature.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.create_time")
    public static final SqlColumn<Date> createTime = uacAppFeature.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.editor")
    public static final SqlColumn<String> editor = uacAppFeature.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.edit_time")
    public static final SqlColumn<Date> editTime = uacAppFeature.editTime;

    /**
     * Database Column Remarks:
     *   应用编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.app_key")
    public static final SqlColumn<String> appKey = uacAppFeature.appKey;

    /**
     * Database Column Remarks:
     *   功能编码(英文逗号分隔)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_app_feature.feature_codes")
    public static final SqlColumn<String> featureCodes = uacAppFeature.featureCodes;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    public static final class UacAppFeature extends AliasableSqlTable<UacAppFeature> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> appKey = column("app_key", JDBCType.VARCHAR);

        public final SqlColumn<String> featureCodes = column("feature_codes", JDBCType.VARCHAR);

        public UacAppFeature() {
            super("uac_app_feature", UacAppFeature::new);
        }
    }
}