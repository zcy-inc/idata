package cn.zhengcaiyun.idata.develop.dal.dao;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
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
public interface DevLabelDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, labelCode, tableId, columnName, labelParamValue);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevLabel> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevLabelResult")
    Optional<DevLabel> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevLabelResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="label_code", property="labelCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="table_id", property="tableId", jdbcType=JdbcType.BIGINT),
        @Result(column="column_name", property="columnName", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_param_value", property="labelParamValue", jdbcType=JdbcType.VARCHAR)
    })
    List<DevLabel> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int insert(DevLabel record) {
        return MyBatis3Utils.insert(this::insert, record, devLabel, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(labelCode).toProperty("labelCode")
            .map(tableId).toProperty("tableId")
            .map(columnName).toProperty("columnName")
            .map(labelParamValue).toProperty("labelParamValue")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int insertSelective(DevLabel record) {
        return MyBatis3Utils.insert(this::insert, record, devLabel, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(labelCode).toPropertyWhenPresent("labelCode", record::getLabelCode)
            .map(tableId).toPropertyWhenPresent("tableId", record::getTableId)
            .map(columnName).toPropertyWhenPresent("columnName", record::getColumnName)
            .map(labelParamValue).toPropertyWhenPresent("labelParamValue", record::getLabelParamValue)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default Optional<DevLabel> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default List<DevLabel> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default List<DevLabel> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default Optional<DevLabel> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    static UpdateDSL<UpdateModel> updateAllColumns(DevLabel record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(labelCode).equalTo(record::getLabelCode)
                .set(tableId).equalTo(record::getTableId)
                .set(columnName).equalTo(record::getColumnName)
                .set(labelParamValue).equalTo(record::getLabelParamValue);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevLabel record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(labelCode).equalToWhenPresent(record::getLabelCode)
                .set(tableId).equalToWhenPresent(record::getTableId)
                .set(columnName).equalToWhenPresent(record::getColumnName)
                .set(labelParamValue).equalToWhenPresent(record::getLabelParamValue);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int updateByPrimaryKey(DevLabel record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(labelCode).equalTo(record::getLabelCode)
            .set(tableId).equalTo(record::getTableId)
            .set(columnName).equalTo(record::getColumnName)
            .set(labelParamValue).equalTo(record::getLabelParamValue)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label")
    default int updateByPrimaryKeySelective(DevLabel record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(labelCode).equalToWhenPresent(record::getLabelCode)
            .set(tableId).equalToWhenPresent(record::getTableId)
            .set(columnName).equalToWhenPresent(record::getColumnName)
            .set(labelParamValue).equalToWhenPresent(record::getLabelParamValue)
            .where(id, isEqualTo(record::getId))
        );
    }
}