package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DSDependRelationDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    public static final DSDependRelation DS_DEPEND_RELATION = new DSDependRelation();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.id")
    public static final SqlColumn<Long> id = DS_DEPEND_RELATION.id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.create_time")
    public static final SqlColumn<Date> createTime = DS_DEPEND_RELATION.createTime;

    /**
     * Database Column Remarks:
     *   ds工作流code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.workflow_code")
    public static final SqlColumn<Long> workflowCode = DS_DEPEND_RELATION.workflowCode;

    /**
     * Database Column Remarks:
     *   ds任务节点code，depend_level是workflow时为0
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.task_code")
    public static final SqlColumn<Long> taskCode = DS_DEPEND_RELATION.taskCode;

    /**
     * Database Column Remarks:
     *   上游ds工作流code，depend_level是task时为0
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.prev_workflow_code")
    public static final SqlColumn<Long> prevWorkflowCode = DS_DEPEND_RELATION.prevWorkflowCode;

    /**
     * Database Column Remarks:
     *   上游ds业务实体code，depend_level是workflow时为0
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.prev_task_code")
    public static final SqlColumn<Long> prevTaskCode = DS_DEPEND_RELATION.prevTaskCode;

    /**
     * Database Column Remarks:
     *   ds依赖节点code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.depend_node_code")
    public static final SqlColumn<Long> dependNodeCode = DS_DEPEND_RELATION.dependNodeCode;

    /**
     * Database Column Remarks:
     *   依赖level: workflow, task
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_depend_relation.depend_level")
    public static final SqlColumn<String> dependLevel = DS_DEPEND_RELATION.dependLevel;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    public static final class DSDependRelation extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> workflowCode = column("workflow_code", JDBCType.BIGINT);

        public final SqlColumn<Long> taskCode = column("task_code", JDBCType.BIGINT);

        public final SqlColumn<Long> prevWorkflowCode = column("prev_workflow_code", JDBCType.BIGINT);

        public final SqlColumn<Long> prevTaskCode = column("prev_task_code", JDBCType.BIGINT);

        public final SqlColumn<Long> dependNodeCode = column("depend_node_code", JDBCType.BIGINT);

        public final SqlColumn<String> dependLevel = column("depend_level", JDBCType.VARCHAR);

        public DSDependRelation() {
            super("ite_ds_depend_relation");
        }
    }
}