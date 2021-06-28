package cn.zhengcaiyun.idata.label.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class LabObjectLabelDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    public static final LabObjectLabel labObjectLabel = new LabObjectLabel();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.id")
    public static final SqlColumn<Long> id = labObjectLabel.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.del")
    public static final SqlColumn<Integer> del = labObjectLabel.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.creator")
    public static final SqlColumn<String> creator = labObjectLabel.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.create_time")
    public static final SqlColumn<Date> createTime = labObjectLabel.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.editor")
    public static final SqlColumn<String> editor = labObjectLabel.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.edit_time")
    public static final SqlColumn<Date> editTime = labObjectLabel.editTime;

    /**
     * Database Column Remarks:
     *   名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.name")
    public static final SqlColumn<String> name = labObjectLabel.name;

    /**
     * Database Column Remarks:
     *   英文名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.name_en")
    public static final SqlColumn<String> nameEn = labObjectLabel.nameEn;

    /**
     * Database Column Remarks:
     *   主体类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.object_type")
    public static final SqlColumn<String> objectType = labObjectLabel.objectType;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.remark")
    public static final SqlColumn<String> remark = labObjectLabel.remark;

    /**
     * Database Column Remarks:
     *   版本
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.version")
    public static final SqlColumn<Integer> version = labObjectLabel.version;

    /**
     * Database Column Remarks:
     *   起源记录id，修改时指向第一次创建的记录id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.origin_id")
    public static final SqlColumn<Long> originId = labObjectLabel.originId;

    /**
     * Database Column Remarks:
     *   文件夹id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.folder_id")
    public static final SqlColumn<Long> folderId = labObjectLabel.folderId;

    /**
     * Database Column Remarks:
     *   标签规则，json格式
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: lab_object_label.rules")
    public static final SqlColumn<String> rules = labObjectLabel.rules;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    public static final class LabObjectLabel extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> nameEn = column("name_en", JDBCType.VARCHAR);

        public final SqlColumn<String> objectType = column("object_type", JDBCType.VARCHAR);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public final SqlColumn<Integer> version = column("version", JDBCType.INTEGER);

        public final SqlColumn<Long> originId = column("origin_id", JDBCType.BIGINT);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public final SqlColumn<String> rules = column("rules", JDBCType.LONGVARCHAR);

        public LabObjectLabel() {
            super("lab_object_label");
        }
    }
}