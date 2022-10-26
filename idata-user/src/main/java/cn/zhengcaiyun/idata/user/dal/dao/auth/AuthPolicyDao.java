package cn.zhengcaiyun.idata.user.dal.dao.auth;

import static cn.zhengcaiyun.idata.user.dal.dao.auth.AuthPolicyDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
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
public interface AuthPolicyDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, authRecordId, effect, actions, resourceType, remark);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<AuthPolicy> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthPolicyResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="auth_record_id", property="authRecordId", jdbcType=JdbcType.BIGINT),
        @Result(column="effect", property="effect", jdbcType=JdbcType.VARCHAR),
        @Result(column="actions", property="actions", jdbcType=JdbcType.VARCHAR),
        @Result(column="resource_type", property="resourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<AuthPolicy> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthPolicyResult")
    Optional<AuthPolicy> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int insert(AuthPolicy row) {
        return MyBatis3Utils.insert(this::insert, row, AUTH_POLICY, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(authRecordId).toProperty("authRecordId")
            .map(effect).toProperty("effect")
            .map(actions).toProperty("actions")
            .map(resourceType).toProperty("resourceType")
            .map(remark).toProperty("remark")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int insertSelective(AuthPolicy row) {
        return MyBatis3Utils.insert(this::insert, row, AUTH_POLICY, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(authRecordId).toPropertyWhenPresent("authRecordId", row::getAuthRecordId)
            .map(effect).toPropertyWhenPresent("effect", row::getEffect)
            .map(actions).toPropertyWhenPresent("actions", row::getActions)
            .map(resourceType).toPropertyWhenPresent("resourceType", row::getResourceType)
            .map(remark).toPropertyWhenPresent("remark", row::getRemark)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default Optional<AuthPolicy> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default List<AuthPolicy> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default List<AuthPolicy> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default Optional<AuthPolicy> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, AUTH_POLICY, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthPolicy row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(authRecordId).equalTo(row::getAuthRecordId)
                .set(effect).equalTo(row::getEffect)
                .set(actions).equalTo(row::getActions)
                .set(resourceType).equalTo(row::getResourceType)
                .set(remark).equalTo(row::getRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthPolicy row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(authRecordId).equalToWhenPresent(row::getAuthRecordId)
                .set(effect).equalToWhenPresent(row::getEffect)
                .set(actions).equalToWhenPresent(row::getActions)
                .set(resourceType).equalToWhenPresent(row::getResourceType)
                .set(remark).equalToWhenPresent(row::getRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int updateByPrimaryKey(AuthPolicy row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(authRecordId).equalTo(row::getAuthRecordId)
            .set(effect).equalTo(row::getEffect)
            .set(actions).equalTo(row::getActions)
            .set(resourceType).equalTo(row::getResourceType)
            .set(remark).equalTo(row::getRemark)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_policy")
    default int updateByPrimaryKeySelective(AuthPolicy row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(authRecordId).equalToWhenPresent(row::getAuthRecordId)
            .set(effect).equalToWhenPresent(row::getEffect)
            .set(actions).equalToWhenPresent(row::getActions)
            .set(resourceType).equalToWhenPresent(row::getResourceType)
            .set(remark).equalToWhenPresent(row::getRemark)
            .where(id, isEqualTo(row::getId))
        );
    }
}