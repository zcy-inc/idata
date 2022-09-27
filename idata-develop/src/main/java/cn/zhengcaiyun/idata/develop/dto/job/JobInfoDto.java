package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobInfoDto extends BaseDto {
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
    /**
     *   作业有效截止时间
     */
    private Date activityEnd;

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

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public static JobInfoDto from(JobInfo info) {
        JobInfoDto dto = new JobInfoDto();
        BeanUtils.copyProperties(info, dto);
        Optional<JobTypeEnum> typeEnumOptional = JobTypeEnum.getEnum(info.getJobType());
        typeEnumOptional.ifPresent(typeEnum -> dto.setJobType(typeEnum));
        return dto;
    }

    public JobInfo toModel() {
        JobInfo info = new JobInfo();
        BeanUtils.copyProperties(this, info);
        info.setJobType(Objects.isNull(this.jobType) ? null : this.jobType.getCode());
        return info;
    }
}