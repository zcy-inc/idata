package cn.zhengcaiyun.idata.user.dal.repo.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.dal.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Repository
public class UserRepoImpl implements UserRepo {

    private final UacUserDao userDao;

    @Autowired
    public UserRepoImpl(UacUserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public List<UacUser> queryList() {
        return userDao.select(dsl -> dsl.where(uacUser.del, isEqualTo(DeleteEnum.DEL_NO.val)));
    }

    @Override
    public Optional<UacUser> query(Long id) {
        Optional<UacUser> optional = userDao.selectByPrimaryKey(id);
        if (optional.isEmpty()) return optional;

        if (optional.get().getDel().equals(DeleteEnum.DEL_YES.val)) return Optional.empty();
        return optional;
    }

    @Override
    public List<UacUser> query(List<Long> ids) {
        return userDao.select(dsl -> dsl.where(uacUser.id, isIn(ids),
                        and(uacUser.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(uacUser.id.descending()));
    }
}
