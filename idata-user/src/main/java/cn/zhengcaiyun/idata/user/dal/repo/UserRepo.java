package cn.zhengcaiyun.idata.user.dal.repo;

import cn.zhengcaiyun.idata.user.dal.model.UacUser;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

    List<UacUser> queryList();

    Optional<UacUser> query(Long id);

    List<UacUser> query(List<Long> ids);
}
