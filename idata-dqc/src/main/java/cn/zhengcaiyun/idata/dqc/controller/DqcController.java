package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.utils.ExecutorServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(DqcController.class);

    @Autowired
    private MonitorRuleService monitorRuleService;

    @Autowired
    private MessageSendService messageSendService;

    @RequestMapping("/analyse")
    public Result dqc(@RequestBody Map<String, Object> param) {
        if (param.get("jobId") == null) {
            return Result.failureResult("jobId不能为空");
        }
        if (param.get("jobEndStatus") == null) {
            return Result.failureResult("jobEndStatus不能为空");
        }

        if (param.get("env") == null) {
            return Result.failureResult("env不能为空");
        }

        Long jobId = Long.parseLong(param.get("jobId").toString());
        String env = param.get("env").toString();
        String status = param.get("jobEndStatus").toString();

        logger.info(String.format("数据质量分析接口调用，jobId：%s,env:%s,jobEndStatus:%s", jobId, env, status));

        //作业执行失败直接返回
        if (!"0".equals(status)) {
            logger.error(String.format("作业%s执行失败", jobId));
            messageSendService.sendDutyPhone(String.format("您在IData上的作业执行失败，作业id：%s,环境：%s", jobId, env));
            return Result.failureResult("作业执行失败，已经发送语音电话到值班人员");
        }

        ExecutorServiceHelper.submit(() -> monitorRuleService.analyse(jobId, env));
        return Result.successResult();
    }


}
