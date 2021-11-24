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

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.develop.constant.enums.JobPriorityEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:41
 **/
@Component
public class TaskIntegrator extends DolphinIntegrationAdapter implements IJobIntegrator {

    @Autowired
    public TaskIntegrator(DSEntityMappingRepo dsEntityMappingRepo) {
        super(dsEntityMappingRepo);
    }

    @Override
    public void create(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("创建DS任务失败：s%", resultDto.getMsg()));
        }

        JSONObject result = resultDto.getData();
        Long taskCode = result.getLong("code");
        //保存job和ds task关系
        DSEntityMapping mapping = new DSEntityMapping();
        mapping.setEntityId(jobInfo.getId());
        mapping.setEnvironment(environment);
        mapping.setDsEntityType(ENTITY_TYPE_TASK);
        mapping.setDsEntityCode(taskCode);
        dsEntityMappingRepo.create(new DSEntityMapping());
    }

    private Map<String, String> buildJobReqParam(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildTaskJson(jobInfo, executeConfig);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJson", taskJson);
        return paramMap;
    }

    private String buildTaskJson(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = JSONObject.parseObject("{\"code\":null,\"name\":\"task-template-1\",\"description\":\"It is a task\",\"taskType\":\"SHELL\"," +
                "\"taskParams\":{\"resourceList\":[],\"localParams\":[],\"rawScript\":\"run_etl\",\"dependence\":{},\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{},\"switchResult\":{}}," +
                "\"flag\":\"NO\",\"taskPriority\":\"MEDIUM\",\"workerGroup\":\"default\",\"failRetryTimes\":\"0\",\"failRetryInterval\":\"1\"," +
                "\"timeoutFlag\":\"OPEN\",\"timeoutNotifyStrategy\":\"WARN,FAILED\",\"timeout\":30,\"delayTime\":\"0\",\"environmentCode\":-1}");
        taskJson.put("name", buildJobName(jobInfo));
        taskJson.getJSONObject("taskParams").put("rawScript", String.format("run_etl jobId=%d env=%s", jobInfo.getId(), executeConfig.getEnvironment()));
        taskJson.put("flag", "NO");
        taskJson.put("taskPriority", getJobPriority(executeConfig));
        taskJson.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        taskJson.put("timeoutFlag", "OPEN");
        taskJson.put("timeout", executeConfig.getSchTimeOut());
        taskJson.put("timeoutNotifyStrategy", getJobTimeoutStrategy(executeConfig));

        JSONArray taskJsonArray = new JSONArray();
        taskJsonArray.add(taskJson);
        return taskJsonArray.toJSONString();
    }

    private String getJobTimeoutStrategy(JobExecuteConfig executeConfig) {
        // 超时策略，alarm：超时告警，fail：超时失败，都有时用,号分隔
        String timeOutStrategy = executeConfig.getSchTimeOutStrategy();
        if (StringUtils.isEmpty(timeOutStrategy)) return null;

        timeOutStrategy = timeOutStrategy.replace("alarm", "WARN");
        timeOutStrategy = timeOutStrategy.replace("fail", "FAILED");
        return timeOutStrategy;
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

    @Override
    public void update(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%", projectCode, taskCode);
        String req_method = "PUT";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("更新DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void delete(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%", projectCode, taskCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        HttpInput req_input = buildHttpReq(null, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void enableRunning(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("上线DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void disableRunning(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.pause.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("下线DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void publish(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("发布DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void bindDag(JobInfo jobInfo, Long dagId, String environment) {

    }

    @Override
    public void unBindDag(JobInfo jobInfo, Long dagId, String environment) {

    }

    @Override
    public void run(JobInfo jobInfo, String environment) {

    }

    private Map<String, String> buildJobRunningStatusChangedParam(Integer status) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("releaseState", RunningStateEnum.resume.val == status ? "ONLINE" : "OFFLINE");
        return paramMap;
    }

    private String buildJobName(JobInfo jobInfo) {
        return jobInfo.getName() + NAME_DELIMITER + Strings.padStart(jobInfo.getId().toString(), 6, '0');
    }

}
