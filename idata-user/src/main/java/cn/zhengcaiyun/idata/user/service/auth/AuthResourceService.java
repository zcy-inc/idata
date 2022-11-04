package cn.zhengcaiyun.idata.user.service.auth;

import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface AuthResourceService {

    List<String> fetchAuthResource(AuthResourceTypeEnum resourceType, JSONObject paramJson);
}
