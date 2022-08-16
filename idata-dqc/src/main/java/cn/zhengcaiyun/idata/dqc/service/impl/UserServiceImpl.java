package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.dqc.model.entity.User;
import cn.zhengcaiyun.idata.dqc.dao.UserDao;
import cn.zhengcaiyun.idata.dqc.model.entity.User;
import cn.zhengcaiyun.idata.dqc.service.UserService;
import org.springframework.stereotype.Service;
import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-07-15 17:34:00
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public List<User> getList(String name) {
        return userDao.getList(name);
    }
}
