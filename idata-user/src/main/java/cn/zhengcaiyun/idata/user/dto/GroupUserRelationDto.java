package cn.zhengcaiyun.idata.user.dto;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

public class GroupUserRelationDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 组id
     */
    private Long groupId;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户
     */
    private Long userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserName() {
        return userName;
    }

    public void setUserName(Long userName) {
        this.userName = userName;
    }
}
