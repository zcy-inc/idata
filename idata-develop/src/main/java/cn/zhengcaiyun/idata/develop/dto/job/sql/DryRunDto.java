package cn.zhengcaiyun.idata.develop.dto.job.sql;

/**
 * @author caizhedong
 * @date 2022-05-27 下午2:35
 */

public class DryRunDto {
    private Long jobId;
    private Integer jobVersion;

    // GaS
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getJobVersion() {
        return jobVersion;
    }

    public void setJobVersion(Integer jobVersion) {
        this.jobVersion = jobVersion;
    }
}
