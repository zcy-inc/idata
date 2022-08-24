package cn.zhengcaiyun.idata.develop.dal.dao.job.instance;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.instance.StreamJobFlinkInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.develop.dal.model.job.instance.StreamJobFlinkInfo;
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
public interface StreamJobFlinkInfoDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, createTime, editTime, jobId, environment, secondaryId, flinkJobId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<StreamJobFlinkInfo> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="StreamJobFlinkInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="secondary_id", property="secondaryId", jdbcType=JdbcType.VARCHAR),
        @Result(column="flink_job_id", property="flinkJobId", jdbcType=JdbcType.VARCHAR)
    })
    List<StreamJobFlinkInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("StreamJobFlinkInfoResult")
    Optional<StreamJobFlinkInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int insert(StreamJobFlinkInfo row) {
        return MyBatis3Utils.insert(this::insert, row, STREAM_JOB_FLINK_INFO, c ->
            c.map(del).toProperty("del")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(environment).toProperty("environment")
            .map(secondaryId).toProperty("secondaryId")
            .map(flinkJobId).toProperty("flinkJobId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int insertSelective(StreamJobFlinkInfo row) {
        return MyBatis3Utils.insert(this::insert, row, STREAM_JOB_FLINK_INFO, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", row::getJobId)
            .map(environment).toPropertyWhenPresent("environment", row::getEnvironment)
            .map(secondaryId).toPropertyWhenPresent("secondaryId", row::getSecondaryId)
            .map(flinkJobId).toPropertyWhenPresent("flinkJobId", row::getFlinkJobId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default Optional<StreamJobFlinkInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default List<StreamJobFlinkInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default List<StreamJobFlinkInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default Optional<StreamJobFlinkInfo> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, STREAM_JOB_FLINK_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    static UpdateDSL<UpdateModel> updateAllColumns(StreamJobFlinkInfo row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editTime).equalTo(row::getEditTime)
                .set(jobId).equalTo(row::getJobId)
                .set(environment).equalTo(row::getEnvironment)
                .set(secondaryId).equalTo(row::getSecondaryId)
                .set(flinkJobId).equalTo(row::getFlinkJobId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(StreamJobFlinkInfo row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(jobId).equalToWhenPresent(row::getJobId)
                .set(environment).equalToWhenPresent(row::getEnvironment)
                .set(secondaryId).equalToWhenPresent(row::getSecondaryId)
                .set(flinkJobId).equalToWhenPresent(row::getFlinkJobId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int updateByPrimaryKey(StreamJobFlinkInfo row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editTime).equalTo(row::getEditTime)
            .set(jobId).equalTo(row::getJobId)
            .set(environment).equalTo(row::getEnvironment)
            .set(secondaryId).equalTo(row::getSecondaryId)
            .set(flinkJobId).equalTo(row::getFlinkJobId)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_stream_job_flink_info")
    default int updateByPrimaryKeySelective(StreamJobFlinkInfo row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(jobId).equalToWhenPresent(row::getJobId)
            .set(environment).equalToWhenPresent(row::getEnvironment)
            .set(secondaryId).equalToWhenPresent(row::getSecondaryId)
            .set(flinkJobId).equalToWhenPresent(row::getFlinkJobId)
            .where(id, isEqualTo(row::getId))
        );
    }
}