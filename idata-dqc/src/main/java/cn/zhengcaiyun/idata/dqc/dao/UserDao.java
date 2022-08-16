package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;

/**
 * 用户表(User)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-15 17:34:00
 */
public interface UserDao {
    List<User> getList(@Param("name") String name);

    //该接口涉及到手机隐私，严禁对外提供接口
    List<User> getMobilesByNickname(@Param("nicknames") String[] nicknames);

    List<User> getMobilesByUsername(@Param("usernames") String[] usernames);

}

