package cn.zhengcaiyun.idata.develop.dal.dao.metric;

import static cn.zhengcaiyun.idata.develop.dal.dao.metric.MetricApprovalRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.zhengcaiyun.idata.develop.dal.model.metric.MetricApprovalRecord;
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
public interface MetricApprovalRecordDao extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, metricId, metricName, metricTag, bizDomain, bizProcess, approvalStatus, submitRemark, approveOperator, approveTime, approveRemark);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="row.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<MetricApprovalRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="MetricApprovalRecordResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="metric_id", property="metricId", jdbcType=JdbcType.VARCHAR),
        @Result(column="metric_name", property="metricName", jdbcType=JdbcType.VARCHAR),
        @Result(column="metric_tag", property="metricTag", jdbcType=JdbcType.VARCHAR),
        @Result(column="biz_domain", property="bizDomain", jdbcType=JdbcType.VARCHAR),
        @Result(column="biz_process", property="bizProcess", jdbcType=JdbcType.VARCHAR),
        @Result(column="approval_status", property="approvalStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="submit_remark", property="submitRemark", jdbcType=JdbcType.VARCHAR),
        @Result(column="approve_operator", property="approveOperator", jdbcType=JdbcType.VARCHAR),
        @Result(column="approve_time", property="approveTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="approve_remark", property="approveRemark", jdbcType=JdbcType.VARCHAR)
    })
    List<MetricApprovalRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("MetricApprovalRecordResult")
    Optional<MetricApprovalRecord> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int insert(MetricApprovalRecord row) {
        return MyBatis3Utils.insert(this::insert, row, METRIC_APPROVAL_RECORD, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(metricId).toProperty("metricId")
            .map(metricName).toProperty("metricName")
            .map(metricTag).toProperty("metricTag")
            .map(bizDomain).toProperty("bizDomain")
            .map(bizProcess).toProperty("bizProcess")
            .map(approvalStatus).toProperty("approvalStatus")
            .map(submitRemark).toProperty("submitRemark")
            .map(approveOperator).toProperty("approveOperator")
            .map(approveTime).toProperty("approveTime")
            .map(approveRemark).toProperty("approveRemark")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int insertSelective(MetricApprovalRecord row) {
        return MyBatis3Utils.insert(this::insert, row, METRIC_APPROVAL_RECORD, c ->
            c.map(del).toPropertyWhenPresent("del", row::getDel)
            .map(creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", row::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", row::getEditTime)
            .map(metricId).toPropertyWhenPresent("metricId", row::getMetricId)
            .map(metricName).toPropertyWhenPresent("metricName", row::getMetricName)
            .map(metricTag).toPropertyWhenPresent("metricTag", row::getMetricTag)
            .map(bizDomain).toPropertyWhenPresent("bizDomain", row::getBizDomain)
            .map(bizProcess).toPropertyWhenPresent("bizProcess", row::getBizProcess)
            .map(approvalStatus).toPropertyWhenPresent("approvalStatus", row::getApprovalStatus)
            .map(submitRemark).toPropertyWhenPresent("submitRemark", row::getSubmitRemark)
            .map(approveOperator).toPropertyWhenPresent("approveOperator", row::getApproveOperator)
            .map(approveTime).toPropertyWhenPresent("approveTime", row::getApproveTime)
            .map(approveRemark).toPropertyWhenPresent("approveRemark", row::getApproveRemark)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default Optional<MetricApprovalRecord> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default List<MetricApprovalRecord> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default List<MetricApprovalRecord> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default Optional<MetricApprovalRecord> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, METRIC_APPROVAL_RECORD, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    static UpdateDSL<UpdateModel> updateAllColumns(MetricApprovalRecord row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(row::getDel)
                .set(creator).equalTo(row::getCreator)
                .set(createTime).equalTo(row::getCreateTime)
                .set(editor).equalTo(row::getEditor)
                .set(editTime).equalTo(row::getEditTime)
                .set(metricId).equalTo(row::getMetricId)
                .set(metricName).equalTo(row::getMetricName)
                .set(metricTag).equalTo(row::getMetricTag)
                .set(bizDomain).equalTo(row::getBizDomain)
                .set(bizProcess).equalTo(row::getBizProcess)
                .set(approvalStatus).equalTo(row::getApprovalStatus)
                .set(submitRemark).equalTo(row::getSubmitRemark)
                .set(approveOperator).equalTo(row::getApproveOperator)
                .set(approveTime).equalTo(row::getApproveTime)
                .set(approveRemark).equalTo(row::getApproveRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(MetricApprovalRecord row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(row::getDel)
                .set(creator).equalToWhenPresent(row::getCreator)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(editor).equalToWhenPresent(row::getEditor)
                .set(editTime).equalToWhenPresent(row::getEditTime)
                .set(metricId).equalToWhenPresent(row::getMetricId)
                .set(metricName).equalToWhenPresent(row::getMetricName)
                .set(metricTag).equalToWhenPresent(row::getMetricTag)
                .set(bizDomain).equalToWhenPresent(row::getBizDomain)
                .set(bizProcess).equalToWhenPresent(row::getBizProcess)
                .set(approvalStatus).equalToWhenPresent(row::getApprovalStatus)
                .set(submitRemark).equalToWhenPresent(row::getSubmitRemark)
                .set(approveOperator).equalToWhenPresent(row::getApproveOperator)
                .set(approveTime).equalToWhenPresent(row::getApproveTime)
                .set(approveRemark).equalToWhenPresent(row::getApproveRemark);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int updateByPrimaryKey(MetricApprovalRecord row) {
        return update(c ->
            c.set(del).equalTo(row::getDel)
            .set(creator).equalTo(row::getCreator)
            .set(createTime).equalTo(row::getCreateTime)
            .set(editor).equalTo(row::getEditor)
            .set(editTime).equalTo(row::getEditTime)
            .set(metricId).equalTo(row::getMetricId)
            .set(metricName).equalTo(row::getMetricName)
            .set(metricTag).equalTo(row::getMetricTag)
            .set(bizDomain).equalTo(row::getBizDomain)
            .set(bizProcess).equalTo(row::getBizProcess)
            .set(approvalStatus).equalTo(row::getApprovalStatus)
            .set(submitRemark).equalTo(row::getSubmitRemark)
            .set(approveOperator).equalTo(row::getApproveOperator)
            .set(approveTime).equalTo(row::getApproveTime)
            .set(approveRemark).equalTo(row::getApproveRemark)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    default int updateByPrimaryKeySelective(MetricApprovalRecord row) {
        return update(c ->
            c.set(del).equalToWhenPresent(row::getDel)
            .set(creator).equalToWhenPresent(row::getCreator)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(editor).equalToWhenPresent(row::getEditor)
            .set(editTime).equalToWhenPresent(row::getEditTime)
            .set(metricId).equalToWhenPresent(row::getMetricId)
            .set(metricName).equalToWhenPresent(row::getMetricName)
            .set(metricTag).equalToWhenPresent(row::getMetricTag)
            .set(bizDomain).equalToWhenPresent(row::getBizDomain)
            .set(bizProcess).equalToWhenPresent(row::getBizProcess)
            .set(approvalStatus).equalToWhenPresent(row::getApprovalStatus)
            .set(submitRemark).equalToWhenPresent(row::getSubmitRemark)
            .set(approveOperator).equalToWhenPresent(row::getApproveOperator)
            .set(approveTime).equalToWhenPresent(row::getApproveTime)
            .set(approveRemark).equalToWhenPresent(row::getApproveRemark)
            .where(id, isEqualTo(row::getId))
        );
    }
}