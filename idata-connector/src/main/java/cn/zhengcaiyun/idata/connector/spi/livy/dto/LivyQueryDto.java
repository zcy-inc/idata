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

import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-13 15:08
 **/
public class LivyQueryDto {
    private Integer sessionId;
    private String sourceQuery;
    private List<Long> udfIds;
    private String externalTables;
    private LivySessionKindEnum sessionKind;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getSourceQuery() {
        return sourceQuery;
    }

    public void setSourceQuery(String sourceQuery) {
        this.sourceQuery = sourceQuery;
    }

    public List<Long> getUdfIds() {
        return udfIds;
    }

    public void setUdfIds(List<Long> udfIds) {
        this.udfIds = udfIds;
    }

    public String getExternalTables() {
        return externalTables;
    }

    public void setExternalTables(String externalTables) {
        this.externalTables = externalTables;
    }

    public LivySessionKindEnum getSessionKind() {
        return sessionKind;
    }

    public void setSessionKind(LivySessionKindEnum sessionKind) {
        this.sessionKind = sessionKind;
    }
}
