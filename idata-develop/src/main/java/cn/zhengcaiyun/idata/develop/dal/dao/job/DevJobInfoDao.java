package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobInfo;
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
public interface DevJobInfoDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, name, jobType, jobLanguage, jobExecutionEngine, dwLayerCode, status, remark, folderId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobInfo> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobInfoResult")
    Optional<DevJobInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_type", property="jobType", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_language", property="jobLanguage", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_execution_engine", property="jobExecutionEngine", jdbcType=JdbcType.VARCHAR),
        @Result(column="dw_layer_code", property="dwLayerCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="folder_id", property="folderId", jdbcType=JdbcType.BIGINT)
    })
    List<DevJobInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int insert(DevJobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, devJobInfo, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(name).toProperty("name")
            .map(jobType).toProperty("jobType")
            .map(jobLanguage).toProperty("jobLanguage")
            .map(jobExecutionEngine).toProperty("jobExecutionEngine")
            .map(dwLayerCode).toProperty("dwLayerCode")
            .map(status).toProperty("status")
            .map(remark).toProperty("remark")
            .map(folderId).toProperty("folderId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int insertSelective(DevJobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, devJobInfo, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(jobType).toPropertyWhenPresent("jobType", record::getJobType)
            .map(jobLanguage).toPropertyWhenPresent("jobLanguage", record::getJobLanguage)
            .map(jobExecutionEngine).toPropertyWhenPresent("jobExecutionEngine", record::getJobExecutionEngine)
            .map(dwLayerCode).toPropertyWhenPresent("dwLayerCode", record::getDwLayerCode)
            .map(status).toPropertyWhenPresent("status", record::getStatus)
            .map(remark).toPropertyWhenPresent("remark", record::getRemark)
            .map(folderId).toPropertyWhenPresent("folderId", record::getFolderId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default Optional<DevJobInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default List<DevJobInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default List<DevJobInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default Optional<DevJobInfo> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobInfo, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(name).equalTo(record::getName)
                .set(jobType).equalTo(record::getJobType)
                .set(jobLanguage).equalTo(record::getJobLanguage)
                .set(jobExecutionEngine).equalTo(record::getJobExecutionEngine)
                .set(dwLayerCode).equalTo(record::getDwLayerCode)
                .set(status).equalTo(record::getStatus)
                .set(remark).equalTo(record::getRemark)
                .set(folderId).equalTo(record::getFolderId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(name).equalToWhenPresent(record::getName)
                .set(jobType).equalToWhenPresent(record::getJobType)
                .set(jobLanguage).equalToWhenPresent(record::getJobLanguage)
                .set(jobExecutionEngine).equalToWhenPresent(record::getJobExecutionEngine)
                .set(dwLayerCode).equalToWhenPresent(record::getDwLayerCode)
                .set(status).equalToWhenPresent(record::getStatus)
                .set(remark).equalToWhenPresent(record::getRemark)
                .set(folderId).equalToWhenPresent(record::getFolderId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int updateByPrimaryKey(DevJobInfo record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(name).equalTo(record::getName)
            .set(jobType).equalTo(record::getJobType)
            .set(jobLanguage).equalTo(record::getJobLanguage)
            .set(jobExecutionEngine).equalTo(record::getJobExecutionEngine)
            .set(dwLayerCode).equalTo(record::getDwLayerCode)
            .set(status).equalTo(record::getStatus)
            .set(remark).equalTo(record::getRemark)
            .set(folderId).equalTo(record::getFolderId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_info")
    default int updateByPrimaryKeySelective(DevJobInfo record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(name).equalToWhenPresent(record::getName)
            .set(jobType).equalToWhenPresent(record::getJobType)
            .set(jobLanguage).equalToWhenPresent(record::getJobLanguage)
            .set(jobExecutionEngine).equalToWhenPresent(record::getJobExecutionEngine)
            .set(dwLayerCode).equalToWhenPresent(record::getDwLayerCode)
            .set(status).equalToWhenPresent(record::getStatus)
            .set(remark).equalToWhenPresent(record::getRemark)
            .set(folderId).equalToWhenPresent(record::getFolderId)
            .where(id, isEqualTo(record::getId))
        );
    }
}