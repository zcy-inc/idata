package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DSEntityMappingDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    public static final DSEntityMapping DS_ENTITY_MAPPING = new DSEntityMapping();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.id")
    public static final SqlColumn<Long> id = DS_ENTITY_MAPPING.id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.create_time")
    public static final SqlColumn<Date> createTime = DS_ENTITY_MAPPING.createTime;

    /**
     * Database Column Remarks:
     *   业务实体id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.entity_id")
    public static final SqlColumn<Long> entityId = DS_ENTITY_MAPPING.entityId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.environment")
    public static final SqlColumn<String> environment = DS_ENTITY_MAPPING.environment;

    /**
     * Database Column Remarks:
     *   业务实体type: workflow, task
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_type")
    public static final SqlColumn<String> dsEntityType = DS_ENTITY_MAPPING.dsEntityType;

    /**
     * Database Column Remarks:
     *   ds 业务实体code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_code")
    public static final SqlColumn<Long> dsEntityCode = DS_ENTITY_MAPPING.dsEntityCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    public static final class DSEntityMapping extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> entityId = column("entity_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<String> dsEntityType = column("ds_entity_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> dsEntityCode = column("ds_entity_code", JDBCType.BIGINT);

        public DSEntityMapping() {
            super("ite_ds_entity_mapping");
        }
    }
}