package cn.zhengcaiyun.idata.system.dal.dao;

import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
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
public interface SysFeatureDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, createTime, editTime, featureCode, featureName, featureType, parentCode);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<SysFeature> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SysFeatureResult")
    Optional<SysFeature> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SysFeatureResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.SMALLINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="feature_code", property="featureCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="feature_name", property="featureName", jdbcType=JdbcType.VARCHAR),
        @Result(column="feature_type", property="featureType", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_code", property="parentCode", jdbcType=JdbcType.VARCHAR)
    })
    List<SysFeature> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int insert(SysFeature record) {
        return MyBatis3Utils.insert(this::insert, record, sysFeature, c ->
            c.map(del).toProperty("del")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(featureCode).toProperty("featureCode")
            .map(featureName).toProperty("featureName")
            .map(featureType).toProperty("featureType")
            .map(parentCode).toProperty("parentCode")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int insertSelective(SysFeature record) {
        return MyBatis3Utils.insert(this::insert, record, sysFeature, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(featureCode).toPropertyWhenPresent("featureCode", record::getFeatureCode)
            .map(featureName).toPropertyWhenPresent("featureName", record::getFeatureName)
            .map(featureType).toPropertyWhenPresent("featureType", record::getFeatureType)
            .map(parentCode).toPropertyWhenPresent("parentCode", record::getParentCode)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default Optional<SysFeature> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default List<SysFeature> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default List<SysFeature> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default Optional<SysFeature> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, sysFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    static UpdateDSL<UpdateModel> updateAllColumns(SysFeature record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(featureCode).equalTo(record::getFeatureCode)
                .set(featureName).equalTo(record::getFeatureName)
                .set(featureType).equalTo(record::getFeatureType)
                .set(parentCode).equalTo(record::getParentCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SysFeature record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(featureCode).equalToWhenPresent(record::getFeatureCode)
                .set(featureName).equalToWhenPresent(record::getFeatureName)
                .set(featureType).equalToWhenPresent(record::getFeatureType)
                .set(parentCode).equalToWhenPresent(record::getParentCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int updateByPrimaryKey(SysFeature record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editTime).equalTo(record::getEditTime)
            .set(featureCode).equalTo(record::getFeatureCode)
            .set(featureName).equalTo(record::getFeatureName)
            .set(featureType).equalTo(record::getFeatureType)
            .set(parentCode).equalTo(record::getParentCode)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_feature")
    default int updateByPrimaryKeySelective(SysFeature record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(featureCode).equalToWhenPresent(record::getFeatureCode)
            .set(featureName).equalToWhenPresent(record::getFeatureName)
            .set(featureType).equalToWhenPresent(record::getFeatureType)
            .set(parentCode).equalToWhenPresent(record::getParentCode)
            .where(id, isEqualTo(record::getId))
        );
    }
}