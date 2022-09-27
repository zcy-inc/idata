package cn.zhengcaiyun.idata.portal.model.request.job;

import javax.validation.constraints.NotNull;

/**
 * @author zhanqian
 * @date 2022/9/27 3:36 PM
 * @description
 */
public class JobQueryRequest {

    /**
     * 模糊作业名称匹配
     */
    private String name;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 查询的作业是否过期
     */
    @NotNull
    private Boolean overdue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }
}
