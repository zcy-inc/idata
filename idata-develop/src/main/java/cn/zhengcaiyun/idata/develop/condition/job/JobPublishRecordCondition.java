package cn.zhengcaiyun.idata.develop.condition.job;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobPublishRecordCondition {

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业内容id
     */
    private Long jobContentId;

    /**
     * 作业内容版本号
     */
    private Integer jobContentVersion;

    /**
     * 环境
     */
    private String environment;

    /**
     * 发布状态，1：待发布，2：已发布，4：已驳回，9：已归档
     */
    private Integer publishStatus;

    /**
     * 作业类型
     */
    private String jobTypeCode;

    /**
     * 数仓分层
     */
    private String dwLayerCode;

    /**
     * 提交人
     */
    private String submitOperator;

}