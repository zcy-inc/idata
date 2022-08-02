package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorBaseline;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorBaselineQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;

/**
 * 数据质量基线表(MonitorBaseline)表服务接口
 *
 * @author makejava
 * @since 2022-07-06 14:01:50
 */
public interface MonitorBaselineService {

    MonitorBaselineVO getById(Long id);

    MonitorBaseline getByRuleId(Long ruleId);

    PageResult<MonitorBaselineVO> getByPage(MonitorBaselineQuery query);

    MonitorBaselineVO insert(MonitorBaselineVO monitorBaseline);

    boolean update(MonitorBaselineVO monitorBaseline);

    boolean setStatus(Long id, Integer status);

    boolean delById(Long id);

}
