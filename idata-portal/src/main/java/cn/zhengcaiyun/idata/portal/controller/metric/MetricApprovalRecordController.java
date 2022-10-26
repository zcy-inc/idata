package cn.zhengcaiyun.idata.portal.controller.metric;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.metric.MetricApprovalRecordCondition;
import cn.zhengcaiyun.idata.develop.dto.measure.MetricApprovalRecordDto;
import cn.zhengcaiyun.idata.develop.service.measure.MetricApprovalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Metric-Approval
 */
@RestController
@RequestMapping(path = "/p1/dev/metric/approvals")
public class MetricApprovalRecordController {

    private final MetricApprovalRecordService metricApprovalRecordService;

    @Autowired
    public MetricApprovalRecordController(MetricApprovalRecordService metricApprovalRecordService) {
        this.metricApprovalRecordService = metricApprovalRecordService;
    }

    /**
     * 分页查询
     *
     * @param condition 查询条件
     * @return
     */
    @PostMapping("/paging")
    public RestResult<Page<MetricApprovalRecordDto>> paging(@RequestBody MetricApprovalRecordCondition condition) {
        return RestResult.success(metricApprovalRecordService.paging(condition));
    }

    /**
     * 审批
     *
     * @param id 记录id
     * @return
     */
    @PutMapping("/{id}/approve")
    public RestResult<Boolean> approve(@PathVariable Long id) {
        return RestResult.success(metricApprovalRecordService.approve(id, null, OperatorContext.getCurrentOperator()));
    }

    /**
     * 批量审批
     *
     * @param param 参数
     * @return
     */
    @PutMapping("/approve")
    public RestResult<Boolean> approve(@RequestBody ApprovalParam param) {
        List<Long> ids = param.getIds();
        checkArgument(!CollectionUtils.isEmpty(ids), "审批记录id不能为空");
        for (Long id : ids) {
            metricApprovalRecordService.approve(id, null, OperatorContext.getCurrentOperator());
        }
        return RestResult.success(Boolean.TRUE);
    }

    /**
     * 驳回
     *
     * @param id 记录id
     * @return
     */
    @PutMapping("/{id}/reject")
    public RestResult<Boolean> reject(@PathVariable Long id) {
        return RestResult.success(metricApprovalRecordService.reject(id, null, OperatorContext.getCurrentOperator()));
    }

    public static class ApprovalParam {
        /**
         * 记录id集合
         */
        private List<Long> ids;

        public List<Long> getIds() {
            return ids;
        }
    }
}
