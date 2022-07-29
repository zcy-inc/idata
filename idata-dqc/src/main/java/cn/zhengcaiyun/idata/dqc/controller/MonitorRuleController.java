package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorHistoryVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author:zheng
 * Date:2022/6/17
 */
@RestController
@RequestMapping("monitorRule")
public class MonitorRuleController {
    @Autowired
    private MonitorRuleService monitorRuleService;

    @RequestMapping("/add")
    public Result<MonitorRuleVO> add(@RequestBody MonitorRuleVO monitorRule) {
        if (monitorRule.getBaselineId() == null) {
            return Result.failureResult("基线id不能为空");
        }
        return monitorRuleService.add(monitorRule);
    }

    @RequestMapping("/update")
    public Result<MonitorRuleVO> update(@RequestBody MonitorRuleVO monitorRule) {
        MonitorRuleVO old = monitorRuleService.getById(monitorRule.getId());
        if (old.getStatus() == 1) {
            return Result.failureResult("规则启用中，需要停用才能编辑");
        }

        if (!old.getCreator().equals(OperatorContext.getCurrentOperator().getNickname())) {
            return Result.failureResult("您无权限编辑该规则");
        }

        return monitorRuleService.update(monitorRule);
    }

    @RequestMapping("/getByPage")
    public Result<PageResult<List<MonitorRuleVO>>> getByPage(@RequestBody MonitorRuleQuery query) {
        if (query.getBaselineId() == null) {
            query.setBaselineId(-1L);
        }
        return Result.successResult(monitorRuleService.getMonitorRules(query));
    }

    @RequestMapping("/get/{id}")
    public Result<MonitorRuleVO> getById(@PathVariable Long id) {
        return Result.successResult(monitorRuleService.getById(id));
    }

    @RequestMapping("/del/{id}/")
    public Result<Boolean> delById(@PathVariable Long id) {
        MonitorRuleVO old = monitorRuleService.getById(id);
        if (old.getStatus() == 1) {
            return Result.failureResult("规则启用中，需要停用才能删除");
        }

        monitorRuleService.delById(id);
        return Result.successResult();
    }

    @RequestMapping("/setStatus/{id}/{status}")
    public Result<Boolean> setStatus(@PathVariable Long id, @PathVariable Integer status) {
        monitorRuleService.setStatus(id, status);
        //开启告警规则后初始化历史数据
        if (status == 1) {
            monitorRuleService.initHistory(id, OperatorContext.getCurrentOperator().getNickname());
        }
        return Result.successResult();
    }

    @RequestMapping("/tryRun/{baselineId}/{id}")
    public Result<Boolean> add(@PathVariable Long id, @PathVariable Long baselineId) {
        String nickname = OperatorContext.getCurrentOperator().getNickname();
        monitorRuleService.tryRun(id, baselineId, nickname);
        return Result.successResult();
    }

}
