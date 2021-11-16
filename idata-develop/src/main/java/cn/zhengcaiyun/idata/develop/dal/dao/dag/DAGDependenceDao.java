package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGDependenceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGDependence;
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
public interface DAGDependenceDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    BasicColumn[] selectList = BasicColumn.columnList(id, creator, createTime, dagId, prevDagId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DAGDependence> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DAGDependenceResult")
    Optional<DAGDependence> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DAGDependenceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="dag_id", property="dagId", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_dag_id", property="prevDagId", jdbcType=JdbcType.BIGINT)
    })
    List<DAGDependence> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int insert(DAGDependence record) {
        return MyBatis3Utils.insert(this::insert, record, DAG_DEPENDENCE, c ->
            c.map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(dagId).toProperty("dagId")
            .map(prevDagId).toProperty("prevDagId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int insertSelective(DAGDependence record) {
        return MyBatis3Utils.insert(this::insert, record, DAG_DEPENDENCE, c ->
            c.map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(dagId).toPropertyWhenPresent("dagId", record::getDagId)
            .map(prevDagId).toPropertyWhenPresent("prevDagId", record::getPrevDagId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default Optional<DAGDependence> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default List<DAGDependence> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default List<DAGDependence> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default Optional<DAGDependence> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DAG_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    static UpdateDSL<UpdateModel> updateAllColumns(DAGDependence record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(dagId).equalTo(record::getDagId)
                .set(prevDagId).equalTo(record::getPrevDagId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DAGDependence record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(dagId).equalToWhenPresent(record::getDagId)
                .set(prevDagId).equalToWhenPresent(record::getPrevDagId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int updateByPrimaryKey(DAGDependence record) {
        return update(c ->
            c.set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(dagId).equalTo(record::getDagId)
            .set(prevDagId).equalTo(record::getPrevDagId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_dependence")
    default int updateByPrimaryKeySelective(DAGDependence record) {
        return update(c ->
            c.set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(dagId).equalToWhenPresent(record::getDagId)
            .set(prevDagId).equalToWhenPresent(record::getPrevDagId)
            .where(id, isEqualTo(record::getId))
        );
    }
}