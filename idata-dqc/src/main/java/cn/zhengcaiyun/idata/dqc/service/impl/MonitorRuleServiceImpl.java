package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.encrypt.DigestUtil;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivyQueryDto;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySqlResultDto;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivyStatementDto;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivyOutputStatusEnum;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;
import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.dao.JobOutputDao;
import cn.zhengcaiyun.idata.dqc.dao.MonitorHistoryDao;
import cn.zhengcaiyun.idata.dqc.dao.MonitorRuleDao;
import cn.zhengcaiyun.idata.dqc.dao.MonitorTableDao;
import cn.zhengcaiyun.idata.dqc.model.common.BizException;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.enums.CompareTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorObjEnum;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorTemplateEnum;
import cn.zhengcaiyun.idata.dqc.model.enums.RuleTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.*;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTemplateService;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import cn.zhengcaiyun.idata.dqc.utils.DateUtils;
import cn.zhengcaiyun.idata.dqc.utils.ParameterUtils;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * author:zheng
 * Date:2022/6/23
 */
@Service
public class MonitorRuleServiceImpl implements MonitorRuleService {
    @Autowired
    private MonitorRuleDao monitorRuleDao;

    @Autowired
    private JobOutputDao jobOutputDao;

    @Autowired
    private LivyService livyService;

    @Autowired
    private MonitorHistoryDao monitorHistoryDao;

    @Autowired
    private TableService tableService;

    @Autowired
    private MonitorTableDao monitorTableDao;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private MonitorTemplateService monitorTemplateService;

    @Override
    @Transactional
    public Result<MonitorRuleVO> add(MonitorRuleVO vo) {
        MonitorRule monitorRule = Converter.MONITOR_RULE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorRule.setCreator(nickname);
        monitorRule.setEditor(nickname);
        monitorRule.setStatus(0);

        Result result = this.check(vo);
        if (!result.isSuccess()) {
            return result;
        }

        if (RuleTypeEnum.TEMPLATE.getValue().equals(vo.getRuleType())) {
            MonitorTemplateVO template = monitorTemplateService.getById(vo.getTemplateId());
            monitorRule.setContent(template.getContent());
        }

        monitorRule.setVersion(this.getRuleVersion(monitorRule));
        monitorRuleDao.insert(monitorRule);
        vo.setId(monitorRule.getId());
        return Result.successResult(vo);
    }

    @Override
    public Result<MonitorRuleVO> update(MonitorRuleVO vo) {
        MonitorRule monitorRule = Converter.MONITOR_RULE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorRule.setEditor(nickname);

        Result result = this.check(vo);
        if (!result.isSuccess()) {
            return result;
        }

        if (RuleTypeEnum.TEMPLATE.getValue().equals(vo.getRuleType())) {
            MonitorTemplateVO template = monitorTemplateService.getById(vo.getTemplateId());
            monitorRule.setContent(template.getContent());
        }

        monitorRule.setVersion(this.getRuleVersion(monitorRule));
        monitorRuleDao.update(monitorRule);
        return Result.successResult(vo);
    }

    @Override
    public boolean updateAccessTime(Long ruleId, String accessTime) {
        return monitorRuleDao.updateAccessTime(ruleId, accessTime) == 1 ? true : false;
    }


    private String getRuleVersion(MonitorRule rule) {
        return DigestUtil.md5(rule.getFieldName() + rule.getRuleType() + rule.getTemplateId()
                + rule.getMonitorObj() + rule.getCheckType() + rule.getCompareType()
                + rule.getContent() + rule.getFixValue() + rule.getRangeStart() + rule.getRangeEnd());
    }

    @Override
    public Result<Boolean> updateByTemplateId(MonitorRuleVO vo) {
        MonitorRule monitorRule = Converter.MONITOR_RULE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorRule.setEditor(nickname);

        monitorRuleDao.updateByTemplateId(monitorRule);
        return null;
    }

    private Result check(MonitorRuleVO vo) {
        if (StringUtils.isNotEmpty(vo.getContent())) {
            RuleUtils.checkSql(vo.getContent());
        }

        if (StringUtils.isEmpty(vo.getName()) || StringUtils.isEmpty(vo.getTableName()) || vo.getTemplateId() == null || StringUtils.isEmpty(vo.getMonitorObj()) || vo.getAlarmLevel() == null || StringUtils.isEmpty(vo.getRuleType())) {
            return Result.failureResult("参数不完整");
        }

        if (RuleTypeEnum.SYSTEM.name().equals(vo.getRuleType()) && MonitorObjEnum.FIELD.name().equals(vo.getMonitorObj()) && StringUtils.isEmpty(vo.getFieldName())) {
            return Result.failureResult("请选择字段");
        }
        if (RuleTypeEnum.CUSTOME.name().equals(vo.getRuleType()) && vo.getOutputType() == null) {
            return Result.failureResult("请选择输出类型");
        }
        ArrayList<String> floatList = Lists.newArrayList("up", "down");
        if ((floatList.contains(vo.getCompareType()) && (vo.getRangeStart() == null || vo.getRangeEnd() == null)) || ((vo.getRangeStart() != null || vo.getRangeEnd() != null) && vo.getCompareType() == null)) {
            return Result.failureResult("未选择比较方式或开始值/结束值");
        }

        ArrayList<String> fixList = Lists.newArrayList(">", ">=", "=", "<", "<=", "<>");
        if (fixList.contains(vo.getCompareType()) && vo.getFixValue() == null) {
            return Result.failureResult("未选择比较方式或固定值");
        }

        if (vo.getFixValue() != null && vo.getFixValue() < 0) {
            return Result.failureResult("固定值不能为负值");
        }
        if ((vo.getRangeStart() != null && vo.getRangeStart() < 0) || vo.getRangeEnd() != null && vo.getRangeEnd() < 0) {
            return Result.failureResult("范围值不能为负值");
        }
        return Result.successResult();
    }

    @Override
    public PageResult<List<MonitorRuleVO>> getMonitorRules(MonitorRuleQuery query) {
        int count = monitorRuleDao.getCount(query);
        PageResult page = new PageResult();
        page.setCurPage(query.getCurPage());
        page.setPageSize(query.getPageSize());
        page.setTotalPages(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorRuleVO> list = monitorRuleDao.getByPage(query);
        page.setData(list);

        return page;
    }

    @Override
    public HashMap<String, MonitorTableVO> getRuleCountByTableName(List<String> tables) {
        return monitorRuleDao.getRuleCountByTableName(tables);
    }

    @Override
    public HashMap<Long, MonitorTableVO> getRuleCountByBaselineId(List<Long> baselineIdList) {
        return monitorRuleDao.getRuleCountByBaselineId(baselineIdList);
    }

    @Override
    public int getCount(MonitorRuleQuery query) {
        return monitorRuleDao.getCount(query);
    }

    @Override
    public MonitorRuleVO getById(Long id) {
        return Converter.MONITOR_RULE_CONVERTER.toVo(monitorRuleDao.getById(id));
    }

    @Override
    public boolean delById(Long id) {
        MonitorRule rule = new MonitorRule();
        rule.setId(id);
        rule.setDel(1);

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        rule.setEditor(nickname);
        return monitorRuleDao.updateNotNull(rule) == 1 ? true : false;
    }

    @Override
    public boolean del(Long baselineId, String tableName) {
        if (baselineId == -1 && StringUtils.isEmpty(tableName)) {
            return false;
        }

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        return monitorRuleDao.del(baselineId, tableName, nickname);
    }

    @Override
    public boolean setStatus(Long id, Integer status) {
        MonitorRule rule = new MonitorRule();
        rule.setId(id);
        rule.setStatus(status);

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        rule.setEditor(nickname);
        monitorRuleDao.updateNotNull(rule);

        //开启告警规则后初始化历史数据
        if (status == 1) {
            initHistory(id, nickname);
        }
        return true;
    }

    @Async
    public void initHistory(Long id, String nickname) {
        MonitorRule rule = monitorRuleDao.getById(id);
        MonitorRuleVO vo = Converter.MONITOR_RULE_CONVERTER.toVo(rule);
        if ((RuleTypeEnum.SYSTEM.getValue().equals(rule.getRuleType()) && MonitorTemplateEnum.TABLE_ROW.getValue().equals(rule.getContent())) ||
                RuleTypeEnum.CUSTOME.getValue().equals(rule.getRuleType()) || RuleTypeEnum.TEMPLATE.getValue().equals(rule.getRuleType())) {

            MonitorTable monitorTable = monitorTableDao.getByTableName(rule.getTableName(), -1L);
            vo.setPartitionExpr(monitorTable.getPartitionExpr());

            MonitorHistoryVO historyVO = this.getRuleCount(vo);
            if (historyVO.getDataValue() != null) {
                try {
                    monitorHistoryDao.insert(Converter.MONITOR_HISTORY_CONVERTER.toDto(historyVO));
                } catch (BizException e) {
                    String message = String.format("你在数据质量上配置的规则{%s}校验错误，请检查配置/SQL是否正确", rule.getName());
                    messageSendService.sengDingdingByNickname(nickname, message);
                }
            }
        }
    }

    /**
     * 根据规则统计数据
     *
     * @param rule
     * @return
     */
    public MonitorHistoryVO getRuleCount(MonitorRuleVO rule) {
        String partitionExpr = rule.getPartitionExpr();

        String curPartition = "";
        if (StringUtils.isNotEmpty(partitionExpr)) {
            curPartition = ParameterUtils.dateTemplateParse(partitionExpr, new Date());
        }
        String condition = curPartition.replaceAll(",", " and ");

        Long count = null;
        String sql = "";
        String tableName = rule.getTableName();

        if (RuleTypeEnum.SYSTEM.getValue().equals(rule.getRuleType())) {
            boolean isPartition = StringUtils.isEmpty(rule.getPartitionExpr()) ? false : true;

            MonitorTemplateEnum templateEnum = MonitorTemplateEnum.valueOf(rule.getContent());
            switch (templateEnum) {
                case TABLE_ROW:

                    if (StringUtils.isNotEmpty(partitionExpr)) {
                        count = tableService.getTableCount(rule.getTableName(), partitionExpr);
                    } else {
                        sql = String.format("select count(*) from %s where %s", rule.getTableName(), curPartition);
                    }

                    break;
                case FIELD_UNIQUE:
                    sql = String.format("select count(*) num from %s group by %s having coung(*)>1", tableName, rule.getFieldName());
                    if (isPartition) {
                        sql += "where " + condition;
                    }

                    break;
                case FIELD_ENUM_CONTENT:
                    String[] arr = rule.getContent().split(",");
                    sql = String.format("select count(*) num from %s where %s not in (%s) ", tableName, rule.getFieldName(), Joiner.on(",").join(arr));
                    if (isPartition) {
                        sql += " and " + condition;
                    }

                    break;
                case FIELD_ENUM_COUNT:
                    sql = String.format("select count(distinct %s) num from %s ", rule.getFieldName(), tableName);
                    if (isPartition) {
                        sql += "where " + condition;
                    }

                    break;
                case FIELD_DATA_RANGE:
                    sql = String.format("select count(*) num from %s where %s<%s and %s>%s ", tableName, rule.getFieldName(), rule.getRangeStart(), rule.getFieldName(), rule.getRangeEnd());
                    if (isPartition) {
                        sql += "where " + condition;
                    }

                    break;
                case FIELD_NOT_NULL:
                    sql = String.format("select count(*) num from %s where %s is null", tableName, rule.getFieldName());
                    if (isPartition) {
                        sql += "where " + condition;
                    }

                    break;
            }
        } else {
            //替换自定义sql中${tableName}的占位符
            sql = rule.getContent().replaceAll("\\$\\{tableName\\}", tableName);

            Set<String> placeholders = RuleUtils.getPlaceholder(rule.getContent());
            if (placeholders.size() > 0) {
                curPartition = ParameterUtils.dateTemplateParse(placeholders.iterator().next(), new Date());
                rule.setPartitionCondition(curPartition);
            }

            sql = ParameterUtils.dateTemplateParse(rule.getContent(), new Date());
        }

        if (count != null) {
            count = this.queryRuleSql(sql);
        }

        MonitorHistoryVO historyVO = new MonitorHistoryVO();
        historyVO.setPartition(curPartition);
        historyVO.setDataValue(count);
        historyVO.setSql(sql);
        return historyVO;

    }

    @Override
    public List<MonitorRuleVO> getScheduleRuleList(List<String> typeList, Integer startIndex, boolean isBaseline) {
        if (isBaseline) {
            return monitorRuleDao.getBaselineScheduleRuleList(typeList, startIndex);
        }
        return monitorRuleDao.getScheduleRuleList(typeList, startIndex);
    }

    @Async
    public void analyse(Long jobId) {
        JobOutputVO jobOutputVO = jobOutputDao.getById(jobId);
        String tableName = "";
        if (jobOutputVO != null) {
            tableName = jobOutputVO.getDestTable();
        } else {
            JobOutputVO diJobOutput = jobOutputDao.getDIJobTable(jobId);
            if (diJobOutput != null) {
                tableName = diJobOutput.getDestTable();
            }
        }

        if (StringUtils.isEmpty(tableName)) {
            return;
        }

        List<MonitorRuleVO> list1 = monitorRuleDao.getRulesByTable(tableName);
        List<MonitorRuleVO> list2 = monitorRuleDao.getBaselineRulesByTableName(tableName);

        Set<MonitorRuleVO> ruleSet = new HashSet();
        ruleSet.addAll(list1);
        ruleSet.addAll(list2);

        StringBuilder str = new StringBuilder("你在数据质量上监控的表" + tableName + "触发以下预警：");
        boolean isAlaram = false;
        List<MonitorHistory> historyList = new ArrayList<>();
        int count = 0;
        Integer latestAlarmLevel = null;
        for (MonitorRuleVO rule : ruleSet) {
            MonitorHistoryVO historyVO = this.getRuleCheckResult(rule);
            historyList.add(Converter.MONITOR_HISTORY_CONVERTER.toDto(historyVO));

            if (historyVO.getAlarm()) {
                isAlaram = true;
                str.append(String.format("规则'%s',预警值为%s，超过设置值", rule.getName(), historyVO.getDataValue()));
                //只告警第一个规则
                if (count == 0) {
                    latestAlarmLevel = rule.getAlarmLevel();
                    String[] nicknames = rule.getAlarmReceivers().split(",");
                    String message = String.format("根据你在数据质量平台上配置的规则{%s}，监测到表%s的数据产出时间已超过设置时间%s",
                            rule.getName(), rule.getContent());
                    messageSendService.send(RuleUtils.getAlarmTypes(latestAlarmLevel), nicknames, message);
                }
                count++;
            }
        }

        if (isAlaram) {
            MonitorTable monitorTable = new MonitorTable();
            monitorTable.setTableName(tableName);
            monitorTable.setLatestAlarmLevel(latestAlarmLevel);
            monitorTable.setAccessTime(DateUtils.getCurrentTime());
            monitorTableDao.updateByTableName(monitorTable);
            monitorHistoryDao.insertBatch(historyList);
        }

    }

    private MonitorHistoryVO getRuleCheckResult(MonitorRuleVO rule) {
        MonitorHistoryVO historyVO = this.getRuleCount(rule);
        Long count = historyVO.getDataValue();

        boolean isAlarm = false;
        if (RuleTypeEnum.SYSTEM.getValue().equals(rule.getRuleType())) {

            MonitorTemplateEnum templateEnum = MonitorTemplateEnum.valueOf(rule.getContent());
            switch (templateEnum) {
                case TABLE_ROW:
                    MonitorHistoryVO alarmHistory = this.isAlarm(rule, count);
                    isAlarm = alarmHistory.getAlarm();
                    historyVO.setRuleValue(alarmHistory.getRuleValue());
                    break;
                case FIELD_UNIQUE:
                    if (count > 0) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_ENUM_CONTENT:
                    if (count > 0) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_ENUM_COUNT:
                    if (count > rule.getFixValue()) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_DATA_RANGE:
                    if (count > rule.getFixValue()) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_NOT_NULL:
                    if (count > rule.getFixValue()) {
                        isAlarm = true;
                    }
                    break;
            }
        } else {
            MonitorHistoryVO alarmHistory = this.isAlarm(rule, count);
            isAlarm = alarmHistory.getAlarm();
            historyVO.setRuleValue(alarmHistory.getRuleValue());
        }

        historyVO.setAlarm(isAlarm);
        historyVO.setTableName(rule.getTableName());
        historyVO.setRuleId(rule.getId());
        historyVO.setMonitorObj(rule.getMonitorObj());
        historyVO.setAlarmLevel(rule.getAlarmLevel());
        historyVO.setRuleName(rule.getName());
        historyVO.setRuleType(rule.getRuleType());
        return historyVO;
    }

    private MonitorHistoryVO isAlarm(MonitorRuleVO rule, Long count) {
        MonitorHistoryVO historyVO = new MonitorHistoryVO();

        CompareTypeEnum compareType = CompareTypeEnum.valueOf(rule.getCompareType());

        String prePartition = null;
        boolean isAlarm = false;
        if (CompareTypeEnum.UP == compareType || CompareTypeEnum.DOWN == compareType) {
            prePartition = ParameterUtils.getPreCycleDate(rule.getPartitionExpr(), new Date());
            MonitorHistory history = monitorHistoryDao.getLatest(rule.getId(), prePartition);
            Long oldValue = history.getDataValue();

            BigDecimal tmpData = new BigDecimal(count - oldValue).divide(new BigDecimal(oldValue));
            historyVO.setRuleValue(tmpData.doubleValue());
            if (CompareTypeEnum.DOWN == compareType) {
                tmpData = tmpData.negate();
                historyVO.setRuleValue(tmpData.doubleValue());
            }

            Double data = tmpData.doubleValue();
            Double rangeStart = Double.valueOf(rule.getRangeStart() / 100);
            Double rangeEnd = Double.valueOf(rule.getRangeEnd() / 100);
            if (!(data > 0 && data >= rangeStart && data <= rangeEnd)) {
                isAlarm = true;
            }
        }

        switch (compareType) {
            case GREATER:
                if (count <= rule.getFixValue()) {
                    isAlarm = true;
                }
                break;
            case GREATER_OR_EQUAL:
                if (count < rule.getFixValue()) {
                    isAlarm = true;
                }
                break;
            case EQUAL:
                if (!count.equals(rule.getFixValue())) {
                    isAlarm = true;
                }
                break;
            case LESS:
                if (count >= rule.getFixValue()) {
                }
                break;
            case LESS_OR_EQUAL:
                if (count > rule.getFixValue()) {
                    isAlarm = true;
                }
                break;
            case NOT_EQUAL:
                if (count.equals(rule.getFixValue())) {
                    isAlarm = true;
                }
                break;
        }

        historyVO.setAlarm(isAlarm);
        return historyVO;
    }


    //todo 暂定方法
    public Long queryRuleSql(String sql) {
        LivyQueryDto query = new LivyQueryDto();
        query.setSessionKind(LivySessionKindEnum.spark);
        query.setQuerySource(sql);

        LivyStatementDto statementDto = livyService.createStatement(query);

        while (true) {
            LivySqlResultDto sqlResultDto = livyService.queryResult(statementDto.getSessionId(), statementDto.getStatementId(), LivySessionKindEnum.spark);
            if (LivyOutputStatusEnum.ok.equals(sqlResultDto.getOutputStatus())) {
                List<Map<String, Object>> resultSet = sqlResultDto.getResultSet();

                //数据质量sql查询结果只会是一行一列
                if (resultSet.size() > 1) {
                    throw new BizException("Sql查询失败，请检查数据质量sql是否正确");
                }

                if (CollectionUtils.isEmpty(resultSet)) {
                    return 0L;
                } else {
                    Map<String, Object> resMap = resultSet.get(0);
                    if (resMap.size() != 1) {
                        throw new BizException("Sql查询失败，请检查数据质量sql是否正确");
                    }

                    Iterator<Map.Entry<String, Object>> iterator = resMap.entrySet().iterator();
                    if (!iterator.hasNext()) {
                        return 0L;
                    }
                    Map.Entry<String, Object> entry = iterator.next();
                    return (Long) entry.getValue();
                }
            } else if (LivyOutputStatusEnum.error.equals(sqlResultDto.getOutputStatus())) {
                throw new BizException("Sql查询失败，请检查数据质量sql是否正确");
            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Result<MonitorHistoryVO> tryRun(Long id) {
        MonitorRule rule = monitorRuleDao.getById(id);
        if (RuleTypeEnum.SYSTEM.getValue().equals(rule.getRuleType()) && MonitorTemplateEnum.TABLE_OUTPUT_TIME.getValue().equals(rule.getContent())) {
            return Result.failureResult("表产出时间不支持试跑");
        }

        MonitorTable monitorTable = monitorTableDao.getByTableName(rule.getTableName(), -1L);

        MonitorRuleVO vo = Converter.MONITOR_RULE_CONVERTER.toVo(rule);
        vo.setPartitionExpr(monitorTable.getPartitionExpr());

        MonitorHistoryVO history = this.getRuleCheckResult(vo);
        return Result.successResult(history);
    }

}


