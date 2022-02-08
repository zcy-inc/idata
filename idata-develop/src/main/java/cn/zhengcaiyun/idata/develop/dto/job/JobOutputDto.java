package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import org.springframework.beans.BeanUtils;

public class JobOutputDto extends BaseDto {
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
     * 数据去向-数据源类型
     */
    private String destDataSourceType;

    /**
     * 数据去向-数据源id
     */
    private Long destDataSourceId;

    /**
     * 数据去向-数据源名称
     */
    private String destDataSourceName;

    /**
     * 数据去向-目标表
     */
    private String destTable;

    /**
     * 数据去向-写入模式，overwrite，upsert
     */
    private String destWriteMode;

    /**
     * 数据来源表主键(写入模式为upsert时必填)
     */
    private String jobTargetTablePk;

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

    public String getDestDataSourceType() {
        return destDataSourceType;
    }

    public void setDestDataSourceType(String destDataSourceType) {
        this.destDataSourceType = destDataSourceType;
    }

    public Long getDestDataSourceId() {
        return destDataSourceId;
    }

    public void setDestDataSourceId(Long destDataSourceId) {
        this.destDataSourceId = destDataSourceId;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public String getDestWriteMode() {
        return destWriteMode;
    }

    public void setDestWriteMode(String destWriteMode) {
        this.destWriteMode = destWriteMode;
    }

    public String getJobTargetTablePk() {
        return jobTargetTablePk;
    }

    public void setJobTargetTablePk(String jobTargetTablePk) {
        this.jobTargetTablePk = jobTargetTablePk;
    }

    public String getDestDataSourceName() {
        return destDataSourceName;
    }

    public void setDestDataSourceName(String destDataSourceName) {
        this.destDataSourceName = destDataSourceName;
    }

    public static JobOutputDto from(JobOutput model) {
        JobOutputDto dto = new JobOutputDto();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }

    public JobOutput toModel() {
        JobOutput model = new JobOutput();
        BeanUtils.copyProperties(this, model);
        return model;
    }
}