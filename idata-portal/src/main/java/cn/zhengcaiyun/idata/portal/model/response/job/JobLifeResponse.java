package cn.zhengcaiyun.idata.portal.model.response.job;

import java.util.Date;

/**
 * @author zhanqian
 * @date 2022/9/28 11:47 AM
 * @description
 */
public class JobLifeResponse {

    /**
     *   作业id
     */
    private Long id;
    /**
     *   作业名称
     */
    private String name;
    /**
     *   作业类型
     */
    private String jobType;
    /**
     *   数仓分层
     */
    private String dwLayerCode;
    /**
     *   作业有效截止时间
     */
    private Date activityEnd;
    /**
     *   作业上线环境
     */
    private String env;
    /**
     *   创建者（负责人）
     */
    private String creator;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
