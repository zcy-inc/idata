package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorHistoryVO;
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

    boolean updateAccessTime(Long ruleId, String accessTime);

    Result<Boolean> updateByTemplateId(MonitorRuleVO monitorRule);

    PageResult<List<MonitorRuleVO>> getMonitorRules(MonitorRuleQuery query);

    List<MonitorRuleVO> getByBaselineId(Long baselineId, Integer status);

    HashMap<String, MonitorTableVO> getRuleCountByTableName(List<String> tables, Long baselineId);

    HashMap<Long, MonitorTableVO> getRuleCountByBaselineId(List<Long> baselineIdList);

    int getCount(MonitorRuleQuery query);

    MonitorRuleVO getById(Long id);

    boolean delById(Long id);

    boolean del(Long baselineId, String tableName);

    boolean setStatus(Long id, Integer status, String nickname);

    MonitorHistoryVO getRuleQueryCount(MonitorRuleVO rule);

    void initHistoryByRule(Long id, String nickname);

    List<MonitorRuleVO> getScheduleRuleList(List<Long> templateIdList, Integer startIndex);

    List<MonitorRuleVO> getBaselineScheduleRuleList(List<Long> templateIdList, Integer startIndex);

    void analyse(Long jobId);

    void tryRun(Long id, Long baselineId, String nickname);

}
