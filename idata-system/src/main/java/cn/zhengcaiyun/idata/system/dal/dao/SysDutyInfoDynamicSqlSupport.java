package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SysDutyInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    public static final SysDutyInfo SYS_DUTY_INFO = new SysDutyInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.id")
    public static final SqlColumn<Long> id = SYS_DUTY_INFO.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.del")
    public static final SqlColumn<Integer> del = SYS_DUTY_INFO.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.creator")
    public static final SqlColumn<String> creator = SYS_DUTY_INFO.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.create_time")
    public static final SqlColumn<Date> createTime = SYS_DUTY_INFO.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.editor")
    public static final SqlColumn<String> editor = SYS_DUTY_INFO.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.edit_time")
    public static final SqlColumn<Date> editTime = SYS_DUTY_INFO.editTime;

    /**
     * Database Column Remarks:
     *   默认值班电话
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.duty_default_phone")
    public static final SqlColumn<String> dutyDefaultPhone = SYS_DUTY_INFO.dutyDefaultPhone;

    /**
     * Database Column Remarks:
     *   值班电话信息
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_duty_info.duty_info")
    public static final SqlColumn<String> dutyInfo = SYS_DUTY_INFO.dutyInfo;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    public static final class SysDutyInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> dutyDefaultPhone = column("duty_default_phone", JDBCType.VARCHAR);

        public final SqlColumn<String> dutyInfo = column("duty_info", JDBCType.LONGVARCHAR);

        public SysDutyInfo() {
            super("sys_duty_info");
        }
    }
}