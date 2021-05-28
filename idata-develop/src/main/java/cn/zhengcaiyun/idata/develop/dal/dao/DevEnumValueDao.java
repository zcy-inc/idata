package cn.zhengcaiyun.idata.develop.dal.dao;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
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
public interface DevEnumValueDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, enumCode, valueCode, enumValue, enumAttributes, parentCode);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevEnumValue> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevEnumValueResult")
    Optional<DevEnumValue> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevEnumValueResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enum_code", property="enumCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_code", property="valueCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="enum_value", property="enumValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="enum_attributes", property="enumAttributes", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_code", property="parentCode", jdbcType=JdbcType.VARCHAR)
    })
    List<DevEnumValue> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int insert(DevEnumValue record) {
        return MyBatis3Utils.insert(this::insert, record, devEnumValue, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(enumCode).toProperty("enumCode")
            .map(valueCode).toProperty("valueCode")
            .map(enumValue).toProperty("enumValue")
            .map(enumAttributes).toProperty("enumAttributes")
            .map(parentCode).toProperty("parentCode")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int insertSelective(DevEnumValue record) {
        return MyBatis3Utils.insert(this::insert, record, devEnumValue, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(enumCode).toPropertyWhenPresent("enumCode", record::getEnumCode)
            .map(valueCode).toPropertyWhenPresent("valueCode", record::getValueCode)
            .map(enumValue).toPropertyWhenPresent("enumValue", record::getEnumValue)
            .map(enumAttributes).toPropertyWhenPresent("enumAttributes", record::getEnumAttributes)
            .map(parentCode).toPropertyWhenPresent("parentCode", record::getParentCode)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default Optional<DevEnumValue> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default List<DevEnumValue> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default List<DevEnumValue> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default Optional<DevEnumValue> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devEnumValue, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    static UpdateDSL<UpdateModel> updateAllColumns(DevEnumValue record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(enumCode).equalTo(record::getEnumCode)
                .set(valueCode).equalTo(record::getValueCode)
                .set(enumValue).equalTo(record::getEnumValue)
                .set(enumAttributes).equalTo(record::getEnumAttributes)
                .set(parentCode).equalTo(record::getParentCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevEnumValue record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(enumCode).equalToWhenPresent(record::getEnumCode)
                .set(valueCode).equalToWhenPresent(record::getValueCode)
                .set(enumValue).equalToWhenPresent(record::getEnumValue)
                .set(enumAttributes).equalToWhenPresent(record::getEnumAttributes)
                .set(parentCode).equalToWhenPresent(record::getParentCode);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int updateByPrimaryKey(DevEnumValue record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(enumCode).equalTo(record::getEnumCode)
            .set(valueCode).equalTo(record::getValueCode)
            .set(enumValue).equalTo(record::getEnumValue)
            .set(enumAttributes).equalTo(record::getEnumAttributes)
            .set(parentCode).equalTo(record::getParentCode)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_enum_value")
    default int updateByPrimaryKeySelective(DevEnumValue record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(enumCode).equalToWhenPresent(record::getEnumCode)
            .set(valueCode).equalToWhenPresent(record::getValueCode)
            .set(enumValue).equalToWhenPresent(record::getEnumValue)
            .set(enumAttributes).equalToWhenPresent(record::getEnumAttributes)
            .set(parentCode).equalToWhenPresent(record::getParentCode)
            .where(id, isEqualTo(record::getId))
        );
    }
}