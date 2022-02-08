package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DSDependenceNodeDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    public static final DSDependenceNode DS_DEPENDENCE_NODE = new DSDependenceNode();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.id")
    public static final SqlColumn<Long> id = DS_DEPENDENCE_NODE.id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.create_time")
    public static final SqlColumn<Date> createTime = DS_DEPENDENCE_NODE.createTime;

    /**
     * Database Column Remarks:
     *   ds任务code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.task_code")
    public static final SqlColumn<Long> taskCode = DS_DEPENDENCE_NODE.taskCode;

    /**
     * Database Column Remarks:
     *   ds工作流code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.workflow_code")
    public static final SqlColumn<Long> workflowCode = DS_DEPENDENCE_NODE.workflowCode;

    /**
     * Database Column Remarks:
     *   ds依赖节点code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.dependence_node_code")
    public static final SqlColumn<Long> dependenceNodeCode = DS_DEPENDENCE_NODE.dependenceNodeCode;

    /**
     * Database Column Remarks:
     *   上游ds任务code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_task_code")
    public static final SqlColumn<Long> prevTaskCode = DS_DEPENDENCE_NODE.prevTaskCode;

    /**
     * Database Column Remarks:
     *   上游ds工作流code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_workflow_code")
    public static final SqlColumn<Long> prevWorkflowCode = DS_DEPENDENCE_NODE.prevWorkflowCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    public static final class DSDependenceNode extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> taskCode = column("task_code", JDBCType.BIGINT);

        public final SqlColumn<Long> workflowCode = column("workflow_code", JDBCType.BIGINT);

        public final SqlColumn<Long> dependenceNodeCode = column("dependence_node_code", JDBCType.BIGINT);

        public final SqlColumn<Long> prevTaskCode = column("prev_task_code", JDBCType.BIGINT);

        public final SqlColumn<Long> prevWorkflowCode = column("prev_workflow_code", JDBCType.BIGINT);

        public DSDependenceNode() {
            super("ite_ds_dependence_node");
        }
    }
}