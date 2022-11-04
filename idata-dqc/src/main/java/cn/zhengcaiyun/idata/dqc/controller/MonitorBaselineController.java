package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorBaseline;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorBaselineQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorBaselineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据质量基线表(MonitorBaseline)表控制层
 *
 * @author zheng
 * @since 2022-07-06 14:01:49
 */
@RestController
@RequestMapping("monitorBaseline")
public class MonitorBaselineController {
    @Resource
    private MonitorBaselineService monitorBaselineService;

    @RequestMapping("add")
    public Result<MonitorBaselineVO> add(@RequestBody MonitorBaselineVO monitorBaseline) {
        if (StringUtils.isEmpty(monitorBaseline.getName())) {
            return Result.failureResult("基线名称不能为空");
        }
        return Result.successResult(monitorBaselineService.insert(monitorBaseline));
    }

    @RequestMapping("update")
    public Result<Boolean> update(@RequestBody MonitorBaselineVO monitorBaseline) {
        if (StringUtils.isEmpty(monitorBaseline.getName())) {
            return Result.failureResult("基线名称不能为空");
        }
        return Result.successResult(monitorBaselineService.update(monitorBaseline));
    }

    @RequestMapping("getByPage")
    public Result<PageResult<MonitorBaselineVO>> getByPage(@RequestBody MonitorBaselineQuery query) {
        return Result.successResult(monitorBaselineService.getByPage(query));
    }

    @RequestMapping("get/{id}")
    public Result<MonitorBaselineVO> getById(@PathVariable Long id) {
        return Result.successResult(monitorBaselineService.getById(id));
    }

    @RequestMapping("del/{id}")
    public Result<Boolean> delById(@PathVariable Long id) {
        return Result.successResult(monitorBaselineService.delById(id));
    }

    @RequestMapping("setStatus/{id}/{status}")
    public Result<Boolean> setStatus(@PathVariable Long id, @PathVariable Integer status) {
        return Result.successResult(monitorBaselineService.setStatus(id, status));
    }

}

