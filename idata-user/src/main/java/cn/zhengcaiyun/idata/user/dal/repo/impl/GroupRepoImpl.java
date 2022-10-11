package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.user.condition.GroupCondition;
import cn.zhengcaiyun.idata.user.dal.dao.GroupDao;
import cn.zhengcaiyun.idata.user.dal.model.Group;
import cn.zhengcaiyun.idata.user.dal.repo.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.user.dal.dao.GroupDynamicSqlSupport.GROUP;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Repository
public class GroupRepoImpl implements GroupRepo {

    private final GroupDao groupDao;

    @Autowired
    public GroupRepoImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public Page<Group> paging(GroupCondition condition) {
        long total = count(condition);
        List<Group> recordList = null;
        if (total > 0) {
            recordList = queryList(condition, condition.getLimit(), condition.getOffset());
        }
        return Page.newOne(recordList, total);
    }

    @Override
    public List<Group> queryList(GroupCondition condition, long limit, long offset) {
        return groupDao.select(dsl -> dsl.where(
                        GROUP.name, isLikeWhenPresent(condition.getNamePattern()).map(MybatisHelper::appendWildCards),
                        and(GROUP.del, isEqualTo(DeleteEnum.DEL_NO.val))
                ).orderBy(GROUP.editTime.descending())
                .limit(limit).offset(offset));
    }

    @Override
    public long count(GroupCondition condition) {
        return groupDao.count(dsl -> dsl.where(
                GROUP.name, isLikeWhenPresent(condition.getNamePattern()).map(MybatisHelper::appendWildCards),
                and(GROUP.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ));
    }

    @Override
    public List<Group> queryList(GroupCondition condition) {
        return groupDao.select(dsl -> dsl.where(
                GROUP.name, isLikeWhenPresent(condition.getNamePattern()).map(MybatisHelper::appendWildCards),
                and(GROUP.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ).orderBy(GROUP.id.descending()));
    }

    @Override
    public Long save(Group group) {
        int ret = groupDao.insertSelective(group);
        if (ret > 0) {
            return group.getId();
        }
        return null;
    }

    @Override
    public Boolean update(Group group) {
        groupDao.updateByPrimaryKeySelective(group);
        return Boolean.TRUE;
    }

    @Override
    public Optional<Group> query(Long id) {
        Optional<Group> optional = groupDao.selectByPrimaryKey(id);
        if (optional.isEmpty()) return optional;

        if (DeleteEnum.DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public Boolean delete(Long id, String operator) {
        int ret = groupDao.update(dsl -> dsl.set(GROUP.del).equalTo(DEL_YES.val)
                .set(GROUP.editor).equalTo(operator)
                .where(GROUP.id, isEqualTo(id)));
        return ret > 0;
    }
}
