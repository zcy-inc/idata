package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import static cn.zhengcaiyun.idata.develop.dal.dao.integration.DSDependRelationDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependRelation;
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
public interface DSDependRelationDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    BasicColumn[] selectList = BasicColumn.columnList(id, createTime, workflowCode, taskCode, prevWorkflowCode, prevTaskCode, dependNodeCode, dependLevel);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DSDependRelation> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DSDependRelationResult")
    Optional<DSDependRelation> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DSDependRelationResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="workflow_code", property="workflowCode", jdbcType=JdbcType.BIGINT),
        @Result(column="task_code", property="taskCode", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_workflow_code", property="prevWorkflowCode", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_task_code", property="prevTaskCode", jdbcType=JdbcType.BIGINT),
        @Result(column="depend_node_code", property="dependNodeCode", jdbcType=JdbcType.BIGINT),
        @Result(column="depend_level", property="dependLevel", jdbcType=JdbcType.VARCHAR)
    })
    List<DSDependRelation> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int insert(DSDependRelation record) {
        return MyBatis3Utils.insert(this::insert, record, DS_DEPEND_RELATION, c ->
            c.map(createTime).toProperty("createTime")
            .map(workflowCode).toProperty("workflowCode")
            .map(taskCode).toProperty("taskCode")
            .map(prevWorkflowCode).toProperty("prevWorkflowCode")
            .map(prevTaskCode).toProperty("prevTaskCode")
            .map(dependNodeCode).toProperty("dependNodeCode")
            .map(dependLevel).toProperty("dependLevel")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int insertSelective(DSDependRelation record) {
        return MyBatis3Utils.insert(this::insert, record, DS_DEPEND_RELATION, c ->
            c.map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(workflowCode).toPropertyWhenPresent("workflowCode", record::getWorkflowCode)
            .map(taskCode).toPropertyWhenPresent("taskCode", record::getTaskCode)
            .map(prevWorkflowCode).toPropertyWhenPresent("prevWorkflowCode", record::getPrevWorkflowCode)
            .map(prevTaskCode).toPropertyWhenPresent("prevTaskCode", record::getPrevTaskCode)
            .map(dependNodeCode).toPropertyWhenPresent("dependNodeCode", record::getDependNodeCode)
            .map(dependLevel).toPropertyWhenPresent("dependLevel", record::getDependLevel)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default Optional<DSDependRelation> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default List<DSDependRelation> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default List<DSDependRelation> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default Optional<DSDependRelation> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DS_DEPEND_RELATION, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    static UpdateDSL<UpdateModel> updateAllColumns(DSDependRelation record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalTo(record::getCreateTime)
                .set(workflowCode).equalTo(record::getWorkflowCode)
                .set(taskCode).equalTo(record::getTaskCode)
                .set(prevWorkflowCode).equalTo(record::getPrevWorkflowCode)
                .set(prevTaskCode).equalTo(record::getPrevTaskCode)
                .set(dependNodeCode).equalTo(record::getDependNodeCode)
                .set(dependLevel).equalTo(record::getDependLevel);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DSDependRelation record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(workflowCode).equalToWhenPresent(record::getWorkflowCode)
                .set(taskCode).equalToWhenPresent(record::getTaskCode)
                .set(prevWorkflowCode).equalToWhenPresent(record::getPrevWorkflowCode)
                .set(prevTaskCode).equalToWhenPresent(record::getPrevTaskCode)
                .set(dependNodeCode).equalToWhenPresent(record::getDependNodeCode)
                .set(dependLevel).equalToWhenPresent(record::getDependLevel);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int updateByPrimaryKey(DSDependRelation record) {
        return update(c ->
            c.set(createTime).equalTo(record::getCreateTime)
            .set(workflowCode).equalTo(record::getWorkflowCode)
            .set(taskCode).equalTo(record::getTaskCode)
            .set(prevWorkflowCode).equalTo(record::getPrevWorkflowCode)
            .set(prevTaskCode).equalTo(record::getPrevTaskCode)
            .set(dependNodeCode).equalTo(record::getDependNodeCode)
            .set(dependLevel).equalTo(record::getDependLevel)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_depend_relation")
    default int updateByPrimaryKeySelective(DSDependRelation record) {
        return update(c ->
            c.set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(workflowCode).equalToWhenPresent(record::getWorkflowCode)
            .set(taskCode).equalToWhenPresent(record::getTaskCode)
            .set(prevWorkflowCode).equalToWhenPresent(record::getPrevWorkflowCode)
            .set(prevTaskCode).equalToWhenPresent(record::getPrevTaskCode)
            .set(dependNodeCode).equalToWhenPresent(record::getDependNodeCode)
            .set(dependLevel).equalToWhenPresent(record::getDependLevel)
            .where(id, isEqualTo(record::getId))
        );
    }
}