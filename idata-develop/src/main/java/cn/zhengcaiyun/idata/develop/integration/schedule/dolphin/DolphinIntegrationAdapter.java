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

package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.commons.rpc.HttpUtil;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-24 15:06
 **/
public abstract class DolphinIntegrationAdapter {

    public static final String ENTITY_TYPE_WORKFLOW = "workflow";
    public static final String ENTITY_TYPE_TASK = "task";
    public static final String NAME_DELIMITER = "__";

    protected final DSEntityMappingRepo dsEntityMappingRepo;
    protected final DSDependenceNodeRepo dsDependenceNodeRepo;

    public DolphinIntegrationAdapter(DSEntityMappingRepo dsEntityMappingRepo, DSDependenceNodeRepo dsDependenceNodeRepo) {
        this.dsEntityMappingRepo = dsEntityMappingRepo;
        this.dsDependenceNodeRepo = dsDependenceNodeRepo;
    }

    protected Long getWorkflowCode(Long dagId, String environment) {
        Optional<Long> optional = dsEntityMappingRepo.queryDsEntityCode(dagId, ENTITY_TYPE_WORKFLOW, environment);
        if (optional.isEmpty()) throw new ExternalIntegrationException("DS工作流code不存在");
        return optional.get();
    }

    protected Long getTaskCode(Long jobId, String environment) {
        Optional<Long> optional = dsEntityMappingRepo.queryDsEntityCode(jobId, ENTITY_TYPE_TASK, environment);
        if (optional.isEmpty()) throw new ExternalIntegrationException("DS任务code不存在");
        return optional.get();
    }

    protected String getDSToken(String environment) {
        return "eee73991b02bbe05e29499ad6871206d";
    }

    protected String getDSProjectCode(String environment) {
        if (EnvEnum.prod.name().equals(environment)) {
            return "3678830244416";
        } else {
            return "3678829258432";
        }
    }

    protected String getDSBaseUrl(String environment) {
//        return "http://10.200.3.49:12345/dolphinscheduler";
        return "http://172.29.108.238:8688/dolphinscheduler";
    }

    protected String getDSTenantCode(String environment) {
        return "root";
    }

    protected String getDSWorkGroup(String environment) {
        return "stage";
    }

    protected Integer getDagTimeout(String environment) {
        return 120;
    }

    protected ResultDto<JSONObject> sendReq(HttpInput req_input) {
        ResultDto<JSONObject> resultDto = HttpUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<JSONObject>>() {
        });
        return resultDto;
    }

    protected ResultDto<Object> simpleSendReq(HttpInput req_input) {
        ResultDto<Object> resultDto = HttpUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<Object>>() {
        });
        return resultDto;
    }

    protected HttpInput buildHttpReq(Map<String, String> req_param, String req_url, String req_method, String token) {
        HttpInput input = new HttpInput();
        input.setServerName("[Dolphin Scheduler]");
        input.setUri(req_url);
        input.setMethod(req_method);
        input.setQueryParamMap(req_param);
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("token", token);
        input.setHeaderMap(headMap);
        return input;
    }

}
