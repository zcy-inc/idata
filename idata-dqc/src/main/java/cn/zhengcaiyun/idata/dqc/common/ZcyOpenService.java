package cn.zhengcaiyun.idata.dqc.common;

import cn.gov.zcy.open.HttpMethod;
import cn.gov.zcy.open.ZcyClient;
import cn.gov.zcy.open.ZcyOpenRequest;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caizhedong
 * @date 2021-04-06 14:15
 */

@Service
public class ZcyOpenService {

    private static ZcyClient zcyOpenClient = new ZcyClient();

    private static final String API_GATEWAY_ONLINE = "http://api.zcygov.cn";
    private static final String SECRET_ONLINE  = "q6917HPK25F9z";
    private static final String APP_KEY_ONLINE = "8000183";

    public String zcyOpenVoice(String key, Map<String, String> params, String phone) throws Exception {
        ZcyOpenRequest zcyOpenRequest = new ZcyOpenRequest(APP_KEY_ONLINE, SECRET_ONLINE, API_GATEWAY_ONLINE);
        String uri = "/communication/zcy.communication.voice.send";
        zcyOpenRequest.setUri(uri);
        zcyOpenRequest.setMethod(HttpMethod.POST);

        Map<String, Object> bodyMap = new HashMap<String, Object>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("phone", phone);
        jsonObject.put("context", params);
        bodyMap.put("_data_", jsonObject.toString());
        return zcyOpenClient.doPost(API_GATEWAY_ONLINE, uri, APP_KEY_ONLINE, SECRET_ONLINE,
                HttpMethod.POST, bodyMap);
    }

}
