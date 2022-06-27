package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author:zheng
 * Date:2022/6/17
 */
@RestController
@RequestMapping("monitorTable")
public class MonitorTableController {

    @RequestMapping("add")
    public String add(MonitorTableVO monitorTableVO){
        return "add";
    }
}
