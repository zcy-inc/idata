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

import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IDagIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.WorkflowDefinitionLog;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.WorkflowInstanceDto;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:40
 **/
@Component
public class WorkflowIntegrator extends DolphinIntegrationAdapter implements IDagIntegrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowIntegrator.class);

    public static final String PARAM_DEFAULT_WARNING_TYPE = "NONE";
    public static final Integer PARAM_DEFAULT_NOTIFY_GROUP_ID = 1;
    public static final String PARAM_DEFAULT_FAILURE_POLICY = "CONTINUE";
    public static final String PARAM_DEFAULT_PROCESS_INSTANCE_PRIORITY = "MEDIUM";

    public static final int RESULT_PROCESS_DEFINITION_NAME_EXIST = 10168;

    @Autowired
    public WorkflowIntegrator(DSEntityMappingRepo dsEntityMappingRepo, DSDependenceNodeRepo dsDependenceNodeRepo, SystemConfigService systemConfigService) {
        super(dsEntityMappingRepo, dsDependenceNodeRepo, systemConfigService);
    }

    @Override
    public void create(DAGInfo dagInfo, DAGSchedule dagSchedule) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/%s/process-definition/empty", projectCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());
        // 创建空工作流和定时器，状态为下线
        Map<String, String> req_param = buildDagReqParam(dagInfo, dagSchedule);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            if (resultDto.isStatus(RESULT_PROCESS_DEFINITION_NAME_EXIST)) {
                // 存在同名工作流，两种方案：1、手动删除DS同名工作流；2、将同名工作流和DAG关联，可能会将无关工作流关联，本期暂用第一种方案实现
                throw new ExternalIntegrationException(String.format("DS存在同名工作流：%s", req_param.get("name")));
            } else {
                throw new ExternalIntegrationException(String.format("创建DS工作流失败：%s", resultDto.getMsg()));
            }
        }

        JSONObject workflow = resultDto.getData();
        Long workflowCode = workflow.getLong("code");
        //保存DAG和workflow关系
        DSEntityMapping mapping = new DSEntityMapping();
        mapping.setEntityId(dagInfo.getId());
        mapping.setEnvironment(dagInfo.getEnvironment());
        mapping.setDsEntityType(ENTITY_TYPE_WORKFLOW);
        mapping.setDsEntityCode(workflowCode);
        dsEntityMappingRepo.create(mapping);
    }

    @Override
    public void update(DAGInfo dagInfo, DAGSchedule dagSchedule) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/%s/process-definition/%s/basic-info", projectCode, workflowCode);
        String req_method = "PUT";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagReqParam(dagInfo, dagSchedule);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("更新DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void delete(Long dagId, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-definition/%s", projectCode, workflowCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        HttpInput req_input = buildHttpReq(null, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void online(DAGInfo dagInfo) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/%s/process-definition/%s/release-workflow", projectCode, workflowCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagStatusChangedParam(dagInfo.getStatus());
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("上线DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void offline(DAGInfo dagInfo) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/%s/process-definition/%s/release-workflow", projectCode, workflowCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagStatusChangedParam(dagInfo.getStatus());
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("下线DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void updateSchedule(DAGInfo dagInfo, DAGSchedule dagSchedule) {
        // 暂不单独实现schedule的更新，合并到update方法中
    }

    @Override
    public void run(DAGInfo dagInfo) {
        // 暂不实现
    }

    @Override
    public List<Integer> cleanHistory(Long dagId, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);

        List<WorkflowDefinitionLog> definitionLogs = getWorkflowDefinitionLogs(environment, projectCode, workflowCode);
        List<Integer> versions = definitionLogs.stream()
                .map(WorkflowDefinitionLog::getVersion)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(20)
                .collect(Collectors.toList());
        return deleteWorkflowVersions(environment, projectCode, workflowCode, versions);
    }

    private List<WorkflowDefinitionLog> getWorkflowDefinitionLogs(String environment, String projectCode, Long workflowCode) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-definition/%s/versions", projectCode, workflowCode);
        String req_method = "GET";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildQueryDagVersionParam(1, 10000);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("查询DS工作流版本失败：%s", resultDto.getMsg()));
        }

        JSONObject data = resultDto.getData();
        JSONArray totalList = data.getJSONArray("totalList");
        if (totalList.size() == 0) {
            return Lists.newArrayList();
        }

        List<WorkflowDefinitionLog> definitionLogs = JSONObject.parseObject(totalList.toJSONString(), new TypeReference<>() {
        });
        return definitionLogs;
    }

    private List<Integer> deleteWorkflowVersions(String environment, String projectCode, Long workflowCode, List<Integer> versions) {
        if (CollectionUtils.isEmpty(versions)) {
            return Lists.newArrayList();
        }

        List<Integer> deleteVersions = Lists.newArrayList();
        for (Integer version : versions) {
            try {
                boolean suc = deleteWorkflowVersion(environment, projectCode, workflowCode, version);
                if (suc) {
                    deleteVersions.add(version);
                }
            } catch (Exception ex) {
                LOGGER.warn("删除DS工作流版本{}失败，异常信息：{}", version, Throwables.getStackTraceAsString(ex));
            }
        }
        return deleteVersions;
    }

    private boolean deleteWorkflowVersion(String environment, String projectCode, Long workflowCode, Integer version) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-definition/%s/versions/%s", projectCode, workflowCode, version);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        Map<String, String> req_param = Maps.newHashMap();
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS工作流版本%d失败：%s", version, resultDto.getMsg()));
        }
        return Boolean.TRUE;
    }

    @Override
    public List<Integer> cleanExecutionHistory(String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);

        LocalDateTime now = LocalDateTime.now();
        Date startTimeBegin = Date.from(now.minusDays(100).atZone(ZoneId.systemDefault()).toInstant());
        Date startTimeEnd = Date.from(now.minusDays(20).atZone(ZoneId.systemDefault()).toInstant());
        List<WorkflowInstanceDto> instanceDtoList = getWorkflowInstances(environment, projectCode, startTimeBegin, startTimeEnd,
                1, 2000);

        List<Integer> deletedIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instanceDtoList)) {
            for (WorkflowInstanceDto instanceDto : instanceDtoList) {
                Boolean suc = deleteWorkflowInstance(environment, projectCode, instanceDto.getId());
                if (BooleanUtils.isTrue(suc)) {
                    deletedIds.add(instanceDto.getId());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException iex) {

                }
            }
        }
        return deletedIds;
    }

    private List<WorkflowInstanceDto> getWorkflowInstances(String environment, String projectCode, Date startTimeBegin, Date startTimeEnd,
                                                           Integer pageNo, Integer pageSize) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-instances", projectCode);
        String req_method = "GET";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildQueryWorkflowInstanceParam(startTimeBegin, startTimeEnd, pageNo, pageSize);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("查询DS工作流运行实例失败：%s", resultDto.getMsg()));
        }

        JSONObject data = resultDto.getData();
        JSONArray totalList = data.getJSONArray("totalList");
        if (totalList.size() == 0) {
            return Lists.newArrayList();
        }
        return JSONObject.parseArray(totalList.toJSONString(), WorkflowInstanceDto.class);
    }

    private Boolean deleteWorkflowInstance(String environment, String projectCode, Integer instanceId) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-instances/%s", projectCode, instanceId.toString());
        String req_method = "DELETE";
        String token = getDSToken(environment);

        Map<String, String> req_param = new HashMap<>();
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            if (Objects.equals(resultDto.getCode(), 50001)) {
                //50001, "process instance id does not exist"
                return Boolean.TRUE;
            }
            throw new ExternalIntegrationException(String.format("删除DS工作流运行实例%s失败：%s", instanceId.toString(), resultDto.getMsg()));
        }
        return Boolean.TRUE;
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) {
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) {
    }

    private Map<String, String> buildDagReqParam(DAGInfo dagInfo, DAGSchedule dagSchedule) {
        String name = buildWorkflowName(dagInfo);
        Integer timeout = getDagTimeout(dagInfo.getEnvironment());
        String tenantCode = getDSTenantCode(dagInfo.getEnvironment());
        List<String> globalParams = Lists.newArrayList();
        String description = "";
        String scheduleJson = buildScheduleJson(dagSchedule, dagInfo.getEnvironment());

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("name", name);
        paramMap.put("timeout", timeout.toString());
        paramMap.put("tenantCode", tenantCode);
        paramMap.put("globalParams", new Gson().toJson(globalParams));
        paramMap.put("description", description);
        paramMap.put("scheduleJson", scheduleJson);
        return paramMap;
    }

    private String buildScheduleJson(DAGSchedule dagSchedule, String environment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObject scheduleJson = new JsonObject();
        scheduleJson.addProperty("warningType", PARAM_DEFAULT_WARNING_TYPE);
        scheduleJson.addProperty("warningGroupId", PARAM_DEFAULT_NOTIFY_GROUP_ID);
        scheduleJson.addProperty("failureStrategy", PARAM_DEFAULT_FAILURE_POLICY);
        scheduleJson.addProperty("workerGroup", getDSWorkGroup(environment));
        scheduleJson.addProperty("environmentCode", -1L);
        scheduleJson.addProperty("processInstancePriority", PARAM_DEFAULT_PROCESS_INSTANCE_PRIORITY);
        scheduleJson.addProperty("startTime", dateFormat.format(dagSchedule.getBeginTime()));
        scheduleJson.addProperty("endTime", dateFormat.format(dagSchedule.getEndTime()));
        scheduleJson.addProperty("crontab", dagSchedule.getCronExpression());
        scheduleJson.addProperty("timezoneId", "Asia/Shanghai");
        return scheduleJson.toString();
    }

    private Map<String, String> buildDagStatusChangedParam(Integer status) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("releaseState", UsingStatusEnum.ONLINE.val == status ? "ONLINE" : "OFFLINE");
        return paramMap;
    }

    private String buildWorkflowName(DAGInfo dagInfo) {
        return dagInfo.getName() + NAME_DELIMITER + Strings.padStart(dagInfo.getId().toString(), 6, '0');
    }

    private Map<String, String> buildQueryDagVersionParam(Integer pageNo, Integer pageSize) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("pageNo", pageNo.toString());
        paramMap.put("pageSize", pageSize.toString());
        return paramMap;
    }

    private Map<String, String> buildQueryWorkflowInstanceParam(Date startTimeBegin, Date startTimeEnd, Integer pageNo, Integer pageSize) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("pageNo", pageNo.toString());
        paramMap.put("pageSize", pageSize.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!Objects.isNull(startTimeBegin)) {
            paramMap.put("startDate", dateFormat.format(startTimeBegin));
        }
        if (!Objects.isNull(startTimeEnd)) {
            paramMap.put("endDate", dateFormat.format(startTimeEnd));
        }

        return paramMap;
    }

}
