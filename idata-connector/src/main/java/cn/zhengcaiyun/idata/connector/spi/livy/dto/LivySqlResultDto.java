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

import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivyOutputStatusEnum;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivyStatementStateEnum;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-13 15:03
 **/
public class LivySqlResultDto {
    private Integer sessionId;
    private Integer statementId;
    private LivyStatementStateEnum statementState;
    private LivyOutputStatusEnum outputStatus;
    private List<Map<String, Object>> resultSet;
    private String pythonResults;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getStatementId() {
        return statementId;
    }

    public void setStatementId(Integer statementId) {
        this.statementId = statementId;
    }

    public LivyStatementStateEnum getStatementState() {
        return statementState;
    }

    public void setStatementState(LivyStatementStateEnum statementState) {
        this.statementState = statementState;
    }

    public LivyOutputStatusEnum getOutputStatus() {
        return outputStatus;
    }

    public void setOutputStatus(LivyOutputStatusEnum outputStatus) {
        this.outputStatus = outputStatus;
    }

    public List<Map<String, Object>> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<Map<String, Object>> resultSet) {
        this.resultSet = resultSet;
    }

    public String getPythonResults() {
        return pythonResults;
    }

    public void setPythonResults(String pythonResults) {
        this.pythonResults = pythonResults;
    }
}
