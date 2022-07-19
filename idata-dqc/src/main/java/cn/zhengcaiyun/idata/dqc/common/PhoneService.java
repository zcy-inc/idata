//package cn.zhengcaiyun.idata.dqc.common;
//
//import cn.gov.zcy.communication.api.CommunicationService;
//import cn.gov.zcy.communication.api.model.VoiceRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author shiyin(沐泽)
// * @date 2020/12/28 09:26
// */
//@Slf4j
//@Service
//public class PhoneService {
//
//    @Autowired
//    private HttpService httpService;
//
//    private static final String API_GATEWAY_ONLINE = "http://api.zcygov.cn";
//    private static final String APP_KEY_ONLINE  = "8000183";
//    private static final String SECRET_ONLINE = "q6917HPK25F9z";
//
//    @Autowired
//    private CommunicationService communicationService;
//    @Autowired
//    private ZcyOpenService zcyOpenService;
//
//    public boolean voice(String userName, String message, String phone) {
//        try {
//            Map<String, String> params = new HashMap<>();
//            params.put("userName", userName);
//            params.put("message", message);
//            return communicationService.sendVoice(new VoiceRequest("idata_dqc", params, phone)).isSuccess();
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean zcyOpenVoice(String userName, String message, String phone) {
//        try {
//            Map<String, String> params = new HashMap<>();
//            params.put("userName", userName);
//            params.put("message", message);
//            return "200".equals(zcyOpenService.zcyOpenVoice("idata_dqc", params, phone));
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//        }
//        return false;
//    }
//
//}
