package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.commons.dto.general.KeyValuePair;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SqlColumn;
import org.springframework.beans.BeanUtils;

import javax.annotation.Generated;
import java.util.List;
import java.util.Objects;

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
     * 运行配置-告警等级
     */
    private String execWarnLevel;

    /**
     * 调度配置-超时策略，alarm：超时告警，fail：超时失败
     */
    private String schTimeOutStrategy;

    /**
     * 调度配置-优先级，1：低，2：中，3：高
     */
    private Integer schPriority;

    /**
     * 运行配置-驱动器内存
     */
    private Integer execDriverMem;

    /**
     * 运行配置-执行器内存
     */
    private Integer execWorkerMem;

    /**
     * 运行配置-执行器核心数
     */
    private Integer execCores;

    /**
     * 作业运行状态（环境级），0：暂停运行；1：恢复运行
     */
    private Integer runningState;

    /**
     * 抽数配置
     */
    private String execEngine;

    /**
     * 自定义参数
     */
    private List<KeyValuePair<String, String>> extProperties;

    /**
     *  开启小文件合并 0:否，1:是
     */
    private Integer isOpenMergeFile;

    /**
     *   DS自定义参数
     */
    public List<KeyValuePair<String, String>> customParams;

    /**
     *   写入文件类型 Spark SQL类型：orc、parquet
     */
    private String destFileType;

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

    public String getExecWarnLevel() {
        return execWarnLevel;
    }

    public void setExecWarnLevel(String execWarnLevel) {
        this.execWarnLevel = execWarnLevel;
    }

    public String getSchTimeOutStrategy() {
        return schTimeOutStrategy;
    }

    public void setSchTimeOutStrategy(String schTimeOutStrategy) {
        this.schTimeOutStrategy = schTimeOutStrategy;
    }

    public Integer getSchPriority() {
        return schPriority;
    }

    public void setSchPriority(Integer schPriority) {
        this.schPriority = schPriority;
    }

    public Integer getExecDriverMem() {
        return execDriverMem;
    }

    public void setExecDriverMem(Integer execDriverMem) {
        this.execDriverMem = execDriverMem;
    }

    public Integer getExecWorkerMem() {
        return execWorkerMem;
    }

    public void setExecWorkerMem(Integer execWorkerMem) {
        this.execWorkerMem = execWorkerMem;
    }

    public Integer getRunningState() {
        return runningState;
    }

    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }

    public String getExecEngine() {
        return execEngine;
    }

    public void setExecEngine(String execEngine) {
        this.execEngine = execEngine;
    }

    public List<KeyValuePair<String, String>> getExtProperties() {
        return extProperties;
    }

    public void setExtProperties(List<KeyValuePair<String, String>> extProperties) {
        this.extProperties = extProperties;
    }

    public Integer getExecCores() {
        return execCores;
    }

    public void setExecCores(Integer execCores) {
        this.execCores = execCores;
    }

    public Integer getIsOpenMergeFile() {
        return isOpenMergeFile;
    }

    public void setIsOpenMergeFile(Integer isOpenMergeFile) {
        this.isOpenMergeFile = isOpenMergeFile;
    }

    public List<KeyValuePair<String, String>> getCustomParams() {
        return customParams;
    }

    public void setCustomParams(List<KeyValuePair<String, String>> customParams) {
        this.customParams = customParams;
    }

    public String getDestFileType() {
        return destFileType;
    }

    public void setDestFileType(String destFileType) {
        this.destFileType = destFileType;
    }

    public static JobExecuteConfigDto from(JobExecuteConfig config) {
        JobExecuteConfigDto dto = new JobExecuteConfigDto();
        BeanUtils.copyProperties(config, dto);

        if (dto.getSchTimeOut() <= 0) dto.setSchTimeOut(null);

        if (StringUtils.isNotBlank(config.getExtProperties())) {
            dto.setExtProperties(new Gson().fromJson(config.getExtProperties(), new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType()));
        }

        if (StringUtils.isNotBlank(config.getCustomParams())) {
            dto.setCustomParams(new Gson().fromJson(config.getCustomParams(), new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType()));
        }
        return dto;
    }

    public JobExecuteConfig toModel() {
        JobExecuteConfig config = new JobExecuteConfig();
        BeanUtils.copyProperties(this, config);

        if (!Objects.isNull(this.extProperties)) {
            config.setExtProperties(new Gson().toJson(this.extProperties));
        }
        if (!Objects.isNull(this.customParams)) {
            config.setCustomParams(new Gson().toJson(this.customParams));
        }
        return config;
    }
}