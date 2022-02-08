package cn.zhengcaiyun.idata.map.dal.dao;

import static cn.zhengcaiyun.idata.map.dal.dao.ViewCountDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.map.dal.model.ViewCount;
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
public interface ViewCountDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, entityCode, entitySource, userId, viewCount);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ViewCount> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("MapViewCountResult")
    Optional<ViewCount> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="MapViewCountResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="entity_code", property="entityCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="entity_source", property="entitySource", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="view_count", property="viewCount", jdbcType=JdbcType.BIGINT)
    })
    List<ViewCount> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int insert(ViewCount record) {
        return MyBatis3Utils.insert(this::insert, record, VIEW_COUNT, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(entityCode).toProperty("entityCode")
            .map(entitySource).toProperty("entitySource")
            .map(userId).toProperty("userId")
            .map(viewCount).toProperty("viewCount")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int insertSelective(ViewCount record) {
        return MyBatis3Utils.insert(this::insert, record, VIEW_COUNT, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(entityCode).toPropertyWhenPresent("entityCode", record::getEntityCode)
            .map(entitySource).toPropertyWhenPresent("entitySource", record::getEntitySource)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(viewCount).toPropertyWhenPresent("viewCount", record::getViewCount)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default Optional<ViewCount> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default List<ViewCount> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default List<ViewCount> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default Optional<ViewCount> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, VIEW_COUNT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    static UpdateDSL<UpdateModel> updateAllColumns(ViewCount record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(entityCode).equalTo(record::getEntityCode)
                .set(entitySource).equalTo(record::getEntitySource)
                .set(userId).equalTo(record::getUserId)
                .set(viewCount).equalTo(record::getViewCount);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(ViewCount record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(entityCode).equalToWhenPresent(record::getEntityCode)
                .set(entitySource).equalToWhenPresent(record::getEntitySource)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(viewCount).equalToWhenPresent(record::getViewCount);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int updateByPrimaryKey(ViewCount record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(entityCode).equalTo(record::getEntityCode)
            .set(entitySource).equalTo(record::getEntitySource)
            .set(userId).equalTo(record::getUserId)
            .set(viewCount).equalTo(record::getViewCount)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: map_view_count")
    default int updateByPrimaryKeySelective(ViewCount record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(entityCode).equalToWhenPresent(record::getEntityCode)
            .set(entitySource).equalToWhenPresent(record::getEntitySource)
            .set(userId).equalToWhenPresent(record::getUserId)
            .set(viewCount).equalToWhenPresent(record::getViewCount)
            .where(id, isEqualTo(record::getId))
        );
    }
}