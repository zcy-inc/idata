package cn.zhengcaiyun.idata.user.dal.repo.auth.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.user.dal.dao.auth.AuthResourceCustomizeDao;
import cn.zhengcaiyun.idata.user.dal.dao.auth.AuthResourceDao;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthResourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.zhengcaiyun.idata.user.dal.dao.auth.AuthResourceDynamicSqlSupport.AUTH_RESOURCE;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Repository
public class AuthResourceRepoImpl implements AuthResourceRepo {
    private final AuthResourceDao authResourceDao;
    private final AuthResourceCustomizeDao authResourceCustomizeDao;

    @Autowired
    public AuthResourceRepoImpl(AuthResourceDao authResourceDao,
                                AuthResourceCustomizeDao authResourceCustomizeDao) {
        this.authResourceDao = authResourceDao;
        this.authResourceCustomizeDao = authResourceCustomizeDao;
    }

    @Override
    public int save(List<AuthResource> authResourceList) {
        return authResourceCustomizeDao.insertMultiple(authResourceList);
    }

    @Override
    public List<AuthResource> queryListByPolicyId(Long policyRecordId) {
        return authResourceDao.select(dsl -> dsl.where(AUTH_RESOURCE.policyRecordId, isEqualTo(policyRecordId),
                        and(AUTH_RESOURCE.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(AUTH_RESOURCE.id));
    }

    @Override
    public List<AuthResource> queryListByAuthId(Long authRecordId) {
        return authResourceDao.select(dsl -> dsl.where(AUTH_RESOURCE.authRecordId, isEqualTo(authRecordId),
                        and(AUTH_RESOURCE.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(AUTH_RESOURCE.id));
    }

    @Override
    public Boolean deleteByPolicyId(Long policyRecordId) {
        authResourceDao.delete(dsl -> dsl.where(AUTH_RESOURCE.policyRecordId, isEqualTo(policyRecordId)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteByAuthId(Long authRecordId) {
        authResourceDao.delete(dsl -> dsl.where(AUTH_RESOURCE.authRecordId, isEqualTo(authRecordId)));
        return Boolean.TRUE;
    }
}
