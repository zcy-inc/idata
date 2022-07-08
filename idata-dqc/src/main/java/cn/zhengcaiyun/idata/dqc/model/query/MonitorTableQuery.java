package cn.zhengcaiyun.idata.dqc.model.query;

import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import lombok.Data;

@Data
public class MonitorTableQuery extends BaseQuery {
    private String tableName;
    private Long templateId;
    private Long baselineId;
    private Integer alarmLevel;
}
