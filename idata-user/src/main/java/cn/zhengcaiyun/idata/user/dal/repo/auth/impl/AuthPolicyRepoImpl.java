package cn.zhengcaiyun.idata.user.dal.repo.auth.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.user.dal.dao.auth.AuthPolicyDao;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthPolicyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.user.dal.dao.auth.AuthPolicyDynamicSqlSupport.AUTH_POLICY;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Repository
public class AuthPolicyRepoImpl implements AuthPolicyRepo {
    private final AuthPolicyDao authPolicyDao;

    @Autowired
    public AuthPolicyRepoImpl(AuthPolicyDao authPolicyDao) {
        this.authPolicyDao = authPolicyDao;
    }

    @Override
    public Long save(AuthPolicy authPolicy) {
        int ret = authPolicyDao.insertSelective(authPolicy);
        if (ret > 0) return authPolicy.getId();
        return null;
    }

    @Override
    public Optional<AuthPolicy> query(Long id) {
        Optional<AuthPolicy> optional = authPolicyDao.selectByPrimaryKey(id);
        if (optional.isPresent() && optional.get().getDel().equals(DeleteEnum.DEL_NO.val)) {
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public List<AuthPolicy> queryListByAuthId(Long authRecordId) {
        return authPolicyDao.select(dsl -> dsl.where(AUTH_POLICY.authRecordId, isEqualTo(authRecordId),
                and(AUTH_POLICY.del, isEqualTo(DeleteEnum.DEL_NO.val))).orderBy(AUTH_POLICY.id));
    }

    @Override
    public Boolean deleteByAuthId(Long authRecordId) {
        authPolicyDao.delete(dsl -> dsl.where(AUTH_POLICY.authRecordId, isEqualTo(authRecordId)));
        return Boolean.TRUE;
    }
}
