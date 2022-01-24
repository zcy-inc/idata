package cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.response;

import java.util.List;

/**
 * sample:
 *              {
 *                  "apps":{
 *                      "app":Array[39]
 *                  }
 *              }
 */
public class YarnAppsResourceResponse {
    private APPS apps;

    public APPS getApps() {
        return apps;
    }

    public void setApps(APPS apps) {
        this.apps = apps;
    }

    public static class APPS {
        private List<YarnAppResourceResponse> app;

        public List<YarnAppResourceResponse> getApp() {
            return app;
        }

        public void setApp(List<YarnAppResourceResponse> app) {
            this.app = app;
        }
    }
}
