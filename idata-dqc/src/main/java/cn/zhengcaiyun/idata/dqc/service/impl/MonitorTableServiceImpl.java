package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.dao.MonitorHistoryDao;
import cn.zhengcaiyun.idata.dqc.dao.MonitorTableDao;
import cn.zhengcaiyun.idata.dqc.dao.TableDao;
import cn.zhengcaiyun.idata.dqc.model.common.BizException;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.enums.RuleCheckTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.enums.RuleTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTableQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorHistoryVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorHistoryService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTableService;
import cn.zhengcaiyun.idata.dqc.utils.ExecutorServiceHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private TableDao tableDao;

    @Autowired
    private MonitorHistoryDao monitorHistoryDao;

    @Autowired
    private MessageSendService messageSendService;

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
        page.setTotalElements(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorTable> list = monitorTableDao.getByPage(query);
        List<String> tableList = new ArrayList<>();
        for (MonitorTable monitorTable : list) {
            tableList.add(monitorTable.getTableName());
        }

        //获取表对应的规则数
        HashMap<String, MonitorTableVO> tableMap = monitorRuleService.getRuleCountByTableName(tableList, query.getBaselineId());
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
    public Result<MonitorTableVO> insert(MonitorTableVO vo) {
        List<MonitorTable> oldList = monitorTableDao.getByTableName(vo.getTableName(), vo.getBaselineId());
        if (oldList.size() > 0) {
            return Result.failureResult("该表已经存在，请勿重复创建");
        }

        MonitorTable monitorTable = Converter.MONITOR_TABLE_CONVERTER.toDto(vo);

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorTable.setCreator(nickname);
        monitorTable.setEditor(nickname);

        monitorTable.setAccessTime("");
        monitorTable.setPartitionExpr(StringUtils.isEmpty(vo.getPartitionExpr()) ? "" : vo.getPartitionExpr());

        monitorTableDao.insert(monitorTable);
        vo.setId(monitorTable.getId());

        //基线表新增后需要初始化规则历史数据
        ExecutorServiceHelper.submit(() -> this.initTableHistory(vo, nickname));
        return Result.successResult(vo);
    }

    public void initTableHistory(MonitorTableVO vo, String nickname) {
        if (vo.getBaselineId() == -1) {
            return;
        }

        List<MonitorRuleVO> ruleList = monitorRuleService.getByBaselineId(vo.getBaselineId(), 1);
        for (MonitorRuleVO rule : ruleList) {
            rule.setPartitionExpr(vo.getPartitionExpr());
            rule.setTableName(vo.getTableName());

            if (!RuleCheckTypeEnum.PRE_PREIOD.getValue().equals(rule.getCheckType()) &&
                    !(RuleTypeEnum.CUSTOME.getValue().equals(rule.getRuleType()) || RuleTypeEnum.TEMPLATE.getValue().equals(rule.getRuleType()))) {
                continue;
            }

            MonitorHistoryVO historyVO = null;
            try {
                //校验sql是否正确
                historyVO = monitorRuleService.getRuleQueryCount(rule);

                //只有算上浮和下浮才初始化历史数据
                if (RuleCheckTypeEnum.PRE_PREIOD.getValue().equals(rule.getCheckType())) {
                    historyVO.setAlarm(0);
                    monitorHistoryDao.insert(Converter.MONITOR_HISTORY_CONVERTER.toDto(historyVO));
                }
            } catch (BizException e) {
                monitorRuleService.setStatus(vo.getId(), 0, nickname);
                String sql = historyVO == null ? "" : "执行sql[" + historyVO.getRunSql() + "]，";
                //sql报错则将规则关闭
                String message = String.format("你在数据质量上配置的规则[%s]校验错误，请检查配置/SQL是否正确，%s错误信息： %s", rule.getName(), sql, e.getMessage());
                messageSendService.sengDingdingByNickname(nickname,  message);
            }
        }

    }

    @Override
    public Result<Boolean> update(MonitorTableVO vo) {
        List<MonitorTable> oldList = monitorTableDao.getByTableName(vo.getTableName(), vo.getBaselineId());
        if (oldList.size() > 0) {
            for(MonitorTable old:oldList){
                if(!old.getId().equals(vo.getId())){
                    return Result.failureResult("该表已经存在，请勿重复创建");
                }
            }
        }
        MonitorTable monitorTable = Converter.MONITOR_TABLE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorTable.setEditor(nickname);
        monitorTableDao.updateFull(monitorTable);

        ExecutorServiceHelper.submit(() -> this.initTableHistory(vo, nickname));
        return Result.successResult();
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delById(Long id, Boolean isBaseline) {
        MonitorTable tableVO = monitorTableDao.getById(id);
        if (tableVO == null) {
            return Result.failureResult("该表不存在");
        }

        MonitorRuleQuery query = new MonitorRuleQuery();
        query.setStatus(1);
        query.setTableName(tableVO.getTableName());
        if (!isBaseline && monitorRuleService.getCount(query) > 0) {
            return Result.failureResult("该表有开启的规则，请停止规则后再删除");
        }

        //非基线中的表，删除不需要删规则
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
