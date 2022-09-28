package cn.zhengcaiyun.idata.user.dal.dao;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAppFeatureDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.user.dal.model.UacAppFeature;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface UacAppFeatureDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, appKey, featureCodes);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<UacAppFeature> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UacAppFeatureResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="app_key", property="appKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="feature_codes", property="featureCodes", jdbcType=JdbcType.VARCHAR)
    })
    List<UacAppFeature> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UacAppFeatureResult")
    Optional<UacAppFeature> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int insert(UacAppFeature row) {
        return MyBatis3Utils.insert(this::insert, row, uacAppFeature, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(appKey).toProperty("appKey")
            .map(featureCodes).toProperty("featureCodes")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int insertSelective(UacAppFeature row) {
        return MyBatis3Utils.insert(this::insert, row, uacAppFeature, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(appKey).toPropertyWhenPresent("appKey", row::getAppKey)
            .map(featureCodes).toPropertyWhenPresent("featureCodes", row::getFeatureCodes)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default Optional<UacAppFeature> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default List<UacAppFeature> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default List<UacAppFeature> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default Optional<UacAppFeature> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, uacAppFeature, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    static UpdateDSL<UpdateModel> updateAllColumns(UacAppFeature row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(appKey).equalTo(row::getAppKey)
                .set(featureCodes).equalTo(row::getFeatureCodes);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UacAppFeature row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(appKey).equalToWhenPresent(row::getAppKey)
                .set(featureCodes).equalToWhenPresent(row::getFeatureCodes);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int updateByPrimaryKey(UacAppFeature row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(appKey).equalTo(row::getAppKey)
            .set(featureCodes).equalTo(row::getFeatureCodes)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_app_feature")
    default int updateByPrimaryKeySelective(UacAppFeature row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(appKey).equalToWhenPresent(row::getAppKey)
            .set(featureCodes).equalToWhenPresent(row::getFeatureCodes)
            .where(id, isEqualTo(row::getId))
        );
    }
}