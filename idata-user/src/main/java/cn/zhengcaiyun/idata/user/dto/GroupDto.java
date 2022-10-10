package cn.zhengcaiyun.idata.user.dto;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

public class GroupDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组负责人id
     */
    private Long ownerId;

    /**
     * 组负责人
     */
    private String ownerName;

    /**
     * 备注
     */
    private String remark;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
