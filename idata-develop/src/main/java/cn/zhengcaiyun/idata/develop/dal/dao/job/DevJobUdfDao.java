package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
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
public interface DevJobUdfDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, editor, createTime, editTime, udfName, udfType, fileName, hdfsPath, returnType, returnSample, folderId, description, commandFormat, udfSample);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobUdf> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobUdfResult")
    Optional<DevJobUdf> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobUdfResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.BIT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="udf_name", property="udfName", jdbcType=JdbcType.VARCHAR),
        @Result(column="udf_type", property="udfType", jdbcType=JdbcType.VARCHAR),
        @Result(column="file_name", property="fileName", jdbcType=JdbcType.VARCHAR),
        @Result(column="hdfs_path", property="hdfsPath", jdbcType=JdbcType.VARCHAR),
        @Result(column="return_type", property="returnType", jdbcType=JdbcType.VARCHAR),
        @Result(column="return_sample", property="returnSample", jdbcType=JdbcType.VARCHAR),
        @Result(column="folder_id", property="folderId", jdbcType=JdbcType.BIGINT),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="command_format", property="commandFormat", jdbcType=JdbcType.VARCHAR),
        @Result(column="udf_sample", property="udfSample", jdbcType=JdbcType.VARCHAR)
    })
    List<DevJobUdf> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int insert(DevJobUdf record) {
        return MyBatis3Utils.insert(this::insert, record, devJobUdf, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(editor).toProperty("editor")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(udfName).toProperty("udfName")
            .map(udfType).toProperty("udfType")
            .map(fileName).toProperty("fileName")
            .map(hdfsPath).toProperty("hdfsPath")
            .map(returnType).toProperty("returnType")
            .map(returnSample).toProperty("returnSample")
            .map(folderId).toProperty("folderId")
            .map(description).toProperty("description")
            .map(commandFormat).toProperty("commandFormat")
            .map(udfSample).toProperty("udfSample")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int insertSelective(DevJobUdf record) {
        return MyBatis3Utils.insert(this::insert, record, devJobUdf, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(udfName).toPropertyWhenPresent("udfName", record::getUdfName)
            .map(udfType).toPropertyWhenPresent("udfType", record::getUdfType)
            .map(fileName).toPropertyWhenPresent("fileName", record::getFileName)
            .map(hdfsPath).toPropertyWhenPresent("hdfsPath", record::getHdfsPath)
            .map(returnType).toPropertyWhenPresent("returnType", record::getReturnType)
            .map(returnSample).toPropertyWhenPresent("returnSample", record::getReturnSample)
            .map(folderId).toPropertyWhenPresent("folderId", record::getFolderId)
            .map(description).toPropertyWhenPresent("description", record::getDescription)
            .map(commandFormat).toPropertyWhenPresent("commandFormat", record::getCommandFormat)
            .map(udfSample).toPropertyWhenPresent("udfSample", record::getUdfSample)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default Optional<DevJobUdf> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default List<DevJobUdf> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default List<DevJobUdf> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default Optional<DevJobUdf> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobUdf, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobUdf record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(editor).equalTo(record::getEditor)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(udfName).equalTo(record::getUdfName)
                .set(udfType).equalTo(record::getUdfType)
                .set(fileName).equalTo(record::getFileName)
                .set(hdfsPath).equalTo(record::getHdfsPath)
                .set(returnType).equalTo(record::getReturnType)
                .set(returnSample).equalTo(record::getReturnSample)
                .set(folderId).equalTo(record::getFolderId)
                .set(description).equalTo(record::getDescription)
                .set(commandFormat).equalTo(record::getCommandFormat)
                .set(udfSample).equalTo(record::getUdfSample);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobUdf record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(udfName).equalToWhenPresent(record::getUdfName)
                .set(udfType).equalToWhenPresent(record::getUdfType)
                .set(fileName).equalToWhenPresent(record::getFileName)
                .set(hdfsPath).equalToWhenPresent(record::getHdfsPath)
                .set(returnType).equalToWhenPresent(record::getReturnType)
                .set(returnSample).equalToWhenPresent(record::getReturnSample)
                .set(folderId).equalToWhenPresent(record::getFolderId)
                .set(description).equalToWhenPresent(record::getDescription)
                .set(commandFormat).equalToWhenPresent(record::getCommandFormat)
                .set(udfSample).equalToWhenPresent(record::getUdfSample);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int updateByPrimaryKey(DevJobUdf record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(editor).equalTo(record::getEditor)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editTime).equalTo(record::getEditTime)
            .set(udfName).equalTo(record::getUdfName)
            .set(udfType).equalTo(record::getUdfType)
            .set(fileName).equalTo(record::getFileName)
            .set(hdfsPath).equalTo(record::getHdfsPath)
            .set(returnType).equalTo(record::getReturnType)
            .set(returnSample).equalTo(record::getReturnSample)
            .set(folderId).equalTo(record::getFolderId)
            .set(description).equalTo(record::getDescription)
            .set(commandFormat).equalTo(record::getCommandFormat)
            .set(udfSample).equalTo(record::getUdfSample)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_udf")
    default int updateByPrimaryKeySelective(DevJobUdf record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(udfName).equalToWhenPresent(record::getUdfName)
            .set(udfType).equalToWhenPresent(record::getUdfType)
            .set(fileName).equalToWhenPresent(record::getFileName)
            .set(hdfsPath).equalToWhenPresent(record::getHdfsPath)
            .set(returnType).equalToWhenPresent(record::getReturnType)
            .set(returnSample).equalToWhenPresent(record::getReturnSample)
            .set(folderId).equalToWhenPresent(record::getFolderId)
            .set(description).equalToWhenPresent(record::getDescription)
            .set(commandFormat).equalToWhenPresent(record::getCommandFormat)
            .set(udfSample).equalToWhenPresent(record::getUdfSample)
            .where(id, isEqualTo(record::getId))
        );
    }
}