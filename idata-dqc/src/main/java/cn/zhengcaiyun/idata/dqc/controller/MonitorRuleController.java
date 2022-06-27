package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author:zheng
 * Date:2022/6/17
 */
@RestController
@RequestMapping("monitorRule")
public class MonitorRuleController {
    @Autowired
    private MonitorRuleService monitorRuleService;

    @RequestMapping("add")
    public String add(){
        return "add";
    }

    @RequestMapping("getMonitorRules")
    public String getMonitorRules(){
        monitorRuleService.add(null);
        return "add";
    }
}
