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

import cn.hutool.core.date.DateUtil;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.core.http.HttpInput;
import cn.zhengcaiyun.idata.develop.constant.enums.JobPriorityEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependenceNode;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.*;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:41
 **/
@Component
public class TaskIntegrator extends DolphinIntegrationAdapter implements IJobIntegrator {

    @Autowired
    public TaskIntegrator(DSEntityMappingRepo dsEntityMappingRepo, DSDependenceNodeRepo dsDependenceNodeRepo, SystemConfigService systemConfigService) {
        super(dsEntityMappingRepo, dsDependenceNodeRepo, systemConfigService);
    }

    @Override
    public void create(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildCreateJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("创建DS任务失败：%s", resultDto.getMsg()));
        }

        JSONObject result = resultDto.getData();
        Long taskCode = result.getLong("code");
        //保存job和ds task关系
        DSEntityMapping mapping = new DSEntityMapping();
        mapping.setEntityId(jobInfo.getId());
        mapping.setEnvironment(environment);
        mapping.setDsEntityType(ENTITY_TYPE_TASK);
        mapping.setDsEntityCode(taskCode);
        dsEntityMappingRepo.create(mapping);
    }

    @Override
    public void update(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition/%s", projectCode, taskCode);
        String req_method = "PUT";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildUpdateJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("更新DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void delete(JobInfo jobInfo, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition/%s", projectCode, taskCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        HttpInput req_input = buildHttpReq(null, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void enableRunning(JobInfo jobInfo, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition/%s/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("上线DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void disableRunning(JobInfo jobInfo, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition/%s/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.pause.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("下线DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void publish(JobInfo jobInfo, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition/%s/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("发布DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void bindDag(JobInfo jobInfo, Long dagId, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildBindDagParam(workflowCode, taskCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            if (Objects.equals(resultDto.getCode(), 50034)) {
                // 50034: process task relation is already exist
                return;
            }
            throw new ExternalIntegrationException(String.format("关联DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void unBindDag(JobInfo jobInfo, Long dagId, String environment) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation/%s", projectCode, taskCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildUnBindDagParam(workflowCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("解除关联DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void run(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, boolean runPost) {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(executeConfig.getSchDagId(), environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/executors/start-process-instance", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildRunningParam(workflowCode, taskCode, executeConfig, runPost);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("运行DS任务失败：%s", resultDto.getMsg()));
        }
    }

    @Override
    public void buildJobRelation(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, List<DagJobPair> addingPrevRelations, List<DagJobPair> removingPrevRelations) {
        String token = getDSToken(environment);
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(executeConfig.getSchDagId(), environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);

        if (!CollectionUtils.isEmpty(addingPrevRelations)) {
            addPrevRelation(token, projectCode, workflowCode, taskCode, jobInfo, executeConfig, environment, addingPrevRelations);
        }
        if (!CollectionUtils.isEmpty(removingPrevRelations)) {
            removePrevRelation(token, projectCode, workflowCode, taskCode, jobInfo, executeConfig, environment, removingPrevRelations);
        }

    }

    @Override
    public String queryLog(Long jobId, String environment, Integer jobInstanceId, Integer lineNum, Integer skipLineNum) {
        String req_url = getDSBaseUrl(environment) + "/log/detail";
        String req_method = "GET";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildQueryLogParam(jobInstanceId, lineNum, skipLineNum);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("运行DS任务失败：%s", resultDto.getMsg()));
        }
        return resultDto.getData().toString();
    }

    @Override
    public List<JobRunOverviewDto> getJobLatestRecords(String environment, Integer limit) {
        String projectCode = getDSProjectCode(environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-instances?pageSize=%d&pageNo=%d", projectCode, limit, 1);
        String req_method = "GET";
        String token = getDSToken(environment);
        HttpInput req_input = buildHttpReq(Maps.newHashMap(), req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("获取DS任务实例信息失败：%s", resultDto.getMsg()));
        }
        JSONObject data = resultDto.getData();
        JSONArray totalList = data.getJSONArray("totalList");
        List<JobRunOverviewDto> list = JSONObject.parseObject(totalList.toJSONString(), new TypeReference<>() {
        });
        return list;
    }

    @Override
    public List<TaskCountDto> getTaskCountGroupState(String environment, Date startTime, Date endTime) {
        String projectCode = getDSProjectCode(environment);
        String startStr = "";
        String endStr = "";
        if (startTime != null) {
            startStr = DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        if (endTime != null) {
            endStr = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
        }
        String req_url = getDSBaseUrl(environment) + String.format("/projects/analysis/task-state-count?projectCode=%s&startDate=%s&endDate=%s", projectCode, startStr, endStr);

        HttpInput req_input = buildHttpReq(Maps.newHashMap(), req_url, "GET", getDSToken(environment));
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("获取DS任务实例信息失败：%s", resultDto.getMsg()));
        }
        JSONObject data = resultDto.getData();
        JSONArray taskCountDtos = data.getJSONArray("taskCountDtos");
        List<TaskCountDto> list = JSONObject.parseObject(taskCountDtos.toJSONString(), new TypeReference<>() {
        });
        return list;
    }

    @Override
    public PageInfoDto<TaskInstanceDto> pagingJobHistory(Long jobId, String environment, String state, Date startTime, Date endTime,
                                                         Integer pageNo, Integer pageSize) {
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobId, environment);

        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-instances/another?pageSize=%d&pageNo=%d", projectCode, pageSize, pageNo);
        String req_method = "GET";
        String token = getDSToken(environment);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskCode", taskCode.toString());
        if (!StringUtils.isEmpty(state)) {
            paramMap.put("stateType", state);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!Objects.isNull(startTime)) {
            paramMap.put("startDate", dateFormat.format(startTime));
        }
        if (!Objects.isNull(endTime)) {
            paramMap.put("endDate", dateFormat.format(endTime));
        }

        HttpInput req_input = buildHttpReq(paramMap, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("获取DS任务实例信息失败：%s", resultDto.getMsg()));
        }
        JSONObject data = resultDto.getData();
        PageInfoDto<TaskInstanceDto> pageInfoDto = data.toJavaObject(new TypeReference<PageInfoDto<TaskInstanceDto>>() {
        });
        return pageInfoDto;
    }

    private void removePrevRelation(String token, String projectCode, Long workflowCode, Long taskCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, List<DagJobPair> removingPrevRelations) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation/%s/upstream", projectCode, taskCode);
        String req_method = "DELETE";

        List<Long> prevTaskCodes = Lists.newArrayList();
        List<DSDependenceNode> deleteDepNodes = Lists.newArrayList();
        for (DagJobPair dagJobPair : removingPrevRelations) {
            List<Long> prevJobIds = dagJobPair.getJobIds();
            if (dagJobPair.getDagId().equals(executeConfig.getSchDagId())) {
                for (Long prevJobId : prevJobIds) {
                    Long prevTaskCode = getTaskCode(prevJobId, environment);
                    prevTaskCodes.add(prevTaskCode);
                }
            } else {
                for (Long prevJobId : prevJobIds) {
                    Long prevTaskCode = getTaskCode(prevJobId, environment);
                    List<DSDependenceNode> dsDependenceNodes = dsDependenceNodeRepo.queryDependenceNodeInWorkflow(taskCode, workflowCode, prevTaskCode);
                    if (!CollectionUtils.isEmpty(dsDependenceNodes)) {
                        prevTaskCodes.add(dsDependenceNodes.get(0).getDependenceNodeCode());
                        deleteDepNodes.addAll(dsDependenceNodes);
                    }
                }
            }
        }

        Map<String, String> req_param = buildDeleteJobRelationParam(prevTaskCodes);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS任务依赖关系失败：%s", resultDto.getMsg()));
        }

        if (!CollectionUtils.isEmpty(deleteDepNodes)) {
            Set<Long> depNodeCodes = Sets.newHashSet();
            List<Long> ids = Lists.newArrayList();
            for (DSDependenceNode dependenceNode : deleteDepNodes) {
                ids.add(dependenceNode.getId());
                depNodeCodes.add(dependenceNode.getDependenceNodeCode());
            }
            dsDependenceNodeRepo.delete(ids);

            for (Long depNodeCode : depNodeCodes) {
                if (CollectionUtils.isEmpty(dsDependenceNodeRepo.queryByDependenceNode(depNodeCode))) {
                    unBindAndDeleteDepNode(token, projectCode, workflowCode, depNodeCode, environment);
                }
            }
        }
    }

    private void unBindAndDeleteDepNode(String token, String projectCode, Long workflowCode, Long depNodeCode, String environment) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation/%s", projectCode, depNodeCode);
        String req_method = "DELETE";

        Map<String, String> req_param = buildUnBindDagParam(workflowCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<Object> resultDto = simpleSendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("解除依赖节点关联DS工作流失败：%s", resultDto.getMsg()));
        }
    }

    private Map<String, String> buildDeleteJobRelationParam(List<Long> prevTaskCodes) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("preTaskCodes", Joiner.on(",").join(prevTaskCodes));
        return paramMap;
    }

    private void addPrevRelation(String token, String projectCode, Long workflowCode, Long taskCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, List<DagJobPair> addingPrevRelations) {
        for (DagJobPair dagJobPair : addingPrevRelations) {
            if (dagJobPair.getDagId().equals(executeConfig.getSchDagId())) {
                addPrevRelationInSameDAG(token, projectCode, workflowCode, taskCode, jobInfo, executeConfig, environment, dagJobPair);
            } else {
                addPrevRelationInDifferentDAG(token, projectCode, workflowCode, taskCode, jobInfo, executeConfig, environment, dagJobPair);
            }
        }
    }

    private void addPrevRelationInSameDAG(String token, String projectCode, Long workflowCode, Long taskCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, DagJobPair dagJobPair) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation", projectCode);
        String req_method = "POST";

        List<Long> prevJobIds = dagJobPair.getJobIds();
        for (Long prevJobId : prevJobIds) {
            Long prevTaskCode = getTaskCode(prevJobId, environment);
            Map<String, String> req_param = buildJobRelationParam(workflowCode, taskCode, prevTaskCode);
            HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
            ResultDto<Object> resultDto = simpleSendReq(req_input);
            if (!resultDto.isSuccess()) {
                // 50034: process task relation is already exist
                if (!Objects.equals(resultDto.getCode(), 50034)) {
                    throw new ExternalIntegrationException(String.format("创建DS任务依赖关系失败：%s", resultDto.getMsg()));
                }
            }
        }
    }

    private void addPrevRelationInDifferentDAG(String token, String projectCode, Long workflowCode, Long taskCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, DagJobPair dagJobPair) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/process-task-relation", projectCode);
        String req_method = "POST";

        Long prevDagId = dagJobPair.getDagId();
        Long prevWorkflowCode = getWorkflowCode(prevDagId, environment);
        List<Long> prevJobIds = dagJobPair.getJobIds();
        for (Long prevJobId : prevJobIds) {
            Long prevTaskCode = getTaskCode(prevJobId, environment);
            Long dependenceNodeCode;
            // 查询上游依赖任务在当前dag有没有依赖节点
            List<DSDependenceNode> dsDependenceNodes = dsDependenceNodeRepo.queryDependenceNodeInWorkflow(workflowCode, prevTaskCode);
            if (CollectionUtils.isEmpty(dsDependenceNodes)) {
                // 创建依赖节点
                dependenceNodeCode = createDependenceNode(token, projectCode, jobInfo, executeConfig, environment, prevWorkflowCode, prevTaskCode, prevDagId, prevJobId);
            } else {
                dependenceNodeCode = dsDependenceNodes.get(0).getDependenceNodeCode();
            }
            DSDependenceNode node = new DSDependenceNode();
            node.setTaskCode(taskCode);
            node.setWorkflowCode(workflowCode);
            node.setDependenceNodeCode(dependenceNodeCode);
            node.setPrevTaskCode(prevTaskCode);
            node.setPrevWorkflowCode(prevWorkflowCode);
            dsDependenceNodeRepo.create(node);

            // 建立当前任务和依赖节点关系
            Map<String, String> req_param = buildJobRelationParam(workflowCode, taskCode, dependenceNodeCode);
            HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
            ResultDto<Object> resultDto = simpleSendReq(req_input);
            if (!resultDto.isSuccess()) {
                // 50034: process task relation is already exist
                if (!Objects.equals(resultDto.getCode(), 50034)) {
                    throw new ExternalIntegrationException(String.format("创建DS任务跨工作流依赖关系失败：%s", resultDto.getMsg()));
                }
            }
        }
    }

    private Long createDependenceNode(String token, String projectCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment, Long prevWorkflowCode, Long prevTaskCode, Long prevDagId, Long prevJobId) {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/%s/task-definition", projectCode);
        String req_method = "POST";

        Map<String, String> req_param = buildDependenceNodeParam(projectCode, prevWorkflowCode, prevTaskCode, prevDagId, prevJobId, jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("创建DS依赖节点失败：%s", resultDto.getMsg()));
        }

        JSONObject result = resultDto.getData();
        Long depCode = result.getLong("code");
        return depCode;
    }

    private Map<String, String> buildDependenceNodeParam(String projectCode, Long prevWorkflowCode, Long prevTaskCode, Long prevDagId, Long prevJobId, JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildDependenceNodeJson(projectCode, prevWorkflowCode, prevTaskCode, prevDagId, prevJobId, jobInfo, executeConfig);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJson", taskJson);
        return paramMap;
    }

    private String buildDependenceNodeJson(String projectCode, Long prevWorkflowCode, Long prevTaskCode, Long prevDagId, Long prevJobId, JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = JSONObject.parseObject("{\"code\":null,\"name\":\"depend-node-template-1\",\"description\":\"It is a dependent node\",\"delayTime\":0," + "\"taskType\":\"DEPENDENT\",\"taskParams\":{\"dependence\":{\"relation\":\"AND\",\"dependTaskList\":[{\"relation\":\"AND\",\"dependItemList\":[{\"projectCode\":811464712822784,\"definitionCode\":3602075624352,\"depTaskCode\":3602064345376,\"cycle\":\"day\",\"dateValue\":\"today\"}]}]}," + "\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{\"strategy\":\"FAILED\",\"interval\":null,\"checkInterval\":null,\"enable\":false},\"switchResult\":{}}," + "\"flag\":\"YES\",\"taskPriority\":\"MEDIUM\",\"workerGroup\":\"default\",\"failRetryTimes\":0,\"failRetryInterval\":1,\"timeoutFlag\":\"CLOSE\",\"timeoutNotifyStrategy\":\"\",\"timeout\":0,\"environmentCode\":-1}");

        String name = "dep#to__t-" + Strings.padStart(prevJobId.toString(), 6, '0') + "__w-" + Strings.padStart(prevDagId.toString(), 6, '0') + "__f-" + Strings.padStart(jobInfo.getId().toString(), 6, '0');
        taskJson.put("name", name);
        taskJson.put("taskType", "DEPENDENT");
        taskJson.put("flag", "YES");
        taskJson.put("taskPriority", "MEDIUM");
        taskJson.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        taskJson.put("timeoutFlag", "OPEN");
        taskJson.put("timeout", "15");
        taskJson.put("timeoutNotifyStrategy", "FAILED");
        taskJson.put("failRetryTimes", "6");
        taskJson.put("failRetryInterval", "5");

        JSONObject depItemJson = taskJson.getJSONObject("taskParams").getJSONObject("dependence").getJSONArray("dependTaskList").getJSONObject(0).getJSONArray("dependItemList").getJSONObject(0);
        depItemJson.put("projectCode", projectCode);
        depItemJson.put("definitionCode", prevWorkflowCode.toString());
        depItemJson.put("depTaskCode", prevTaskCode.toString());
        depItemJson.put("cycle", "day");
        depItemJson.put("dateValue", "today");

        JSONArray taskJsonArray = new JSONArray();
        taskJsonArray.add(taskJson);
        return taskJsonArray.toJSONString();
    }

    private Map<String, String> buildRunningParam(Long workflowCode, Long taskCode, JobExecuteConfig executeConfig, boolean runPost) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
//        paramMap.put("scheduleTime", null); // scheduleTime: 2022-04-14 00:00:00,2022-04-14 00:00:00
        paramMap.put("failureStrategy", "END");
        paramMap.put("warningType", "NONE");
        paramMap.put("warningGroupId", "0");
//        paramMap.put("execType", null);
        paramMap.put("taskDependType", runPost ? "TASK_POST" : "TASK_ONLY");
        paramMap.put("runMode", "RUN_MODE_SERIAL");
        paramMap.put("processInstancePriority", "HIGHEST");
        paramMap.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
//        paramMap.put("environmentCode", null);
//        paramMap.put("expectedParallelismNumber", null);
//        paramMap.put("timeout", null);
//        paramMap.put("startParams", null);
        paramMap.put("startNodeList", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildJobRelationParam(Long workflowCode, Long taskCode, Long prevTaskCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        paramMap.put("preTaskCode", prevTaskCode.toString());
        paramMap.put("postTaskCode", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildBindDagParam(Long workflowCode, Long taskCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        paramMap.put("preTaskCode", "0");
        paramMap.put("postTaskCode", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildUnBindDagParam(Long workflowCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        return paramMap;
    }

    private Map<String, String> buildJobRunningStatusChangedParam(Integer status) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("releaseState", RunningStateEnum.resume.val == status ? "ONLINE" : "OFFLINE");
        return paramMap;
    }

    private Map<String, String> buildUpdateJobReqParam(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildSingleTaskJson(jobInfo, executeConfig).toJSONString();

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJsonObj", taskJson);
        return paramMap;
    }

    private Map<String, String> buildCreateJobReqParam(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildTaskJson(jobInfo, executeConfig);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJson", taskJson);
        return paramMap;
    }

    private String buildTaskJson(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = buildSingleTaskJson(jobInfo, executeConfig);
        JSONArray taskJsonArray = new JSONArray();
        taskJsonArray.add(taskJson);
        return taskJsonArray.toJSONString();
    }

    private JSONObject buildSingleTaskJson(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = JSONObject.parseObject("{\"code\":null,\"name\":\"task-template-1\",\"description\":\"It is a task\",\"taskType\":\"SHELL\"," + "\"taskParams\":{\"resourceList\":[],\"localParams\":[],\"rawScript\":\"run_etl\",\"dependence\":{},\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{},\"switchResult\":{}}," + "\"flag\":\"NO\",\"taskPriority\":\"MEDIUM\",\"workerGroup\":\"default\",\"failRetryTimes\":\"2\",\"failRetryInterval\":\"5\"," + "\"timeoutFlag\":\"OPEN\",\"timeoutNotifyStrategy\":\"WARN,FAILED\",\"timeout\":30,\"delayTime\":\"0\",\"environmentCode\":-1}");
        taskJson.put("name", buildJobName(jobInfo));
        taskJson.getJSONObject("taskParams").put("rawScript", String.format("run_etl jobId=%d env=%s", jobInfo.getId(), executeConfig.getEnvironment()));
        taskJson.put("flag", "NO");
        taskJson.put("taskPriority", getJobPriority(executeConfig));
        taskJson.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        if (executeConfig.getSchTimeOut() <= 0) {
            taskJson.put("timeoutFlag", "CLOSE");
        } else {
            taskJson.put("timeoutFlag", "OPEN");
            taskJson.put("timeout", executeConfig.getSchTimeOut() / 60);
        }
        taskJson.put("timeoutNotifyStrategy", getJobTimeoutStrategy(executeConfig));
        return taskJson;
    }

    private Map<String, String> buildQueryLogParam(Integer jobInstanceId, Integer lineNum, Integer skipLineNum) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskInstanceId", jobInstanceId.toString());
        paramMap.put("skipLineNum", skipLineNum.toString());
        paramMap.put("limit", lineNum.toString());
        return paramMap;
    }

    private String getJobTimeoutStrategy(JobExecuteConfig executeConfig) {
        // 超时策略，alarm：超时告警，fail：超时失败
        String timeOutStrategy = executeConfig.getSchTimeOutStrategy();
        if ("alarm".equals(timeOutStrategy)) {
            return "WARN";
        } else if ("fail".equals(timeOutStrategy)) {
            return "FAILED";
        } else {
            return null;
        }
    }

    private String getJobPriority(JobExecuteConfig executeConfig) {
        Optional<JobPriorityEnum> priorityEnumOptional = JobPriorityEnum.getEnum(executeConfig.getSchPriority());
        if (priorityEnumOptional.isEmpty()) return "MEDIUM";

        JobPriorityEnum priorityEnum = priorityEnumOptional.get();
        if (JobPriorityEnum.low == priorityEnum) {
            return "LOW";
        } else if (JobPriorityEnum.middle == priorityEnum) {
            return "MEDIUM";
        } else if (JobPriorityEnum.high == priorityEnum) {
            return "HIGH";
        }
        return "MEDIUM";
    }

    private String buildJobName(JobInfo jobInfo) {
        return jobInfo.getName() + NAME_DELIMITER + Strings.padStart(jobInfo.getId().toString(), 6, '0');
    }

}
