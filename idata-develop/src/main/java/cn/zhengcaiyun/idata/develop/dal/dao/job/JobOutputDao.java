package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
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
public interface JobOutputDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, jobId, environment, destDataSourceType, destDataSourceId, destTable, destWriteMode, jobTargetTablePk);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<JobOutput> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobOutputResult")
    Optional<JobOutput> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobOutputResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_data_source_type", property="destDataSourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_data_source_id", property="destDataSourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="dest_table", property="destTable", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_write_mode", property="destWriteMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_target_table_pk", property="jobTargetTablePk", jdbcType=JdbcType.VARCHAR)
    })
    List<JobOutput> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int insert(JobOutput record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_OUTPUT, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(jobId).toProperty("jobId")
            .map(environment).toProperty("environment")
            .map(destDataSourceType).toProperty("destDataSourceType")
            .map(destDataSourceId).toProperty("destDataSourceId")
            .map(destTable).toProperty("destTable")
            .map(destWriteMode).toProperty("destWriteMode")
            .map(jobTargetTablePk).toProperty("jobTargetTablePk")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int insertSelective(JobOutput record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_OUTPUT, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(environment).toPropertyWhenPresent("environment", record::getEnvironment)
            .map(destDataSourceType).toPropertyWhenPresent("destDataSourceType", record::getDestDataSourceType)
            .map(destDataSourceId).toPropertyWhenPresent("destDataSourceId", record::getDestDataSourceId)
            .map(destTable).toPropertyWhenPresent("destTable", record::getDestTable)
            .map(destWriteMode).toPropertyWhenPresent("destWriteMode", record::getDestWriteMode)
            .map(jobTargetTablePk).toPropertyWhenPresent("jobTargetTablePk", record::getJobTargetTablePk)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default Optional<JobOutput> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default List<JobOutput> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default List<JobOutput> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default Optional<JobOutput> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, JOB_OUTPUT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    static UpdateDSL<UpdateModel> updateAllColumns(JobOutput record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(jobId).equalTo(record::getJobId)
                .set(environment).equalTo(record::getEnvironment)
                .set(destDataSourceType).equalTo(record::getDestDataSourceType)
                .set(destDataSourceId).equalTo(record::getDestDataSourceId)
                .set(destTable).equalTo(record::getDestTable)
                .set(destWriteMode).equalTo(record::getDestWriteMode)
                .set(jobTargetTablePk).equalTo(record::getJobTargetTablePk);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobOutput record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(environment).equalToWhenPresent(record::getEnvironment)
                .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
                .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
                .set(destTable).equalToWhenPresent(record::getDestTable)
                .set(destWriteMode).equalToWhenPresent(record::getDestWriteMode)
                .set(jobTargetTablePk).equalToWhenPresent(record::getJobTargetTablePk);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int updateByPrimaryKey(JobOutput record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(jobId).equalTo(record::getJobId)
            .set(environment).equalTo(record::getEnvironment)
            .set(destDataSourceType).equalTo(record::getDestDataSourceType)
            .set(destDataSourceId).equalTo(record::getDestDataSourceId)
            .set(destTable).equalTo(record::getDestTable)
            .set(destWriteMode).equalTo(record::getDestWriteMode)
            .set(jobTargetTablePk).equalTo(record::getJobTargetTablePk)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_output")
    default int updateByPrimaryKeySelective(JobOutput record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(environment).equalToWhenPresent(record::getEnvironment)
            .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
            .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
            .set(destTable).equalToWhenPresent(record::getDestTable)
            .set(destWriteMode).equalToWhenPresent(record::getDestWriteMode)
            .set(jobTargetTablePk).equalToWhenPresent(record::getJobTargetTablePk)
            .where(id, isEqualTo(record::getId))
        );
    }
}