package cn.zhengcaiyun.idata.dqc.utils;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import okhttp3.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HttpService implements InitializingBean {

    private final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build();

    @Override
    public void afterPropertiesSet() throws Exception {
        client.dispatcher().setMaxRequestsPerHost(32);
    }

    public Response executeHttpRequest(HttpInput httpInput, String uri, Object... uriParams) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(String.format(uri, uriParams)).newBuilder();
        if (httpInput.getQueryParamMap() != null) {
            for (Map.Entry<String, String> entry: httpInput.getQueryParamMap().entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());
        if (httpInput.getHeaderMap() != null) {
            requestBuilder = requestBuilder.headers(Headers.of(httpInput.getHeaderMap()));
        }
        String body;
        if (httpInput.getObjectBody() instanceof String) {
            body = (String) httpInput.getObjectBody();
        }
        else {
            body = httpInput.getObjectBody() != null ? JSON.toJSONString(httpInput.getObjectBody()) : "";
        }
        switch (httpInput.getMethod().name()) {
            case "GET": break;
            case "POST":
                requestBuilder = requestBuilder.post(RequestBody.create(body, JSON_MEDIA_TYPE));
                break;
            case "PUT":
                requestBuilder = requestBuilder.put(RequestBody.create(body, JSON_MEDIA_TYPE));
                break;
            case "DELETE":
                requestBuilder = requestBuilder.delete(RequestBody.create(body, JSON_MEDIA_TYPE));
                break;
            default:
                throw new RuntimeException("not support http method " + httpInput.getMethod().name());
        }
        return client.newCall(requestBuilder.build()).execute();
    }

    @Getter
    public static class HttpInput {
        private RequestMethod method;
        private Map<String, String> headerMap;
        private Map<String, String> queryParamMap;
        private Object objectBody;

        public HttpInput setMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        public HttpInput setHeaderMap(Map<String, String> headerMap) {
            this.headerMap = headerMap;
            return this;
        }

        public HttpInput setQueryParamMap(Map<String, String> queryParamMap) {
            this.queryParamMap = queryParamMap;
            return this;
        }

        public HttpInput setObjectBody(Object objectBody) {
            this.objectBody = objectBody;
            return this;
        }
    }

}
