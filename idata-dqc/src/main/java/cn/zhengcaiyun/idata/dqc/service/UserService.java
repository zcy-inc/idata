package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.entity.User;
import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author zheng
 * @since 2022-07-15 17:34:00
 */
public interface UserService {

    List<User> getList(String name);

}
