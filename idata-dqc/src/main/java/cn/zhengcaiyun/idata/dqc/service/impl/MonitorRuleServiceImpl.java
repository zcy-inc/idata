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
import cn.zhengcaiyun.idata.dqc.model.enums.*;
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
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MonitorRuleServiceImpl.class);

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
    @Transactional(rollbackFor = Exception.class)
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

        //基线的时候没有表名
        if (vo.getBaselineId() != -1) {
            monitorRule.setTableName("");
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

        //基线的时候没有表名
        if (vo.getBaselineId() != -1) {
            monitorRule.setTableName("");
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
        return DigestUtil.md5(rule.getFieldName() + rule.getRuleType() + rule.getTemplateId() + rule.getMonitorObj() + rule.getCheckType() + rule.getCompareType() + rule.getContent() + rule.getFixValue() + rule.getRangeStart() + rule.getRangeEnd());
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

        if (vo.getBaselineId() == -1L && StringUtils.isEmpty(vo.getTableName())) {
            return Result.failureResult("表名未传");
        }

        if (StringUtils.isEmpty(vo.getName()) || vo.getTemplateId() == null || StringUtils.isEmpty(vo.getMonitorObj()) || vo.getAlarmLevel() == null || StringUtils.isEmpty(vo.getRuleType())) {
            return Result.failureResult("参数不完整");
        }

        if (RuleTypeEnum.SYSTEM.name().equals(vo.getRuleType()) && MonitorObjEnum.FIELD.name().equals(vo.getMonitorObj()) && StringUtils.isEmpty(vo.getFieldName())) {
            return Result.failureResult("请选择字段");
        }

        if (RuleTypeEnum.CUSTOME.name().equals(vo.getRuleType()) && vo.getOutputType() == null) {
            return Result.failureResult("请选择输出类型");
        }
        ArrayList<String> floatList = Lists.newArrayList("up", "down");
        if (floatList.contains(vo.getCompareType()) && (vo.getRangeStart() == null || vo.getRangeEnd() == null)) {
            return Result.failureResult("未选择比较方式或开始值/结束值");
        }

        ArrayList<String> fixList = Lists.newArrayList(">", ">=", "=", "<", "<=", "<>");
        if (fixList.contains(vo.getCompareType()) && RuleCheckTypeEnum.FIX.getValue().equals(vo.getCheckType()) && vo.getFixValue() == null) {
            return Result.failureResult("请输入固定值");
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
        page.setTotalElements(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorRuleVO> list = monitorRuleDao.getByPage(query);
        page.setData(list);

        return page;
    }

    @Override
    public List<MonitorRuleVO> getByBaselineId(Long baselineId, Integer status) {
        MonitorRuleQuery query = new MonitorRuleQuery();
        query.setBaselineId(baselineId);
        query.setNotPage(true);
        query.setStatus(status);
        return monitorRuleDao.getByPage(query);
    }

    @Override
    public HashMap<String, MonitorTableVO> getRuleCountByTableName(List<String> tables, Long baselineId) {
        return monitorRuleDao.getRuleCountByTableName(tables, baselineId);
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
    public boolean setStatus(Long id, Integer status, String nickname) {
        MonitorRule rule = new MonitorRule();
        rule.setId(id);
        rule.setStatus(status);


        rule.setEditor(nickname);
        monitorRuleDao.updateNotNull(rule);

        return true;
    }

    @Async
    public void initHistory(Long id, String nickname) {
        MonitorRule rule = monitorRuleDao.getById(id);
        MonitorRuleVO vo = Converter.MONITOR_RULE_CONVERTER.toVo(rule);

        //涉及到上周期的规则需要初始化历史数据
        if (RuleCheckTypeEnum.PRE_PREIOD.getValue().equals(vo.getCheckType())) {
            this.calc(vo, nickname, true);
        } else if (RuleTypeEnum.CUSTOME.getValue().equals(rule.getRuleType()) || RuleTypeEnum.TEMPLATE.getValue().equals(rule.getRuleType())) {
            //如果是自定义或者模板规则，则校验sql的准确性
            this.calc(vo, nickname, false);
        }
    }

    private void calc(MonitorRuleVO vo, String nickname, boolean needInsert) {
        List<MonitorTable> tableList = monitorTableDao.getByTableName(vo.getTableName(), vo.getBaselineId());
        int count = 0;
        for (MonitorTable monitorTable : tableList) {
            vo.setPartitionExpr(monitorTable.getPartitionExpr());

            MonitorHistoryVO historyVO = null;
            try {
                //校验sql是否正确
                historyVO = this.getRuleQueryCount(vo);

                //只有算上浮和下浮才初始化历史数据
                if (needInsert) {
                    historyVO.setAlarm(0);
                    monitorHistoryDao.insert(Converter.MONITOR_HISTORY_CONVERTER.toDto(historyVO));
                }
            } catch (BizException e) {
                if (count == 0) { //一个规则对应多张表的情况，只告警一次
                    //sql报错则将规则关闭
                    this.setStatus(vo.getId(), 0, nickname);
                    String message = String.format("你在数据质量上配置的规则[%s]校验错误，请检查配置/SQL是否正确，执行sql [ %s ]", vo.getName(), vo.getSql());
                    messageSendService.sengDingdingByNickname(nickname, "数据质量规则配置错误", message);
                }
                count++;
            }
        }
    }

    /**
     * 根据规则统计数据
     *
     * @param rule
     * @return
     */
    public MonitorHistoryVO getRuleQueryCount(MonitorRuleVO rule) {
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

            MonitorTemplateEnum templateEnum = MonitorTemplateEnum.getEnum(rule.getTemplateId());
            switch (templateEnum) {
                case TABLE_ROW:
                    if (StringUtils.isEmpty(partitionExpr)) {
                        String[] arr = rule.getTableName().split("\\.");
                        count = tableService.getTableCount(arr[0], arr[1]);
                    } else {
                        sql = String.format("select count(*) from %s where %s ", rule.getTableName(), curPartition);
                    }

                    break;
                case FIELD_UNIQUE:
                    sql = String.format("select count(*) num from %s ", tableName);
                    if (isPartition) {
                        sql += "where " + condition;
                    }
                    sql += String.format(" group by %s having count(*)>1 ", rule.getFieldName());
                    sql = "select count(*) from (" + sql + ")";

                    break;
                case FIELD_ENUM_CONTENT:
                    String[] arr = rule.getContent().split(",");
                    sql = String.format("select count(*) num from %s where %s not in (%s) ", tableName, rule.getFieldName(), Joiner.on(",").join(arr));
                    if (isPartition) {
                        sql += "and " + condition;
                    }

                    break;
                case FIELD_ENUM_COUNT:
                    sql = String.format("select count(distinct `%s`) num from %s ", rule.getFieldName(), tableName);
                    if (isPartition) {
                        sql += "where " + condition;
                    }

                    break;
                case FIELD_DATA_RANGE:
                    sql = String.format("select count(*) num from %s where `%s`<%s or `%s`>%s ", tableName, rule.getFieldName(), rule.getRangeStart(), rule.getFieldName(), rule.getRangeEnd());
                    if (isPartition) {
                        sql += "and " + condition;
                    }

                    break;
                case FIELD_NOT_NULL:
                    sql = String.format("select count(*) num from %s where %s is null ", tableName, rule.getFieldName());
                    if (isPartition) {
                        sql += "and " + condition;
                    }

                    break;
            }
        } else {
            //获取日期占位符
//            Set<String> placeholders = RuleUtils.getDatePlaceholder(rule.getContent());
//            if (placeholders.size() > 0) {
//                curPartition = ParameterUtils.dateTemplateParse(placeholders.iterator().next(), new Date());
//                rule.setPartitionCondition(curPartition);
//            }

            sql = RuleUtils.replaceSql(rule.getContent(), rule.getTableName(), rule.getTableName(), new Date());
        }

        rule.setSql(sql);
        if (count == null) {
            try {
                count = this.queryRuleSql(sql);
            } catch (Exception e) {
                throw new BizException(e.getMessage());
            }
        }

        MonitorHistoryVO historyVO = new MonitorHistoryVO();
        historyVO.setPartition(curPartition);
        historyVO.setDataValue(count);
        historyVO.setSql(sql);
        historyVO.setTableName(rule.getTableName());
        historyVO.setTemplateId(rule.getTemplateId());
        historyVO.setBaselineId(rule.getBaselineId());
        historyVO.setRuleId(rule.getId());
        historyVO.setMonitorObj(rule.getMonitorObj());
        historyVO.setAlarmLevel(rule.getAlarmLevel());
        historyVO.setRuleName(rule.getName());
        historyVO.setRuleType(rule.getRuleType());
        historyVO.setAlarmReceivers(rule.getAlarmReceivers());
        historyVO.setVersion(rule.getVersion());
        historyVO.setFixValue(rule.getFixValue());
        historyVO.setRangeStart(rule.getRangeStart());
        historyVO.setRangeEnd(rule.getRangeEnd());
        historyVO.setCompareType(rule.getCompareType());
        historyVO.setCreator("系统管理员");
        historyVO.setEditor("系统管理员");
        historyVO.setCreateTime(new Date());
        historyVO.setCheckType(rule.getCheckType());
        return historyVO;

    }

    @Override
    public List<MonitorRuleVO> getScheduleRuleList(List<Long> templateIdList, Integer startIndex) {
        return monitorRuleDao.getScheduleRuleList(templateIdList, startIndex);
    }

    @Override
    public List<MonitorRuleVO> getBaselineScheduleRuleList(List<Long> templateIdList, Integer startIndex) {
        return monitorRuleDao.getBaselineScheduleRuleList(templateIdList, startIndex);
    }

    /**
     * 数据质量分析
     *
     * @param jobId
     */
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

        List<MonitorRuleVO> list1 = monitorRuleDao.getRulesByTable(tableName); //普通规则
        List<MonitorRuleVO> list2 = monitorRuleDao.getBaselineRulesByTableName(tableName); //基线规则

        Set<MonitorRuleVO> ruleSet = new HashSet();
        ruleSet.addAll(list1);
        ruleSet.addAll(list2);

        boolean isAlarm = false;
        List<MonitorHistory> historyList = new ArrayList<>();
        int count = 0;
        Integer latestAlarmLevel = null;
        for (MonitorRuleVO rule : ruleSet) {
            MonitorHistoryVO historyVO = this.getRuleHistory(rule);
            historyList.add(Converter.MONITOR_HISTORY_CONVERTER.toDto(historyVO));

            if (historyVO.getAlarm() == 1) {
                isAlarm = true;

                //只告警第一个规则
                if (count == 0) {
                    String message = getAlarmMessage(historyVO);
                    String[] nicknames = rule.getAlarmReceivers().split(",");
                    messageSendService.send(RuleUtils.getAlarmTypes(latestAlarmLevel), nicknames, message);
                }
                count++;
            }
        }

        if (isAlarm) {
            MonitorTable monitorTable = new MonitorTable();
            monitorTable.setTableName(tableName);
            monitorTable.setLatestAlarmLevel(latestAlarmLevel);
            monitorTable.setAccessTime(DateUtils.getCurrentTime());
            monitorTableDao.updateByTableName(monitorTable);
            monitorHistoryDao.insertBatch(historyList);
        }

    }

    /**
     * 根据规则获取检测结果
     *
     * @param rule
     * @return
     */
    private MonitorHistoryVO getRuleHistory(MonitorRuleVO rule) {
        //根据规则拼接sql查询结果
        MonitorHistoryVO curHistory = this.getRuleQueryCount(rule);
        Long count = curHistory.getDataValue();//查询结果

        boolean isAlarm = false;
        if (RuleTypeEnum.SYSTEM.getValue().equals(rule.getRuleType())) {

            MonitorTemplateEnum templateEnum = MonitorTemplateEnum.getEnum(rule.getTemplateId());
            switch (templateEnum) {
                case TABLE_ROW:
                    MonitorHistoryVO calc = this.getCalcRes(rule, count);
                    isAlarm = calc.getAlarm() == 1;
                    curHistory.setRuleValue(calc.getRuleValue()); //计算结果（浮动的时候需要计算）
                    curHistory.setFixValue(calc.getFixValue());
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
                    if (count > 0) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_DATA_RANGE:
                    if (count > 0) {
                        isAlarm = true;
                    }
                    break;
                case FIELD_NOT_NULL:
                    if (count > 0) {
                        isAlarm = true;
                    }
                    break;
            }
        } else {
            MonitorHistoryVO calc = this.getCalcRes(rule, count);
            isAlarm = calc.getAlarm() == 1;
            curHistory.setRuleValue(calc.getRuleValue());
            curHistory.setFixValue(calc.getFixValue());
        }

        curHistory.setAlarm(isAlarm ? 1 : 0);
        return curHistory;
    }

    /**
     * @param rule
     * @param count
     * @return
     */
    private MonitorHistoryVO getCalcRes(MonitorRuleVO rule, Long count) {
        MonitorHistoryVO historyVO = new MonitorHistoryVO();

        Double fixValue = rule.getFixValue();//比较值

        RuleCheckTypeEnum checkType = RuleCheckTypeEnum.getEnum(rule.getCheckType());

        //只有调度作业会调到，所以最新一条数据比如是作业调度后监测的结果数据，而非临时作业监测的结果
        if (RuleCheckTypeEnum.PRE_PREIOD == checkType) {
            MonitorHistory old = monitorHistoryDao.getLatest(rule.getId(), null);
            if (old == null) { //试跑的时候没有历史数据
                fixValue = 0d;
            } else {
                fixValue = Double.valueOf(old.getDataValue());
            }
        }

        CompareTypeEnum compareType = CompareTypeEnum.getEnum(rule.getCompareType());

        boolean isAlarm = false;
        if (CompareTypeEnum.UP == compareType || CompareTypeEnum.DOWN == compareType) {
//            String prePartition = ParameterUtils.getPreCycleDate(rule.getPartitionExpr(), new Date());
//            MonitorHistory old = monitorHistoryDao.getLatest(rule.getId(), prePartition);

            Double data = 0d;
            if (fixValue != 0) {
                //乘以100是为了转换成百分比
                data = new BigDecimal(count - fixValue).divide(new BigDecimal(fixValue)).multiply(new BigDecimal(100)).doubleValue();
            }
            historyVO.setRuleValue(data);

            if ((CompareTypeEnum.UP == compareType && data < 0) || (CompareTypeEnum.DOWN == compareType && data > 0)) {
                isAlarm = true;
            } else {
                Double rangeStart = rule.getRangeStart();
                Double rangeEnd = rule.getRangeEnd();

                if (data < rangeStart || data > rangeEnd) {
                    isAlarm = true;
                }
            }


        } else {
            switch (compareType) {
                case GREATER:
                    if (count <= fixValue) {
                        isAlarm = true;
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (count < fixValue) {
                        isAlarm = true;
                    }
                    break;
                case EQUAL:
                    if (!count.equals(fixValue)) {
                        isAlarm = true;
                    }
                    break;
                case LESS:
                    if (count >= fixValue) {
                        isAlarm = true;
                    }
                    break;
                case LESS_OR_EQUAL:
                    if (count > fixValue) {
                        isAlarm = true;
                    }
                    break;
                case NOT_EQUAL:
                    if (count.equals(fixValue)) {
                        isAlarm = true;
                    }
                    break;
            }

        }


        historyVO.setFixValue(fixValue); //上周期的值设置进去
        historyVO.setAlarm(isAlarm ? 1 : 0);
        return historyVO;
    }


    //todo 暂定方法
    public Long queryRuleSql(String sql) {
        logger.info("Dqc livy query：" + sql);

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
                    if (!(entry.getValue() instanceof Long)) {
                        return Long.parseLong(entry.getValue().toString());
                    }
                    return (Long) entry.getValue();
                }
            } else if (LivyOutputStatusEnum.error.equals(sqlResultDto.getOutputStatus())) {
                String error = sqlResultDto.getResultSet().get(0).get("Error").toString();
                logger.error("数据质量查询报错：" + error);
                throw new BizException("Sql查询失败，请检查数据质量sql是否正确:" + error);
            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Async
    @Override
    public void tryRun(Long id, Long baselineId, String nickname) {
        MonitorRule rule = monitorRuleDao.getById(id);
        if (MonitorTemplateEnum.TABLE_OUTPUT_TIME.getId().equals(rule.getTemplateId())) {
            messageSendService.sengDingdingByNickname(nickname, "数据质量试跑结果", "表产出时间不支持试跑");
            return;
        }

        MonitorRuleVO vo = Converter.MONITOR_RULE_CONVERTER.toVo(rule);

        List<MonitorTable> tableList = monitorTableDao.getByTableName(rule.getTableName(), baselineId);
        if (tableList.size() == 0) {
            messageSendService.sengDingdingByNickname(nickname, "数据质量试跑结果", "该规则未对应任何表，请正确配置后重试");
            return;
        }
        StringBuilder str = new StringBuilder();
        for (MonitorTable monitorTable : tableList) {
            vo.setPartitionExpr(monitorTable.getPartitionExpr());
            MonitorHistoryVO history = this.getRuleHistory(vo);
            String message = getAlarmMessage(history);

            str.append(message);
        }


        messageSendService.sengDingdingByNickname(nickname, "数据质量试跑结果", str.toString());
    }

    private String getAlarmMessage(MonitorHistoryVO history) {
        String compareType = (history.getCompareType() == null ? "" : history.getCompareType());
        if (CompareTypeEnum.UP.getValue().equals(compareType)) {
            compareType = "上浮";
        } else if (CompareTypeEnum.DOWN.getValue().equals(compareType)) {
            compareType = "下浮";
        }

        String message = "";
        if (history.getCompareType() == null && history.getRangeStart() != null) {
            message = "规则内容:[" + history.getRangeStart() + "-" + history.getRangeEnd() + "], ";
        } else if (history.getCompareType() != null) {
            String fixValue = history.getFixValue() == null ? "" : "" + Math.round(history.getFixValue());

            String range = history.getRangeStart() == null ? "" : (history.getRangeStart() + "%-" + history.getRangeEnd() + "%");
            if (history.getRangeStart() == null) {
                message = "规则内容:[" + compareType + fixValue + "], ";
            } else {
                message = "规则内容:[" + fixValue + compareType + range + "], ";
            }

        }

        String value = "监控结果:[" + (history.getRuleValue() == null ? history.getDataValue().toString() : history.getRuleValue().toString()) + "], ";
        if (CompareTypeEnum.UP.getValue().equals(history.getCompareType()) || CompareTypeEnum.DOWN.getValue().equals(history.getCompareType())) {
            value = "监控结果:[" + (history.getRuleValue() == null ? history.getDataValue().toString() : history.getRuleValue().toString()) + "%], ";
        }

        return String.format("[%s] 表:[%s], 规则名称:[%s], %s %s 是否告警:[%s], 告警等级:[%s]\n",
                DateUtils.format(history.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), history.getTableName(),
                history.getRuleName(), message, value,
                history.getAlarm() == 1 ? "告警" : "未告警",
                AlarmLevelEnum.getDest(history.getAlarmLevel()));

    }

}


