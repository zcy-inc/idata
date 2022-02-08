package cn.zhengcaiyun.idata.mergedata.dal.dao;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.mergedata.dal.model.MigrateResult;
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
public interface MigrateResultDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    BasicColumn[] selectList = BasicColumn.columnList(MigrateResultDynamicSqlSupport.id, MigrateResultDynamicSqlSupport.createTime, MigrateResultDynamicSqlSupport.editTime, MigrateResultDynamicSqlSupport.migrateType, MigrateResultDynamicSqlSupport.reason, MigrateResultDynamicSqlSupport.data);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<MigrateResult> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("MigrateResultResult")
    Optional<MigrateResult> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="MigrateResultResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="migrate_type", property="migrateType", jdbcType=JdbcType.VARCHAR),
        @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
        @Result(column="data", property="data", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<MigrateResult> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(MigrateResultDynamicSqlSupport.id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int insert(MigrateResult record) {
        return MyBatis3Utils.insert(this::insert, record, MigrateResultDynamicSqlSupport.migrateResult, c ->
            c.map(MigrateResultDynamicSqlSupport.createTime).toProperty("createTime")
            .map(MigrateResultDynamicSqlSupport.editTime).toProperty("editTime")
            .map(MigrateResultDynamicSqlSupport.migrateType).toProperty("migrateType")
            .map(MigrateResultDynamicSqlSupport.reason).toProperty("reason")
            .map(MigrateResultDynamicSqlSupport.data).toProperty("data")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int insertSelective(MigrateResult record) {
        return MyBatis3Utils.insert(this::insert, record, MigrateResultDynamicSqlSupport.migrateResult, c ->
            c.map(MigrateResultDynamicSqlSupport.createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(MigrateResultDynamicSqlSupport.editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(MigrateResultDynamicSqlSupport.migrateType).toPropertyWhenPresent("migrateType", record::getMigrateType)
            .map(MigrateResultDynamicSqlSupport.reason).toPropertyWhenPresent("reason", record::getReason)
            .map(MigrateResultDynamicSqlSupport.data).toPropertyWhenPresent("data", record::getData)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default Optional<MigrateResult> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default List<MigrateResult> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default List<MigrateResult> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default Optional<MigrateResult> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(MigrateResultDynamicSqlSupport.id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, MigrateResultDynamicSqlSupport.migrateResult, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    static UpdateDSL<UpdateModel> updateAllColumns(MigrateResult record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(MigrateResultDynamicSqlSupport.createTime).equalTo(record::getCreateTime)
                .set(MigrateResultDynamicSqlSupport.editTime).equalTo(record::getEditTime)
                .set(MigrateResultDynamicSqlSupport.migrateType).equalTo(record::getMigrateType)
                .set(MigrateResultDynamicSqlSupport.reason).equalTo(record::getReason)
                .set(MigrateResultDynamicSqlSupport.data).equalTo(record::getData);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(MigrateResult record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(MigrateResultDynamicSqlSupport.createTime).equalToWhenPresent(record::getCreateTime)
                .set(MigrateResultDynamicSqlSupport.editTime).equalToWhenPresent(record::getEditTime)
                .set(MigrateResultDynamicSqlSupport.migrateType).equalToWhenPresent(record::getMigrateType)
                .set(MigrateResultDynamicSqlSupport.reason).equalToWhenPresent(record::getReason)
                .set(MigrateResultDynamicSqlSupport.data).equalToWhenPresent(record::getData);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int updateByPrimaryKey(MigrateResult record) {
        return update(c ->
            c.set(MigrateResultDynamicSqlSupport.createTime).equalTo(record::getCreateTime)
            .set(MigrateResultDynamicSqlSupport.editTime).equalTo(record::getEditTime)
            .set(MigrateResultDynamicSqlSupport.migrateType).equalTo(record::getMigrateType)
            .set(MigrateResultDynamicSqlSupport.reason).equalTo(record::getReason)
            .set(MigrateResultDynamicSqlSupport.data).equalTo(record::getData)
            .where(MigrateResultDynamicSqlSupport.id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: oth_migrate_result")
    default int updateByPrimaryKeySelective(MigrateResult record) {
        return update(c ->
            c.set(MigrateResultDynamicSqlSupport.createTime).equalToWhenPresent(record::getCreateTime)
            .set(MigrateResultDynamicSqlSupport.editTime).equalToWhenPresent(record::getEditTime)
            .set(MigrateResultDynamicSqlSupport.migrateType).equalToWhenPresent(record::getMigrateType)
            .set(MigrateResultDynamicSqlSupport.reason).equalToWhenPresent(record::getReason)
            .set(MigrateResultDynamicSqlSupport.data).equalToWhenPresent(record::getData)
            .where(MigrateResultDynamicSqlSupport.id, isEqualTo(record::getId))
        );
    }
}