package cn.zhengcaiyun.idata.develop.dto;

import java.util.Objects;

public class JobDependencyDto {

    private String name;
    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 上级任务id
     */
    private Long prevJobId;

    /**
     * 环境
     */
    private String environment;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrevJobId() {
        return prevJobId;
    }

    public void setPrevJobId(Long prevJobId) {
        this.prevJobId = prevJobId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDependencyDto that = (JobDependencyDto) o;
        return Objects.equals(environment, that.environment) && Objects.equals(jobId, that.jobId) && Objects.equals(prevJobId, that.prevJobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, jobId, prevJobId);
    }
}
