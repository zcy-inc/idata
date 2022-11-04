package cn.zhengcaiyun.idata.dqc.model.entity;

import cn.zhengcaiyun.idata.dqc.model.common.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据质量基线表(MonitorBaseline)实体类
 *
 * @author zheng
 * @since 2022-07-06 14:01:50
 */
@Data
public class MonitorBaseline extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 355109664106790315L;
    /**
     * 基线名称
     */
    private String name;
    /**
     * 状态:0关闭，1开始
     */
    private Integer status;
}

