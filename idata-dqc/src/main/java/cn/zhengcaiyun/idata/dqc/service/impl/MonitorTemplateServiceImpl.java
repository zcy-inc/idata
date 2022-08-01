package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.hutool.core.lang.Assert;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.dao.MonitorTemplateDao;
import cn.zhengcaiyun.idata.dqc.model.common.Converter;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTemplate;
import cn.zhengcaiyun.idata.dqc.model.enums.RuleTypeEnum;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTemplateQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTemplateVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTemplateService;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据质量模板表(DqcMonitorTemplate)表服务实现类
 *
 * @author makejava
 * @since 2022-07-04 11:02:57
 */
@Service("dqcMonitorTemplateService")
public class MonitorTemplateServiceImpl implements MonitorTemplateService {
    @Resource
    private MonitorTemplateDao monitorTemplateDao;

    @Lazy
    @Autowired
    private MonitorRuleService monitorRuleService;

    @Override
    public MonitorTemplateVO getById(Long id) {
        MonitorTemplate template = monitorTemplateDao.getById(id);
        return Converter.MONITOR_TEMPLATE_CONVERTER.toVo(template);
    }

    @Override
    public PageResult<MonitorTemplate> getByPage(MonitorTemplateQuery query) {
        int count = monitorTemplateDao.count(query);
        PageResult page = new PageResult();
        page.setCurPage(query.getCurPage());
        page.setPageSize(query.getPageSize());
        page.setTotalElements(count);

        if (count == 0) {
            page.setData(new ArrayList<>());
            return page;
        }
        List<MonitorTemplate> list = monitorTemplateDao.getByPage(query);
        page.setData(list);

        return page;

    }

    @Override
    public Result<MonitorTemplateVO> insert(MonitorTemplateVO vo) {
        try {
            check(vo);
        } catch (Exception e) {
            return Result.failureResult(e.getMessage());
        }
        MonitorTemplate template = Converter.MONITOR_TEMPLATE_CONVERTER.toDto(vo);
        template.setStatus(1);
        template.setDel(0);

        String nickname = OperatorContext.getCurrentOperator().getNickname();
        template.setCreator(nickname);
        template.setEditor(nickname);

        monitorTemplateDao.insert(template);
        vo.setId(template.getId());
        return Result.successResult(vo);
    }

    public void check(MonitorTemplateVO vo) {
        RuleUtils.checkSql(vo.getContent());

        Assert.isFalse(StringUtils.isEmpty(vo.getType()), "模板类型不能为空");
        Assert.isFalse(StringUtils.isEmpty(vo.getName()), "规则名称不能为空");
        Assert.isFalse(StringUtils.isEmpty(vo.getMonitorObj()), "监控对戏不能为空");
        Assert.isFalse(StringUtils.isEmpty(vo.getCategory()), "维度不能为空");
        Assert.isFalse(StringUtils.isEmpty(vo.getContent()), "sql不能为空");
        Assert.isFalse(vo.getOutputType() == null, "输出不能为空");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MonitorTemplateVO update(MonitorTemplateVO vo) {
        MonitorTemplate template = Converter.MONITOR_TEMPLATE_CONVERTER.toDto(vo);
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        template.setEditor(nickname);

        monitorTemplateDao.update(template);

        MonitorRuleVO rule = new MonitorRuleVO();
        rule.setTemplateId(vo.getId());
        rule.setMonitorObj(vo.getMonitorObj());

        if (RuleTypeEnum.TEMPLATE.getValue().equals(vo.getType())) {
            rule.setContent(vo.getContent());
        }

        monitorRuleService.updateByTemplateId(rule);

        return vo;
    }

    @Override
    public boolean setStatus(Long id, Integer status) {
        MonitorTemplate monitorTemplate = new MonitorTemplate();
        monitorTemplate.setId(id);
        monitorTemplate.setStatus(status);
        monitorTemplate.setEditor(OperatorContext.getCurrentOperator().getNickname());

        return monitorTemplateDao.update(monitorTemplate) == 1 ? true : false;
    }

}
