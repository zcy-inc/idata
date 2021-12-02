package cn.zhengcaiyun.idata.portal.model.response.dag;

import java.util.Date;
import java.util.List;

public class JobTreeResponse {

    private Long jobId;

    private String jobName;

    private List<JobNode> parents;

    private List<JobNode> children;

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

    public List<JobNode> getParents() {
        return parents;
    }

    public void setParents(List<JobNode> parents) {
        this.parents = parents;
    }

    public List<JobNode> getChildren() {
        return children;
    }

    public void setChildren(List<JobNode> children) {
        this.children = children;
    }

    public static class JobNode {

        private Long jobId;
        private String jobName;
        private List<JobNode> nextList;

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

        public List<JobNode> getNextList() {
            return nextList;
        }

        public void setNextList(List<JobNode> nextList) {
            this.nextList = nextList;
        }
    }
}
