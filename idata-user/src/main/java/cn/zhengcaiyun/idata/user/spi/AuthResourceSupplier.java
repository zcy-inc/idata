package cn.zhengcaiyun.idata.user.spi;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface AuthResourceSupplier {

    List<String> supplyResources(JSONObject paramJson);
}
