package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
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
public interface JobExecuteConfigDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, environment, schDagId, schRerunMode, schTimeOut, schDryRun, execQueue, execWarnLevel, schTimeOutStrategy, schPriority, execDriverMem, execWorkerMem, execCores, runningState, execEngine, extProperties, isOpenMergeFile);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<JobExecuteConfig> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobExecuteConfigResult")
    Optional<JobExecuteConfig> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobExecuteConfigResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="sch_dag_id", property="schDagId", jdbcType=JdbcType.BIGINT),
        @Result(column="sch_rerun_mode", property="schRerunMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sch_time_out", property="schTimeOut", jdbcType=JdbcType.INTEGER),
        @Result(column="sch_dry_run", property="schDryRun", jdbcType=JdbcType.TINYINT),
        @Result(column="exec_queue", property="execQueue", jdbcType=JdbcType.VARCHAR),
        @Result(column="exec_warn_level", property="execWarnLevel", jdbcType=JdbcType.VARCHAR),
        @Result(column="sch_time_out_strategy", property="schTimeOutStrategy", jdbcType=JdbcType.VARCHAR),
        @Result(column="sch_priority", property="schPriority", jdbcType=JdbcType.INTEGER),
        @Result(column="exec_driver_mem", property="execDriverMem", jdbcType=JdbcType.INTEGER),
        @Result(column="exec_worker_mem", property="execWorkerMem", jdbcType=JdbcType.INTEGER),
        @Result(column="exec_cores", property="execCores", jdbcType=JdbcType.INTEGER),
        @Result(column="running_state", property="runningState", jdbcType=JdbcType.INTEGER),
        @Result(column="exec_engine", property="execEngine", jdbcType=JdbcType.VARCHAR),
        @Result(column="ext_properties", property="extProperties", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_open_merge_file", property="isOpenMergeFile", jdbcType=JdbcType.INTEGER)
    })
    List<JobExecuteConfig> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int insert(JobExecuteConfig record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_EXECUTE_CONFIG, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(environment).toProperty("environment")
            .map(schDagId).toProperty("schDagId")
            .map(schRerunMode).toProperty("schRerunMode")
            .map(schTimeOut).toProperty("schTimeOut")
            .map(schDryRun).toProperty("schDryRun")
            .map(execQueue).toProperty("execQueue")
            .map(execWarnLevel).toProperty("execWarnLevel")
            .map(schTimeOutStrategy).toProperty("schTimeOutStrategy")
            .map(schPriority).toProperty("schPriority")
            .map(execDriverMem).toProperty("execDriverMem")
            .map(execWorkerMem).toProperty("execWorkerMem")
            .map(execCores).toProperty("execCores")
            .map(runningState).toProperty("runningState")
            .map(execEngine).toProperty("execEngine")
            .map(extProperties).toProperty("extProperties")
            .map(isOpenMergeFile).toProperty("isOpenMergeFile")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int insertSelective(JobExecuteConfig record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_EXECUTE_CONFIG, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(environment).toPropertyWhenPresent("environment", record::getEnvironment)
            .map(schDagId).toPropertyWhenPresent("schDagId", record::getSchDagId)
            .map(schRerunMode).toPropertyWhenPresent("schRerunMode", record::getSchRerunMode)
            .map(schTimeOut).toPropertyWhenPresent("schTimeOut", record::getSchTimeOut)
            .map(schDryRun).toPropertyWhenPresent("schDryRun", record::getSchDryRun)
            .map(execQueue).toPropertyWhenPresent("execQueue", record::getExecQueue)
            .map(execWarnLevel).toPropertyWhenPresent("execWarnLevel", record::getExecWarnLevel)
            .map(schTimeOutStrategy).toPropertyWhenPresent("schTimeOutStrategy", record::getSchTimeOutStrategy)
            .map(schPriority).toPropertyWhenPresent("schPriority", record::getSchPriority)
            .map(execDriverMem).toPropertyWhenPresent("execDriverMem", record::getExecDriverMem)
            .map(execWorkerMem).toPropertyWhenPresent("execWorkerMem", record::getExecWorkerMem)
            .map(execCores).toPropertyWhenPresent("execCores", record::getExecCores)
            .map(runningState).toPropertyWhenPresent("runningState", record::getRunningState)
            .map(execEngine).toPropertyWhenPresent("execEngine", record::getExecEngine)
            .map(extProperties).toPropertyWhenPresent("extProperties", record::getExtProperties)
            .map(isOpenMergeFile).toPropertyWhenPresent("isOpenMergeFile", record::getIsOpenMergeFile)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default Optional<JobExecuteConfig> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default List<JobExecuteConfig> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default List<JobExecuteConfig> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default Optional<JobExecuteConfig> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, JOB_EXECUTE_CONFIG, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    static UpdateDSL<UpdateModel> updateAllColumns(JobExecuteConfig record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(environment).equalTo(record::getEnvironment)
                .set(schDagId).equalTo(record::getSchDagId)
                .set(schRerunMode).equalTo(record::getSchRerunMode)
                .set(schTimeOut).equalTo(record::getSchTimeOut)
                .set(schDryRun).equalTo(record::getSchDryRun)
                .set(execQueue).equalTo(record::getExecQueue)
                .set(execWarnLevel).equalTo(record::getExecWarnLevel)
                .set(schTimeOutStrategy).equalTo(record::getSchTimeOutStrategy)
                .set(schPriority).equalTo(record::getSchPriority)
                .set(execDriverMem).equalTo(record::getExecDriverMem)
                .set(execWorkerMem).equalTo(record::getExecWorkerMem)
                .set(execCores).equalTo(record::getExecCores)
                .set(runningState).equalTo(record::getRunningState)
                .set(execEngine).equalTo(record::getExecEngine)
                .set(extProperties).equalTo(record::getExtProperties)
                .set(isOpenMergeFile).equalTo(record::getIsOpenMergeFile);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobExecuteConfig record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(environment).equalToWhenPresent(record::getEnvironment)
                .set(schDagId).equalToWhenPresent(record::getSchDagId)
                .set(schRerunMode).equalToWhenPresent(record::getSchRerunMode)
                .set(schTimeOut).equalToWhenPresent(record::getSchTimeOut)
                .set(schDryRun).equalToWhenPresent(record::getSchDryRun)
                .set(execQueue).equalToWhenPresent(record::getExecQueue)
                .set(execWarnLevel).equalToWhenPresent(record::getExecWarnLevel)
                .set(schTimeOutStrategy).equalToWhenPresent(record::getSchTimeOutStrategy)
                .set(schPriority).equalToWhenPresent(record::getSchPriority)
                .set(execDriverMem).equalToWhenPresent(record::getExecDriverMem)
                .set(execWorkerMem).equalToWhenPresent(record::getExecWorkerMem)
                .set(execCores).equalToWhenPresent(record::getExecCores)
                .set(runningState).equalToWhenPresent(record::getRunningState)
                .set(execEngine).equalToWhenPresent(record::getExecEngine)
                .set(extProperties).equalToWhenPresent(record::getExtProperties)
                .set(isOpenMergeFile).equalToWhenPresent(record::getIsOpenMergeFile);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int updateByPrimaryKey(JobExecuteConfig record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(environment).equalTo(record::getEnvironment)
            .set(schDagId).equalTo(record::getSchDagId)
            .set(schRerunMode).equalTo(record::getSchRerunMode)
            .set(schTimeOut).equalTo(record::getSchTimeOut)
            .set(schDryRun).equalTo(record::getSchDryRun)
            .set(execQueue).equalTo(record::getExecQueue)
            .set(execWarnLevel).equalTo(record::getExecWarnLevel)
            .set(schTimeOutStrategy).equalTo(record::getSchTimeOutStrategy)
            .set(schPriority).equalTo(record::getSchPriority)
            .set(execDriverMem).equalTo(record::getExecDriverMem)
            .set(execWorkerMem).equalTo(record::getExecWorkerMem)
            .set(execCores).equalTo(record::getExecCores)
            .set(runningState).equalTo(record::getRunningState)
            .set(execEngine).equalTo(record::getExecEngine)
            .set(extProperties).equalTo(record::getExtProperties)
            .set(isOpenMergeFile).equalTo(record::getIsOpenMergeFile)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_execute_config")
    default int updateByPrimaryKeySelective(JobExecuteConfig record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(environment).equalToWhenPresent(record::getEnvironment)
            .set(schDagId).equalToWhenPresent(record::getSchDagId)
            .set(schRerunMode).equalToWhenPresent(record::getSchRerunMode)
            .set(schTimeOut).equalToWhenPresent(record::getSchTimeOut)
            .set(schDryRun).equalToWhenPresent(record::getSchDryRun)
            .set(execQueue).equalToWhenPresent(record::getExecQueue)
            .set(execWarnLevel).equalToWhenPresent(record::getExecWarnLevel)
            .set(schTimeOutStrategy).equalToWhenPresent(record::getSchTimeOutStrategy)
            .set(schPriority).equalToWhenPresent(record::getSchPriority)
            .set(execDriverMem).equalToWhenPresent(record::getExecDriverMem)
            .set(execWorkerMem).equalToWhenPresent(record::getExecWorkerMem)
            .set(execCores).equalToWhenPresent(record::getExecCores)
            .set(runningState).equalToWhenPresent(record::getRunningState)
            .set(execEngine).equalToWhenPresent(record::getExecEngine)
            .set(extProperties).equalToWhenPresent(record::getExtProperties)
            .set(isOpenMergeFile).equalToWhenPresent(record::getIsOpenMergeFile)
            .where(id, isEqualTo(record::getId))
        );
    }
}