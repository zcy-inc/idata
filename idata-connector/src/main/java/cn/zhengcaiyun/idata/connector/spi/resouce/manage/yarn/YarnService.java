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
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.zhengcaiyun.idata.system.common.constant.SystemConfigConstant;

import java.util.List;

@Service("yarnService")
public class YarnService implements ResourceManageService {

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public List<AppResourceDetail> loadAppResourceDetailList(Long finishTimeBegin, Long finishTimeEnd) {
        String yarnRmURI = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_HTOOL_CONFIG, SystemConfigConstant.HTOOL_CONFIG_YARN_ADDR);

        if (finishTimeBegin == null) {
            finishTimeBegin = 0L;
        }
        if (finishTimeEnd == null) {
            finishTimeEnd = Long.MAX_VALUE;
        }
        String url = String.format("%s/ws/v1/cluster/apps?states=FINISHED,KILLED&finishedTimeBegin=%d&finishedTimeEnd=%d",
                yarnRmURI, finishTimeBegin, finishTimeEnd);
        String jsonResponse = HttpUtil.get(url);
        YarnAppsResourceResponse yarnAppsResourceResponse = JSON.parseObject(jsonResponse, YarnAppsResourceResponse.class);
        List<YarnAppResourceResponse> app = yarnAppsResourceResponse.getApps().getApp();
        List<AppResourceDetail> appResourceDetails = Convert.toList(AppResourceDetail.class, app);
        return appResourceDetails;
    }

    @Override
    public ClusterMetricsResponse clusterMetrics() {
        String yarnRmURI = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_HTOOL_CONFIG, SystemConfigConstant.HTOOL_CONFIG_YARN_ADDR);
        String url = String.format("%s/ws/v1/cluster/metrics", yarnRmURI);
        HttpResponse res = HttpRequest.get(url).execute();
        int httpStatus = res.getStatus();
        if (httpStatus < 200 || httpStatus >= 300) {
            throw new GeneralException("http request fail:" + httpStatus + ". " + url);
        }
        JSONObject jsonObject = JSON.parseObject(res.body());
        ClusterMetricsResponse response = JSONObject.parseObject(jsonObject.get("clusterMetrics").toString(), ClusterMetricsResponse.class);
        return response;
    }

}
