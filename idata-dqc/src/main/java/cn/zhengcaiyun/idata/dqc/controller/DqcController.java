package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.dqc.model.common.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dqc")
public class DqcController {
    @RequestMapping("/analyse/{jobId}")
    public Result dqc(@PathVariable Long jobId){

        return Result.successResult();
    }
}
