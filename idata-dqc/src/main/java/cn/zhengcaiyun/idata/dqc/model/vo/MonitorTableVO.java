package cn.zhengcaiyun.idata.dqc.model.vo;

import lombok.Data;

/**
 * author:zheng
 * Date:2022/6/23
 */
@Data
public class MonitorTableVO {
    private Long id;
    /**
     * 基线id
     */
    private Long baselineId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 分区信息
     */
    private String partitionExpr;
    /**
     * 告警等级，1一般，2重要，3严重
     */
    private Integer latestAlarmLevel;
    /**
     * 规则条数
     */
    private Integer ruleCount;

    private String accessTime;

}
