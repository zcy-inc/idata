package cn.zhengcaiyun.idata.portal.model.response.dag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel("任务依赖树")
public class JobTreeResponse {

    @ApiModelProperty("任务id")
    private Long jobId;

    @ApiModelProperty("任务名称")
    private String jobName;

    @ApiModelProperty("上游节点树")
    private List<JobNode> parents;

    @ApiModelProperty("下游节点树")
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
