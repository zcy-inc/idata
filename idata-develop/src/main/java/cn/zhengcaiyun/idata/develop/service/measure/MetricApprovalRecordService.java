package cn.zhengcaiyun.idata.develop.service.measure;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.condition.metric.MetricApprovalRecordCondition;
import cn.zhengcaiyun.idata.develop.dto.measure.MetricApprovalRecordDto;

public interface MetricApprovalRecordService {

    Page<MetricApprovalRecordDto> paging(MetricApprovalRecordCondition condition);

    Boolean approve(Long id, String remark, Operator operator);

    Boolean reject(Long id, String remark, Operator operator);

}
