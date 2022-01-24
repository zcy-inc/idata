package cn.zhengcaiyun.idata.develop.dal.dao.integration;

import static cn.zhengcaiyun.idata.develop.dal.dao.integration.DSEntityMappingDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
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
public interface DSEntityMappingDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    BasicColumn[] selectList = BasicColumn.columnList(id, createTime, entityId, environment, dsEntityType, dsEntityCode);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DSEntityMapping> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DSEntityMappingResult")
    Optional<DSEntityMapping> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DSEntityMappingResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="entity_id", property="entityId", jdbcType=JdbcType.BIGINT),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="ds_entity_type", property="dsEntityType", jdbcType=JdbcType.VARCHAR),
        @Result(column="ds_entity_code", property="dsEntityCode", jdbcType=JdbcType.BIGINT)
    })
    List<DSEntityMapping> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int insert(DSEntityMapping record) {
        return MyBatis3Utils.insert(this::insert, record, DS_ENTITY_MAPPING, c ->
            c.map(createTime).toProperty("createTime")
            .map(entityId).toProperty("entityId")
            .map(environment).toProperty("environment")
            .map(dsEntityType).toProperty("dsEntityType")
            .map(dsEntityCode).toProperty("dsEntityCode")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int insertSelective(DSEntityMapping record) {
        return MyBatis3Utils.insert(this::insert, record, DS_ENTITY_MAPPING, c ->
            c.map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(entityId).toPropertyWhenPresent("entityId", record::getEntityId)
            .map(environment).toPropertyWhenPresent("environment", record::getEnvironment)
            .map(dsEntityType).toPropertyWhenPresent("dsEntityType", record::getDsEntityType)
            .map(dsEntityCode).toPropertyWhenPresent("dsEntityCode", record::getDsEntityCode)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default Optional<DSEntityMapping> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default List<DSEntityMapping> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default List<DSEntityMapping> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default Optional<DSEntityMapping> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DS_ENTITY_MAPPING, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    static UpdateDSL<UpdateModel> updateAllColumns(DSEntityMapping record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalTo(record::getCreateTime)
                .set(entityId).equalTo(record::getEntityId)
                .set(environment).equalTo(record::getEnvironment)
                .set(dsEntityType).equalTo(record::getDsEntityType)
                .set(dsEntityCode).equalTo(record::getDsEntityCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DSEntityMapping record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(entityId).equalToWhenPresent(record::getEntityId)
                .set(environment).equalToWhenPresent(record::getEnvironment)
                .set(dsEntityType).equalToWhenPresent(record::getDsEntityType)
                .set(dsEntityCode).equalToWhenPresent(record::getDsEntityCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int updateByPrimaryKey(DSEntityMapping record) {
        return update(c ->
            c.set(createTime).equalTo(record::getCreateTime)
            .set(entityId).equalTo(record::getEntityId)
            .set(environment).equalTo(record::getEnvironment)
            .set(dsEntityType).equalTo(record::getDsEntityType)
            .set(dsEntityCode).equalTo(record::getDsEntityCode)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: ite_ds_entity_mapping")
    default int updateByPrimaryKeySelective(DSEntityMapping record) {
        return update(c ->
            c.set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(entityId).equalToWhenPresent(record::getEntityId)
            .set(environment).equalToWhenPresent(record::getEnvironment)
            .set(dsEntityType).equalToWhenPresent(record::getDsEntityType)
            .set(dsEntityCode).equalToWhenPresent(record::getDsEntityCode)
            .where(id, isEqualTo(record::getId))
        );
    }
}