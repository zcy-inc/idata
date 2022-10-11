package cn.zhengcaiyun.idata.user.service;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.user.condition.GroupCondition;
import cn.zhengcaiyun.idata.user.dto.GroupCombinedDto;

public interface GroupService {

    Page<GroupCombinedDto> paging(GroupCondition condition);

    Long addGroup(GroupCombinedDto dto, Operator operator);

    Boolean editGroup(Long id, GroupCombinedDto dto, Operator operator);

    GroupCombinedDto getGroup(Long id);

    Boolean deleteGroup(Long id, Operator operator);

}
