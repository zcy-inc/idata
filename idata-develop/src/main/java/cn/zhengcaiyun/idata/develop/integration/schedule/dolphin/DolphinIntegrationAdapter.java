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
import cn.zhengcaiyun.idata.core.http.HttpClientUtil;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.system.dto.ConfigValueDto;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-24 15:06
 **/
public abstract class DolphinIntegrationAdapter {

    public static final String ENTITY_TYPE_WORKFLOW = "workflow";
    public static final String ENTITY_TYPE_TASK = "task";
    public static final String NAME_DELIMITER = "__";

    // 调用DS接口锁
    private static final Object dsLock = new Object();

    protected final DSEntityMappingRepo dsEntityMappingRepo;
    protected final DSDependenceNodeRepo dsDependenceNodeRepo;
    protected final SystemConfigService systemConfigService;

    public DolphinIntegrationAdapter(DSEntityMappingRepo dsEntityMappingRepo, DSDependenceNodeRepo dsDependenceNodeRepo, SystemConfigService systemConfigService) {
        this.dsEntityMappingRepo = dsEntityMappingRepo;
        this.dsDependenceNodeRepo = dsDependenceNodeRepo;
        this.systemConfigService = systemConfigService;
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
        Optional<ConfigValueDto> valueDtoOptional = getSystemConfig("ds-config", "token");
        checkState(valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()), "调度系统token未配置，请在系统配置中配置");
        return valueDtoOptional.get().getConfigValue();
    }

    protected String getDSProjectCode(String environment) {
        Optional<ConfigValueDto> valueDtoOptional;
        if (EnvEnum.prod.name().equals(environment)) {
            valueDtoOptional = getSystemConfig("ds-config", "prodDSProjectCode");
        } else {
            valueDtoOptional = getSystemConfig("ds-config", "stagDSProjectCode");
        }
        checkState(valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()), "调度系统%s环境对应DS项目code未配置，请在系统配置中配置", environment);
        return valueDtoOptional.get().getConfigValue();
    }

    protected String getDSBaseUrl(String environment) {
        Optional<ConfigValueDto> valueDtoOptional = getSystemConfig("ds-config", "url");
        checkState(valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()), "调度系统地址未配置，请在系统配置中配置");
        return valueDtoOptional.get().getConfigValue();
    }

    protected String getDSTenantCode(String environment) {
        Optional<ConfigValueDto> valueDtoOptional = getSystemConfig("ds-config", "DStenantCode");
        checkState(valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()), "调度系统租户未配置，请在系统配置中配置");
        return valueDtoOptional.get().getConfigValue();
    }

    protected String getDSWorkGroup(String environment) {
        Optional<ConfigValueDto> valueDtoOptional = getSystemConfig("ds-config", "DSWorkGroup");
        checkState(valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()), "调度系统工作组未配置，请在系统配置中配置");
        return valueDtoOptional.get().getConfigValue();
    }

    protected Integer getDagTimeout(String environment) {
        Optional<ConfigValueDto> valueDtoOptional = getSystemConfig("ds-config", "dagTimeout");
        if (valueDtoOptional.isPresent() && StringUtils.isNotBlank(valueDtoOptional.get().getConfigValue()))
            return Integer.parseInt(valueDtoOptional.get().getConfigValue());
        else return 120;
    }

    protected Optional<ConfigValueDto> getSystemConfig(String configKey, String subKey) {
        ConfigDto configDto = systemConfigService.getSystemConfigByKey(configKey);
        if (Objects.isNull(configDto) || CollectionUtils.isEmpty(configDto.getValueOne())) return Optional.empty();

        return Optional.ofNullable(configDto.getValueOne().get(subKey));
    }

    protected ResultDto<JSONObject> sendReq(HttpInput req_input) {
        ResultDto<JSONObject> resultDto;
        synchronized (dsLock) {
            resultDto = HttpClientUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<JSONObject>>() {
            });
        }
        return resultDto;
    }

    protected ResultDto<Object> simpleSendReq(HttpInput req_input) {
        ResultDto<Object> resultDto;
        synchronized (dsLock) {
            resultDto = HttpClientUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<Object>>() {
            });
        }
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
