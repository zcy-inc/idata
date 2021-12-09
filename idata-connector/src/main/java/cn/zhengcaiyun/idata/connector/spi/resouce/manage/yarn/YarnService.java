package cn.zhengcaiyun.idata.connector.spi.resouce.manage.yarn;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.ResourceManageService;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.AppResourceDetail;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.ClusterMetricsResponse;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.response.YarnAppResourceResponse;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.response.YarnAppsResourceResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("yarnService")
public class YarnService implements ResourceManageService {

    @Value("${yarn.resourceManagerUri}")
    private String YARN_RM_URI;

    @Override
    public List<AppResourceDetail> loadAppResourceDetailList(Long finishTimeBegin, Long finishTimeEnd) {
        if (finishTimeBegin == null) {
            finishTimeBegin = 0L;
        }
        if (finishTimeEnd == null) {
            finishTimeEnd = Long.MAX_VALUE;
        }
        String url = String.format("%s/ws/v1/cluster/apps?states=FINISHED,KILLED&finishedTimeBegin=%d&finishedTimeEnd=%d",
                YARN_RM_URI, finishTimeBegin, finishTimeEnd);
        String jsonResponse = HttpUtil.get(url);
        YarnAppsResourceResponse yarnAppsResourceResponse = JSON.parseObject(jsonResponse, YarnAppsResourceResponse.class);
        List<YarnAppResourceResponse> app = yarnAppsResourceResponse.getApps().getApp();
        List<AppResourceDetail> appResourceDetails = Convert.toList(AppResourceDetail.class, app);
        return appResourceDetails;
    }

    @Override
    public ClusterMetricsResponse clusterMetrics() {
        String url = String.format("%s/ws/v1/cluster/metrics", YARN_RM_URI);
        HttpResponse res = HttpRequest.get(url).execute();
        int httpStatus = res.getStatus();
        if (httpStatus < 200 || httpStatus >= 300) {
            throw new GeneralException("http request fail:" + httpStatus + ". " + url);
        }
        JSONObject jsonObject = JSON.parseObject(res.body());
        ClusterMetricsResponse response = JSONObject.parseObject(jsonObject.get("clusterMetrics").toString(), ClusterMetricsResponse.class);
        return response;
    }

    public static void main(String[] args) {

//        String url = String.format("%s/ws/v1/cluster/apps?states=FINISHED,KILLED&finishedTimeBegin=%d&finishedTimeEnd=%d",
//                "http://bigdata-master3.cai-inc.com:8088", DateUtil.offset(DateUtil.yesterday(), DateField.HOUR, 23).getTime(), DateUtil.date().getTime());
//        String jsonResponse = HttpUtil.get(url);
//
//        System.out.println(jsonResponse);
//
////        YarnAppsResourceResponse yarnAppsResourceResponse = JSONUtil.toBean(jsonResponse, YarnAppsResourceResponse.class);
////        System.out.println(yarnAppsResourceResponse.getApps().getApp().get(0).getName());
//        YarnAppsResourceResponse yarnAppsResourceResponse = JSON.parseObject(jsonResponse, YarnAppsResourceResponse.class);
//        System.out.println(yarnAppsResourceResponse.getApps().getApp().get(0).getName());
//
//
//        List<YarnAppResourceResponse> app = yarnAppsResourceResponse.getApps().getApp();
//        List<AppResourceDetail> appResourceDetails = Convert.toList(AppResourceDetail.class, app);
//        System.out.println(appResourceDetails.size());

        String url = String.format("%s/ws/v1/cluster/metrics", "http://bigdata-master3.cai-inc.com:8088");
//        String jsonResponse = HttpUtil.get(url);
        HttpResponse res = HttpRequest.get(url).execute();
        System.out.println(res.getStatus());
        String jsonResponse = res.body();
        System.out.println(jsonResponse);
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        ClusterMetricsResponse response = JSONObject.parseObject(jsonObject.get("clusterMetrics").toString(), ClusterMetricsResponse.class);
        System.out.println(JSON.toJSONString(response));
    }
}
