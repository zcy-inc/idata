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

package cn.zhengcaiyun.idata.develop.service.opt.stream.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.StreamJobInstanceStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobTable;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobFlinkInfo;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobTableRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobFlinkInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobInstanceRepo;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobInstanceDto;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobRunParamDto;
import cn.zhengcaiyun.idata.develop.manager.FlinkJobManager;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import cn.zhengcaiyun.idata.develop.service.opt.stream.StreamJobInstanceService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 17:31
 **/
@Service
public class StreamJobInstanceServiceImpl implements StreamJobInstanceService {

    private final StreamJobInstanceRepo streamJobInstanceRepo;
    private final StreamJobFlinkInfoRepo streamJobFlinkInfoRepo;
    private final DIStreamJobContentRepo diStreamJobContentRepo;
    private final DIStreamJobTableRepo diStreamJobTableRepo;
    private final JobContentCommonService jobContentCommonService;
    private final JobScheduleManager jobScheduleManager;
    private final JobPublishManager jobPublishManager;
    private final FlinkJobManager flinkJobManager;

    @Autowired
    public StreamJobInstanceServiceImpl(StreamJobInstanceRepo streamJobInstanceRepo,
                                        StreamJobFlinkInfoRepo streamJobFlinkInfoRepo,
                                        DIStreamJobContentRepo diStreamJobContentRepo,
                                        DIStreamJobTableRepo diStreamJobTableRepo,
                                        JobContentCommonService jobContentCommonService,
                                        JobScheduleManager jobScheduleManager,
                                        JobPublishManager jobPublishManager,
                                        FlinkJobManager flinkJobManager) {
        this.streamJobInstanceRepo = streamJobInstanceRepo;
        this.streamJobFlinkInfoRepo = streamJobFlinkInfoRepo;
        this.diStreamJobContentRepo = diStreamJobContentRepo;
        this.diStreamJobTableRepo = diStreamJobTableRepo;
        this.jobContentCommonService = jobContentCommonService;
        this.jobScheduleManager = jobScheduleManager;
        this.jobPublishManager = jobPublishManager;
        this.flinkJobManager = flinkJobManager;
    }

    @Override
    public Page<StreamJobInstanceDto> paging(StreamJobInstanceCondition condition) {
        Page<StreamJobInstance> page = streamJobInstanceRepo.paging(condition);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }

        Map<Long, Map<Integer, String>> jobVersionMap = getJobVersionDisplay(page.getContent());
        List<StreamJobInstanceDto> dtoList = page.getContent().stream()
                .map(record -> {
                    StreamJobInstanceDto dto = StreamJobInstanceDto.from(record);
                    Map<Integer, String> versionMap = jobVersionMap.get(record.getJobId());
                    String versionDisplay = null;
                    if (!CollectionUtils.isEmpty(versionMap)) {
                        versionDisplay = versionMap.get(record.getJobContentVersion());
                    }
                    dto.setJobContentVersionDisplay(Strings.nullToEmpty(versionDisplay));
                    return dto;
                }).collect(Collectors.toList());
        return Page.newOne(dtoList, page.getTotal());
    }

    /**
     * 勿加事务，作业运行参数必须commit之后，htool获取jobDetail时才能获取运行参数
     *
     * @param id
     * @param runParamDto
     * @param operator
     * @return
     */
    @Override
    public Boolean start(Long id, StreamJobRunParamDto runParamDto, Operator operator) {
        Optional<StreamJobInstance> instanceOptional = streamJobInstanceRepo.query(id);
        checkArgument(instanceOptional.isPresent(), "编号：%s 的实例不存在", id);
        StreamJobInstance jobInstance = instanceOptional.get();
        checkArgument(StreamJobInstanceStatusEnum.WAIT_START.val == jobInstance.getStatus(), "运行实例状态不是待启动状态，不能启动");

        Long jobId = jobInstance.getJobId();
        Integer version = jobInstance.getJobContentVersion();
        String env = jobInstance.getEnvironment();
        // 当前版本是否发布版本
        checkState(jobPublishManager.isJobPublished(jobId, version, env), "当前所选版本不是发布状态，不能启动");
        List<Integer> queryStatusList = StreamJobInstanceStatusEnum.getStartToStopStatusValList();
        List<StreamJobInstance> afterStartInstances = streamJobInstanceRepo.queryList(jobId, env, queryStatusList);
        if (!CollectionUtils.isEmpty(afterStartInstances)) {
            Set<Long> afterStartInstanceIds = afterStartInstances.stream()
                    .map(StreamJobInstance::getId)
                    .filter(tempId -> !tempId.equals(id))
                    .collect(Collectors.toSet());
            checkState(afterStartInstanceIds.size() > 0, "作业存在已启动或已停止实例，先下线该实例再启动");
        }

        List<ClusterAppDto> existAppDtoList = flinkJobManager.fetchFlinkApp(jobId, env);
        boolean notExistApp = CollectionUtils.isEmpty(existAppDtoList);
        checkArgument(notExistApp, "Yarn已有同名作业 %s 在运行，应用id：%s 不能启动",
                notExistApp ? "" : existAppDtoList.get(0).getAppName(), notExistApp ? "" : existAppDtoList.get(0).getAppId());

        // 先保存参数
        if (Objects.nonNull(runParamDto)) {
            streamJobInstanceRepo.updateRunParam(id, new Gson().toJson(runParamDto), operator.getNickname());
        }
        // 更新作业状态，若状态更新后，启动失败，可以先操作停止作业，再重新启动
        streamJobInstanceRepo.updateStatus(Lists.newArrayList(id), StreamJobInstanceStatusEnum.STARTING, operator.getNickname());
        // 启动作业
        jobScheduleManager.runJob(jobId, env, false);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean stop(Long id, Operator operator) {
        Optional<StreamJobInstance> instanceOptional = streamJobInstanceRepo.query(id);
        checkArgument(instanceOptional.isPresent(), "编号：%s 的实例不存在", id);
        StreamJobInstance jobInstance = instanceOptional.get();
        Long jobId = jobInstance.getJobId();
        String env = jobInstance.getEnvironment();
        Integer status = jobInstance.getStatus();
        checkArgument(StreamJobInstanceStatusEnum.STOPPED.val != status, "作业已停止");
        checkArgument(StreamJobInstanceStatusEnum.WAIT_START.val != status
                        && StreamJobInstanceStatusEnum.DESTROYED.val != status,
                "待启动或已下线实例不需要停止");
        streamJobInstanceRepo.updateStatus(Lists.newArrayList(id), StreamJobInstanceStatusEnum.STOPPED, operator.getNickname());
        flinkJobManager.stopFlinkApp(jobId, env);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean destroy(Long id, Operator operator) {
        Optional<StreamJobInstance> instanceOptional = streamJobInstanceRepo.query(id);
        checkArgument(instanceOptional.isPresent(), "编号：%s 的实例不存在", id);
        StreamJobInstance jobInstance = instanceOptional.get();
        Integer status = jobInstance.getStatus();
        checkArgument(StreamJobInstanceStatusEnum.DESTROYED.val != status, "作业已下线");
        checkArgument(StreamJobInstanceStatusEnum.WAIT_START.val == status
                        || StreamJobInstanceStatusEnum.STOPPED.val == status,
                "待启动或已停止实例可以操作下线");
        streamJobInstanceRepo.updateStatus(Lists.newArrayList(id), StreamJobInstanceStatusEnum.DESTROYED, operator.getNickname());
        return Boolean.TRUE;
    }

    @Override
    public List<String> getForceInitTable(Long id) {
        Optional<StreamJobInstance> instanceOptional = streamJobInstanceRepo.query(id);
        checkArgument(instanceOptional.isPresent(), "编号：%s 的实例不存在", id);
        StreamJobInstance jobInstance = instanceOptional.get();
        if (!JobTypeEnum.DI_STREAM.getCode().equals(jobInstance.getJobTypeCode())) {
            return Lists.newArrayList();
        }
        List<DIStreamJobTable> jobTableList = diStreamJobTableRepo.query(jobInstance.getJobId(), jobInstance.getJobContentVersion());
        if (CollectionUtils.isEmpty(jobTableList)) {
            return Lists.newArrayList();
        }

        List<StreamJobFlinkInfo> flinkInfoList = streamJobFlinkInfoRepo.queryList(jobInstance.getJobId(), jobInstance.getEnvironment(), null);
        if (CollectionUtils.isEmpty(flinkInfoList)) {
            return Lists.newArrayList();
        }
        Set<String> checkPointTables = flinkInfoList.stream()
                .map(StreamJobFlinkInfo::getSecondaryId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        return jobTableList.stream()
                .filter(jobTable -> checkPointTables.contains(jobTable.getDestTable()))
                .map(DIStreamJobTable::getSrcTable)
                .collect(Collectors.toList());
    }

    private Map<Long, Map<Integer, String>> getJobVersionDisplay(List<StreamJobInstance> recordList) {
        Map<Long, String> jobIdAndTypeMap = Maps.newHashMap();
        for (StreamJobInstance jobInstance : recordList) {
            jobIdAndTypeMap.put(jobInstance.getJobId(), jobInstance.getJobTypeCode());
        }

        Map<Long, Map<Integer, String>> jobVersionMap = Maps.newHashMap();
        for (Map.Entry<Long, String> entry : jobIdAndTypeMap.entrySet()) {
            Map<Integer, String> versionMap = jobContentCommonService.getJobContentVersion(entry.getKey(), entry.getValue());
            jobVersionMap.put(entry.getKey(), versionMap);
        }
        return jobVersionMap;
    }
}
