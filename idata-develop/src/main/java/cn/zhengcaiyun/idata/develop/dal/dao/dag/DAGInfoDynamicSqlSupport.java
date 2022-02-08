package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DAGInfoDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_info")
    public static final DAGInfo dag_info = new DAGInfo();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.id")
    public static final SqlColumn<Long> id = dag_info.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.del")
    public static final SqlColumn<Integer> del = dag_info.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.creator")
    public static final SqlColumn<String> creator = dag_info.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.create_time")
    public static final SqlColumn<Date> createTime = dag_info.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.editor")
    public static final SqlColumn<String> editor = dag_info.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.edit_time")
    public static final SqlColumn<Date> editTime = dag_info.editTime;

    /**
     * Database Column Remarks:
     *   名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.name")
    public static final SqlColumn<String> name = dag_info.name;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.dw_layer_code")
    public static final SqlColumn<String> dwLayerCode = dag_info.dwLayerCode;

    /**
     * Database Column Remarks:
     *   状态，1启用，0停用
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.status")
    public static final SqlColumn<Integer> status = dag_info.status;

    /**
     * Database Column Remarks:
     *   备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.remark")
    public static final SqlColumn<String> remark = dag_info.remark;

    /**
     * Database Column Remarks:
     *   文件夹id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.folder_id")
    public static final SqlColumn<Long> folderId = dag_info.folderId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_info.environment")
    public static final SqlColumn<String> environment = dag_info.environment;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_info")
    public static final class DAGInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> dwLayerCode = column("dw_layer_code", JDBCType.VARCHAR);

        public final SqlColumn<Integer> status = column("status", JDBCType.INTEGER);

        public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

        public final SqlColumn<Long> folderId = column("folder_id", JDBCType.BIGINT);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public DAGInfo() {
            super("dev_dag_info");
        }
    }
}