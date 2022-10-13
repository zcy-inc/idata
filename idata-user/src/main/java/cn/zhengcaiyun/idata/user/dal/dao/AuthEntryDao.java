package cn.zhengcaiyun.idata.user.dal.dao;

import static cn.zhengcaiyun.idata.user.dal.dao.AuthEntryDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.user.dal.model.AuthEntry;
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
public interface AuthEntryDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, subjectId, subjectType, remark);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<AuthEntry> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthEntryResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="subject_id", property="subjectId", jdbcType=JdbcType.VARCHAR),
        @Result(column="subject_type", property="subjectType", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<AuthEntry> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthEntryResult")
    Optional<AuthEntry> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int insert(AuthEntry row) {
        return MyBatis3Utils.insert(this::insert, row, authEntry, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(subjectId).toProperty("subjectId")
            .map(subjectType).toProperty("subjectType")
            .map(remark).toProperty("remark")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int insertSelective(AuthEntry row) {
        return MyBatis3Utils.insert(this::insert, row, authEntry, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(subjectId).toPropertyWhenPresent("subjectId", row::getSubjectId)
            .map(subjectType).toPropertyWhenPresent("subjectType", row::getSubjectType)
            .map(remark).toPropertyWhenPresent("remark", row::getRemark)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default Optional<AuthEntry> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default List<AuthEntry> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default List<AuthEntry> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default Optional<AuthEntry> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authEntry, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthEntry row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(subjectId).equalTo(row::getSubjectId)
                .set(subjectType).equalTo(row::getSubjectType)
                .set(remark).equalTo(row::getRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthEntry row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(subjectId).equalToWhenPresent(row::getSubjectId)
                .set(subjectType).equalToWhenPresent(row::getSubjectType)
                .set(remark).equalToWhenPresent(row::getRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int updateByPrimaryKey(AuthEntry row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(subjectId).equalTo(row::getSubjectId)
            .set(subjectType).equalTo(row::getSubjectType)
            .set(remark).equalTo(row::getRemark)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_auth_entry")
    default int updateByPrimaryKeySelective(AuthEntry row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(subjectId).equalToWhenPresent(row::getSubjectId)
            .set(subjectType).equalToWhenPresent(row::getSubjectType)
            .set(remark).equalToWhenPresent(row::getRemark)
            .where(id, isEqualTo(row::getId))
        );
    }
}