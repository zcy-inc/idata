package cn.zhengcaiyun.idata.portal.model.request.job;

import cn.zhengcaiyun.idata.develop.dto.JobDependencyDto;

import java.util.List;

public class CycleJobQueryRequest {

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 环境
     */
    private String env;

    /**
     * 额外列表
     */
    private List<JobDependencyDto> extraList;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<JobDependencyDto> getExtraList() {
        return extraList;
    }

    public void setExtraList(List<JobDependencyDto> extraList) {
        this.extraList = extraList;
    }
}
