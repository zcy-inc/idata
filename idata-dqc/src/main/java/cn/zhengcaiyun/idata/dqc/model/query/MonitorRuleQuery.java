package cn.zhengcaiyun.idata.dqc.model.query;

import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import lombok.Data;

@Data
public class MonitorRuleQuery extends BaseQuery {
    private String tableName;
    private Long templateId;
    private Long baselineId;
    /**
     * 监控对象，table,filed
     */
    private String monitorObj;
}
