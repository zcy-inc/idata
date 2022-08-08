package cn.zhengcaiyun.idata.system.dal.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SysResourceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    public static final SysResource sysResource = new SysResource();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.id")
    public static final SqlColumn<Long> id = sysResource.id;

    /**
     * Database Column Remarks:
     *   是否删除(1:是,0:否)
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.del")
    public static final SqlColumn<Integer> del = sysResource.del;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.create_time")
    public static final SqlColumn<Date> createTime = sysResource.createTime;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.edit_time")
    public static final SqlColumn<Date> editTime = sysResource.editTime;

    /**
     * Database Column Remarks:
     *   资源编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.resource_code")
    public static final SqlColumn<String> resourceCode = sysResource.resourceCode;

    /**
     * Database Column Remarks:
     *   资源名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.resource_name")
    public static final SqlColumn<String> resourceName = sysResource.resourceName;

    /**
     * Database Column Remarks:
     *   资源类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.resource_type")
    public static final SqlColumn<String> resourceType = sysResource.resourceType;

    /**
     * Database Column Remarks:
     *   上级资源编码
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.parent_code")
    public static final SqlColumn<String> parentCode = sysResource.parentCode;

    /**
     * Database Column Remarks:
     *   资源请求路径
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: sys_resource.resource_url_path")
    public static final SqlColumn<String> resourceUrlPath = sysResource.resourceUrlPath;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    public static final class SysResource extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> resourceCode = column("resource_code", JDBCType.VARCHAR);

        public final SqlColumn<String> resourceName = column("resource_name", JDBCType.VARCHAR);

        public final SqlColumn<String> resourceType = column("resource_type", JDBCType.VARCHAR);

        public final SqlColumn<String> parentCode = column("parent_code", JDBCType.VARCHAR);

        public final SqlColumn<String> resourceUrlPath = column("resource_url_path", JDBCType.VARCHAR);

        public SysResource() {
            super("sys_resource");
        }
    }
}