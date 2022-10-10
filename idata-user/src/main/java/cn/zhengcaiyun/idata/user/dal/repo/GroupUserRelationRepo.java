package cn.zhengcaiyun.idata.user.dal.repo;

import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;

import java.util.List;

public interface GroupUserRelationRepo {

    int save(List<GroupUserRelation> relationList);

    int delete(Long groupId);

    List<GroupUserRelation> queryByGroup(Long groupId);

    List<GroupUserRelation> queryByUser(Long userId);
}
