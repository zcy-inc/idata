package cn.zhengcaiyun.idata.develop.dal.repo.metric.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.condition.metric.MetricApprovalRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.MetricApprovalStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.metric.MetricApprovalRecordDao;
import cn.zhengcaiyun.idata.develop.dal.model.metric.MetricApprovalRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.metric.MetricApprovalRecordRepo;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.metric.MetricApprovalRecordDynamicSqlSupport.METRIC_APPROVAL_RECORD;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Repository
public class MetricApprovalRecordRepoImpl implements MetricApprovalRecordRepo {

    private final MetricApprovalRecordDao metricApprovalRecordDao;

    @Autowired
    public MetricApprovalRecordRepoImpl(MetricApprovalRecordDao metricApprovalRecordDao) {
        this.metricApprovalRecordDao = metricApprovalRecordDao;
    }

    @Override
    public Page<MetricApprovalRecord> paging(MetricApprovalRecordCondition condition) {
        long total = count(condition);
        List<MetricApprovalRecord> recordList = null;
        if (total > 0) {
            recordList = queryList(condition, condition.getLimit(), condition.getOffset());
        }
        return Page.newOne(recordList, total);
    }

    @Override
    public List<MetricApprovalRecord> queryList(MetricApprovalRecordCondition condition, long limit, long offset) {
        return metricApprovalRecordDao.select(dsl -> dsl.where(
                        METRIC_APPROVAL_RECORD.metricTag, isEqualToWhenPresent(condition.getMetricTag()),
                        and(METRIC_APPROVAL_RECORD.creator, isEqualToWhenPresent(condition.getSubmitOperatorName())),
                        and(METRIC_APPROVAL_RECORD.metricName, isLikeWhenPresent(condition.getMetricNamePattern()).map(MybatisHelper::appendWildCards)),
                        and(METRIC_APPROVAL_RECORD.approvalStatus, isInWhenPresent(condition.getStatusList())),
                        and(METRIC_APPROVAL_RECORD.del, isEqualTo(DeleteEnum.DEL_NO.val))
                ).orderBy(METRIC_APPROVAL_RECORD.editTime.descending())
                .limit(limit).offset(offset));
    }

    @Override
    public long count(MetricApprovalRecordCondition condition) {
        return metricApprovalRecordDao.count(dsl -> dsl.where(
                METRIC_APPROVAL_RECORD.metricTag, isEqualToWhenPresent(condition.getMetricTag()),
                and(METRIC_APPROVAL_RECORD.creator, isEqualToWhenPresent(condition.getSubmitOperatorName())),
                and(METRIC_APPROVAL_RECORD.metricName, isLikeWhenPresent(condition.getMetricNamePattern()).map(MybatisHelper::appendWildCards)),
                and(METRIC_APPROVAL_RECORD.approvalStatus, isInWhenPresent(condition.getStatusList())),
                and(METRIC_APPROVAL_RECORD.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ));
    }

    @Override
    public List<MetricApprovalRecord> queryList(MetricApprovalRecordCondition condition) {
        return metricApprovalRecordDao.select(dsl -> dsl.where(
                METRIC_APPROVAL_RECORD.metricTag, isEqualToWhenPresent(condition.getMetricTag()),
                and(METRIC_APPROVAL_RECORD.creator, isEqualToWhenPresent(condition.getSubmitOperatorName())),
                and(METRIC_APPROVAL_RECORD.metricName, isLikeWhenPresent(condition.getMetricNamePattern()).map(MybatisHelper::appendWildCards)),
                and(METRIC_APPROVAL_RECORD.approvalStatus, isInWhenPresent(condition.getStatusList())),
                and(METRIC_APPROVAL_RECORD.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ).orderBy(METRIC_APPROVAL_RECORD.id.descending()));
    }

    @Override
    public Long save(MetricApprovalRecord record) {
        metricApprovalRecordDao.insertSelective(record);
        return record.getId();
    }

    @Override
    public Optional<MetricApprovalRecord> query(Long id) {
        Optional<MetricApprovalRecord> optional = metricApprovalRecordDao.selectByPrimaryKey(id);
        if (optional.isPresent() && optional.get().getDel().equals(DeleteEnum.DEL_NO.val)) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public Boolean updateStatusWhenApprove(Long id, MetricApprovalStatusEnum oldStatus,
                                           MetricApprovalStatusEnum newStatus, String approveOperator, String approveRemark) {
        metricApprovalRecordDao.update(dsl -> dsl.set(METRIC_APPROVAL_RECORD.approvalStatus).equalTo(newStatus.getVal())
                .set(METRIC_APPROVAL_RECORD.approveOperator).equalTo(approveOperator)
                .set(METRIC_APPROVAL_RECORD.approveRemark).equalTo(Strings.nullToEmpty(approveRemark))
                .set(METRIC_APPROVAL_RECORD.approveTime).equalTo(new Date())
                .where(METRIC_APPROVAL_RECORD.id, isEqualTo(id),
                        and(METRIC_APPROVAL_RECORD.approvalStatus, isEqualTo(oldStatus.getVal()))));
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateStatus(String metricId, MetricApprovalStatusEnum oldStatus, MetricApprovalStatusEnum newStatus) {
        metricApprovalRecordDao.update(dsl -> dsl.set(METRIC_APPROVAL_RECORD.approvalStatus).equalTo(newStatus.getVal())
                .where(METRIC_APPROVAL_RECORD.metricId, isEqualTo(metricId),
                        and(METRIC_APPROVAL_RECORD.approvalStatus, isEqualTo(oldStatus.getVal()))));
        return Boolean.TRUE;
    }
}
