package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.service.MonitorHistoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据质量监控历史记录表(MonitorHistory)表控制层
 *
 * @author zheng
 * @since 2022-07-15 14:16:16
 */
@RestController
@RequestMapping("monitorHistory")
public class MonitorHistoryController {
    @Resource
    private MonitorHistoryService monitorHistoryService;


    @RequestMapping("/get")
    public Result<List<MonitorHistory>> getHistorys(Long baselineId, Long ruleId, Integer days) {
        return Result.successResult(monitorHistoryService.getHistorys(baselineId, ruleId, days));
    }

}

