package cn.zhengcaiyun.idata.dqc.model.entity;

import java.io.Serializable;
import java.util.Date;

import cn.zhengcaiyun.idata.dqc.model.common.BaseEntity;
import lombok.Data;

/**
 * dqc_monitor_rule
 * @author 
 */
@Data
public class MonitorRule extends BaseEntity {
    private Long baselineId; //默认-1，非基线规则

    /**
     * 表名名称
     */
    private String tableName;

    private String fieldName;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则类型，system内置规则，template模板规则，custom自定义规则
     */
    private String ruleType;

    /**
     * 模板规则id
     */
    private Long templateId;

    /**
     * 监控对象，table,filed
     */
    private String monitorObj;

    /**
     * 告警等级，1一般，2重要，3严重
     */
    private Integer alarmLevel;

    /**
     * 告警接收人，逗号分隔
     */
    private String alarmReceiver;

    /**
     * 校验类型:1数值型，2波动率型
     */
    private Integer checkType;

    /**
     * 比较方式：>,>=,<,<=,<>,=,range,up,down
     */
    private String compareType;

    /**
     * 内置规则为对应的值，自定义规则为sql
     */
    private String content;

    private Integer outputType;

    private Long fixValue;

    /**
     * 开始值
     */
    private Long rangeStart;

    /**
     * 结束值
     */
    private Long rangeEnd;

    /**
     * 状态:0关闭，1开始
     */
    private Integer status;


}