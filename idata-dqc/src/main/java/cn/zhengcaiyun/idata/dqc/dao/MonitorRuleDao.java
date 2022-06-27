package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;

public interface MonitorRuleDao {
    int deleteByPrimaryKey(Long id);

    int insert(MonitorRule record);

    int insertSelective(MonitorRule record);

    MonitorRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MonitorRule record);

    int updateByPrimaryKey(MonitorRule record);
}