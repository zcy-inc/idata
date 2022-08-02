package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorBaseline;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorBaselineQuery;

/**
 * 数据质量基线表(MonitorBaseline)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-06 14:01:50
 */
public interface MonitorBaselineDao {
    MonitorBaseline getById(Long id);
    MonitorBaseline getByRuleId(@Param("ruleId") Long ruleId);

    List<MonitorBaseline> getByPage(MonitorBaselineQuery query);

    int count(MonitorBaselineQuery query);

    int insert(MonitorBaseline monitorBaseline);

    int update(MonitorBaseline monitorBaseline);

}

