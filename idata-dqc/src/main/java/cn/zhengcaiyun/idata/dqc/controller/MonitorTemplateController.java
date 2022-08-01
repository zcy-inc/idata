package cn.zhengcaiyun.idata.dqc.controller;

import cn.hutool.core.lang.Assert;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTemplate;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorRuleQuery;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTemplateQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTemplateVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据质量模板表(DqcMonitorTemplate)表控制层
 *
 * @author zheng
 * @since 2022-07-04 11:02:54
 */
@RestController
@RequestMapping("monitorTemplate")
public class MonitorTemplateController {
    @Resource
    private MonitorTemplateService monitorTemplateService;

    @Autowired
    private MonitorRuleService monitorRuleService;

    @RequestMapping("/add")
    public Result<MonitorTemplateVO> add(@RequestBody MonitorTemplateVO vo) {
        return monitorTemplateService.insert(vo);
    }

    @RequestMapping("/update")
    public Result<MonitorTemplateVO> update(@RequestBody MonitorTemplateVO vo) {
        MonitorRuleQuery query = new MonitorRuleQuery();
        query.setTemplateId(vo.getId());
        query.setStatus(1);

        if (monitorRuleService.getCount(query) > 0) {
            return Result.failureResult("该模板有关联规则，停止后才能删除");
        }

        try {
            Assert.isFalse(vo.getId() == null, "id不能为空");
            monitorTemplateService.check(vo);
            return Result.successResult(monitorTemplateService.update(vo));
        } catch (Exception e) {
            return Result.failureResult(e.getMessage());
        }
    }

    @RequestMapping("/getByPage")
    public Result<PageResult<MonitorTemplate>> getByPage(@RequestBody MonitorTemplateQuery query) {
        return Result.successResult(monitorTemplateService.getByPage(query));
    }

    @RequestMapping("/get/{id}")
    public Result<MonitorTemplateVO> getById(@PathVariable Long id) {
        return Result.successResult(monitorTemplateService.getById(id));
    }

    @RequestMapping("/del/{id}")
    public Result<Boolean> delById(Long id) {
        //判断有无规则依赖
        MonitorRuleQuery query = new MonitorRuleQuery();
        query.setTemplateId(id);

        if (monitorRuleService.getCount(query) > 0) {
            return Result.failureResult("该模板有关联规则，停止规则后才能删除");
        }

        MonitorTemplateVO monitorTemplateVO = new MonitorTemplateVO();
        monitorTemplateVO.setId(id);
        monitorTemplateVO.setDel(1);
        monitorTemplateService.update(monitorTemplateVO);
        return Result.successResult();
    }

    @RequestMapping("/setStatus/{id}/{status}")
    public Result<Boolean> setStatus(@PathVariable Long id, @PathVariable Integer status) {
        //判断有无规则依赖
        if (status == 0) {
            MonitorRuleQuery query = new MonitorRuleQuery();
            query.setTemplateId(id);
            query.setStatus(1);

            if (monitorRuleService.getCount(query) > 0) {
                return Result.failureResult("该模板有关联规则，停止规则后才能停用");
            }
        }

        return Result.successResult(monitorTemplateService.setStatus(id,status));
    }

}

