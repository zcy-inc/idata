package cn.zhengcaiyun.idata.dqc.common;

import cn.gov.zcy.message.center.api.req.MessageBody;
import cn.gov.zcy.message.center.api.req.RequestHeader;
import cn.gov.zcy.open.HttpMethod;
import cn.gov.zcy.open.ZcyClient;
import cn.zhengcaiyun.idata.dqc.dao.UserDao;
import cn.zhengcaiyun.idata.dqc.model.common.BizException;
import cn.zhengcaiyun.idata.dqc.model.entity.User;
import cn.zhengcaiyun.idata.dqc.utils.HttpService;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageSendService {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);
//    private static final int DEFAULT_DUBBO_TIMEOUT = 10000;

//    @Reference(timeout = DEFAULT_DUBBO_TIMEOUT, check = false, version = "1.0.0")
//    private CommunicationService communicationService;
//
//    @Reference(timeout = DEFAULT_DUBBO_TIMEOUT, check = false, version = "1.0.0")
//    private MessageFacade messageFacade;

    @Autowired
    private UserDao userDao;

    @Autowired
    HttpService httpService;

    @Value("${duty.phone.url}")
    private String dutyPhoneUrl;

    @Value("${sms.client.app.uri}")
    private String smsClientAppUri;

    @Value("${alert.key}")
    private String alertKey;

    @Value("${phone.client.app.uri}")
    private String phoneClientAppUri;

    @Value("${phone.client.app.host}")
    private String phoneClientAppHost;

    @Value("${phone.client.app.secret}")
    private String phoneClientAppSecret;

    @Value("${phone.client.app.key}")
    private String phoneClientAppKey;

    @Value("${dingding.webhook}")
    private String dingdingWebhook;

    /**
     * @param types     dingding,phone，sms
     * @param nicknames
     * @param message
     */
    public void send(String[] types, String[] nicknames, String message, String tableName, String ruleName) {
        for (String type : types) {
            if ("phone".equals(type)) {
                this.zcyOpenVoice(nicknames);
            } else if ("sms".equals(type)) {
                this.zcyOpenSms(nicknames, tableName, ruleName);
            } else {
                this.sendToDingDing(nicknames, message);
            }
        }
    }

    public void sendToDingDing(String[] nicknames, String message) {
        List<String> mobiles = new ArrayList();
        if (nicknames.length > 0) {
            List<User> mobileList = userDao.getMobilesByNickname(nicknames);
            for (User user : mobileList) {
                mobiles.add(user.getMobile());
            }
        }
        for (String nickname : nicknames) {
            if (RuleUtils.DW_DUTY.equals(nickname)) {
                mobiles.add(this.getDutyPhone());
                break;
            }
        }

        if (mobiles.size() == 0) {
            return;
        }
        this.sendDingGroup(dingdingWebhook, message, mobiles.toArray(new String[mobiles.size()]));
    }

    public void sendDingGroup(String webhook, String message, String[] mobiles) {
        JSONObject body = new JSONObject()
                .fluentPut("msgtype", "text")
                .fluentPut("text", new JSONObject().fluentPut("content", "【数据质量】" + message))
                .fluentPut("at", new JSONObject().fluentPut("isAtAll", true));
        if (mobiles != null) {
            body.fluentPut("at", new JSONObject().fluentPut("atMobiles", mobiles));
        }
        sendToDingTalk(new HttpService.HttpInput().setMethod(RequestMethod.POST).setObjectBody(body.toJSONString()),
                new TypeReference<String>() {
                },
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
            } else {
                throw new BizException("[dingding] sever return error, " + response.code() + ", " + body);
            }
        } catch (IOException ioe) {
            throw new BizException("[dingding] sever network error, " + ioe.getMessage());
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            throw new BizException("[dingding] return: " + body);
        }
    }

//    public void sengDingding(String username, String message) {
//        this.sengDingding("username", new String[]{username}, message);
//    }

    public void sengDingdingByNickname(String nickname, String message) {
        this.sendToDingDing(new String[]{nickname}, message);
    }
//
//    public void sengDingding(String nameType, String[] names, String message) {
//        this.sengDingding(nameType, names, "告警", message);
//    }

    /**
     * 发送钉钉消息
     *
     * @param nameType:username,nickname
     * @param message
     */
//    public void sengDingding(String nameType, String[] names, String title, String message) {
//        List userList = new ArrayList();
//        if ("nickname".equals(nameType)) {
//            List<User> mobileList = userDao.getMobilesByNickname(names);
//            for (User user : mobileList) {
//                userList.add(user.getUsername());
//            }
//        } else {
//            userList = Lists.newArrayList(names);
//        }
//        RequestHeader header = new RequestHeader();
//        header.setBizCode("dingding_person_send");
//        header.setToUsers(userList);
//        header.setChannelCodes(Lists.newArrayList("dingding_person_msg"));
//        header.setTemplateCode("dingding template");
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("dingdingtitle", title);
//        map.put("dingdingtext", message);
//
//        MessageBody messageBody = new MessageBody();
//        messageBody.setBody(map);
//        messageBody.setHeader(header);
//
//        Response response = messageFacade.sendBizMsg(messageBody);
//        if (!response.isSuccess()) {
//            logger.error(response.getMessage());
//        }
//    }

    /**
     * 语音电话
     *
     * @param nameType:username,nickname
     * @param message
     */
//    public void sengPhone(String nameType, String[] names, String message) {
//        List<User> mobileList = null;
//        if ("username".equals(nameType)) {
//            mobileList = userDao.getMobilesByUsername(names);
//        } else {
//            mobileList = userDao.getMobilesByNickname(names);
//        }
//
//        for (User user : mobileList) {
//            Map<String, String> params = new HashMap<>();
//            params.put("userName", user.getNickname());
//            params.put("message", message);
//
//            cn.gov.zcy.common.api.Response<VoiceResponse> response = communicationService.sendVoice(new VoiceRequest("idata_dqc", params, user.getMobile()));
//            if (!response.isSuccess()) {
//                logger.error(String.format("发送电话语音消息失败：用户[%s],消息[%s],错误原因[%s]", user.getNickname(), message,
//                        StringUtils.isEmpty(response.getMessage()) ? response.getCode() : response.getMessage()));
//            }
//        }
//    }

    /**
     * 发送短信
     *
     * @param nameType:username,nickname
     * @param message
     */
//    public void sengMessage(String nameType, String name, String message) {
//        this.sengMessage(nameType, new String[]{name}, message);
//    }

    /**
     * 群发短信
     *
     * @param nameType:username,nickname
     * @param message
     */
//    public void sengMessage(String nameType, String[] names, String message) {
//        List<User> mobileList = null;
//        if ("username".equals(nameType)) {
//            mobileList = userDao.getMobilesByUsername(names);
//        } else {
//            mobileList = userDao.getMobilesByNickname(names);
//        }
//        for (User user : mobileList) {
//            Map<String, String> params = new HashMap<>();
//            params.put("userName", user.getNickname());
//            params.put("message", message);
//
//            cn.gov.zcy.common.api.Response<SmsResponse> response = communicationService.sendSms(new SmsRequest("idata_dqc", params, user.getMobile()));
//            if (!response.isSuccess()) {
//                logger.error(String.format("发送短信消息失败：用户[%s],消息[%s],错误原因[%s]", user.getNickname(), message,
//                        StringUtils.isEmpty(response.getMessage()) ? response.getCode() : response.getMessage()));
//            }
//        }
//    }
    public void sendDutyPhone() {
        String mobile = this.getDutyPhone();
        if (StringUtils.isEmpty(mobile)) {
            return;
        }

        this.zcyOpenVoice(mobile);
    }

    private String getDutyPhone() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(new URIBuilder(dutyPhoneUrl).build());
            response = httpclient.execute(httpGet);
            String get = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            JSONObject jsonObject = JSON.parseObject(get);
            if (!"true".equals(jsonObject.get("success").toString())) {
                logger.error("the error msg: {}", jsonObject.get("msg"));
                return null;
            }
            return jsonObject.get("data").toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                logger.error("close error ", e);
            }
        }
        return null;
    }

    public void zcyOpenVoice(String[] nicknames) {
        for (String nickname : nicknames) {
            if (RuleUtils.DW_DUTY.equals(nickname)) {
                this.zcyOpenVoice(this.getDutyPhone());
            }
        }
        if (nicknames.length > 0) {
            List<User> mobileList = userDao.getMobilesByNickname(nicknames);
            for (User user : mobileList) {
                this.zcyOpenVoice(user.getMobile());
            }
        }
    }

    public boolean zcyOpenVoice(String phone) {
        try {
            ZcyClient zcyOpenClient = new ZcyClient();
            JSONObject params = new JSONObject();
            params.put("userName", "");
            params.put("message", "");
            JSONObject nodes = new JSONObject();
            nodes.put("phone", phone);
            nodes.put("context", params);
            nodes.put("key", alertKey);
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("_data_", nodes.toString());
            String code = zcyOpenClient.doPost(phoneClientAppHost, phoneClientAppUri, phoneClientAppKey, phoneClientAppSecret, HttpMethod.POST, bodyMap);
            return "200".equals(code);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void zcyOpenSms(String[] nicknames, String tableName, String ruleName) {
        for (String nickname : nicknames) {
            if (RuleUtils.DW_DUTY.equals(nickname)) {
                this.zcyOpenSms(this.getDutyPhone(), tableName, ruleName);
                break;
            }
        }

        List<User> mobileList = userDao.getMobilesByNickname(nicknames);
        for (User user : mobileList) {
            this.zcyOpenSms(user.getMobile(), tableName, ruleName);
        }
    }

    public boolean zcyOpenSms(String phone, String tableName, String ruleName) {
        try {
            ZcyClient zcyOpenClient = new ZcyClient();
            JSONObject params = new JSONObject();
            params.put("tableName", tableName);
            params.put("ruleName", ruleName);
            JSONObject nodes = new JSONObject();
            nodes.put("phones", phone);
            nodes.put("key", alertKey);
            nodes.put("context", params);
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("_data_", nodes.toString());
            String code = zcyOpenClient.doPost(phoneClientAppHost, smsClientAppUri, phoneClientAppKey, phoneClientAppSecret, HttpMethod.POST, bodyMap);
            return "200".equals(code);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
