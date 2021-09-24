package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
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
public interface JobPublishRecordDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, jobContentId, jobContentVersion, jobTypeCode, dwLayerCode, environment, publishStatus, submitRemark, approveOperator, approveTime, approveRemark);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<JobPublishRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobPublishRecordResult")
    Optional<JobPublishRecord> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobPublishRecordResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_content_id", property="jobContentId", jdbcType=JdbcType.BIGINT),
        @Result(column="job_content_version", property="jobContentVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="job_type_code", property="jobTypeCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="dw_layer_code", property="dwLayerCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="environment", property="environment", jdbcType=JdbcType.VARCHAR),
        @Result(column="publish_status", property="publishStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="submit_remark", property="submitRemark", jdbcType=JdbcType.VARCHAR),
        @Result(column="approve_operator", property="approveOperator", jdbcType=JdbcType.VARCHAR),
        @Result(column="approve_time", property="approveTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="approve_remark", property="approveRemark", jdbcType=JdbcType.VARCHAR)
    })
    List<JobPublishRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int insert(JobPublishRecord record) {
        return MyBatis3Utils.insert(this::insert, record, jobPublishRecord, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(jobContentId).toProperty("jobContentId")
            .map(jobContentVersion).toProperty("jobContentVersion")
            .map(jobTypeCode).toProperty("jobTypeCode")
            .map(dwLayerCode).toProperty("dwLayerCode")
            .map(environment).toProperty("environment")
            .map(publishStatus).toProperty("publishStatus")
            .map(submitRemark).toProperty("submitRemark")
            .map(approveOperator).toProperty("approveOperator")
            .map(approveTime).toProperty("approveTime")
            .map(approveRemark).toProperty("approveRemark")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int insertSelective(JobPublishRecord record) {
        return MyBatis3Utils.insert(this::insert, record, jobPublishRecord, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(jobContentId).toPropertyWhenPresent("jobContentId", record::getJobContentId)
            .map(jobContentVersion).toPropertyWhenPresent("jobContentVersion", record::getJobContentVersion)
            .map(jobTypeCode).toPropertyWhenPresent("jobTypeCode", record::getJobTypeCode)
            .map(dwLayerCode).toPropertyWhenPresent("dwLayerCode", record::getDwLayerCode)
            .map(environment).toPropertyWhenPresent("environment", record::getEnvironment)
            .map(publishStatus).toPropertyWhenPresent("publishStatus", record::getPublishStatus)
            .map(submitRemark).toPropertyWhenPresent("submitRemark", record::getSubmitRemark)
            .map(approveOperator).toPropertyWhenPresent("approveOperator", record::getApproveOperator)
            .map(approveTime).toPropertyWhenPresent("approveTime", record::getApproveTime)
            .map(approveRemark).toPropertyWhenPresent("approveRemark", record::getApproveRemark)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default Optional<JobPublishRecord> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default List<JobPublishRecord> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default List<JobPublishRecord> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default Optional<JobPublishRecord> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobPublishRecord, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    static UpdateDSL<UpdateModel> updateAllColumns(JobPublishRecord record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(jobContentId).equalTo(record::getJobContentId)
                .set(jobContentVersion).equalTo(record::getJobContentVersion)
                .set(jobTypeCode).equalTo(record::getJobTypeCode)
                .set(dwLayerCode).equalTo(record::getDwLayerCode)
                .set(environment).equalTo(record::getEnvironment)
                .set(publishStatus).equalTo(record::getPublishStatus)
                .set(submitRemark).equalTo(record::getSubmitRemark)
                .set(approveOperator).equalTo(record::getApproveOperator)
                .set(approveTime).equalTo(record::getApproveTime)
                .set(approveRemark).equalTo(record::getApproveRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobPublishRecord record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(jobContentId).equalToWhenPresent(record::getJobContentId)
                .set(jobContentVersion).equalToWhenPresent(record::getJobContentVersion)
                .set(jobTypeCode).equalToWhenPresent(record::getJobTypeCode)
                .set(dwLayerCode).equalToWhenPresent(record::getDwLayerCode)
                .set(environment).equalToWhenPresent(record::getEnvironment)
                .set(publishStatus).equalToWhenPresent(record::getPublishStatus)
                .set(submitRemark).equalToWhenPresent(record::getSubmitRemark)
                .set(approveOperator).equalToWhenPresent(record::getApproveOperator)
                .set(approveTime).equalToWhenPresent(record::getApproveTime)
                .set(approveRemark).equalToWhenPresent(record::getApproveRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int updateByPrimaryKey(JobPublishRecord record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(jobContentId).equalTo(record::getJobContentId)
            .set(jobContentVersion).equalTo(record::getJobContentVersion)
            .set(jobTypeCode).equalTo(record::getJobTypeCode)
            .set(dwLayerCode).equalTo(record::getDwLayerCode)
            .set(environment).equalTo(record::getEnvironment)
            .set(publishStatus).equalTo(record::getPublishStatus)
            .set(submitRemark).equalTo(record::getSubmitRemark)
            .set(approveOperator).equalTo(record::getApproveOperator)
            .set(approveTime).equalTo(record::getApproveTime)
            .set(approveRemark).equalTo(record::getApproveRemark)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    default int updateByPrimaryKeySelective(JobPublishRecord record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(jobContentId).equalToWhenPresent(record::getJobContentId)
            .set(jobContentVersion).equalToWhenPresent(record::getJobContentVersion)
            .set(jobTypeCode).equalToWhenPresent(record::getJobTypeCode)
            .set(dwLayerCode).equalToWhenPresent(record::getDwLayerCode)
            .set(environment).equalToWhenPresent(record::getEnvironment)
            .set(publishStatus).equalToWhenPresent(record::getPublishStatus)
            .set(submitRemark).equalToWhenPresent(record::getSubmitRemark)
            .set(approveOperator).equalToWhenPresent(record::getApproveOperator)
            .set(approveTime).equalToWhenPresent(record::getApproveTime)
            .set(approveRemark).equalToWhenPresent(record::getApproveRemark)
            .where(id, isEqualTo(record::getId))
        );
    }
}