package cn.zhengcaiyun.idata.mergedata.dal.model;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table oth_migrate_result
 */
public class MigrateResult {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   迁移操作类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.migrate_type")
    private String migrateType;

    /**
     * Database Column Remarks:
     *   原因
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.reason")
    private String reason;

    /**
     * Database Column Remarks:
     *   具体数据
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.data")
    private String data;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.migrate_type")
    public String getMigrateType() {
        return migrateType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.migrate_type")
    public void setMigrateType(String migrateType) {
        this.migrateType = migrateType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.reason")
    public String getReason() {
        return reason;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.data")
    public String getData() {
        return data;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: oth_migrate_result.data")
    public void setData(String data) {
        this.data = data;
    }
}