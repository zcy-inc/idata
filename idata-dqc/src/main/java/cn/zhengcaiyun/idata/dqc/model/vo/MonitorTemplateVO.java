package cn.zhengcaiyun.idata.dqc.model.vo;

import cn.zhengcaiyun.idata.dqc.model.common.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据质量模板表(DqcMonitorTemplate)实体类
 *
 * @author makejava
 * @since 2022-07-05 11:14:57
 */
@Data
public class MonitorTemplateVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 522248803188332063L;
    /**
     * 模板类型，system、custom
     */
    private String type;
    /**
     * 监控对象，table,filed
     */
    private String monitorObj;
    /**
     * sql
     */
    private String content;
    /**
     * 状态:0停用，1启用
     */
    private Integer status;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 维度：及时timely，准确性accuracy，完整性integrity
     */
    private String category;
    /**
     * 输出类型，1数值，2文本
     */
    private Integer outputType;

}

