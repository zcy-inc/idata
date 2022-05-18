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
package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySessionLogDto;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySqlResultDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-11-22 上午10:58
 */

public class QueryRunResultDto extends LivySqlResultDto {
    private LivySessionLogDto queryRunLog;
    private List<String> resultHeader;
    private List<List<Object>> result;

    // GaS
    public LivySessionLogDto getQueryRunLog() {
        return queryRunLog;
    }

    public void setQueryRunLog(LivySessionLogDto queryRunLog) {
        this.queryRunLog = queryRunLog;
    }

    public List<String> getResultHeader() {
        return resultHeader;
    }

    public void setResultHeader(List<String> resultHeader) {
        this.resultHeader = resultHeader;
    }

    public List<List<Object>> getResult() {
        return result;
    }

    public void setResult(List<List<Object>> result) {
        this.result = result;
    }
}
