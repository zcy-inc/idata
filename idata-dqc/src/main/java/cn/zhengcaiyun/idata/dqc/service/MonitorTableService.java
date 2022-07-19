package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTableQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;

import java.util.HashMap;
import java.util.List;

/**
 * 数据质量被监控的表(DqcMonitorTable)表服务接口
 *
 * @author zheng
 * @since 2022-06-28 11:01:50
 */
public interface MonitorTableService {

    MonitorTableVO getById(Long id);
    List<MonitorTableVO> getByBaselineId(Long baselineId);

    PageResult<MonitorTableVO> getByPage(MonitorTableQuery query);

    MonitorTableVO insert(MonitorTableVO monitorTable);

    boolean update(MonitorTableVO monitorTable);

    Result<Boolean> delById(Long id, Boolean isBaseline);

    int delByBaselineId(Long id);

    HashMap<Long, MonitorBaselineVO> getTableCountByBaselineId(List<Long> baselineIdList);

}
