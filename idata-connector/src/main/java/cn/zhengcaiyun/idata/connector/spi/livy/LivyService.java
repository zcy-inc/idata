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

package cn.zhengcaiyun.idata.connector.spi.livy;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import cn.zhengcaiyun.idata.core.http.HttpClientUtil;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.*;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivyOutputStatusEnum;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionStateEnum;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivyStatementStateEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-13 14:45
 **/
@Service
public class LivyService {

    private final Map<String, Integer> DEFAULT_MAX_EXECUTORS = new HashMap<String, Integer>() {{
        put("spark.dynamicAllocation.maxExecutors", 20);
    }};
    private final String DEFAULT_DRIVER_MEMORY = "1G";
    private final String DEFAULT_EXECUTOR_MEMORY = "2G";

    @Value("${livy.baseUrl}")
    private String LIVY_BASE_URL;
    @Value("${livy.sessionMax}")
    private Integer LIVY_SESSION_MAX;

    // TODO 暂时不考虑多进程
    public synchronized LivySessionDto getOrCreateSession(LivySessionKindEnum sessionKind) {
        LivySessionDto sessionDto = new LivySessionDto();
        Map<String, Object> sessions = sendToLivy(new HttpInput().setMethod("GET"),
                new TypeReference<Map<String, Object>>() {
                },
                "/sessions");
        List<Map<String, Object>> sessionList = ((List<Map<String, Object>>) sessions.get("sessions"));
        for (Map<String, Object> session : sessionList) {
            if (LivySessionStateEnum.idle.name().equals(session.get("state"))) {
                sessionDto.setSessionId((Integer) session.get("id"));
                return sessionDto;
            }
        }
        if (sessionList.size() >= LIVY_SESSION_MAX) {
            Map<String, Object> session = sessionList.get(new Random().nextInt(sessionList.size()));
            sessionDto.setSessionId((Integer) session.get("id"));
            return sessionDto;
        }
        Map<String, Object> body = new HashMap<>();
        body.put("kind", sessionKind.name());
        body.put("driverMemory", DEFAULT_DRIVER_MEMORY);
        body.put("executorMemory", DEFAULT_EXECUTOR_MEMORY);
        body.put("conf", DEFAULT_MAX_EXECUTORS);
        Map<String, Object> session = sendToLivy(new HttpInput().setMethod("POST").setObjectBody(body),
                new TypeReference<Map<String, Object>>() {
                },
                "/sessions");
        sessionDto.setSessionId((Integer) session.get("id"));
        return sessionDto;
    }

    public void deleteSession(Integer sessionId) {
        if (sessionId == null) return;
        sendToLivy(new HttpInput().setMethod("DELETE"),
                new TypeReference<String>() {
                },
                "/sessions/%d", sessionId);
    }

    public LivyStatementDto createStatement(LivyQueryDto query) {
        if (query.getSessionId() == null) {
            query.setSessionId(getOrCreateSession(query.getSessionKind()).getSessionId());
        }
        waitSessionAvailable(query.getSessionId());

        Map<String, Object> body = new HashMap<>();
        String code = LivySessionKindEnum.spark.equals(query.getSessionKind())
                ? String.format("println(spark.sql(\"\"\"%s\"\"\").toJSON.collect.mkString(\"[\", \",\", \"]\"))", query.getQuerySource())
                : query.getQuerySource();
        body.put("kind", query.getSessionKind().name());
        // TODO 这里也可以执行ddl和dml，后续需要增加日志记录和权限控制
        body.put("code", code);
        Map<String, Object> response = sendToLivy(new HttpInput().setMethod("POST").setObjectBody(body),
                new TypeReference<Map<String, Object>>() {
                },
                "/sessions/%d/statements", query.getSessionId());
        Integer statementId = (Integer) response.get("id");
        LivyStatementDto statementDto = new LivyStatementDto();
        statementDto.setSessionId(query.getSessionId());
        statementDto.setStatementId(statementId);
        return statementDto;
    }

    public void cancelStatement(LivyStatementDto statement) {
        sendToLivy(new HttpInput().setMethod("POST"),
                new TypeReference<String>() {
                },
                "/sessions/%d/statements/%d/cancel", statement.getSessionId(), statement.getStatementId());
    }

    public LivySqlResultDto queryResult(Integer sessionId, Integer statementId, LivySessionKindEnum sessionKind) {
        Map<String, Object> response = sendToLivy(new HttpInput().setMethod("GET"),
                new TypeReference<Map<String, Object>>() {
                },
                "/sessions/%d/statements/%d", sessionId, statementId);
        LivySqlResultDto sqlResult = new LivySqlResultDto();
        sqlResult.setSessionId(sessionId);
        sqlResult.setStatementId((Integer) response.get("id"));
        sqlResult.setStatementState(LivyStatementStateEnum.valueOf((String) response.get("state")));
        if (response.get("output") != null) {
            Map<String, Object> output = (Map) response.get("output");
            LivyOutputStatusEnum outputStatus = LivyOutputStatusEnum.valueOf((String) output.get("status"));
            sqlResult.setOutputStatus(outputStatus);
            if (outputStatus.equals(LivyOutputStatusEnum.ok)) {
                String data = (String) ((Map) output.get("data")).get("text/plain");
                if (LivySessionKindEnum.spark.equals(sessionKind)) {
                    sqlResult.setResultSet(JSON.parseObject(data, new TypeReference<List<Map<String, Object>>>() {
                    }));
                }
                else if(LivySessionKindEnum.pyspark.equals(sessionKind)) {
                    sqlResult.setPythonResults(data);
                }
            } else if (outputStatus.equals(LivyOutputStatusEnum.error)) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put((String) output.get("ename"), output.get("evalue"));
                if (output.get("traceback") != null) {
                    errorResult.put("traceback", String.join("", (List<String>) output.get("traceback")));
                }
                if (LivySessionKindEnum.spark.equals(sessionKind)) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    list.add(errorResult);
                    sqlResult.setResultSet(list);
                }
                else if(LivySessionKindEnum.pyspark.equals(sessionKind)) {
                    StringBuilder errorMsg = new StringBuilder();
                    for (Map.Entry<String, Object> mapEntry : errorResult.entrySet()) {
                        errorMsg.append(mapEntry.getKey()).append(":").append((String) mapEntry.getValue()).append("\n");
                    }
                    sqlResult.setPythonResults(errorMsg.toString());
                }
            }
        }
        return sqlResult;
    }

    public LivySessionLogDto queryLog(Integer sessionId, Integer from, Integer size) {
        Map<String, String> queryParamMap = new HashMap<>();
        if (from != null) {
            queryParamMap.put("from", String.valueOf(from));
        }
        if (size != null) {
            queryParamMap.put("size", String.valueOf(size));
        }
        Map<String, Object> response = sendToLivy(new HttpInput().setMethod("GET").setQueryParamMap(queryParamMap),
                new TypeReference<Map<String, Object>>() {
                },
                "/sessions/%d/log", sessionId);
        LivySessionLogDto sessionLog = new LivySessionLogDto();
        sessionLog.setSessionId(sessionId);
        sessionLog.setFrom((Integer) response.get("from"));
        sessionLog.setTotal((Integer) response.get("total"));
        sessionLog.setLog((List<String>) response.get("log"));
        return sessionLog;
    }

    public void submitSparkCode(String code, LivySessionKindEnum sessionKind) {
        Integer sessionId = null;
        try {
            sessionId = getOrCreateSession(sessionKind).getSessionId();
            waitSessionAvailable(sessionId);
            Map<String, Object> body = new HashMap<>();
            body.put("kind", sessionKind.name());
            body.put("code", code);
            Map<String, Object> response = sendToLivy(new HttpInput().setMethod("POST").setObjectBody(body),
                    new TypeReference<Map<String, Object>>() {
                    },
                    "/sessions/%d/statements", sessionId);
            Integer statementId = (Integer) response.get("id");
            waitStatementAvailable(sessionId, statementId);
        } finally {
            deleteSession(sessionId);
        }
    }

    public LivySessionDto createBatches(String file, List<String> args, String driverMemory, String executorMemory) {
        Map<String, Object> body = new HashMap<>();
        driverMemory = isNotEmpty(driverMemory) ? driverMemory : DEFAULT_DRIVER_MEMORY;
        executorMemory = isNotEmpty(executorMemory) ? executorMemory : DEFAULT_EXECUTOR_MEMORY;
        body.put("file", file);
        body.put("args", args);
        body.put("driverMemory", driverMemory);
        body.put("executorMemory", executorMemory);
        body.put("conf", DEFAULT_MAX_EXECUTORS);

        Map<String, Object> response = sendToLivy(new HttpInput().setMethod("POST").setObjectBody(body),
                new TypeReference<Map<String, Object>>() {
                },
                "/batches");
        LivySessionDto sessionDto = new LivySessionDto();
        sessionDto.setSessionId((Integer) response.get("id"));
        return sessionDto;
    }

    public LivySessionLogDto getBatchesLog(Integer sessionId, Integer from, Integer size) {
        Map<String, String> queryParamMap = new HashMap<>();
        if (from != null) {
            queryParamMap.put("from", String.valueOf(from));
        }
        if (size != null) {
            queryParamMap.put("size", String.valueOf(size));
        }
        Map<String, Object> response = sendToLivy(new HttpInput().setMethod("GET").setQueryParamMap(queryParamMap),
                new TypeReference<Map<String, Object>>() {
                },
                "/batches/%d/log", sessionId);
        List<String> log = new ArrayList<>();
        int total = (Integer) response.get("total");
        if (response.get("log") != null) {
            for (String l : (List<String>) response.get("log")) {
                if (l.contains("stderr: ") || l.contains("YARN Diagnostics: ")) {
                    total -= 1;
                    continue;
                }
                log.add(l);
            }
        }
        LivySessionLogDto sessionLog = new LivySessionLogDto();
        sessionLog.setSessionId(sessionId);
        sessionLog.setFrom((Integer) response.get("from"));
        sessionLog.setTotal(total);
        sessionLog.setLog(log);
        sessionLog.setState("");
        return sessionLog;
    }

    public String getBatchesState(Integer sessionId) {
        Map<String, String> response = sendToLivy(new HttpInput().setMethod("GET"),
                new TypeReference<Map<String, String>>() {
                },
                "/batches/%d/state", sessionId);
        String state = response.get("state");
        return state;
    }

    public void deleteBatches(Integer sessionId) {
        if (sessionId == null) return;
        sendToLivy(new HttpInput().setMethod("DELETE"),
                new TypeReference<String>() {
                },
                "/batches/%d", sessionId);
    }

    private void waitStatementAvailable(int sessionId, int statementId) {
        long now = System.currentTimeMillis();
        while (true) {
            Map<String, Object> response = sendToLivy(new HttpInput().setMethod("GET"),
                    new TypeReference<Map<String, Object>>() {
                    },
                    "/sessions/%d/statements/%d", sessionId, statementId);
            if (LivyStatementStateEnum.available.equals(LivyStatementStateEnum.valueOf((String) response.get("state")))) {
                if (response.get("output") != null) {
                    Map<String, Object> output = (Map) response.get("output");
                    LivyOutputStatusEnum outputStatus = LivyOutputStatusEnum.valueOf((String) output.get("status"));
                    if (outputStatus.equals(LivyOutputStatusEnum.error)) {
                        String message = "evalue: " + output.get("evalue");
                        if (output.get("traceback") != null) {
                            message = message + ", traceback: " + String.join("", (List<String>) output.get("traceback"));
                        }
                        throw new GeneralException(message);
                    }
                }
                break;
            }
            if (System.currentTimeMillis() - now > 100000) {
                throw new GeneralException("statement wait timeout, sessionId: "
                        + sessionId + ", statementId: " + statementId
                        + ", state: " + response.get("state"));
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void waitSessionAvailable(int sessionId) {
        long now = System.currentTimeMillis();
        while (true) {
            Map<String, Object> response = sendToLivy(new HttpInput().setMethod("GET"),
                    new TypeReference<Map<String, Object>>() {
                    },
                    "/sessions/%d/state", sessionId);
            if (LivySessionStateEnum.idle.equals(LivySessionStateEnum.valueOf((String) response.get("state")))
                    || LivySessionStateEnum.busy.equals(LivySessionStateEnum.valueOf((String) response.get("state")))) {
                break;
            }
            if (System.currentTimeMillis() - now > 50000) {
                throw new GeneralException("session wait timeout, sessionId: "
                        + sessionId + ", state: " + response.get("state"));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private <T> T sendToLivy(HttpInput httpInput, TypeReference<T> typeReference, String uri, Object... uriParams) {
        String body = null;
        httpInput.setUri(String.format(LIVY_BASE_URL + uri, uriParams));
        try (Response response = HttpClientUtil.executeHttpRequest(httpInput)) {
            body = response.body().string();
            if ((response.code() == 200 || response.code() == 201)
                    && body != null) {
                if (String.class.equals(typeReference.getType())) {
                    return (T) body;
                }
                return JSON.parseObject(body, typeReference);
            } else {
                throw new GeneralException("[livy] sever return error, " + response.code() + ", " + body);
            }
        } catch (IOException ioe) {
            throw new GeneralException("[livy] sever network error, ", ioe);
        } catch (Exception ex) {
            throw new GeneralException("[livy] return: ", ex);
        }
    }

}
