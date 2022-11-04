package cn.zhengcaiyun.idata.user.dal.repo.auth;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;

import java.util.Optional;

public interface AuthEntryRepo {

    Long save(AuthEntry authEntry);

    Boolean update(AuthEntry authEntry);

    Optional<AuthEntry> query(Long id);

    Optional<AuthEntry> query(String subjectType, String subjectId);
}
