package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.user.dal.dao.GroupDao;
import cn.zhengcaiyun.idata.user.dal.repo.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepoImpl implements GroupRepo {

    private final GroupDao groupDao;

    @Autowired
    public GroupRepoImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

}
