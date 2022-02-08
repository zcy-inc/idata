package cn.zhengcaiyun.idata.system.dal.dao;

import static cn.zhengcaiyun.idata.system.dal.dao.WorkspaceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.system.dal.model.Workspace;
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
public interface WorkspaceDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, editor, createTime, editTime, name, code, urlPath);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<Workspace> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("WorkspaceResult")
    Optional<Workspace> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="WorkspaceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.BIT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="url_path", property="urlPath", jdbcType=JdbcType.VARCHAR)
    })
    List<Workspace> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int insert(Workspace record) {
        return MyBatis3Utils.insert(this::insert, record, workspace, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(editor).toProperty("editor")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(name).toProperty("name")
            .map(code).toProperty("code")
            .map(urlPath).toProperty("urlPath")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int insertSelective(Workspace record) {
        return MyBatis3Utils.insert(this::insert, record, workspace, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(code).toPropertyWhenPresent("code", record::getCode)
            .map(urlPath).toPropertyWhenPresent("urlPath", record::getUrlPath)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default Optional<Workspace> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default List<Workspace> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default List<Workspace> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default Optional<Workspace> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, workspace, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    static UpdateDSL<UpdateModel> updateAllColumns(Workspace record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(editor).equalTo(record::getEditor)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(name).equalTo(record::getName)
                .set(code).equalTo(record::getCode)
                .set(urlPath).equalTo(record::getUrlPath);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Workspace record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(name).equalToWhenPresent(record::getName)
                .set(code).equalToWhenPresent(record::getCode)
                .set(urlPath).equalToWhenPresent(record::getUrlPath);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int updateByPrimaryKey(Workspace record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(editor).equalTo(record::getEditor)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editTime).equalTo(record::getEditTime)
            .set(name).equalTo(record::getName)
            .set(code).equalTo(record::getCode)
            .set(urlPath).equalTo(record::getUrlPath)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: workspace")
    default int updateByPrimaryKeySelective(Workspace record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(name).equalToWhenPresent(record::getName)
            .set(code).equalToWhenPresent(record::getCode)
            .set(urlPath).equalToWhenPresent(record::getUrlPath)
            .where(id, isEqualTo(record::getId))
        );
    }
}