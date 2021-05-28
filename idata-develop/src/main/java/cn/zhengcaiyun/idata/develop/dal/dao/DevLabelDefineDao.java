package cn.zhengcaiyun.idata.develop.dal.dao;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
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
public interface DevLabelDefineDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, labelCode, labelName, labelTag, labelParamType, labelAttributes, specialAttributes, subjectType, labelIndex, labelRequired, labelScope, folderId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevLabelDefine> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevLabelDefineResult")
    Optional<DevLabelDefine> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevLabelDefineResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="label_code", property="labelCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_name", property="labelName", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_tag", property="labelTag", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_param_type", property="labelParamType", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_attributes", property="labelAttributes", jdbcType=JdbcType.VARCHAR),
        @Result(column="special_attributes", property="specialAttributes", jdbcType=JdbcType.VARCHAR),
        @Result(column="subject_type", property="subjectType", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_index", property="labelIndex", jdbcType=JdbcType.INTEGER),
        @Result(column="label_required", property="labelRequired", jdbcType=JdbcType.BIT),
        @Result(column="label_scope", property="labelScope", jdbcType=JdbcType.BIGINT),
        @Result(column="folder_id", property="folderId", jdbcType=JdbcType.BIGINT)
    })
    List<DevLabelDefine> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int insert(DevLabelDefine record) {
        return MyBatis3Utils.insert(this::insert, record, devLabelDefine, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(labelCode).toProperty("labelCode")
            .map(labelName).toProperty("labelName")
            .map(labelTag).toProperty("labelTag")
            .map(labelParamType).toProperty("labelParamType")
            .map(labelAttributes).toProperty("labelAttributes")
            .map(specialAttributes).toProperty("specialAttributes")
            .map(subjectType).toProperty("subjectType")
            .map(labelIndex).toProperty("labelIndex")
            .map(labelRequired).toProperty("labelRequired")
            .map(labelScope).toProperty("labelScope")
            .map(folderId).toProperty("folderId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int insertSelective(DevLabelDefine record) {
        return MyBatis3Utils.insert(this::insert, record, devLabelDefine, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(labelCode).toPropertyWhenPresent("labelCode", record::getLabelCode)
            .map(labelName).toPropertyWhenPresent("labelName", record::getLabelName)
            .map(labelTag).toPropertyWhenPresent("labelTag", record::getLabelTag)
            .map(labelParamType).toPropertyWhenPresent("labelParamType", record::getLabelParamType)
            .map(labelAttributes).toPropertyWhenPresent("labelAttributes", record::getLabelAttributes)
            .map(specialAttributes).toPropertyWhenPresent("specialAttributes", record::getSpecialAttributes)
            .map(subjectType).toPropertyWhenPresent("subjectType", record::getSubjectType)
            .map(labelIndex).toPropertyWhenPresent("labelIndex", record::getLabelIndex)
            .map(labelRequired).toPropertyWhenPresent("labelRequired", record::getLabelRequired)
            .map(labelScope).toPropertyWhenPresent("labelScope", record::getLabelScope)
            .map(folderId).toPropertyWhenPresent("folderId", record::getFolderId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default Optional<DevLabelDefine> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default List<DevLabelDefine> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default List<DevLabelDefine> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default Optional<DevLabelDefine> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devLabelDefine, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    static UpdateDSL<UpdateModel> updateAllColumns(DevLabelDefine record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(labelCode).equalTo(record::getLabelCode)
                .set(labelName).equalTo(record::getLabelName)
                .set(labelTag).equalTo(record::getLabelTag)
                .set(labelParamType).equalTo(record::getLabelParamType)
                .set(labelAttributes).equalTo(record::getLabelAttributes)
                .set(specialAttributes).equalTo(record::getSpecialAttributes)
                .set(subjectType).equalTo(record::getSubjectType)
                .set(labelIndex).equalTo(record::getLabelIndex)
                .set(labelRequired).equalTo(record::getLabelRequired)
                .set(labelScope).equalTo(record::getLabelScope)
                .set(folderId).equalTo(record::getFolderId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevLabelDefine record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(labelCode).equalToWhenPresent(record::getLabelCode)
                .set(labelName).equalToWhenPresent(record::getLabelName)
                .set(labelTag).equalToWhenPresent(record::getLabelTag)
                .set(labelParamType).equalToWhenPresent(record::getLabelParamType)
                .set(labelAttributes).equalToWhenPresent(record::getLabelAttributes)
                .set(specialAttributes).equalToWhenPresent(record::getSpecialAttributes)
                .set(subjectType).equalToWhenPresent(record::getSubjectType)
                .set(labelIndex).equalToWhenPresent(record::getLabelIndex)
                .set(labelRequired).equalToWhenPresent(record::getLabelRequired)
                .set(labelScope).equalToWhenPresent(record::getLabelScope)
                .set(folderId).equalToWhenPresent(record::getFolderId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int updateByPrimaryKey(DevLabelDefine record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(labelCode).equalTo(record::getLabelCode)
            .set(labelName).equalTo(record::getLabelName)
            .set(labelTag).equalTo(record::getLabelTag)
            .set(labelParamType).equalTo(record::getLabelParamType)
            .set(labelAttributes).equalTo(record::getLabelAttributes)
            .set(specialAttributes).equalTo(record::getSpecialAttributes)
            .set(subjectType).equalTo(record::getSubjectType)
            .set(labelIndex).equalTo(record::getLabelIndex)
            .set(labelRequired).equalTo(record::getLabelRequired)
            .set(labelScope).equalTo(record::getLabelScope)
            .set(folderId).equalTo(record::getFolderId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_label_define")
    default int updateByPrimaryKeySelective(DevLabelDefine record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(labelCode).equalToWhenPresent(record::getLabelCode)
            .set(labelName).equalToWhenPresent(record::getLabelName)
            .set(labelTag).equalToWhenPresent(record::getLabelTag)
            .set(labelParamType).equalToWhenPresent(record::getLabelParamType)
            .set(labelAttributes).equalToWhenPresent(record::getLabelAttributes)
            .set(specialAttributes).equalToWhenPresent(record::getSpecialAttributes)
            .set(subjectType).equalToWhenPresent(record::getSubjectType)
            .set(labelIndex).equalToWhenPresent(record::getLabelIndex)
            .set(labelRequired).equalToWhenPresent(record::getLabelRequired)
            .set(labelScope).equalToWhenPresent(record::getLabelScope)
            .set(folderId).equalToWhenPresent(record::getFolderId)
            .where(id, isEqualTo(record::getId))
        );
    }
}