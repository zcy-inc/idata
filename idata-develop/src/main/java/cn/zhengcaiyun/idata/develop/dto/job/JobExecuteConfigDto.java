package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import org.springframework.beans.BeanUtils;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobExecuteConfigDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 环境
     */
    private String environment;

    /**
     * 调度配置-dag编号
     */
    private Long schDagId;

    /**
     * 调度配置-重跑配置，always：皆可重跑，failed：失败后可重跑，never：皆不可重跑
     */
    private String schRerunMode;

    /**
     * 调度配置-超时时间，单位：秒
     */
    private Integer schTimeOut;

    /**
     * 调度配置-是否空跑，0否，1是
     */
    private Integer schDryRun;

    /**
     * 运行配置-队列
     */
    private String execQueue;

    /**
     * 运行配置-作业最大并发数，配置为0时表示使用默认并发数
     */
    private Integer execMaxParallelism;

    /**
     * 运行配置-告警等级
     */
    private String execWarnLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Long getSchDagId() {
        return schDagId;
    }

    public void setSchDagId(Long schDagId) {
        this.schDagId = schDagId;
    }

    public String getSchRerunMode() {
        return schRerunMode;
    }

    public void setSchRerunMode(String schRerunMode) {
        this.schRerunMode = schRerunMode;
    }

    public Integer getSchTimeOut() {
        return schTimeOut;
    }

    public void setSchTimeOut(Integer schTimeOut) {
        this.schTimeOut = schTimeOut;
    }

    public Integer getSchDryRun() {
        return schDryRun;
    }

    public void setSchDryRun(Integer schDryRun) {
        this.schDryRun = schDryRun;
    }

    public String getExecQueue() {
        return execQueue;
    }

    public void setExecQueue(String execQueue) {
        this.execQueue = execQueue;
    }

    public Integer getExecMaxParallelism() {
        return execMaxParallelism;
    }

    public void setExecMaxParallelism(Integer execMaxParallelism) {
        this.execMaxParallelism = execMaxParallelism;
    }

    public String getExecWarnLevel() {
        return execWarnLevel;
    }

    public void setExecWarnLevel(String execWarnLevel) {
        this.execWarnLevel = execWarnLevel;
    }

    public static JobExecuteConfigDto from(JobExecuteConfig config) {
        JobExecuteConfigDto dto = new JobExecuteConfigDto();
        BeanUtils.copyProperties(config, dto);

        if (dto.getSchTimeOut() <= 0) dto.setSchTimeOut(null);
        if (dto.getExecMaxParallelism() <= 0) dto.setExecMaxParallelism(null);

        return dto;
    }

    public JobExecuteConfig toModel() {
        JobExecuteConfig config = new JobExecuteConfig();
        BeanUtils.copyProperties(this, config);
        return config;
    }
}