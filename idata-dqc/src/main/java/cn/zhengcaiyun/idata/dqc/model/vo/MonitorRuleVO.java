package cn.zhengcaiyun.idata.dqc.model.vo;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

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

}
