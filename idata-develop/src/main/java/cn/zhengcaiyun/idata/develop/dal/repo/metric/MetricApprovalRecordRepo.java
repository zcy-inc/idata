package cn.zhengcaiyun.idata.develop.dal.repo.metric;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.condition.metric.MetricApprovalRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.MetricApprovalStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.metric.MetricApprovalRecord;

import java.util.List;
import java.util.Optional;

public interface MetricApprovalRecordRepo {

    Page<MetricApprovalRecord> paging(MetricApprovalRecordCondition condition);

    List<MetricApprovalRecord> queryList(MetricApprovalRecordCondition condition, long limit, long offset);

    long count(MetricApprovalRecordCondition condition);

    List<MetricApprovalRecord> queryList(MetricApprovalRecordCondition condition);

    Long save(MetricApprovalRecord record);

    Optional<MetricApprovalRecord> query(Long id);

    Boolean updateStatusWhenApprove(Long id, MetricApprovalStatusEnum oldStatus, MetricApprovalStatusEnum newStatus, String approveOperator, String approveRemark);

    Boolean updateStatus(String metricId, MetricApprovalStatusEnum oldStatus, MetricApprovalStatusEnum newStatus);
}
