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
     * 此处的jobType和JobTypeEnum的name不相同，枚举值为DI/BACK_FLOW
     */
    private String jobType;

    /**
     * 作业类型：仅当渲染DI作业的时候，和jobType值有区别，为了前端页面渲染，jobType为DI而jobTypeEnum是实际值
     */
    private JobTypeEnum jobTypeEnum;

    /**
     * BATCH/STREAM
     */
    private String syncMode;
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

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSyncMode() {
        return syncMode;
    }

    public void setSyncMode(String syncMode) {
        this.syncMode = syncMode;
    }

    public JobTypeEnum getJobTypeEnum() {
        return jobTypeEnum;
    }

    public void setJobTypeEnum(JobTypeEnum jobTypeEnum) {
        this.jobTypeEnum = jobTypeEnum;
    }
}