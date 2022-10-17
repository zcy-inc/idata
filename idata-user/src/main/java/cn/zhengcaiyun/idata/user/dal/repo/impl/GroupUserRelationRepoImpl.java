package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationCustomizeDao;
import cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDao;
import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDynamicSqlSupport.GROUP_USER_RELATION;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

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
    @Transactional
    public int saveWithOverride(Long groupId, List<GroupUserRelation> relationList) {
        groupUserRelationDao.delete(dsl -> dsl.where(GROUP_USER_RELATION.groupId, isEqualTo(groupId)));

        if (relationList == null || relationList.isEmpty()) return 0;
        save(relationList);
        return relationList.size();
    }

    @Override
    public int delete(Long groupId, String operator) {
        return groupUserRelationDao.update(dsl -> dsl.set(GROUP_USER_RELATION.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(GROUP_USER_RELATION.editor).equalTo(operator)
                .where(GROUP_USER_RELATION.groupId, isEqualTo(groupId)));
    }

    @Override
    public List<GroupUserRelation> queryByGroup(Long groupId) {
        return groupUserRelationDao.select(dsl -> dsl.where(GROUP_USER_RELATION.groupId, isEqualTo(groupId),
                and(GROUP_USER_RELATION.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<GroupUserRelation> queryByGroup(List<Long> groupIds) {
        return groupUserRelationDao.select(dsl -> dsl.where(GROUP_USER_RELATION.groupId, isIn(groupIds),
                and(GROUP_USER_RELATION.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<GroupUserRelation> queryByUser(Long userId) {
        return groupUserRelationDao.select(dsl -> dsl.where(GROUP_USER_RELATION.userId, isEqualTo(userId),
                and(GROUP_USER_RELATION.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }
}
