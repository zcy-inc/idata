package cn.zhengcaiyun.idata.develop.dal.dao.opt.stream;

import static cn.zhengcaiyun.idata.develop.dal.dao.opt.stream.StreamJobInstanceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface StreamJobInstanceDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, jobName, jobContentId, jobContentVersion, jobTypeCode, dwLayerCode, owner, environment, status, runStartTime, externalUrl, runParams);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<StreamJobInstance> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="StreamJobInstanceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_name", property="jobName", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_content_id", property="jobContentId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_content_version", property="jobContentVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="job_type_code", property="jobTypeCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="dw_layer_code", property="dwLayerCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="owner", property="owner", jdbcType=JdbcType.VARCHAR),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="run_start_time", property="runStartTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="external_url", property="externalUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="run_params", property="runParams", jdbcType=JdbcType.VARCHAR)
    })
    List<StreamJobInstance> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("StreamJobInstanceResult")
    Optional<StreamJobInstance> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int insert(StreamJobInstance row) {
        return MyBatis3Utils.insert(this::insert, row, STREAM_JOB_INSTANCE, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(jobName).toProperty("jobName")
            .map(jobContentId).toProperty("jobContentId")
            .map(jobContentVersion).toProperty("jobContentVersion")
            .map(jobTypeCode).toProperty("jobTypeCode")
            .map(dwLayerCode).toProperty("dwLayerCode")
            .map(owner).toProperty("owner")
            .map(environment).toProperty("environment")
            .map(status).toProperty("status")
            .map(runStartTime).toProperty("runStartTime")
            .map(externalUrl).toProperty("externalUrl")
            .map(runParams).toProperty("runParams")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int insertSelective(StreamJobInstance row) {
        return MyBatis3Utils.insert(this::insert, row, STREAM_JOB_INSTANCE, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", row::getJobId)
            .map(jobName).toPropertyWhenPresent("jobName", row::getJobName)
            .map(jobContentId).toPropertyWhenPresent("jobContentId", row::getJobContentId)
            .map(jobContentVersion).toPropertyWhenPresent("jobContentVersion", row::getJobContentVersion)
            .map(jobTypeCode).toPropertyWhenPresent("jobTypeCode", row::getJobTypeCode)
            .map(dwLayerCode).toPropertyWhenPresent("dwLayerCode", row::getDwLayerCode)
            .map(owner).toPropertyWhenPresent("owner", row::getOwner)
            .map(environment).toPropertyWhenPresent("environment", row::getEnvironment)
            .map(status).toPropertyWhenPresent("status", row::getStatus)
            .map(runStartTime).toPropertyWhenPresent("runStartTime", row::getRunStartTime)
            .map(externalUrl).toPropertyWhenPresent("externalUrl", row::getExternalUrl)
            .map(runParams).toPropertyWhenPresent("runParams", row::getRunParams)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default Optional<StreamJobInstance> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default List<StreamJobInstance> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default List<StreamJobInstance> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default Optional<StreamJobInstance> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, STREAM_JOB_INSTANCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    static UpdateDSL<UpdateModel> updateAllColumns(StreamJobInstance row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(jobId).equalTo(row::getJobId)
                .set(jobName).equalTo(row::getJobName)
                .set(jobContentId).equalTo(row::getJobContentId)
                .set(jobContentVersion).equalTo(row::getJobContentVersion)
                .set(jobTypeCode).equalTo(row::getJobTypeCode)
                .set(dwLayerCode).equalTo(row::getDwLayerCode)
                .set(owner).equalTo(row::getOwner)
                .set(environment).equalTo(row::getEnvironment)
                .set(status).equalTo(row::getStatus)
                .set(runStartTime).equalTo(row::getRunStartTime)
                .set(externalUrl).equalTo(row::getExternalUrl)
                .set(runParams).equalTo(row::getRunParams);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(StreamJobInstance row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(jobId).equalToWhenPresent(row::getJobId)
                .set(jobName).equalToWhenPresent(row::getJobName)
                .set(jobContentId).equalToWhenPresent(row::getJobContentId)
                .set(jobContentVersion).equalToWhenPresent(row::getJobContentVersion)
                .set(jobTypeCode).equalToWhenPresent(row::getJobTypeCode)
                .set(dwLayerCode).equalToWhenPresent(row::getDwLayerCode)
                .set(owner).equalToWhenPresent(row::getOwner)
                .set(environment).equalToWhenPresent(row::getEnvironment)
                .set(status).equalToWhenPresent(row::getStatus)
                .set(runStartTime).equalToWhenPresent(row::getRunStartTime)
                .set(externalUrl).equalToWhenPresent(row::getExternalUrl)
                .set(runParams).equalToWhenPresent(row::getRunParams);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int updateByPrimaryKey(StreamJobInstance row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(jobId).equalTo(row::getJobId)
            .set(jobName).equalTo(row::getJobName)
            .set(jobContentId).equalTo(row::getJobContentId)
            .set(jobContentVersion).equalTo(row::getJobContentVersion)
            .set(jobTypeCode).equalTo(row::getJobTypeCode)
            .set(dwLayerCode).equalTo(row::getDwLayerCode)
            .set(owner).equalTo(row::getOwner)
            .set(environment).equalTo(row::getEnvironment)
            .set(status).equalTo(row::getStatus)
            .set(runStartTime).equalTo(row::getRunStartTime)
            .set(externalUrl).equalTo(row::getExternalUrl)
            .set(runParams).equalTo(row::getRunParams)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_instance")
    default int updateByPrimaryKeySelective(StreamJobInstance row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(jobId).equalToWhenPresent(row::getJobId)
            .set(jobName).equalToWhenPresent(row::getJobName)
            .set(jobContentId).equalToWhenPresent(row::getJobContentId)
            .set(jobContentVersion).equalToWhenPresent(row::getJobContentVersion)
            .set(jobTypeCode).equalToWhenPresent(row::getJobTypeCode)
            .set(dwLayerCode).equalToWhenPresent(row::getDwLayerCode)
            .set(owner).equalToWhenPresent(row::getOwner)
            .set(environment).equalToWhenPresent(row::getEnvironment)
            .set(status).equalToWhenPresent(row::getStatus)
            .set(runStartTime).equalToWhenPresent(row::getRunStartTime)
            .set(externalUrl).equalToWhenPresent(row::getExternalUrl)
            .set(runParams).equalToWhenPresent(row::getRunParams)
            .where(id, isEqualTo(row::getId))
        );
    }
}