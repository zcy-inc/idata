package cn.zhengcaiyun.idata.user.dto;

import cn.zhengcaiyun.idata.user.dal.model.Group;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class GroupCombinedDto extends GroupDto {

    List<UserSimpleDto> relatedUsers;

    public List<UserSimpleDto> getRelatedUsers() {
        return relatedUsers;
    }

    public void setRelatedUsers(List<UserSimpleDto> relatedUsers) {
        this.relatedUsers = relatedUsers;
    }

    public static GroupCombinedDto from(Group group) {
        GroupCombinedDto dto = new GroupCombinedDto();
        BeanUtils.copyProperties(group, dto);
        return dto;
    }
}
