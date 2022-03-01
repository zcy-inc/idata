package cn.zhengcaiyun.idata.portal.model.request.job;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobInfoUpdateRequest extends BaseDto {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空")
    private Long id;
    /**
     * 作业名称
     */
    private String name;
    /**
     * 作业类型
     */
    private String jobType;
    /**
     * 同步方式
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

    public String getSyncMode() {
        return syncMode;
    }

    public void setSyncMode(String syncMode) {
        this.syncMode = syncMode;
    }
}