package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
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
public interface JobDependenceDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, jobId, environment, prevJobId, prevJobDagId);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<JobDependence> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobDependenceResult")
    Optional<JobDependence> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobDependenceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="prev_job_id", property="prevJobId", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_job_dag_id", property="prevJobDagId", jdbcType=JdbcType.VARCHAR)
    })
    List<JobDependence> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int insert(JobDependence record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_DEPENDENCE, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(jobId).toProperty("jobId")
            .map(environment).toProperty("environment")
            .map(prevJobId).toProperty("prevJobId")
            .map(prevJobDagId).toProperty("prevJobDagId")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int insertSelective(JobDependence record) {
        return MyBatis3Utils.insert(this::insert, record, JOB_DEPENDENCE, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(environment).toPropertyWhenPresent("environment", record::getEnvironment)
            .map(prevJobId).toPropertyWhenPresent("prevJobId", record::getPrevJobId)
            .map(prevJobDagId).toPropertyWhenPresent("prevJobDagId", record::getPrevJobDagId)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default Optional<JobDependence> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default List<JobDependence> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default List<JobDependence> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default Optional<JobDependence> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, JOB_DEPENDENCE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    static UpdateDSL<UpdateModel> updateAllColumns(JobDependence record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(jobId).equalTo(record::getJobId)
                .set(environment).equalTo(record::getEnvironment)
                .set(prevJobId).equalTo(record::getPrevJobId)
                .set(prevJobDagId).equalTo(record::getPrevJobDagId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobDependence record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(environment).equalToWhenPresent(record::getEnvironment)
                .set(prevJobId).equalToWhenPresent(record::getPrevJobId)
                .set(prevJobDagId).equalToWhenPresent(record::getPrevJobDagId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int updateByPrimaryKey(JobDependence record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(jobId).equalTo(record::getJobId)
            .set(environment).equalTo(record::getEnvironment)
            .set(prevJobId).equalTo(record::getPrevJobId)
            .set(prevJobDagId).equalTo(record::getPrevJobDagId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_dependence")
    default int updateByPrimaryKeySelective(JobDependence record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(environment).equalToWhenPresent(record::getEnvironment)
            .set(prevJobId).equalToWhenPresent(record::getPrevJobId)
            .set(prevJobDagId).equalToWhenPresent(record::getPrevJobDagId)
            .where(id, isEqualTo(record::getId))
        );
    }
}