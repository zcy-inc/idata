package cn.zhengcaiyun.idata.user.dto;

import java.util.List;

public class GroupCombinedDto extends GroupDto {

    List<UserSimpleDto> relatedUsers;

    public List<UserSimpleDto> getRelatedUsers() {
        return relatedUsers;
    }

    public void setRelatedUsers(List<UserSimpleDto> relatedUsers) {
        this.relatedUsers = relatedUsers;
    }
}
