package cn.zhengcaiyun.idata.portal.controller.spark;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySqlResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p1/spark")
public class SparkApiController {

    @Autowired
    private LivyService livyService;

    @GetMapping("queryResult")
    public RestResult<LivySqlResultDto> queryResult(@RequestParam("sessionId") Integer sessionId,
                                                    @RequestParam("statementId") Integer statementId) {
        return RestResult.success(livyService.queryResult(sessionId, statementId));
    }
}
