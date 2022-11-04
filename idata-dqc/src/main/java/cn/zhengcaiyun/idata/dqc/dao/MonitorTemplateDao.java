package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTemplate;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTemplateQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTemplateVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 数据质量模板表(DqcMonitorTemplate)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-05 11:14:55
 */
public interface MonitorTemplateDao {
    MonitorTemplate getById(Long id);

    List<MonitorTemplate> getByPage(MonitorTemplateQuery query);

    int count(MonitorTemplateQuery query);

    int insert(MonitorTemplate dqcMonitorTemplate);

    int update(MonitorTemplate dqcMonitorTemplate);
    MonitorTemplateVO getByRuleId(@Param("ruleId") Long ruleId);

}

