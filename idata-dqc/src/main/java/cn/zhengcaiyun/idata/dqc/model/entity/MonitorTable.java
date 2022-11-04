package cn.zhengcaiyun.idata.dqc.model.entity;

import java.util.Date;
import java.io.Serializable;

import cn.zhengcaiyun.idata.dqc.model.common.BaseEntity;
import lombok.Data;

/**
 * 数据质量被监控的表(DqcMonitorTable)实体类
 *
 * @author makejava
 * @since 2022-06-28 11:01:50
 */
@Data
public class MonitorTable extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -61154522431058609L;
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

    private String accessTime;

}

