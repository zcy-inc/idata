package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTemplate;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTemplateQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTemplateVO;
import org.apache.ibatis.annotations.Param;

/**
 * 数据质量模板表(DqcMonitorTemplate)表服务接口
 *
 * @author makejava
 * @since 2022-07-04 11:02:57
 */
public interface MonitorTemplateService {

    MonitorTemplateVO getById(Long id);

    PageResult<MonitorTemplate> getByPage(MonitorTemplateQuery query);

    Result<MonitorTemplateVO> insert(MonitorTemplateVO monitorTemplate);

    MonitorTemplateVO update(MonitorTemplateVO monitorTemplate);

    boolean setStatus(Long id, Integer status);

    void check(MonitorTemplateVO monitorTemplate);

    MonitorTemplateVO getByRuleId(Long ruleId);

}
