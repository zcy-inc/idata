package cn.zhengcaiyun.idata.develop.dto;

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

}
