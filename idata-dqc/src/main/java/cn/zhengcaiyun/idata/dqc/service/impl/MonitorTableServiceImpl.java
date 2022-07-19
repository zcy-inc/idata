package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.dao.MonitorTableDao;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTableQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据质量被监控的表(DqcMonitorTable)表服务实现类
 *
 * @author zheng
 * @since 2022-06-28 11:01:50
 */
@Service("dqcMonitorTableService")
public class MonitorTableServiceImpl implements MonitorTableService {
    @Resource
    private MonitorTableDao monitorTableDao;

    @Autowired
    private MonitorRuleService monitorRuleService;

    @Override
    public MonitorTableVO getById(Long id) {
        MonitorTableVO vo = Converter.MONITOR_TABLE_CONVERTER.toVo(monitorTableDao.getById(id));
        return vo;
    }

    @Override
    public List<MonitorTableVO> getByBaselineId(Long baselineId) {
        return monitorTableDao.getByBaselineId(baselineId);
    }

    @Override
    public PageResult<MonitorTableVO> getByPage(MonitorTableQuery query) {
        int count = monitorTableDao.count(query);
        PageResult page = new PageResult();
        page.setCurPage(query.getCurPage());
        page.setPageSize(query.getPageSize());
        page.setTotalPages(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorTable> list = monitorTableDao.getByPage(query);
        List<String> tableList = new ArrayList<>();
        for (MonitorTable monitorTable : list) {
            tableList.add(monitorTable.getTableName());
        }

        HashMap<String, MonitorTableVO> tableMap = monitorRuleService.getRuleCountByTableName(tableList);
        List<MonitorTableVO> voList = new ArrayList<>();
        for (MonitorTable monitorTable : list) {
            MonitorTableVO vo = Converter.MONITOR_TABLE_CONVERTER.toVo(monitorTable);
            Integer ruleCount = tableMap.get(vo.getTableName()) == null ? 0 : tableMap.get(vo.getTableName()).getRuleCount();
            vo.setRuleCount(ruleCount);
            voList.add(vo);
        }
        page.setData(voList);

        return page;
    }

    @Override
    public MonitorTableVO insert(MonitorTableVO vo) {
        MonitorTable monitorTable = Converter.MONITOR_TABLE_CONVERTER.toDto(vo);

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorTable.setCreator(nickname);
        monitorTable.setEditor(nickname);

        monitorTable.setAccessTime("");

        monitorTableDao.insert(monitorTable);
        vo.setId(monitorTable.getId());
        return vo;
    }

    @Override
    public boolean update(MonitorTableVO vo) {
        MonitorTable monitorTable = Converter.MONITOR_TABLE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorTable.setEditor(nickname);
        monitorTableDao.update(monitorTable);
        return true;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public Result<Boolean> delById(Long id, Boolean isBaseline) {
        MonitorTable tableVO = monitorTableDao.getById(id);
        if (tableVO == null) {
            return Result.failureResult("该表不存在");
        }

        if (!isBaseline) {
            String tableName = tableVO.getTableName();
            monitorRuleService.del(-1L, tableName);
        }

        MonitorTable monitorTable = new MonitorTable();
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorTable.setEditor(nickname);
        monitorTable.setId(id);
        monitorTable.setDel(1);

        monitorTableDao.update(monitorTable);
        return Result.successResult();
    }

    @Override
    public int delByBaselineId(Long id) {
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        return monitorTableDao.delByBaselineId(id, nickname);
    }

    @Override
    public HashMap<Long, MonitorBaselineVO> getTableCountByBaselineId(List<Long> baselineIdList) {
        return monitorTableDao.getTableCountByBaselineId(baselineIdList);
    }
}
