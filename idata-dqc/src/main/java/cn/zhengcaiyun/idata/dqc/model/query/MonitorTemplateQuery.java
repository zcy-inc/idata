package cn.zhengcaiyun.idata.dqc.model.query;

import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import lombok.Data;

/**
 * 数据质量模板表(DqcMonitorTemplate)实体类
 *
 * @author makejava
 * @since 2022-07-04 11:02:57
 */
@Data
public class MonitorTemplateQuery extends BaseQuery{
    /**
     * 规则名称
     */
    private String name;
    /**
     * 模板类型，system、custom
     */
    private String type;
    /**
     * 监控对象，table,filed
     */
    private String monitorObj;
    /**
     * 状态:0停用，1启用
     */
    private Integer status;

    private String creator;

    private String category;
}

