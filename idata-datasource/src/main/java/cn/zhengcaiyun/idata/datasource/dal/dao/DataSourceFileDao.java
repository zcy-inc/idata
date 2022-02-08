package cn.zhengcaiyun.idata.datasource.dal.dao;

import static cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceFileDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.datasource.dal.model.DataSourceFile;
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
public interface DataSourceFileDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, type, name, environments, remark, fileName);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DataSourceFile> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DataSourceFileResult")
    Optional<DataSourceFile> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DataSourceFileResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="environments", property="environments", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="file_name", property="fileName", jdbcType=JdbcType.VARCHAR)
    })
    List<DataSourceFile> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int insert(DataSourceFile record) {
        return MyBatis3Utils.insert(this::insert, record, dataSourceFile, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(type).toProperty("type")
            .map(name).toProperty("name")
            .map(environments).toProperty("environments")
            .map(remark).toProperty("remark")
            .map(fileName).toProperty("fileName")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int insertSelective(DataSourceFile record) {
        return MyBatis3Utils.insert(this::insert, record, dataSourceFile, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(type).toPropertyWhenPresent("type", record::getType)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(environments).toPropertyWhenPresent("environments", record::getEnvironments)
            .map(remark).toPropertyWhenPresent("remark", record::getRemark)
            .map(fileName).toPropertyWhenPresent("fileName", record::getFileName)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default Optional<DataSourceFile> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default List<DataSourceFile> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default List<DataSourceFile> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default Optional<DataSourceFile> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, dataSourceFile, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    static UpdateDSL<UpdateModel> updateAllColumns(DataSourceFile record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(type).equalTo(record::getType)
                .set(name).equalTo(record::getName)
                .set(environments).equalTo(record::getEnvironments)
                .set(remark).equalTo(record::getRemark)
                .set(fileName).equalTo(record::getFileName);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DataSourceFile record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(type).equalToWhenPresent(record::getType)
                .set(name).equalToWhenPresent(record::getName)
                .set(environments).equalToWhenPresent(record::getEnvironments)
                .set(remark).equalToWhenPresent(record::getRemark)
                .set(fileName).equalToWhenPresent(record::getFileName);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int updateByPrimaryKey(DataSourceFile record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(type).equalTo(record::getType)
            .set(name).equalTo(record::getName)
            .set(environments).equalTo(record::getEnvironments)
            .set(remark).equalTo(record::getRemark)
            .set(fileName).equalTo(record::getFileName)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: das_data_source_file")
    default int updateByPrimaryKeySelective(DataSourceFile record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(type).equalToWhenPresent(record::getType)
            .set(name).equalToWhenPresent(record::getName)
            .set(environments).equalToWhenPresent(record::getEnvironments)
            .set(remark).equalToWhenPresent(record::getRemark)
            .set(fileName).equalToWhenPresent(record::getFileName)
            .where(id, isEqualTo(record::getId))
        );
    }
}