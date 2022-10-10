package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationCustomizeDao;
import cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDao;
import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDynamicSqlSupport.GROUP_USER_RELATION;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Repository
public class GroupUserRelationRepoImpl implements GroupUserRelationRepo {
    private final GroupUserRelationDao groupUserRelationDao;
    private final GroupUserRelationCustomizeDao groupUserRelationCustomizeDao;

    @Autowired
    public GroupUserRelationRepoImpl(GroupUserRelationDao groupUserRelationDao,
                                     GroupUserRelationCustomizeDao groupUserRelationCustomizeDao) {
        this.groupUserRelationDao = groupUserRelationDao;
        this.groupUserRelationCustomizeDao = groupUserRelationCustomizeDao;
    }

    @Override
    public int save(List<GroupUserRelation> relationList) {
        return groupUserRelationCustomizeDao.insertMultiple(relationList);
    }

    @Override
    public int delete(Long groupId) {
        return groupUserRelationDao.delete(dsl -> dsl.where(GROUP_USER_RELATION.groupId, isEqualTo(groupId)));
    }

    @Override
    public List<GroupUserRelation> queryByGroup(Long groupId) {
        return groupUserRelationDao.select(dsl -> dsl.where(GROUP_USER_RELATION.groupId, isEqualTo(groupId)));
    }

    @Override
    public List<GroupUserRelation> queryByUser(Long userId) {
        return groupUserRelationDao.select(dsl -> dsl.where(GROUP_USER_RELATION.userId, isEqualTo(userId)));
    }
}
