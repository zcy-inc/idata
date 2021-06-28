package cn.zhengcaiyun.idata.label.dal.dao;

import static cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.label.dal.model.LabObjectLabel;
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
public interface LabObjectLabelDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, name, nameEn, objectType, remark, version, originId, folderId, rules);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<LabObjectLabel> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("LabObjectLabelResult")
    Optional<LabObjectLabel> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="LabObjectLabelResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="name_en", property="nameEn", jdbcType=JdbcType.VARCHAR),
        @Result(column="object_type", property="objectType", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="origin_id", property="originId", jdbcType=JdbcType.BIGINT),
        @Result(column="folder_id", property="folderId", jdbcType=JdbcType.BIGINT),
        @Result(column="rules", property="rules", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<LabObjectLabel> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int insert(LabObjectLabel record) {
        return MyBatis3Utils.insert(this::insert, record, labObjectLabel, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(name).toProperty("name")
            .map(nameEn).toProperty("nameEn")
            .map(objectType).toProperty("objectType")
            .map(remark).toProperty("remark")
            .map(version).toProperty("version")
            .map(originId).toProperty("originId")
            .map(folderId).toProperty("folderId")
            .map(rules).toProperty("rules")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int insertSelective(LabObjectLabel record) {
        return MyBatis3Utils.insert(this::insert, record, labObjectLabel, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(nameEn).toPropertyWhenPresent("nameEn", record::getNameEn)
            .map(objectType).toPropertyWhenPresent("objectType", record::getObjectType)
            .map(remark).toPropertyWhenPresent("remark", record::getRemark)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(originId).toPropertyWhenPresent("originId", record::getOriginId)
            .map(folderId).toPropertyWhenPresent("folderId", record::getFolderId)
            .map(rules).toPropertyWhenPresent("rules", record::getRules)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default Optional<LabObjectLabel> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default List<LabObjectLabel> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default List<LabObjectLabel> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default Optional<LabObjectLabel> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, labObjectLabel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    static UpdateDSL<UpdateModel> updateAllColumns(LabObjectLabel record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(name).equalTo(record::getName)
                .set(nameEn).equalTo(record::getNameEn)
                .set(objectType).equalTo(record::getObjectType)
                .set(remark).equalTo(record::getRemark)
                .set(version).equalTo(record::getVersion)
                .set(originId).equalTo(record::getOriginId)
                .set(folderId).equalTo(record::getFolderId)
                .set(rules).equalTo(record::getRules);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(LabObjectLabel record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(name).equalToWhenPresent(record::getName)
                .set(nameEn).equalToWhenPresent(record::getNameEn)
                .set(objectType).equalToWhenPresent(record::getObjectType)
                .set(remark).equalToWhenPresent(record::getRemark)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(originId).equalToWhenPresent(record::getOriginId)
                .set(folderId).equalToWhenPresent(record::getFolderId)
                .set(rules).equalToWhenPresent(record::getRules);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int updateByPrimaryKey(LabObjectLabel record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(name).equalTo(record::getName)
            .set(nameEn).equalTo(record::getNameEn)
            .set(objectType).equalTo(record::getObjectType)
            .set(remark).equalTo(record::getRemark)
            .set(version).equalTo(record::getVersion)
            .set(originId).equalTo(record::getOriginId)
            .set(folderId).equalTo(record::getFolderId)
            .set(rules).equalTo(record::getRules)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_object_label")
    default int updateByPrimaryKeySelective(LabObjectLabel record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(name).equalToWhenPresent(record::getName)
            .set(nameEn).equalToWhenPresent(record::getNameEn)
            .set(objectType).equalToWhenPresent(record::getObjectType)
            .set(remark).equalToWhenPresent(record::getRemark)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(originId).equalToWhenPresent(record::getOriginId)
            .set(folderId).equalToWhenPresent(record::getFolderId)
            .set(rules).equalToWhenPresent(record::getRules)
            .where(id, isEqualTo(record::getId))
        );
    }
}