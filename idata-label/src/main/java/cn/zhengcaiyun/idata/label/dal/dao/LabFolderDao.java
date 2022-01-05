package cn.zhengcaiyun.idata.label.dal.dao;

import static cn.zhengcaiyun.idata.label.dal.dao.LabFolderDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.label.dal.model.LabFolder;
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
public interface LabFolderDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, name, parentId, belong);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<LabFolder> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("LabFolderResult")
    Optional<LabFolder> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="LabFolderResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
        @Result(column="belong", property="belong", jdbcType=JdbcType.VARCHAR)
    })
    List<LabFolder> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int insert(LabFolder record) {
        return MyBatis3Utils.insert(this::insert, record, labFolder, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(name).toProperty("name")
            .map(parentId).toProperty("parentId")
            .map(belong).toProperty("belong")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int insertSelective(LabFolder record) {
        return MyBatis3Utils.insert(this::insert, record, labFolder, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(parentId).toPropertyWhenPresent("parentId", record::getParentId)
            .map(belong).toPropertyWhenPresent("belong", record::getBelong)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default Optional<LabFolder> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default List<LabFolder> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default List<LabFolder> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default Optional<LabFolder> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, labFolder, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    static UpdateDSL<UpdateModel> updateAllColumns(LabFolder record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(name).equalTo(record::getName)
                .set(parentId).equalTo(record::getParentId)
                .set(belong).equalTo(record::getBelong);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(LabFolder record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(name).equalToWhenPresent(record::getName)
                .set(parentId).equalToWhenPresent(record::getParentId)
                .set(belong).equalToWhenPresent(record::getBelong);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int updateByPrimaryKey(LabFolder record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(name).equalTo(record::getName)
            .set(parentId).equalTo(record::getParentId)
            .set(belong).equalTo(record::getBelong)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: lab_folder")
    default int updateByPrimaryKeySelective(LabFolder record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(name).equalToWhenPresent(record::getName)
            .set(parentId).equalToWhenPresent(record::getParentId)
            .set(belong).equalToWhenPresent(record::getBelong)
            .where(id, isEqualTo(record::getId))
        );
    }
}