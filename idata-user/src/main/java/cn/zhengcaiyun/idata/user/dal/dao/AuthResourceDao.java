package cn.zhengcaiyun.idata.user.dal.dao;

import static cn.zhengcaiyun.idata.user.dal.dao.AuthResourceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.user.dal.model.AuthResource;
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
public interface AuthResourceDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, authRecordId, policyRecordId, resourceType, resources);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<AuthResource> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthResourceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="auth_record_id", property="authRecordId", jdbcType=JdbcType.BIGINT),
        @Result(column="policy_record_id", property="policyRecordId", jdbcType=JdbcType.BIGINT),
        @Result(column="resource_type", property="resourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="resources", property="resources", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<AuthResource> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthResourceResult")
    Optional<AuthResource> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int insert(AuthResource row) {
        return MyBatis3Utils.insert(this::insert, row, authResource, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(authRecordId).toProperty("authRecordId")
            .map(policyRecordId).toProperty("policyRecordId")
            .map(resourceType).toProperty("resourceType")
            .map(resources).toProperty("resources")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int insertSelective(AuthResource row) {
        return MyBatis3Utils.insert(this::insert, row, authResource, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(authRecordId).toPropertyWhenPresent("authRecordId", row::getAuthRecordId)
            .map(policyRecordId).toPropertyWhenPresent("policyRecordId", row::getPolicyRecordId)
            .map(resourceType).toPropertyWhenPresent("resourceType", row::getResourceType)
            .map(resources).toPropertyWhenPresent("resources", row::getResources)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default Optional<AuthResource> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default List<AuthResource> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default List<AuthResource> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default Optional<AuthResource> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthResource row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(authRecordId).equalTo(row::getAuthRecordId)
                .set(policyRecordId).equalTo(row::getPolicyRecordId)
                .set(resourceType).equalTo(row::getResourceType)
                .set(resources).equalTo(row::getResources);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthResource row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(authRecordId).equalToWhenPresent(row::getAuthRecordId)
                .set(policyRecordId).equalToWhenPresent(row::getPolicyRecordId)
                .set(resourceType).equalToWhenPresent(row::getResourceType)
                .set(resources).equalToWhenPresent(row::getResources);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int updateByPrimaryKey(AuthResource row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(authRecordId).equalTo(row::getAuthRecordId)
            .set(policyRecordId).equalTo(row::getPolicyRecordId)
            .set(resourceType).equalTo(row::getResourceType)
            .set(resources).equalTo(row::getResources)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_resource")
    default int updateByPrimaryKeySelective(AuthResource row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(authRecordId).equalToWhenPresent(row::getAuthRecordId)
            .set(policyRecordId).equalToWhenPresent(row::getPolicyRecordId)
            .set(resourceType).equalToWhenPresent(row::getResourceType)
            .set(resources).equalToWhenPresent(row::getResources)
            .where(id, isEqualTo(row::getId))
        );
    }
}