package cn.zhengcaiyun.idata.user.service.impl;

import cn.zhengcaiyun.idata.user.dal.repo.GroupRepo;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import cn.zhengcaiyun.idata.user.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepo groupRepo;
    private final GroupUserRelationRepo groupUserRelationRepo;

    @Autowired
    public GroupServiceImpl(GroupRepo groupRepo, GroupUserRelationRepo groupUserRelationRepo) {
        this.groupRepo = groupRepo;
        this.groupUserRelationRepo = groupUserRelationRepo;
    }
}
