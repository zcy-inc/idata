package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobTable;
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
public interface DIStreamJobTableDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    BasicColumn[] selectList = BasicColumn.columnList(id, jobId, jobContentId, jobContentVersion, srcTable, destTable, sharding, forceInit, tableCdcProps);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DIStreamJobTable> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DIStreamJobTableResult")
    Optional<DIStreamJobTable> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DIStreamJobTableResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_content_id", property="jobContentId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_content_version", property="jobContentVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="src_table", property="srcTable", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_table", property="destTable", jdbcType=JdbcType.VARCHAR),
        @Result(column="sharding", property="sharding", jdbcType=JdbcType.INTEGER),
        @Result(column="force_init", property="forceInit", jdbcType=JdbcType.INTEGER),
        @Result(column="table_cdc_props", property="tableCdcProps", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<DIStreamJobTable> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int insert(DIStreamJobTable record) {
        return MyBatis3Utils.insert(this::insert, record, DI_STREAM_JOB_TABLE, c ->
            c.map(jobId).toProperty("jobId")
            .map(jobContentId).toProperty("jobContentId")
            .map(jobContentVersion).toProperty("jobContentVersion")
            .map(srcTable).toProperty("srcTable")
            .map(destTable).toProperty("destTable")
            .map(sharding).toProperty("sharding")
            .map(forceInit).toProperty("forceInit")
            .map(tableCdcProps).toProperty("tableCdcProps")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int insertSelective(DIStreamJobTable record) {
        return MyBatis3Utils.insert(this::insert, record, DI_STREAM_JOB_TABLE, c ->
            c.map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(jobContentId).toPropertyWhenPresent("jobContentId", record::getJobContentId)
            .map(jobContentVersion).toPropertyWhenPresent("jobContentVersion", record::getJobContentVersion)
            .map(srcTable).toPropertyWhenPresent("srcTable", record::getSrcTable)
            .map(destTable).toPropertyWhenPresent("destTable", record::getDestTable)
            .map(sharding).toPropertyWhenPresent("sharding", record::getSharding)
            .map(forceInit).toPropertyWhenPresent("forceInit", record::getForceInit)
            .map(tableCdcProps).toPropertyWhenPresent("tableCdcProps", record::getTableCdcProps)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default Optional<DIStreamJobTable> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default List<DIStreamJobTable> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default List<DIStreamJobTable> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default Optional<DIStreamJobTable> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DI_STREAM_JOB_TABLE, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    static UpdateDSL<UpdateModel> updateAllColumns(DIStreamJobTable record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(jobId).equalTo(record::getJobId)
                .set(jobContentId).equalTo(record::getJobContentId)
                .set(jobContentVersion).equalTo(record::getJobContentVersion)
                .set(srcTable).equalTo(record::getSrcTable)
                .set(destTable).equalTo(record::getDestTable)
                .set(sharding).equalTo(record::getSharding)
                .set(forceInit).equalTo(record::getForceInit)
                .set(tableCdcProps).equalTo(record::getTableCdcProps);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DIStreamJobTable record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(jobId).equalToWhenPresent(record::getJobId)
                .set(jobContentId).equalToWhenPresent(record::getJobContentId)
                .set(jobContentVersion).equalToWhenPresent(record::getJobContentVersion)
                .set(srcTable).equalToWhenPresent(record::getSrcTable)
                .set(destTable).equalToWhenPresent(record::getDestTable)
                .set(sharding).equalToWhenPresent(record::getSharding)
                .set(forceInit).equalToWhenPresent(record::getForceInit)
                .set(tableCdcProps).equalToWhenPresent(record::getTableCdcProps);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int updateByPrimaryKey(DIStreamJobTable record) {
        return update(c ->
            c.set(jobId).equalTo(record::getJobId)
            .set(jobContentId).equalTo(record::getJobContentId)
            .set(jobContentVersion).equalTo(record::getJobContentVersion)
            .set(srcTable).equalTo(record::getSrcTable)
            .set(destTable).equalTo(record::getDestTable)
            .set(sharding).equalTo(record::getSharding)
            .set(forceInit).equalTo(record::getForceInit)
            .set(tableCdcProps).equalTo(record::getTableCdcProps)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    default int updateByPrimaryKeySelective(DIStreamJobTable record) {
        return update(c ->
            c.set(jobId).equalToWhenPresent(record::getJobId)
            .set(jobContentId).equalToWhenPresent(record::getJobContentId)
            .set(jobContentVersion).equalToWhenPresent(record::getJobContentVersion)
            .set(srcTable).equalToWhenPresent(record::getSrcTable)
            .set(destTable).equalToWhenPresent(record::getDestTable)
            .set(sharding).equalToWhenPresent(record::getSharding)
            .set(forceInit).equalToWhenPresent(record::getForceInit)
            .set(tableCdcProps).equalToWhenPresent(record::getTableCdcProps)
            .where(id, isEqualTo(record::getId))
        );
    }
}