package cn.zhengcaiyun.idata.develop.dal.dao;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
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
public interface DevForeignKeyDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, tableId, columnNames, referTableId, referColumnNames, erType);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevForeignKey> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevForeignKeyResult")
    Optional<DevForeignKey> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevForeignKeyResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="table_id", property="tableId", jdbcType=JdbcType.BIGINT),
        @Result(column="column_names", property="columnNames", jdbcType=JdbcType.VARCHAR),
        @Result(column="refer_table_id", property="referTableId", jdbcType=JdbcType.BIGINT),
        @Result(column="refer_column_names", property="referColumnNames", jdbcType=JdbcType.VARCHAR),
        @Result(column="er_type", property="erType", jdbcType=JdbcType.VARCHAR)
    })
    List<DevForeignKey> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int insert(DevForeignKey record) {
        return MyBatis3Utils.insert(this::insert, record, devForeignKey, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(tableId).toProperty("tableId")
            .map(columnNames).toProperty("columnNames")
            .map(referTableId).toProperty("referTableId")
            .map(referColumnNames).toProperty("referColumnNames")
            .map(erType).toProperty("erType")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int insertSelective(DevForeignKey record) {
        return MyBatis3Utils.insert(this::insert, record, devForeignKey, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(tableId).toPropertyWhenPresent("tableId", record::getTableId)
            .map(columnNames).toPropertyWhenPresent("columnNames", record::getColumnNames)
            .map(referTableId).toPropertyWhenPresent("referTableId", record::getReferTableId)
            .map(referColumnNames).toPropertyWhenPresent("referColumnNames", record::getReferColumnNames)
            .map(erType).toPropertyWhenPresent("erType", record::getErType)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default Optional<DevForeignKey> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default List<DevForeignKey> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default List<DevForeignKey> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default Optional<DevForeignKey> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devForeignKey, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    static UpdateDSL<UpdateModel> updateAllColumns(DevForeignKey record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(tableId).equalTo(record::getTableId)
                .set(columnNames).equalTo(record::getColumnNames)
                .set(referTableId).equalTo(record::getReferTableId)
                .set(referColumnNames).equalTo(record::getReferColumnNames)
                .set(erType).equalTo(record::getErType);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevForeignKey record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(tableId).equalToWhenPresent(record::getTableId)
                .set(columnNames).equalToWhenPresent(record::getColumnNames)
                .set(referTableId).equalToWhenPresent(record::getReferTableId)
                .set(referColumnNames).equalToWhenPresent(record::getReferColumnNames)
                .set(erType).equalToWhenPresent(record::getErType);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int updateByPrimaryKey(DevForeignKey record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(tableId).equalTo(record::getTableId)
            .set(columnNames).equalTo(record::getColumnNames)
            .set(referTableId).equalTo(record::getReferTableId)
            .set(referColumnNames).equalTo(record::getReferColumnNames)
            .set(erType).equalTo(record::getErType)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_foreign_key")
    default int updateByPrimaryKeySelective(DevForeignKey record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(tableId).equalToWhenPresent(record::getTableId)
            .set(columnNames).equalToWhenPresent(record::getColumnNames)
            .set(referTableId).equalToWhenPresent(record::getReferTableId)
            .set(referColumnNames).equalToWhenPresent(record::getReferColumnNames)
            .set(erType).equalToWhenPresent(record::getErType)
            .where(id, isEqualTo(record::getId))
        );
    }
}