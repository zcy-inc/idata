package cn.zhengcaiyun.idata.develop.dal.model;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_label_define
 */
public class DevLabelDefine {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   标签唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_code")
    private String labelCode;

    /**
     * Database Column Remarks:
     *   标签名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_name")
    private String labelName;

    /**
     * Database Column Remarks:
     *   标签的标签
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_tag")
    private String labelTag;

    /**
     * Database Column Remarks:
     *   标签参数类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_param_type")
    private String labelParamType;

    /**
     * Database Column Remarks:
     *   标签属性
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_attributes")
    private String labelAttributes;

    /**
     * Database Column Remarks:
     *   特定标签属性，根据标签的标签字段变化
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.special_attributes")
    private String specialAttributes;

    /**
     * Database Column Remarks:
     *   打标主体类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.subject_type")
    private String subjectType;

    /**
     * Database Column Remarks:
     *   标签序号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_index")
    private Integer labelIndex;

    /**
     * Database Column Remarks:
     *   是否必须打标(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_required")
    private Boolean labelRequired;

    /**
     * Database Column Remarks:
     *   标签作用域;null:全局,folder_id:特定文件夹域
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_scope")
    private Long labelScope;

    /**
     * Database Column Remarks:
     *   文件夹ID,null表示最外层文件夹
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.folder_id")
    private Long folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_code")
    public String getLabelCode() {
        return labelCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_code")
    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_name")
    public String getLabelName() {
        return labelName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_name")
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_tag")
    public String getLabelTag() {
        return labelTag;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_tag")
    public void setLabelTag(String labelTag) {
        this.labelTag = labelTag;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_param_type")
    public String getLabelParamType() {
        return labelParamType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_param_type")
    public void setLabelParamType(String labelParamType) {
        this.labelParamType = labelParamType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_attributes")
    public String getLabelAttributes() {
        return labelAttributes;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_attributes")
    public void setLabelAttributes(String labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.special_attributes")
    public String getSpecialAttributes() {
        return specialAttributes;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.special_attributes")
    public void setSpecialAttributes(String specialAttributes) {
        this.specialAttributes = specialAttributes;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.subject_type")
    public String getSubjectType() {
        return subjectType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.subject_type")
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_index")
    public Integer getLabelIndex() {
        return labelIndex;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_index")
    public void setLabelIndex(Integer labelIndex) {
        this.labelIndex = labelIndex;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_required")
    public Boolean getLabelRequired() {
        return labelRequired;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_required")
    public void setLabelRequired(Boolean labelRequired) {
        this.labelRequired = labelRequired;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_scope")
    public Long getLabelScope() {
        return labelScope;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_scope")
    public void setLabelScope(Long labelScope) {
        this.labelScope = labelScope;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.folder_id")
    public Long getFolderId() {
        return folderId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.folder_id")
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}