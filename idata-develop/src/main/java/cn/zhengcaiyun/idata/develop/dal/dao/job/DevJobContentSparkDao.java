package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSparkDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.JsonColumnHandler;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSpark;
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
public interface DevJobContentSparkDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, editable, version, resourceHdfsPath, appArguments, mainClass);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobContentSpark> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobContentSparkResult")
    Optional<DevJobContentSpark> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobContentSparkResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="editable", property="editable", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="resource_hdfs_path", property="resourceHdfsPath", jdbcType=JdbcType.VARCHAR),
        @Result(column="app_arguments", property="appArguments", typeHandler=JsonColumnHandler.class, jdbcType=JdbcType.VARCHAR),
        @Result(column="main_class", property="mainClass", jdbcType=JdbcType.VARCHAR)
    })
    List<DevJobContentSpark> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int insert(DevJobContentSpark record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentSpark, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(editable).toProperty("editable")
            .map(version).toProperty("version")
            .map(resourceHdfsPath).toProperty("resourceHdfsPath")
            .map(appArguments).toProperty("appArguments")
            .map(mainClass).toProperty("mainClass")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int insertSelective(DevJobContentSpark record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentSpark, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(editable).toPropertyWhenPresent("editable", record::getEditable)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(resourceHdfsPath).toPropertyWhenPresent("resourceHdfsPath", record::getResourceHdfsPath)
            .map(appArguments).toPropertyWhenPresent("appArguments", record::getAppArguments)
            .map(mainClass).toPropertyWhenPresent("mainClass", record::getMainClass)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default Optional<DevJobContentSpark> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default List<DevJobContentSpark> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default List<DevJobContentSpark> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default Optional<DevJobContentSpark> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobContentSpark, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobContentSpark record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(editable).equalTo(record::getEditable)
                .set(version).equalTo(record::getVersion)
                .set(resourceHdfsPath).equalTo(record::getResourceHdfsPath)
                .set(appArguments).equalTo(record::getAppArguments)
                .set(mainClass).equalTo(record::getMainClass);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobContentSpark record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(editable).equalToWhenPresent(record::getEditable)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(resourceHdfsPath).equalToWhenPresent(record::getResourceHdfsPath)
                .set(appArguments).equalToWhenPresent(record::getAppArguments)
                .set(mainClass).equalToWhenPresent(record::getMainClass);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int updateByPrimaryKey(DevJobContentSpark record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(editable).equalTo(record::getEditable)
            .set(version).equalTo(record::getVersion)
            .set(resourceHdfsPath).equalTo(record::getResourceHdfsPath)
            .set(appArguments).equalTo(record::getAppArguments)
            .set(mainClass).equalTo(record::getMainClass)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_spark")
    default int updateByPrimaryKeySelective(DevJobContentSpark record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(editable).equalToWhenPresent(record::getEditable)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(resourceHdfsPath).equalToWhenPresent(record::getResourceHdfsPath)
            .set(appArguments).equalToWhenPresent(record::getAppArguments)
            .set(mainClass).equalToWhenPresent(record::getMainClass)
            .where(id, isEqualTo(record::getId))
        );
    }
}