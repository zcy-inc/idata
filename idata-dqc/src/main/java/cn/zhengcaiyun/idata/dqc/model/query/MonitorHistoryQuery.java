package cn.zhengcaiyun.idata.dqc.model.query;

import java.util.Date;

import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import lombok.Data;

/**
 * 数据质量监控历史记录表(MonitorHistory)实体类
 *
 * @author makejava
 * @since 2022-07-12 11:20:01
 */
@Data
public class MonitorHistoryQuery extends BaseQuery{
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
    private Integer ruleType;
    /**
     * 监控对象，table,filed
     */
    private String monitorObj;
    /**
     * 查询结果
     */
    private String dataValue;
    /**
     * 最后访问时间
     */
    private String lastAccessTime;
    /**
     * 查询sql
     */
    private String sql;
    /**
     * 规则统计结果
     */
    private String ruleVallue;
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

}

