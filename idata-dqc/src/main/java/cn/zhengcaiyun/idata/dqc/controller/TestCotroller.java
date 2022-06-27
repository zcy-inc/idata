package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.service.HiveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author:zheng
 * Date:2022/6/17
 */
@Controller
public class TestCotroller {
    @Autowired
    private HiveInfoService hiveInfoService;

    @RequestMapping("ok")
    @ResponseBody
    public String ok(){
        hiveInfoService.getTableInfo("tmp","test_partitioned","year=2022/month=6/day=22");

        return "ok";
    }
}
