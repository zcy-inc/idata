package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import static cn.zhengcaiyun.idata.develop.dal.dao.integration.DSDependenceNodeDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependenceNode;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface DSDependenceNodeDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    BasicColumn[] selectList = BasicColumn.columnList(id, createTime, taskCode, workflowCode, dependenceNodeCode, prevTaskCode, prevWorkflowCode);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DSDependenceNode> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DSDependenceNodeResult")
    Optional<DSDependenceNode> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DSDependenceNodeResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="task_code", property="taskCode", jdbcType=JdbcType.BIGINT),
        @Result(column="workflow_code", property="workflowCode", jdbcType=JdbcType.BIGINT),
        @Result(column="dependence_node_code", property="dependenceNodeCode", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_task_code", property="prevTaskCode", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_workflow_code", property="prevWorkflowCode", jdbcType=JdbcType.BIGINT)
    })
    List<DSDependenceNode> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int insert(DSDependenceNode record) {
        return MyBatis3Utils.insert(this::insert, record, DS_DEPENDENCE_NODE, c ->
            c.map(createTime).toProperty("createTime")
            .map(taskCode).toProperty("taskCode")
            .map(workflowCode).toProperty("workflowCode")
            .map(dependenceNodeCode).toProperty("dependenceNodeCode")
            .map(prevTaskCode).toProperty("prevTaskCode")
            .map(prevWorkflowCode).toProperty("prevWorkflowCode")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int insertSelective(DSDependenceNode record) {
        return MyBatis3Utils.insert(this::insert, record, DS_DEPENDENCE_NODE, c ->
            c.map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(taskCode).toPropertyWhenPresent("taskCode", record::getTaskCode)
            .map(workflowCode).toPropertyWhenPresent("workflowCode", record::getWorkflowCode)
            .map(dependenceNodeCode).toPropertyWhenPresent("dependenceNodeCode", record::getDependenceNodeCode)
            .map(prevTaskCode).toPropertyWhenPresent("prevTaskCode", record::getPrevTaskCode)
            .map(prevWorkflowCode).toPropertyWhenPresent("prevWorkflowCode", record::getPrevWorkflowCode)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default Optional<DSDependenceNode> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default List<DSDependenceNode> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default List<DSDependenceNode> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default Optional<DSDependenceNode> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DS_DEPENDENCE_NODE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    static UpdateDSL<UpdateModel> updateAllColumns(DSDependenceNode record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalTo(record::getCreateTime)
                .set(taskCode).equalTo(record::getTaskCode)
                .set(workflowCode).equalTo(record::getWorkflowCode)
                .set(dependenceNodeCode).equalTo(record::getDependenceNodeCode)
                .set(prevTaskCode).equalTo(record::getPrevTaskCode)
                .set(prevWorkflowCode).equalTo(record::getPrevWorkflowCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DSDependenceNode record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(taskCode).equalToWhenPresent(record::getTaskCode)
                .set(workflowCode).equalToWhenPresent(record::getWorkflowCode)
                .set(dependenceNodeCode).equalToWhenPresent(record::getDependenceNodeCode)
                .set(prevTaskCode).equalToWhenPresent(record::getPrevTaskCode)
                .set(prevWorkflowCode).equalToWhenPresent(record::getPrevWorkflowCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int updateByPrimaryKey(DSDependenceNode record) {
        return update(c ->
            c.set(createTime).equalTo(record::getCreateTime)
            .set(taskCode).equalTo(record::getTaskCode)
            .set(workflowCode).equalTo(record::getWorkflowCode)
            .set(dependenceNodeCode).equalTo(record::getDependenceNodeCode)
            .set(prevTaskCode).equalTo(record::getPrevTaskCode)
            .set(prevWorkflowCode).equalTo(record::getPrevWorkflowCode)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_dependence_node")
    default int updateByPrimaryKeySelective(DSDependenceNode record) {
        return update(c ->
            c.set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(taskCode).equalToWhenPresent(record::getTaskCode)
            .set(workflowCode).equalToWhenPresent(record::getWorkflowCode)
            .set(dependenceNodeCode).equalToWhenPresent(record::getDependenceNodeCode)
            .set(prevTaskCode).equalToWhenPresent(record::getPrevTaskCode)
            .set(prevWorkflowCode).equalToWhenPresent(record::getPrevWorkflowCode)
            .where(id, isEqualTo(record::getId))
        );
    }
}