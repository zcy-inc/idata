package cn.zhengcaiyun.idata.develop.dto.dag;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

/**
 * @description: dag信息
 * @author: yangjianhua
 * @create: 2021-09-15 16:07
 **/
public class DAGInfoDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

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
}