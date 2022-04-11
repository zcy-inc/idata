package cn.zhengcaiyun.idata.portal.controller.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.facade.MetadataFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/p1/script")
public class ScriptController {

    @Autowired
    private MetadataFacade metadataFacade;

    /**
     * 脚本：
     * @param ids
     * @return
     */
    @PostMapping("/pull/hiveInfo")
    public RestResult<Boolean> pullHiveInfo(@RequestBody List<Long> ids) {
        metadataFacade.pullHiveInfo(ids);
        return RestResult.success(true);
    }
}
