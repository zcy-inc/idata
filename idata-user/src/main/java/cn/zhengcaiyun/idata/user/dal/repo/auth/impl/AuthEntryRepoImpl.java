package cn.zhengcaiyun.idata.user.dal.repo.auth.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.user.dal.dao.auth.AuthEntryDao;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.zhengcaiyun.idata.user.dal.dao.auth.AuthEntryDynamicSqlSupport.AUTH_ENTRY;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Repository
public class AuthEntryRepoImpl implements AuthEntryRepo {
    private final AuthEntryDao authEntryDao;

    @Autowired
    public AuthEntryRepoImpl(AuthEntryDao authEntryDao) {
        this.authEntryDao = authEntryDao;
    }

    @Override
    public Long save(AuthEntry authEntry) {
        int ret = authEntryDao.insertSelective(authEntry);
        if (ret > 0) return authEntry.getId();
        return null;
    }

    @Override
    public Boolean update(AuthEntry authEntry) {
        authEntryDao.updateByPrimaryKeySelective(authEntry);
        return Boolean.TRUE;
    }

    @Override
    public Optional<AuthEntry> query(Long id) {
        Optional<AuthEntry> optional = authEntryDao.selectByPrimaryKey(id);
        if (optional.isPresent() && optional.get().getDel().equals(DeleteEnum.DEL_NO.val)) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthEntry> query(String subjectType, String subjectId) {
        return authEntryDao.selectOne(dsl -> dsl.where(AUTH_ENTRY.subjectId, isEqualTo(subjectId),
                        and(AUTH_ENTRY.subjectType, isEqualTo(subjectType)),
                        and(AUTH_ENTRY.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(AUTH_ENTRY.id.descending()));
    }
}
