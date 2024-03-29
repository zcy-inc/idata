package cn.zhengcaiyun.idata.develop.dal.model.integration;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table ite_ds_entity_mapping
 */
public class DSEntityMapping {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   业务实体id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.entity_id")
    private Long entityId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.environment")
    private String environment;

    /**
     * Database Column Remarks:
     *   业务实体type: workflow, task
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_type")
    private String dsEntityType;

    /**
     * Database Column Remarks:
     *   ds 业务实体code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_code")
    private Long dsEntityCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.entity_id")
    public Long getEntityId() {
        return entityId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.entity_id")
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.environment")
    public String getEnvironment() {
        return environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.environment")
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_type")
    public String getDsEntityType() {
        return dsEntityType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_type")
    public void setDsEntityType(String dsEntityType) {
        this.dsEntityType = dsEntityType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_code")
    public Long getDsEntityCode() {
        return dsEntityCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_entity_mapping.ds_entity_code")
    public void setDsEntityCode(Long dsEntityCode) {
        this.dsEntityCode = dsEntityCode;
    }
}