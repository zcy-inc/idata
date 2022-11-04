package cn.zhengcaiyun.idata.user.dto;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import org.springframework.beans.BeanUtils;

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

    public static GroupUserRelationDto from(GroupUserRelation relation) {
        GroupUserRelationDto dto = new GroupUserRelationDto();
        BeanUtils.copyProperties(relation, dto);
        return dto;
    }

    public GroupUserRelation toModel() {
        GroupUserRelation relation = new GroupUserRelation();
        BeanUtils.copyProperties(this, relation);
        return relation;
    }
}
