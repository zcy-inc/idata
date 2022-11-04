package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.dao.MonitorBaselineDao;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorBaseline;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorBaselineQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorBaselineService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTableService;
import cn.zhengcaiyun.idata.dqc.utils.ExecutorServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 数据质量基线表(MonitorBaseline)表服务实现类
 *
 * @author makejava
 * @since 2022-07-06 14:01:50
 */
@Service("monitorBaselineService")
public class MonitorBaselineServiceImpl implements MonitorBaselineService {
    @Resource
    private MonitorBaselineDao monitorBaselineDao;

    @Autowired
    private MonitorRuleService monitorRuleService;

    @Autowired
    private MonitorTableService monitorTableService;

    @Override
    public MonitorBaselineVO getById(Long id) {
        return Converter.MONITOR_BASELINE_CONVERTER.toVo(monitorBaselineDao.getById(id));
    }

    @Override
    public MonitorBaseline getByRuleId(Long ruleId) {
        return monitorBaselineDao.getByRuleId(ruleId);
    }

    @Override
    public List<MonitorBaseline> getByTemplateId(Long templateId, Integer status) {
        return monitorBaselineDao.getByTemplateId(templateId,status);
    }

    @Override
    public PageResult<MonitorBaselineVO> getByPage(MonitorBaselineQuery query) {
        int count = monitorBaselineDao.count(query);
        PageResult page = new PageResult();
        page.setCurPage(query.getCurPage());
        page.setPageSize(query.getPageSize());
        page.setTotalElements(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorBaseline> list = monitorBaselineDao.getByPage(query);
        List<Long> baselineIdList = new ArrayList<>();
        list.forEach(item -> baselineIdList.add(item.getId()));

        HashMap<Long, MonitorTableVO> ruleMap = monitorRuleService.getRuleCountByBaselineId(baselineIdList);
        HashMap<Long, MonitorBaselineVO> tableMap = monitorTableService.getTableCountByBaselineId(baselineIdList);
        List<MonitorBaselineVO> voList = new ArrayList<>();
        for (MonitorBaseline baseline : list) {
            MonitorBaselineVO vo = Converter.MONITOR_BASELINE_CONVERTER.toVo(baseline);
            vo.setRuleCount(ruleMap.get(vo.getId()) == null ? 0 : ruleMap.get(vo.getId()).getRuleCount());
            vo.setTableCount(tableMap.get(vo.getId()) == null ? 0 : tableMap.get(vo.getId()).getTableCount());
            voList.add(vo);
        }
        page.setData(voList);

        return page;

    }

    @Override
    public MonitorBaselineVO insert(MonitorBaselineVO vo) {
        MonitorBaseline baseline = Converter.MONITOR_BASELINE_CONVERTER.toDto(vo);
        baseline.setStatus(0);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        baseline.setCreator(nickname);
        baseline.setEditor(nickname);

        monitorBaselineDao.insert(baseline);
        vo.setId(baseline.getId());
        return vo;
    }

    @Override
    public boolean update(MonitorBaselineVO vo) {
        MonitorBaseline baseline = Converter.MONITOR_BASELINE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        baseline.setEditor(nickname);

        monitorBaselineDao.update(baseline);
        return true;
    }

    @Override
    public boolean setStatus(Long id, Integer status) {
        MonitorBaselineVO monitorBaselineVO = new MonitorBaselineVO();
        monitorBaselineVO.setStatus(status);
        monitorBaselineVO.setId(id);
        this.update(monitorBaselineVO);

        //开启基线初始化规则历史数据
        ExecutorServiceHelper.submit(() -> this.initHistory(id, status));
        return true;
    }

    public void initHistory(Long id, Integer status) {
        if (status == 0) {
            return;
        }
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        List<MonitorRuleVO> ruleList = monitorRuleService.getByBaselineId(id, status);
        ruleList.parallelStream().forEach(rule -> {
            ExecutorServiceHelper.submit(()->monitorRuleService.initHistoryByRule(rule.getId(), nickname));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delById(Long id) {
        //删除表、规则
        monitorRuleService.del(id, null);
        monitorTableService.delByBaselineId(id);

        MonitorBaseline baseline = new MonitorBaseline();
        baseline.setId(id);
        baseline.setDel(1);
        baseline.setEditor(OperatorContext.getCurrentOperator().getNickname());
        return monitorBaselineDao.update(baseline) > 0 ? true : false;
    }
}
