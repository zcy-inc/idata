package cn.zhengcaiyun.idata.dqc.model.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 数据质量监控历史记录表(MonitorHistory)实体类
 *
 * @author makejava
 * @since 2022-07-14 20:18:15
 */
@Data
public class MonitorHistory implements Serializable {
    private static final long serialVersionUID = -19674725158720157L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 分区
     */
    private String partition;
    /**
     * 监控规则id
     */
    private Long ruleId;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则类型，1内置规则，2模板规则，3自定义规则
     */
    private String ruleType;
    /**
     * 监控对象，table,filed
     */
    private String monitorObj;
    /**
     * 查询结果
     */
    private Long dataValue;
    /**
     * 查询sql
     */
    private String runSql;
    /**
     * 规则统计结果
     */
    private String ruleValue;
    /**
     * 规则版本号（规则表的修改日期）
     */
    private String version;
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
    
    private Integer alarmLevel;

    private Integer alarm;

    private String alarmReceivers;

    private Double fixValue;

    /**
     * 开始值
     */
    private Double rangeStart;

    /**
     * 结束值
     */
    private Double rangeEnd;

    private String content;

    private String compareType;

    private Long templateId;

    private Long baselineId;

    private String checkType;

}

