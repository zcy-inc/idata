package cn.zhengcaiyun.idata.develop.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DevLabelDefineDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    public static final DevLabelDefine devLabelDefine = new DevLabelDefine();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.id")
    public static final SqlColumn<Long> id = devLabelDefine.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.del")
    public static final SqlColumn<Integer> del = devLabelDefine.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.creator")
    public static final SqlColumn<String> creator = devLabelDefine.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.create_time")
    public static final SqlColumn<Date> createTime = devLabelDefine.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.editor")
    public static final SqlColumn<String> editor = devLabelDefine.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.edit_time")
    public static final SqlColumn<Date> editTime = devLabelDefine.editTime;

    /**
     * Database Column Remarks:
     *   标签唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_code")
    public static final SqlColumn<String> labelCode = devLabelDefine.labelCode;

    /**
     * Database Column Remarks:
     *   标签名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_name")
    public static final SqlColumn<String> labelName = devLabelDefine.labelName;

    /**
     * Database Column Remarks:
     *   标签的标签
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_tag")
    public static final SqlColumn<String> labelTag = devLabelDefine.labelTag;

    /**
     * Database Column Remarks:
     *   标签参数类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_param_type")
    public static final SqlColumn<String> labelParamType = devLabelDefine.labelParamType;

    /**
     * Database Column Remarks:
     *   标签属性
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_attributes")
    public static final SqlColumn<String> labelAttributes = devLabelDefine.labelAttributes;

    /**
     * Database Column Remarks:
     *   特定标签属性，根据标签的标签字段变化
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.special_attributes")
    public static final SqlColumn<String> specialAttributes = devLabelDefine.specialAttributes;

    /**
     * Database Column Remarks:
     *   打标主体类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.subject_type")
    public static final SqlColumn<String> subjectType = devLabelDefine.subjectType;

    /**
     * Database Column Remarks:
     *   标签序号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_index")
    public static final SqlColumn<Integer> labelIndex = devLabelDefine.labelIndex;

    /**
     * Database Column Remarks:
     *   是否必须打标(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_required")
    public static final SqlColumn<Boolean> labelRequired = devLabelDefine.labelRequired;

    /**
     * Database Column Remarks:
     *   标签作用域;null:全局,folder_id:特定文件夹域
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.label_scope")
    public static final SqlColumn<Long> labelScope = devLabelDefine.labelScope;

    /**
     * Database Column Remarks:
     *   文件夹ID,null表示最外层文件夹
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_label_define.folder_id")
    public static final SqlColumn<Long> folderId = devLabelDefine.folderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    public static final class DevLabelDefine extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> labelCode = column("label_code", JDBCType.VARCHAR);

        public final SqlColumn<String> labelName = column("label_name", JDBCType.VARCHAR);

        public final SqlColumn<String> labelTag = column("label_tag", JDBCType.VARCHAR);

        public final SqlColumn<String> labelParamType = column("label_param_type", JDBCType.VARCHAR);

        public final SqlColumn<String> labelAttributes = column("label_attributes", JDBCType.VARCHAR);

        public final SqlColumn<String> specialAttributes = column("special_attributes", JDBCType.VARCHAR);

        public final SqlColumn<String> subjectType = column("subject_type", JDBCType.VARCHAR);

        public final SqlColumn<Integer> labelIndex = column("label_index", JDBCType.INTEGER);

        public final SqlColumn<Boolean> labelRequired = column("label_required", JDBCType.BIT);

        public final SqlColumn<Long> labelScope = column("label_scope", JDBCType.BIGINT);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public DevLabelDefine() {
            super("dev_label_define");
        }
    }
}