package cn.zhengcaiyun.idata.develop.condition.metric;

import cn.zhengcaiyun.idata.commons.pojo.PageCondition;

import java.util.List;

/**
 * 查询条件
 */
public class MetricApprovalRecordCondition extends PageCondition {

    /**
     * 指标名称（模糊搜索）
     */
    private String metricNamePattern;
    /**
     * 指标类型
     */
    private String metricTag;
    /**
     * 提交人名称
     */
    private String submitOperatorName;

    /**
     * 审批状态，1：待审批，2：已审批，3：已撤回，4：已驳回
     * 待处理包含：1，已处理包含：2、3、4
     */
    private List<Integer> statusList;

    public String getMetricNamePattern() {
        return metricNamePattern;
    }

    public void setMetricNamePattern(String metricNamePattern) {
        this.metricNamePattern = metricNamePattern;
    }

    public String getMetricTag() {
        return metricTag;
    }

    public void setMetricTag(String metricTag) {
        this.metricTag = metricTag;
    }

    public String getSubmitOperatorName() {
        return submitOperatorName;
    }

    public void setSubmitOperatorName(String submitOperatorName) {
        this.submitOperatorName = submitOperatorName;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }
}
