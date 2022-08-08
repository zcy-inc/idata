package cn.zhengcaiyun.idata.portal.controller.sys;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.WriteModeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanqian
 * @date 2022/3/28 11:27 AM
 * @description
 */
@RestController
@RequestMapping("/p1/dictionary/")
public class DictionaryController {

    /**
     * 写入模式枚举 DiEnum/BackFlowEnum/SqlEnum
     *
     * @return
     */
    @GetMapping("/enum/writeMode")
    public RestResult<List<String>> getWriteModeEnumList(@RequestParam("writeMode") String writeMode) {
        switch (writeMode) {
            case "DiEnum" :
                return RestResult.success(Arrays.stream(WriteModeEnum.DiEnum.values()).map(e -> e.name()).collect(Collectors.toList()));
            case "BackFlowEnum":
                return RestResult.success(Arrays.stream(WriteModeEnum.BackFlowEnum.values()).map(e -> e.name()).collect(Collectors.toList()));
            case "SqlEnum":
                return RestResult.success(Arrays.stream(WriteModeEnum.SqlEnum.values()).map(e -> e.name()).collect(Collectors.toList()));
        }
        throw new GeneralException("未匹配写入模式枚举： DiEnum/BackFlowEnum/SqlEnum");
    }

}
