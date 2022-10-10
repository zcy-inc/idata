package cn.zhengcaiyun.idata.user.dal.repo;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.user.condition.GroupCondition;
import cn.zhengcaiyun.idata.user.dal.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepo {
    Page<Group> paging(GroupCondition condition);

    List<Group> queryList(GroupCondition condition, long limit, long offset);

    long count(GroupCondition condition);

    List<Group> queryList(GroupCondition condition);

    Long save(Group group);

    Boolean update(Group group);

    Optional<Group> query(Long id);

}
