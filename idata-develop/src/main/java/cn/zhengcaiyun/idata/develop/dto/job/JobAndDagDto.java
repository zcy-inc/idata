package cn.zhengcaiyun.idata.develop.dto.job;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobAndDagDto {
    /**
     * 作业id
     */
    private Long jobId;
    /**
     * 作业名称
     */
    private String jobName;
    /**
     * 数仓分层
     */
    private String dwLayerCode;
    /**
     * dag id
     */
    private Long dagId;
    /**
     * dag 名称
     */
    private String dagName;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public Long getDagId() {
        return dagId;
    }

    public void setDagId(Long dagId) {
        this.dagId = dagId;
    }

    public String getDagName() {
        return dagName;
    }

    public void setDagName(String dagName) {
        this.dagName = dagName;
    }
}