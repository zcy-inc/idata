package cn.zhengcaiyun.idata.user.dal.dao;

import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UacUserMyDao {

    /**
     * 存在则忽略，不存在则插入
     * @param uacUser
     * @return
     */
    int insertIgnore(UacUser uacUser);
}