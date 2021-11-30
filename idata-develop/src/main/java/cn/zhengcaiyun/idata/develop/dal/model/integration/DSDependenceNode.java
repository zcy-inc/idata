package cn.zhengcaiyun.idata.develop.dal.model.integration;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table ite_ds_dependence_node
 */
public class DSDependenceNode {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   ds任务code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.task_code")
    private Long taskCode;

    /**
     * Database Column Remarks:
     *   ds工作流code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.workflow_code")
    private Long workflowCode;

    /**
     * Database Column Remarks:
     *   ds依赖节点code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.dependence_node_code")
    private Long dependenceNodeCode;

    /**
     * Database Column Remarks:
     *   上游ds任务code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_task_code")
    private Long prevTaskCode;

    /**
     * Database Column Remarks:
     *   上游ds工作流code
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_workflow_code")
    private Long prevWorkflowCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.task_code")
    public Long getTaskCode() {
        return taskCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.task_code")
    public void setTaskCode(Long taskCode) {
        this.taskCode = taskCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.workflow_code")
    public Long getWorkflowCode() {
        return workflowCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.workflow_code")
    public void setWorkflowCode(Long workflowCode) {
        this.workflowCode = workflowCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.dependence_node_code")
    public Long getDependenceNodeCode() {
        return dependenceNodeCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.dependence_node_code")
    public void setDependenceNodeCode(Long dependenceNodeCode) {
        this.dependenceNodeCode = dependenceNodeCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_task_code")
    public Long getPrevTaskCode() {
        return prevTaskCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_task_code")
    public void setPrevTaskCode(Long prevTaskCode) {
        this.prevTaskCode = prevTaskCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_workflow_code")
    public Long getPrevWorkflowCode() {
        return prevWorkflowCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: ite_ds_dependence_node.prev_workflow_code")
    public void setPrevWorkflowCode(Long prevWorkflowCode) {
        this.prevWorkflowCode = prevWorkflowCode;
    }
}