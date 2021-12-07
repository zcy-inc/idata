package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.commons.rpc.HttpUtil;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.JobRunOverviewDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import netscape.javascript.JSObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

public class TaskIntegratorTest {

    public static void main(String[] args) {
        getJobLatestRecordsTest();
    }

    public static void getJobLatestRecordsTest() {
        String projectCode = "3753857349440";
        String prefix = "http://172.29.108.238:8688/dolphinscheduler";
        String req_url = prefix + String.format("/projects/%s/task-instances?pageSize=%d&pageNo=%d", projectCode, 1000, 1);
        String req_method = "GET";
        String token = "ba33abe5da62f976c46a0343fe6efe77";
        HttpInput req_input = buildHttpReq(Maps.newHashMap(), req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("获取DS任务实例信息失败：%s", resultDto.getMsg()));
        }
        JSONObject data = resultDto.getData();
        JSONArray totalList = data.getJSONArray("totalList");
        List<JobRunOverviewDto> list = JSONObject.parseObject(totalList.toJSONString(), new TypeReference<>() {
        });
        Map<Long, JobRunOverviewDto> runInfoMap = Maps.newHashMap();
        list.forEach(e -> {
            // 例如 di_test111__005096
            String jobIdStr = ReUtil.get(".*__(\\d*)", e.getName(), 1);
            if (NumberUtil.isNumber(jobIdStr)) {
                runInfoMap.put(Long.parseLong(jobIdStr), e);
            }
        });
        System.out.println(list.size());
    }

    protected static HttpInput buildHttpReq(Map<String, String> req_param, String req_url, String req_method, String token) {
        HttpInput input = new HttpInput();
        input.setServerName("[Dolphin Scheduler]");
        input.setUri(req_url);
        input.setMethod(req_method);
        input.setQueryParamMap(req_param);
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("token", token);
        input.setHeaderMap(headMap);
        return input;
    }

    protected static ResultDto<JSONObject> sendReq(HttpInput req_input) {
        ResultDto<JSONObject> resultDto = HttpUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<JSONObject>>() {
        });
        return resultDto;
    }
}
