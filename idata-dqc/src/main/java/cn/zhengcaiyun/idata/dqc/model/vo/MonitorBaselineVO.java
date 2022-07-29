package cn.zhengcaiyun.idata.dqc.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据质量基线表(MonitorBaseline)实体类
 *
 * @author zheng
 * @since 2022-07-06 14:01:50
 */
@Data
public class MonitorBaselineVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 基线名称
     */
    private String name;
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

    private Integer ruleCount;

    private Integer tableCount;

    private Long baselineId;

}

