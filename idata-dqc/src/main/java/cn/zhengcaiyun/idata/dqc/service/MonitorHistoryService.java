package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;

import java.util.List;

/**
 * 数据质量监控历史记录表(MonitorHistory)表服务接口
 *
 * @author zheng
 * @since 2022-07-15 14:16:16
 */
public interface MonitorHistoryService {

    List<MonitorHistory> getHistorys(Long baselineId, Long ruleId, Integer days);

    MonitorHistory insert(MonitorHistory monitorHistory);

}
