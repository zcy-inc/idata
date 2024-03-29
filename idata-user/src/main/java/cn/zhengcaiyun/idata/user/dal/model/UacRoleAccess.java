package cn.zhengcaiyun.idata.user.dal.model;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table uac_role_access
 */
public class UacRoleAccess {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   角色编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.role_code")
    private String roleCode;

    /**
     * Database Column Remarks:
     *   权限编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_code")
    private String accessCode;

    /**
     * Database Column Remarks:
     *   权限类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_type")
    private String accessType;

    /**
     * Database Column Remarks:
     *   权限对应的ID或编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_key")
    private String accessKey;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.role_code")
    public String getRoleCode() {
        return roleCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.role_code")
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_code")
    public String getAccessCode() {
        return accessCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_code")
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_type")
    public String getAccessType() {
        return accessType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_type")
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_key")
    public String getAccessKey() {
        return accessKey;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: uac_role_access.access_key")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}