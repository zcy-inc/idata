package cn.zhengcaiyun.idata.dqc.model.query;

import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import lombok.Data;

/**
 * 数据质量基线表(MonitorBaseline)实体类
 *
 * @author zheng
 * @since 2022-07-06 14:01:50
 */
@Data
public class MonitorBaselineQuery extends BaseQuery{
    /**
     * 基线名称
     */
    private String name;
    /**
     * 状态:0关闭，1开始
     */
    private Integer status;

}

