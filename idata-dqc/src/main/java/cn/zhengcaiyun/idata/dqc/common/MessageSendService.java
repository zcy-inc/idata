package cn.zhengcaiyun.idata.dqc.common;

import cn.gov.zcy.communication.api.CommunicationService;
import cn.gov.zcy.communication.api.model.SmsRequest;
import cn.gov.zcy.communication.api.model.SmsResponse;
import cn.gov.zcy.communication.api.model.VoiceRequest;
import cn.gov.zcy.communication.api.model.VoiceResponse;
import cn.gov.zcy.message.center.api.MessageFacade;
import cn.gov.zcy.message.center.api.req.MessageBody;
import cn.gov.zcy.message.center.api.req.RequestHeader;
import cn.gov.zcy.operating.utils.Response;
import cn.zhengcaiyun.idata.dqc.dao.UserDao;
import cn.zhengcaiyun.idata.dqc.model.entity.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageSendService {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);
    private static final int DEFAULT_DUBBO_TIMEOUT = 10000;

    @Reference(timeout = DEFAULT_DUBBO_TIMEOUT, check = false, version = "1.0.0")
    private CommunicationService communicationService;

    @Reference(timeout = DEFAULT_DUBBO_TIMEOUT, check = false, version = "1.0.0")
    private MessageFacade messageFacade;

    @Autowired
    private UserDao userDao;

    /**
     * @param types     dingding,meaasge,phone
     * @param nicknames
     * @param message
     */
    public void send(String[] types, String[] nicknames, String message) {
        for (String type : types) {
            if ("message".equals(type)) {
                this.sengMessage("nickname", nicknames, message);
            } else if ("phone".equals(type)) {
                this.sengPhone("nickname", nicknames, message);
            } else {
                this.sengDingding("nickname", nicknames, message);
            }
        }
    }

    public void sengDingding(String username, String message) {
        this.sengDingding("username", new String[]{username}, message);
    }

    public void sengDingdingByNickname(String nickname,String title, String message) {
        this.sengDingding("nickname", new String[]{nickname},title, message);
    }

    public void sengDingding(String nameType, String[] names, String message) {
        this.sengDingding(nameType,names,"告警",message);
    }
    /**
     * 发送钉钉消息
     *
     * @param nameType:username,nickname
     * @param message
     */
    public void sengDingding(String nameType, String[] names, String title,String message) {
        List userList = new ArrayList();
        if ("nickname".equals(nameType)) {
            List<User> mobileList = userDao.getMobilesByNickname(names);
            for (User user : mobileList) {
                userList.add(user.getUsername());
            }
        } else {
            userList = Lists.newArrayList(names);
        }
        RequestHeader header = new RequestHeader();
        header.setBizCode("dingding_person_send");
        header.setToUsers(userList);
        header.setChannelCodes(Lists.newArrayList("dingding_person_msg"));
        header.setTemplateCode("dingding template");

        Map<String, Object> map = new HashMap<>();
        map.put("dingdingtitle", title);
        map.put("dingdingtext", message);

        MessageBody messageBody = new MessageBody();
        messageBody.setBody(map);
        messageBody.setHeader(header);

        Response response = messageFacade.sendBizMsg(messageBody);
        if (!response.isSuccess()) {
            logger.error(response.getMessage());
        }
    }

    /**
     * 语音电话
     *
     * @param nameType:username,nickname
     * @param message
     */
    public void sengPhone(String nameType, String[] names, String message) {
        List<User> mobileList = null;
        if ("username".equals(nameType)) {
            mobileList = userDao.getMobilesByUsername(names);
        } else {
            mobileList = userDao.getMobilesByNickname(names);
        }

        for (User user : mobileList) {
            Map<String, String> params = new HashMap<>();
            params.put("userName", user.getNickname());
            params.put("message", message);

            cn.gov.zcy.common.api.Response<VoiceResponse> response = communicationService.sendVoice(new VoiceRequest("idata_dqc", params, user.getMobile()));
            if (!response.isSuccess()) {
                logger.error(String.format("发送电话语音消息失败：用户[%s],消息[%s],错误原因[%s]", user.getNickname(), message,
                        StringUtils.isEmpty(response.getMessage()) ? response.getCode() : response.getMessage()));
            }
        }
    }

    /**
     * 发送短信
     *
     * @param nameType:username,nickname
     * @param message
     */
    public void sengMessage(String nameType, String name, String message) {
        this.sengMessage(nameType, new String[]{name}, message);
    }

    /**
     * 群发短信
     *
     * @param nameType:username,nickname
     * @param message
     */
    public void sengMessage(String nameType, String[] names, String message) {
        List<User> mobileList = null;
        if ("username".equals(nameType)) {
            mobileList = userDao.getMobilesByUsername(names);
        } else {
            mobileList = userDao.getMobilesByNickname(names);
        }
        for (User user : mobileList) {
            Map<String, String> params = new HashMap<>();
            params.put("userName", user.getNickname());
            params.put("message", message);

            cn.gov.zcy.common.api.Response<SmsResponse> response = communicationService.sendSms(new SmsRequest("idata_dqc", params, user.getMobile()));
            if (!response.isSuccess()) {
                logger.error(String.format("发送短信消息失败：用户[%s],消息[%s],错误原因[%s]", user.getNickname(),message,
                        StringUtils.isEmpty(response.getMessage()) ? response.getCode() : response.getMessage()));
            }
        }
    }

}
