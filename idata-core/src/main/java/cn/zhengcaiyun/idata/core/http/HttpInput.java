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
package cn.zhengcaiyun.idata.core.http;

import java.util.Map;

/**
 * @author shiyin
 * @date 2021-03-29 09:59
 */
public class HttpInput {
    private String serverName;
    private String method;
    private String uri;
    private Map<String, String> headerMap;
    private Map<String, String> queryParamMap;
    private Object objectBody;

    public String getServerName() {
        return serverName;
    }

    public HttpInput setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpInput setMethod(String requestMethod) {
        this.method = requestMethod;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public HttpInput setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public HttpInput setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
        return this;
    }

    public Map<String, String> getQueryParamMap() {
        return queryParamMap;
    }

    public HttpInput setQueryParamMap(Map<String, String> queryParamMap) {
        this.queryParamMap = queryParamMap;
        return this;
    }

    public Object getObjectBody() {
        return objectBody;
    }

    public HttpInput setObjectBody(Object objectBody) {
        this.objectBody = objectBody;
        return this;
    }
}
