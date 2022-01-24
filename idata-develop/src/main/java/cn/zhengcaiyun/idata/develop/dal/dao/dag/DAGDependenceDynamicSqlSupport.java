package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DAGDependenceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    public static final DAGDependence DAG_DEPENDENCE = new DAGDependence();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_dependence.id")
    public static final SqlColumn<Long> id = DAG_DEPENDENCE.id;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_dependence.creator")
    public static final SqlColumn<String> creator = DAG_DEPENDENCE.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_dependence.create_time")
    public static final SqlColumn<Date> createTime = DAG_DEPENDENCE.createTime;

    /**
     * Database Column Remarks:
     *   dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_dependence.dag_id")
    public static final SqlColumn<Long> dagId = DAG_DEPENDENCE.dagId;

    /**
     * Database Column Remarks:
     *   依赖的前置 dag id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_dag_dependence.prev_dag_id")
    public static final SqlColumn<Long> prevDagId = DAG_DEPENDENCE.prevDagId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    public static final class DAGDependence extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> dagId = column("dag_id", JDBCType.BIGINT);

        public final SqlColumn<Long> prevDagId = column("prev_dag_id", JDBCType.BIGINT);

        public DAGDependence() {
            super("dev_dag_dependence");
        }
    }
}