package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.utils.ExecutorServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dqc")
public class DqcController {
    @Autowired
    private MonitorRuleService monitorRuleService;

    @Autowired
    private MessageSendService messageSendService;

    @RequestMapping("/analyse")
    public Result dqc(@RequestBody Map<String,Object> param) {
        if(param.get("jobId") == null ){
            return Result.failureResult("jobId不能为空");
        }
        if(param.get("jobEndStatus") == null ){
            return Result.failureResult("jobEndStatus不能为空");
        }

        if(param.get("env") == null ){
            return Result.failureResult("env不能为空");
        }

        Long jobId = Long.parseLong(param.get("jobId").toString());
        String jobEndStatus = param.get("jobEndStatus").toString();
        String env = param.get("env").toString();

        System.out.println();
        ExecutorServiceHelper.submit(() -> monitorRuleService.analyse(jobId,env));
        return Result.successResult();
    }




}
