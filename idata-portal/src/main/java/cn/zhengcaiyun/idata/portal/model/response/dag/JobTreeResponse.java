package cn.zhengcaiyun.idata.portal.model.response.dag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel("任务依赖树")
public class JobTreeResponse {

    @ApiModelProperty("任务id")
    private Long jobId;

    @ApiModelProperty("任务名称")
    private String jobName;

    @ApiModelProperty("任务状态")
    private String jobStatus;

    @ApiModelProperty("最后运行时间")
    private String lastRunTime;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("本次返回树上游层级")
    private Integer prevLevel;

    @ApiModelProperty("本次返回树下游层级")
    private Integer nextLevel;

    @ApiModelProperty("上下游关系节点树")
    private List<JobNode> nodeList = new ArrayList<>();

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

    public List<JobNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<JobNode> nodeList) {
        this.nodeList = nodeList;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(String lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Integer getPrevLevel() {
        return prevLevel;
    }

    public void setPrevLevel(Integer prevLevel) {
        this.prevLevel = prevLevel;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    public static class JobNode {

        @ApiModelProperty("任务id")
        private Long jobId;

        @ApiModelProperty("任务名称")
        private String jobName;

        @ApiModelProperty("任务状态")
        private String jobStatus;

        @ApiModelProperty("最后运行时间")
        private String lastRunTime;

        @ApiModelProperty("任务id")
        private Long taskId;

        @ApiModelProperty("prev/next 前继/后继节点关系")
        private String relation;

        private List<JobNode> nextList;

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
            this.jobId = jobId;
        }

        public String getJobStatus() {
            return jobStatus;
        }

        public void setJobStatus(String jobStatus) {
            this.jobStatus = jobStatus;
        }

        public String getLastRunTime() {
            return lastRunTime;
        }

        public void setLastRunTime(String lastRunTime) {
            this.lastRunTime = lastRunTime;
        }

        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public List<JobNode> getNextList() {
            return nextList;
        }

        public void setNextList(List<JobNode> nextList) {
            this.nextList = nextList;
        }
    }
}
