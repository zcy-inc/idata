package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
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
public interface DIStreamJobContentDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, editable, version, srcDataSourceType, srcDataSourceId, destDataSourceType, destDataSourceId, enableSharding, cdcConfig);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DIStreamJobContent> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DIStreamJobContentResult")
    Optional<DIStreamJobContent> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DIStreamJobContentResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="editable", property="editable", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="src_data_source_type", property="srcDataSourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_data_source_id", property="srcDataSourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="dest_data_source_type", property="destDataSourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_data_source_id", property="destDataSourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="enable_sharding", property="enableSharding", jdbcType=JdbcType.INTEGER),
        @Result(column="cdc_config", property="cdcConfig", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<DIStreamJobContent> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int insert(DIStreamJobContent record) {
        return MyBatis3Utils.insert(this::insert, record, DI_STREAM_JOB_CONTENT, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(editable).toProperty("editable")
            .map(version).toProperty("version")
            .map(srcDataSourceType).toProperty("srcDataSourceType")
            .map(srcDataSourceId).toProperty("srcDataSourceId")
            .map(destDataSourceType).toProperty("destDataSourceType")
            .map(destDataSourceId).toProperty("destDataSourceId")
            .map(enableSharding).toProperty("enableSharding")
            .map(cdcConfig).toProperty("cdcConfig")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int insertSelective(DIStreamJobContent record) {
        return MyBatis3Utils.insert(this::insert, record, DI_STREAM_JOB_CONTENT, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(editable).toPropertyWhenPresent("editable", record::getEditable)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(srcDataSourceType).toPropertyWhenPresent("srcDataSourceType", record::getSrcDataSourceType)
            .map(srcDataSourceId).toPropertyWhenPresent("srcDataSourceId", record::getSrcDataSourceId)
            .map(destDataSourceType).toPropertyWhenPresent("destDataSourceType", record::getDestDataSourceType)
            .map(destDataSourceId).toPropertyWhenPresent("destDataSourceId", record::getDestDataSourceId)
            .map(enableSharding).toPropertyWhenPresent("enableSharding", record::getEnableSharding)
            .map(cdcConfig).toPropertyWhenPresent("cdcConfig", record::getCdcConfig)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default Optional<DIStreamJobContent> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default List<DIStreamJobContent> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default List<DIStreamJobContent> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default Optional<DIStreamJobContent> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DI_STREAM_JOB_CONTENT, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    static UpdateDSL<UpdateModel> updateAllColumns(DIStreamJobContent record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(editable).equalTo(record::getEditable)
                .set(version).equalTo(record::getVersion)
                .set(srcDataSourceType).equalTo(record::getSrcDataSourceType)
                .set(srcDataSourceId).equalTo(record::getSrcDataSourceId)
                .set(destDataSourceType).equalTo(record::getDestDataSourceType)
                .set(destDataSourceId).equalTo(record::getDestDataSourceId)
                .set(enableSharding).equalTo(record::getEnableSharding)
                .set(cdcConfig).equalTo(record::getCdcConfig);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DIStreamJobContent record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(editable).equalToWhenPresent(record::getEditable)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(srcDataSourceType).equalToWhenPresent(record::getSrcDataSourceType)
                .set(srcDataSourceId).equalToWhenPresent(record::getSrcDataSourceId)
                .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
                .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
                .set(enableSharding).equalToWhenPresent(record::getEnableSharding)
                .set(cdcConfig).equalToWhenPresent(record::getCdcConfig);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int updateByPrimaryKey(DIStreamJobContent record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(editable).equalTo(record::getEditable)
            .set(version).equalTo(record::getVersion)
            .set(srcDataSourceType).equalTo(record::getSrcDataSourceType)
            .set(srcDataSourceId).equalTo(record::getSrcDataSourceId)
            .set(destDataSourceType).equalTo(record::getDestDataSourceType)
            .set(destDataSourceId).equalTo(record::getDestDataSourceId)
            .set(enableSharding).equalTo(record::getEnableSharding)
            .set(cdcConfig).equalTo(record::getCdcConfig)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream")
    default int updateByPrimaryKeySelective(DIStreamJobContent record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(editable).equalToWhenPresent(record::getEditable)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(srcDataSourceType).equalToWhenPresent(record::getSrcDataSourceType)
            .set(srcDataSourceId).equalToWhenPresent(record::getSrcDataSourceId)
            .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
            .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
            .set(enableSharding).equalToWhenPresent(record::getEnableSharding)
            .set(cdcConfig).equalToWhenPresent(record::getCdcConfig)
            .where(id, isEqualTo(record::getId))
        );
    }
}