package cn.zhengcaiyun.idata.user.dal.repo.auth;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;

import java.util.List;
import java.util.Optional;

public interface AuthPolicyRepo {

    Long save(AuthPolicy authPolicy);

    Optional<AuthPolicy> query(Long id);

    List<AuthPolicy> queryListByAuthId(Long authRecordId);

    Boolean deleteByAuthId(Long authRecordId);
}
