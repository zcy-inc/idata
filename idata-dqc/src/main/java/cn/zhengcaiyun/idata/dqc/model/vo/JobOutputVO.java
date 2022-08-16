package cn.zhengcaiyun.idata.dqc.model.vo;

import java.util.Date;

import lombok.Data;

/**
 * 数据开发-作业输出(JobOutput)实体类
 *
 * @author makejava
 * @since 2022-07-11 10:01:48
 */
@Data
public class JobOutputVO {
    /**
     * 环境
     */
    private String environment;
    /**
     * 数据去向-数据源类型
     */
    private String destDataSourceType;
    /**
     * 数据去向-数据源id
     */
    private Long destDataSourceId;
    /**
     * 数据去向-目标表
     */
    private String destTable;

}

