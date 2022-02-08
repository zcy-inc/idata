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

package cn.zhengcaiyun.idata.connector.spi.livy.dto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-13 15:01
 **/
public class LivySessionLogDto {
    private Integer sessionId;
    private String state;
    private Integer from;
    private Integer total;
    private List<String> log;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }
}
