package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDao;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GroupUserRelationRepoImpl implements GroupUserRelationRepo {
    private final GroupUserRelationDao groupUserRelationDao;

    @Autowired
    public GroupUserRelationRepoImpl(GroupUserRelationDao groupUserRelationDao) {
        this.groupUserRelationDao = groupUserRelationDao;
    }
}
