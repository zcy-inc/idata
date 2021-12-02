package cn.zhengcaiyun.idata.develop.dto.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobTreeNodeDto {

    public JobTreeNodeDto(Long jobId, String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
    }

    private Long jobId;
    private String jobName;
    private List<JobTreeNodeDto> nextList;

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

    public List<JobTreeNodeDto> getNextList() {
        return nextList;
    }

    public void setNextList(List<JobTreeNodeDto> nextList) {
        this.nextList = nextList;
    }

    public void addNextListElem(JobTreeNodeDto jobTreeNodeDto) {
        if (this.nextList == null) {
            this.nextList = new ArrayList<>();
        }
        this.nextList.add(jobTreeNodeDto);
    }
}
