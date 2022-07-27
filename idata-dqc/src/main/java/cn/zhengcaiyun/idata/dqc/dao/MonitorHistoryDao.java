package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorHistoryQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;

/**
 * 数据质量监控历史记录表(MonitorHistory)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-14 20:18:15
 */
public interface MonitorHistoryDao {
    List<MonitorHistory> getByRuleId(@Param("ruleId") Long ruleId,@Param("version")String version,@Param("days")Integer days);

    List<MonitorHistory> getByBaselineId(@Param("baselineId") Long baselineId, @Param("days")Integer days);

    List<MonitorHistory> getByPage(MonitorHistoryQuery query);

    int count(MonitorHistoryQuery query);

    int insert(@Param("entity") MonitorHistory monitorHistory);

    int insertBatch(@Param("entities") List<MonitorHistory> entities);

    MonitorHistory getLatest(@Param("ruleId") Long ruleId, @Param("partition") String partition);

}

