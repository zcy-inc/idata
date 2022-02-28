package cn.zhengcaiyun.idata.portal.model.response.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobInfoResponse extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 作业名称
     */
    private String name;
    /**
     * 作业类型
     */
    private JobTypeEnum jobType;

    private String jobTypeDesc;

    private String syncModeDesc;
    /**
     * 数仓分层
     */
    private String dwLayerCode;
    /**
     * 状态，1启用，0停用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 文件夹id
     */
    private Long folderId;

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

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getJobTypeDesc() {
        return jobTypeDesc;
    }

    public void setJobTypeDesc(String jobTypeDesc) {
        this.jobTypeDesc = jobTypeDesc;
    }

    public String getSyncModeDesc() {
        return syncModeDesc;
    }

    public void setSyncModeDesc(String syncModeDesc) {
        this.syncModeDesc = syncModeDesc;
    }
}