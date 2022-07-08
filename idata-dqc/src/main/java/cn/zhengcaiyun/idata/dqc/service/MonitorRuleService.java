package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;

import java.util.HashMap;
import java.util.List;

/**
 * author:zheng
 * Date:2022/6/23
 */
public interface MonitorRuleService {
    Result<MonitorRuleVO> add(MonitorRuleVO monitorRule);

    Result<MonitorRuleVO> update(MonitorRuleVO monitorRule);
//    Boolean update(MonitorRule monitorRule);

    Result<Boolean> updateByTemplateId(MonitorRuleVO monitorRule);

    PageResult<List<MonitorRuleVO>> getMonitorRules(MonitorRuleQuery query);

    HashMap<String, MonitorTableVO> getRuleCountByTableName(List<String> tables);

    HashMap<Long, MonitorTableVO> getRuleCountByBaselineId(List<Long> baselineIdList);

    int getCount(MonitorRuleQuery query);

    MonitorRuleVO getById(Long id);

    boolean delById(Long id);

    boolean del(Long baselineId, String tableName);

    boolean setStatus(Long id, Integer status);

    List<MonitorRuleVO> getScheduleRuleList(List<String> typeList, Integer startIndex, boolean isBaseline);
}
