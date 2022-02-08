package cn.zhengcaiyun.idata.connector.resourcemanager;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;

public class ResourceManagerServiceTest {

    public static void main(String[] args) {
        application();
    }

    private static void application() {
        String url = String.format("%s/ws/v1/cluster/apps?states=RUNNING",
                "http://bigdata-master3.cai-inc.com:8088");
        String jsonResponse = HttpUtil.get(url);
        System.out.println(jsonResponse);
    }
}
