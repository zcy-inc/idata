package cn.zhengcaiyun.idata.dqc.common;

import cn.zhengcaiyun.idata.dqc.common.HttpService;
import cn.zhengcaiyun.idata.dqc.model.common.BizException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @author shiyin(沐泽)
 * @date 2020/7/15 15:26
 */

@Service
public class DingDingService {

    @Autowired
    HttpService httpService;

    public void sendToDingDing(String webhook, String msg, String atMobile) {
        JSONObject body = new JSONObject()
                .fluentPut("msgtype", "text")
                .fluentPut("text", new JSONObject().fluentPut("content", "【IData】" + msg))
                .fluentPut("at", new JSONObject().fluentPut("isAtAll", true));
        if (atMobile != null) {
            body.fluentPut("at", new JSONObject().fluentPut("atMobiles", new String[]{atMobile}));
        }
        sendToDingTalk(new HttpService.HttpInput().setMethod(RequestMethod.POST).setObjectBody(body.toJSONString()),
                new TypeReference<String>(){},
                webhook);
    }

    private <T> T sendToDingTalk(HttpService.HttpInput httpInput, TypeReference<T> typeReference, String uri, Object... uriParams) {
        String body = null;
        try (Response response = httpService.executeHttpRequest(httpInput, uri, uriParams)) {
            body = response.body().string();
            if (response.code() == 200 && body != null) {
                if (String.class.equals(typeReference.getType())) {
                    return (T) body;
                }
                return JSON.parseObject(body, typeReference);
            }
            else {
                throw new BizException(0, "[dingding] sever return error, " + response.code() + ", " + body);
            }
        }
        catch (IOException ioe) {
            throw new BizException(0, "[dingding] sever network error, " + ioe.getMessage());
        }
        catch (BizException be) {
            throw be;
        }
        catch (Exception e) {
            throw new BizException(0, "[dingding] return: " + body);
        }
    }

}
