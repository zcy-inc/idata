package cn.zhengcaiyun.idata.portal.controller.uac;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.auth.AuthResourceService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UAC-Authorization
 */
@RestController
@RequestMapping(path = "/p1/uac/authResource")
public class AuthResourceController {

    private final AuthResourceService authResourceService;

    @Autowired
    public AuthResourceController(AuthResourceService authResourceService) {
        this.authResourceService = authResourceService;
    }

    /**
     * 获取授权资源列表
     *
     * @param resourceType 授权资源类型
     * @param paramJson    参数，资源类型为 tables 时，传 dbName: "如：dwd"
     * @return
     */
    @PostMapping("/resourceTypes/{resourceType}")
    public RestResult<List<String>> getAuthEntry(@PathVariable("resourceType") AuthResourceTypeEnum resourceType,
                                                 @RequestBody JSONObject paramJson) {
        return RestResult.success(authResourceService.fetchAuthResource(resourceType, paramJson));
    }
}
