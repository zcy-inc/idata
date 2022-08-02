package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.utils.ExecutorServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dqc")
public class DqcController {
    @Autowired
    private MonitorRuleService monitorRuleService;

    @RequestMapping("/analyse/{jobId}")
    public Result dqc(@PathVariable Long jobId) {
        ExecutorServiceHelper.submit(() -> monitorRuleService.analyse(jobId));
        return Result.successResult();
    }

}
