package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.dqc.dao.MonitorHistoryDao;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorHistoryQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorHistoryService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据质量监控历史记录表(MonitorHistory)表服务实现类
 *
 * @author makejava
 * @since 2022-07-15 14:16:16
 */
@Service("monitorHistoryService")
public class MonitorHistoryServiceImpl implements MonitorHistoryService {
    @Resource
    private MonitorHistoryDao monitorHistoryDao;

    @Autowired
    private MonitorRuleService monitorRuleService;

    @Override
    public List<MonitorHistory> getHistorys(Long baselineId, Long ruleId, Integer days) {
        if (days == null) {
            days = 30;
        }

        if(ruleId != null){
            MonitorRuleVO ruleVO = monitorRuleService.getById(ruleId);
            return monitorHistoryDao.getByRuleId(ruleId, ruleVO.getVersion(), days);
        }

        return monitorHistoryDao.getByBaselineId(baselineId,days);

    }

    @Override
    public MonitorHistory insert(MonitorHistory monitorHistory) {
        monitorHistoryDao.insert(monitorHistory);
        return monitorHistory;
    }

}
