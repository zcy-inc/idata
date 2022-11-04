package cn.zhengcaiyun.idata.user.dal.repo.auth;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;

import java.util.List;

public interface AuthResourceRepo {

    int save(List<AuthResource> authResourceList);

    List<AuthResource> queryListByPolicyId(Long policyRecordId);

    List<AuthResource> queryListByAuthId(Long authRecordId);

    Boolean deleteByPolicyId(Long policyRecordId);

    Boolean deleteByAuthId(Long authRecordId);

}
