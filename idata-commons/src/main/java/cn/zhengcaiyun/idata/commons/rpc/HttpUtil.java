/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.zhengcaiyun.idata.commons.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @author shiyin
 * @date 2021-03-29 09:42
 */
public class HttpUtil {

    private final static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build();

    static {
        client.dispatcher().setMaxRequestsPerHost(32);
    }

    public static Response executeHttpRequest(HttpInput httpInput) throws IOException {
        checkArgument(httpInput.getMethod() != null, "http request method is null");
        checkArgument(httpInput.getUri() != null, "http request uri is null");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(httpInput.getUri()).newBuilder();
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
        switch (httpInput.getMethod()) {
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
                throw new RuntimeException("not support http method " + httpInput.getMethod());
        }
        return client.newCall(requestBuilder.build()).execute();
    }

    public static <T> T executeHttpRequest(HttpInput httpInput, TypeReference<T> typeReference) {
        String serverName = isNotEmpty(httpInput.getServerName()) ?
                "[" + httpInput.getServerName() + "]" :
                "[]";
        String body = null;
        try (Response response = HttpUtil.executeHttpRequest(httpInput)) {
            body = Objects.requireNonNull(response.body()).string();
            if (response.code() == 200) {
                if (String.class.equals(typeReference.getType())) {
                    return (T) body;
                }
                return JSON.parseObject(body, typeReference);
            }
            else {
                throw new RuntimeException(serverName + " sever return error, http status code: "
                        + response.code() + ", body: " + body);
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException(serverName + " sever network error, msg: " + ioe.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(serverName + " error, msg: " + e.getMessage() + ", body: " + body);
        }
    }
}
