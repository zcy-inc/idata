package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.dao.MonitorRuleDao;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorRule;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorObjEnum;
import cn.zhengcaiyun.idata.dqc.model.enums.RuleTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author:zheng
 * Date:2022/6/23
 */
@Service
public class MonitorRuleServiceImpl implements MonitorRuleService {
    @Autowired
    private MonitorRuleDao monitorRuleDao;

    @Override
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

        monitorRuleDao.update(monitorRule);
        return Result.successResult(vo);
    }

//    @Override
//    public Boolean updateAccessTime(String accessTime) {
//        return monitorRuleDao.update(monitorRule)==1;
//    }

    @Override
    public Result<Boolean> updateByTemplateId(MonitorRuleVO vo) {
        MonitorRule monitorRule = Converter.MONITOR_RULE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorRule.setEditor(nickname);

        monitorRuleDao.updateByTemplateId(monitorRule);
        return null;
    }

    private Result check(MonitorRuleVO vo) {
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
        return monitorRuleDao.updateNotNull(rule) == 1 ? true : false;
    }

    @Override
    public List<MonitorRuleVO> getScheduleRuleList(List<String> typeList, Integer startIndex, boolean isBaseline) {
        if(isBaseline){
            return monitorRuleDao.getBaselineScheduleRuleList(typeList, startIndex);
        }
        return monitorRuleDao.getScheduleRuleList(typeList, startIndex);
    }


}
