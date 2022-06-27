package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.dqc.dao.MonitorRuleDao;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author:zheng
 * Date:2022/6/23
 */
@Service
public class MonitorRuleServiceImpl implements MonitorRuleService {
    @Autowired
    private MonitorRuleDao monitorRuleDao;

    @Override
    public boolean add(MonitorRuleVO monitorRule) {
        MonitorRule list = monitorRuleDao.selectByPrimaryKey(1L);
        return false;
    }
}
