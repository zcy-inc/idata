package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.constant.enums.DsJobStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class JobTreeNodeDto {

    public JobTreeNodeDto(Long jobId, String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
    }

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 下游节点
     */
    private List<JobTreeNodeDto> children;

    /**
     * 是否高亮
     */
    private Boolean highLight;

    /**
     *  树层级
     */
    private Integer level;

    /**
     * 任务状态
     * @see DsJobStatusEnum
     */
    private Integer jobStatus;

    /**
     * 最后运行时间
     */
    private String lastRunTime;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * relation: prev/next  前继/后继节点关系
     */
    private String relation;

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

    public List<JobTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<JobTreeNodeDto> children) {
        this.children = children;
    }

    public Boolean getHighLight() {
        return highLight;
    }

    public void setHighLight(Boolean highLight) {
        this.highLight = highLight;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void addNextListElem(JobTreeNodeDto jobTreeNodeDto) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(jobTreeNodeDto);
    }
}
