package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
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
public interface DevJobHistoryDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    BasicColumn[] selectList = BasicColumn.columnList(id, createTime, jobId, startTime, finishTime, duration, finalStatus, avgVcores, avgMemory, applicationId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobHistory> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobHistoryResult")
    Optional<DevJobHistory> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobHistoryResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="finish_time", property="finishTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="duration", property="duration", jdbcType=JdbcType.BIGINT),
        @Result(column="final_status", property="finalStatus", jdbcType=JdbcType.VARCHAR),
        @Result(column="avg_vcores", property="avgVcores", jdbcType=JdbcType.DOUBLE),
        @Result(column="avg_memory", property="avgMemory", jdbcType=JdbcType.BIGINT),
        @Result(column="application_id", property="applicationId", jdbcType=JdbcType.VARCHAR)
    })
    List<DevJobHistory> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int insert(DevJobHistory record) {
        return MyBatis3Utils.insert(this::insert, record, devJobHistory, c ->
            c.map(createTime).toProperty("createTime")
            .map(jobId).toProperty("jobId")
            .map(startTime).toProperty("startTime")
            .map(finishTime).toProperty("finishTime")
            .map(duration).toProperty("duration")
            .map(finalStatus).toProperty("finalStatus")
            .map(avgVcores).toProperty("avgVcores")
            .map(avgMemory).toProperty("avgMemory")
            .map(applicationId).toProperty("applicationId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int insertSelective(DevJobHistory record) {
        return MyBatis3Utils.insert(this::insert, record, devJobHistory, c ->
            c.map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(startTime).toPropertyWhenPresent("startTime", record::getStartTime)
            .map(finishTime).toPropertyWhenPresent("finishTime", record::getFinishTime)
            .map(duration).toPropertyWhenPresent("duration", record::getDuration)
            .map(finalStatus).toPropertyWhenPresent("finalStatus", record::getFinalStatus)
            .map(avgVcores).toPropertyWhenPresent("avgVcores", record::getAvgVcores)
            .map(avgMemory).toPropertyWhenPresent("avgMemory", record::getAvgMemory)
            .map(applicationId).toPropertyWhenPresent("applicationId", record::getApplicationId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default Optional<DevJobHistory> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default List<DevJobHistory> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default List<DevJobHistory> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default Optional<DevJobHistory> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobHistory, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobHistory record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalTo(record::getCreateTime)
                .set(jobId).equalTo(record::getJobId)
                .set(startTime).equalTo(record::getStartTime)
                .set(finishTime).equalTo(record::getFinishTime)
                .set(duration).equalTo(record::getDuration)
                .set(finalStatus).equalTo(record::getFinalStatus)
                .set(avgVcores).equalTo(record::getAvgVcores)
                .set(avgMemory).equalTo(record::getAvgMemory)
                .set(applicationId).equalTo(record::getApplicationId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobHistory record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(startTime).equalToWhenPresent(record::getStartTime)
                .set(finishTime).equalToWhenPresent(record::getFinishTime)
                .set(duration).equalToWhenPresent(record::getDuration)
                .set(finalStatus).equalToWhenPresent(record::getFinalStatus)
                .set(avgVcores).equalToWhenPresent(record::getAvgVcores)
                .set(avgMemory).equalToWhenPresent(record::getAvgMemory)
                .set(applicationId).equalToWhenPresent(record::getApplicationId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int updateByPrimaryKey(DevJobHistory record) {
        return update(c ->
            c.set(createTime).equalTo(record::getCreateTime)
            .set(jobId).equalTo(record::getJobId)
            .set(startTime).equalTo(record::getStartTime)
            .set(finishTime).equalTo(record::getFinishTime)
            .set(duration).equalTo(record::getDuration)
            .set(finalStatus).equalTo(record::getFinalStatus)
            .set(avgVcores).equalTo(record::getAvgVcores)
            .set(avgMemory).equalTo(record::getAvgMemory)
            .set(applicationId).equalTo(record::getApplicationId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_history")
    default int updateByPrimaryKeySelective(DevJobHistory record) {
        return update(c ->
            c.set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(startTime).equalToWhenPresent(record::getStartTime)
            .set(finishTime).equalToWhenPresent(record::getFinishTime)
            .set(duration).equalToWhenPresent(record::getDuration)
            .set(finalStatus).equalToWhenPresent(record::getFinalStatus)
            .set(avgVcores).equalToWhenPresent(record::getAvgVcores)
            .set(avgMemory).equalToWhenPresent(record::getAvgMemory)
            .set(applicationId).equalToWhenPresent(record::getApplicationId)
            .where(id, isEqualTo(record::getId))
        );
    }
}