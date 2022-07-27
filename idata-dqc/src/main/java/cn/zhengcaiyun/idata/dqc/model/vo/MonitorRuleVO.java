package cn.zhengcaiyun.idata.dqc.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * author:zheng
 * Date:2022/6/23
 */
@Data
public class MonitorRuleVO {
    public Long id;

    public Long baselineId;

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
    private String alarmReceivers;

    /**
     * 校验类型:abs绝对值，pre_period上周期
     */
    private String checkType;

    /**
     * 比较方式：>,>=,<,<=,<>,=,range,up,down
     */
    private String compareType;

    /**
     * 内置规则为对应的值，自定义规则为sql
     */
    private String content;

    private Integer outputType;

    private Double fixValue;

    /**
     * 开始值
     */
    private Double rangeStart;

    /**
     * 结束值
     */
    private Double rangeEnd;

    /**
     * 状态:0关闭，1开始
     */
    private Integer status;

    /**
     * 是否删除，0否，1是
     */
    private Integer del;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改者
     */
    private String editor;
    /**
     * 修改时间
     */
    private Date editTime;

    private String partitionExpr;

    private String version;

    private String partitionCondition;

    private MonitorHistoryVO monitorHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonitorRuleVO that = (MonitorRuleVO) o;
        return Objects.equals(tableName, that.tableName) && Objects.equals(fieldName, that.fieldName) && Objects.equals(ruleType, that.ruleType) && Objects.equals(templateId, that.templateId) && Objects.equals(monitorObj, that.monitorObj) && Objects.equals(alarmLevel, that.alarmLevel) && Objects.equals(alarmReceivers, that.alarmReceivers) && Objects.equals(checkType, that.checkType) && Objects.equals(compareType, that.compareType) && Objects.equals(content, that.content) && Objects.equals(outputType, that.outputType) && Objects.equals(fixValue, that.fixValue) && Objects.equals(rangeStart, that.rangeStart) && Objects.equals(rangeEnd, that.rangeEnd) && Objects.equals(partitionExpr, that.partitionExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, fieldName, ruleType, templateId, monitorObj, alarmLevel, alarmReceivers, checkType, compareType, content, outputType, fixValue, rangeStart, rangeEnd, partitionExpr);
    }
}
