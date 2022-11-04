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

package cn.zhengcaiyun.idata.develop.service.dag.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.enums.FolderTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGDependence;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGEventLog;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGEventLogRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGScheduleDto;
import cn.zhengcaiyun.idata.develop.event.dag.publisher.DagEventPublisher;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import cn.zhengcaiyun.idata.develop.util.CronExpressionUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 13:42
 **/
@Service
public class DAGServiceImpl implements DAGService {

    private final DAGRepo dagRepo;
    private final DAGEventLogRepo dagEventLogRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;
    private final DagEventPublisher dagEventPublisher;
    private final DevAccessService devAccessService;
    private final CompositeFolderRepo compositeFolderRepo;

    private final JobScheduleManager jobScheduleManager;

    @Autowired
    public DAGServiceImpl(DAGRepo dagRepo,
                          DAGEventLogRepo dagEventLogRepo,
                          JobExecuteConfigRepo jobExecuteConfigRepo,
                          DevTreeNodeLocalCache devTreeNodeLocalCache,
                          DagEventPublisher dagEventPublisher,
                          DevAccessService devAccessService,
                          CompositeFolderRepo compositeFolderRepo,
                          JobScheduleManager jobScheduleManager) {
        this.dagRepo = dagRepo;
        this.dagEventLogRepo = dagEventLogRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
        this.dagEventPublisher = dagEventPublisher;
        this.devAccessService = devAccessService;
        this.compositeFolderRepo = compositeFolderRepo;
        this.jobScheduleManager = jobScheduleManager;
    }

    @Override
    @Transactional
    public Long addDAG(DAGDto dto, Operator operator) throws IllegalAccessException {
        DAGInfoDto dagInfoDto = dto.getDagInfoDto();
        devAccessService.checkAddAccess(operator.getId(), dagInfoDto.getFolderId());

        DAGScheduleDto dagScheduleDto = dto.getDagScheduleDto();
        checkDag(dagInfoDto, dagScheduleDto);

        Optional<CompositeFolder> folderOptional = compositeFolderRepo.queryFolder(dagInfoDto.getFolderId());
        checkArgument(folderOptional.isPresent(), "DAG所属文件夹不存在");
        checkArgument(!FolderTypeEnum.FUNCTION.name().equals(folderOptional.get().getType()), "DAG不能建在模块根目录下");

        List<DAGInfo> dupNameDag = dagRepo.queryDAGInfo(dagInfoDto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameDag), "DAG名称已存在");

        // 默认下线状态，只有包含作业节点的DAG才能上线
        dagInfoDto.setStatus(UsingStatusEnum.OFFLINE.val);
        dagInfoDto.setOperator(operator);
        dagScheduleDto.setOperator(operator);

        DAGInfo info = dagInfoDto.toModel();
        DAGSchedule schedule = dagScheduleDto.toModel();
        Long dagId = dagRepo.saveDAG(info, schedule);

        // 发布dag创建事件
        DAGEventLog eventLog = logDagEvent(dagId, EventTypeEnum.CREATED, operator);
        dagEventPublisher.whenCreated(eventLog);
        // 清理DAG缓存
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DAG);
        return dagId;
    }

    @Override
    @Transactional
    public Boolean editDAG(DAGDto dto, Operator operator) throws IllegalAccessException {
        DAGInfoDto dagInfoDto = dto.getDagInfoDto();
        DAGScheduleDto dagScheduleDto = dto.getDagScheduleDto();
        checkDag(dagInfoDto, dagScheduleDto);
        checkArgument(Objects.nonNull(dagInfoDto.getId()) && Objects.nonNull(dagScheduleDto.getId()), "DAG编号为空");

        DAGInfo oldDagInfo = tryFetchDAGInfo(dagInfoDto.getId());
        devAccessService.checkUpdateAccess(operator.getId(), oldDagInfo.getFolderId(), dagInfoDto.getFolderId());
        // 检查是否已下线，只有下线后才能更改
        checkArgument(Objects.equals(oldDagInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "先下线DAG再更改");
        Optional<DAGSchedule> optionalDAGSchedule = dagRepo.queryDAGSchedule(dagInfoDto.getId());
        checkArgument(optionalDAGSchedule.isPresent(), "DAG不存在或已删除");
        DAGSchedule oldSchedule = optionalDAGSchedule.get();

        List<DAGInfo> dupNameDag = dagRepo.queryDAGInfo(dagInfoDto.getName());
        if (ObjectUtils.isNotEmpty(dupNameDag)) {
            DAGInfo dupNameInfo = dupNameDag.get(0);
            checkArgument(Objects.equals(dagInfoDto.getId(), dupNameInfo.getId()), "DAG名称已存在");
        }

        dagInfoDto.setStatus(null);
        dagInfoDto.resetEditor(operator);
        DAGInfo info = dagInfoDto.toModel();
        dagScheduleDto.setDagId(info.getId());
        dagScheduleDto.resetEditor(operator);
        DAGSchedule schedule = dagScheduleDto.toModel();
        Boolean ret = dagRepo.updateDAG(info, schedule);
        if (ret) {
            // 是否更改基本信息
            if (checkBaseInfoUpdated(info, oldDagInfo) || checkScheduleUpdated(schedule, oldSchedule)) {
                DAGEventLog eventLog = logDagEvent(info.getId(), EventTypeEnum.UPDATED, operator);
                dagEventPublisher.whenUpdated(eventLog);
            }
            // 是够更改定时配置
//            if (checkScheduleUpdated(schedule, oldSchedule)) {
//                DAGEventLog eventLog = logDagEvent(info.getId(), EventTypeEnum.DAG_SCHEDULE_UPDATED, operator);
//                dagEventPublisher.whenScheduleUpdated(eventLog);
//            }
        }

        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DAG);
        return ret;
    }

    @Override
    public DAGDto getDag(Long id) {
        DAGInfo dagInfo = tryFetchDAGInfo(id);
        Optional<DAGSchedule> optionalDAGSchedule = dagRepo.queryDAGSchedule(id);
        checkArgument(optionalDAGSchedule.isPresent(), "DAG不存在或已删除");

        DAGDto dto = new DAGDto();
        dto.setDagInfoDto(DAGInfoDto.from(dagInfo));
        dto.setDagScheduleDto(DAGScheduleDto.from(optionalDAGSchedule.get()));
        dto.setEditable(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.OFFLINE.val));
        dto.setDeletable(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.OFFLINE.val));
        return dto;
    }

    @Override
    @Transactional
    public Boolean removeDag(Long id, Operator operator) throws IllegalAccessException {
        DAGInfo dagInfo = tryFetchDAGInfo(id);
        devAccessService.checkDeleteAccess(operator.getId(), dagInfo.getFolderId());
        // 检查是否已下线，只有下线后才能更改
        checkArgument(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "先下线DAG再删除");

        // 删除时确定是否有作业依赖，有则不能删除
        long jobCount = jobExecuteConfigRepo.countDagJob(id);
        checkState(jobCount == 0, "该DAG存在作业依赖，不能删除");
        Boolean ret = dagRepo.deleteDAG(id, operator.getNickname());
        // 发布dag创建事件
        JsonObject dagJson = new JsonObject();
        dagJson.addProperty("environment", dagInfo.getEnvironment());
        DAGEventLog eventLog = logDagEvent(id, EventTypeEnum.DELETED, dagJson.toString(), operator);
        dagEventPublisher.whenDeleted(eventLog);

        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DAG);
        return ret;
    }

    @Override
    public Boolean cleanDagHistory(Long id, Operator operator) {
        DAGEventLog eventLog = logDagEvent(id, EventTypeEnum.DAG_CLEAN_HISTORY, operator);
        dagEventPublisher.whenToCleanHistory(eventLog);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean online(Long id, Operator operator) {
        DAGInfo dagInfo = tryFetchDAGInfo(id);
        // 检查是否已下线，只有下线后才能上线
        checkArgument(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "DAG已是上线状态");

        // 上线时确定是否有作业存在，空dag不能上线
        long jobCount = jobExecuteConfigRepo.countDagJob(id);
        checkState(jobCount > 0, "DAG不存在作业，不能上线");

        DAGInfo info = new DAGInfo();
        info.setId(id);
        info.setStatus(UsingStatusEnum.ONLINE.val);
        info.setEditor(operator.getNickname());
        dagRepo.updateDAGInfo(info);
        // 发布dag创建事件
        DAGEventLog eventLog = logDagEvent(id, EventTypeEnum.DAG_ONLINE, operator);
        dagEventPublisher.whenToOnline(eventLog);
        return Boolean.TRUE;
    }

    /**
     * DAG下线
     *
     * @param id
     * @param operator
     * @return
     */
    @Override
    @Transactional
    public Boolean offline(Long id, Operator operator) {
        DAGInfo dagInfo = tryFetchDAGInfo(id);
        // 检查是否已下线，只有下线后才能更改
        checkArgument(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.ONLINE.val), "DAG已是下线状态");

        DAGInfo info = new DAGInfo();
        info.setId(id);
        info.setStatus(UsingStatusEnum.OFFLINE.val);
        info.setEditor(operator.getNickname());
        dagRepo.updateDAGInfo(info);

        // 发布dag创建事件
        DAGEventLog eventLog = logDagEvent(id, EventTypeEnum.DAG_OFFLINE, operator);
        dagEventPublisher.whenToOffline(eventLog);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    @Deprecated
    public Boolean saveDependence(Long id, List<Long> dependenceIds, Operator operator) {
        final Long currentDagId = id;
        DAGInfo dagInfo = tryFetchDAGInfo(currentDagId);
        // 检查是否已下线，只有下线后才能更改
        checkArgument(Objects.equals(dagInfo.getStatus(), UsingStatusEnum.OFFLINE.val), "先下线DAG再更改依赖");

        List<DAGDependence> presentDepList = dagRepo.queryDependence(Arrays.asList(currentDagId));
        List<Long> toDeleteDepList = null;
        List<Long> toAddDepList = null;
        if (ObjectUtils.isEmpty(dependenceIds)) {
            // 依赖DAG为空
            if (ObjectUtils.isEmpty(presentDepList)) {
                // 过去没有依赖的DAG，则不处理
                return Boolean.TRUE;
            } else {
                // 删除过去依赖的DAG
                toDeleteDepList = presentDepList.stream()
                        .map(DAGDependence::getPrevDagId)
                        .collect(Collectors.toList());
            }
        } else {
            checkArgument(!dependenceIds.contains(currentDagId), "不能循环依赖");
            checkArgument(!isCycleDependence(currentDagId, dependenceIds), "不能循环依赖");

            if (ObjectUtils.isEmpty(presentDepList)) {
                toAddDepList = dependenceIds;
            } else {
                List<Long> presentDepIds = presentDepList.stream()
                        .map(DAGDependence::getPrevDagId)
                        .collect(Collectors.toList());
                toAddDepList = dependenceIds.stream()
                        .filter(depId -> !presentDepIds.contains(depId))
                        .collect(Collectors.toList());
                toDeleteDepList = presentDepIds.stream()
                        .filter(depId -> !dependenceIds.contains(depId))
                        .collect(Collectors.toList());
            }
        }

        if (ObjectUtils.isNotEmpty(toDeleteDepList)) {
            // 需先检查待删除依赖关系的dag，是否存在作业依赖
            dagRepo.deleteDependence(currentDagId);
            DAGEventLog eventLog = logDagEvent(currentDagId, EventTypeEnum.DAG_DEL_DEPENDENCE, new Gson().toJson(toDeleteDepList), operator);
            dagEventPublisher.whenRemoveDependence(eventLog, toDeleteDepList);
        }

        if (ObjectUtils.isNotEmpty(dependenceIds)) {
            List<DAGDependence> dagDependenceList = buildDependenceList(currentDagId, dependenceIds, operator);
            dagRepo.addDependence(dagDependenceList);
        }
        if (ObjectUtils.isNotEmpty(toAddDepList)) {
            DAGEventLog eventLog = logDagEvent(currentDagId, EventTypeEnum.DAG_ADD_DEPENDENCE, new Gson().toJson(toAddDepList), operator);
            dagEventPublisher.whenAddDependence(eventLog, toAddDepList);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<DAGInfoDto> getDAGInfoList(DAGInfoCondition condition) {
        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo(condition);
        if (ObjectUtils.isEmpty(dagInfoList)) return Lists.newArrayList();

        return dagInfoList.stream()
                .map(DAGInfoDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> cleanDagExecutionHistory(EnvEnum envEnum) {
        return jobScheduleManager.cleanDagExecutionHistory(envEnum);
    }

    private List<DAGDependence> buildDependenceList(final Long currentDagId, List<Long> dependenceIds, Operator operator) {
        Date now = new Date();
        return dependenceIds.stream()
                .map(depId -> {
                    DAGDependence dagDependence = new DAGDependence();
                    dagDependence.setDagId(currentDagId);
                    dagDependence.setPrevDagId(depId);
                    dagDependence.setCreator(operator.getNickname());
                    dagDependence.setCreateTime(now);
                    return dagDependence;
                }).collect(Collectors.toList());
    }

    private boolean isCycleDependence(final Long currentDagId, List<Long> dependenceIds) {
        List<DAGDependence> postDepList = dagRepo.queryDependence(dependenceIds);
        if (ObjectUtils.isEmpty(postDepList))
            return false;

        List<Long> filteredDepList = postDepList.stream()
                .map(DAGDependence::getPrevDagId)
                .filter(depId -> !depId.equals(currentDagId))
                .collect(Collectors.toList());
        if (filteredDepList.size() == postDepList.size()) {
            // 本级没有循环依赖，继续判断下级
            return isCycleDependence(currentDagId, filteredDepList);
        } else {
            // 存在循环依赖
            return true;
        }
    }

    private DAGInfo tryFetchDAGInfo(Long id) {
        checkArgument(Objects.nonNull(id), "DAG编号为空");
        Optional<DAGInfo> optionalDAGInfo = dagRepo.queryDAGInfo(id);
        checkArgument(optionalDAGInfo.isPresent(), "DAG不存在或已删除");
        return optionalDAGInfo.get();
    }

    private void checkDag(DAGInfoDto dagInfoDto, DAGScheduleDto dagScheduleDto) {
        checkArgument(Objects.nonNull(dagInfoDto), "DAG信息为空");
        checkArgument(StringUtils.isNotBlank(dagInfoDto.getName()), "DAG名称为空");
        checkArgument(StringUtils.isNotBlank(dagInfoDto.getDwLayerCode()), "DAG所属数仓分层为空");
        checkArgument(StringUtils.isNotBlank(dagInfoDto.getEnvironment()), "DAG所属环境为空");
        checkArgument(Objects.nonNull(dagInfoDto.getFolderId()), "DAG所属文件夹为空");

        checkArgument(Objects.nonNull(dagScheduleDto), "DAG调度信息为空");
        checkArgument(Objects.nonNull(dagScheduleDto.getBeginTime()) && Objects.nonNull(dagScheduleDto.getEndTime()), "DAG调度起止时间为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getPeriodRange()), "DAG调度周期类型为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getTriggerMode()), "DAG调度触发方式为空");
        checkArgument(StringUtils.isNotBlank(dagScheduleDto.getCronExpression()), "DAG调度时间为空");

        // 检查cron是否合法
        checkArgument(CronExpressionUtil.isValidExpression(dagScheduleDto.getCronExpression()), "DAG调度时间不合法");
    }

    private DAGEventLog logDagEvent(Long dagId, EventTypeEnum typeEnum, Operator operator) {
        return logDagEvent(dagId, typeEnum, null, operator);
    }

    private DAGEventLog logDagEvent(Long dagId, EventTypeEnum typeEnum, String eventInfo, Operator operator) {
        DAGEventLog eventLog = new DAGEventLog();
        eventLog.setDagId(dagId);
        eventLog.setDagEvent(typeEnum.name());
        eventLog.setHandleStatus(EventStatusEnum.PENDING.val);
        eventLog.setCreator(operator.getNickname());
        eventLog.setEditor(operator.getNickname());
        eventLog.setEventInfo(eventInfo);
        dagEventLogRepo.create(eventLog);
        return eventLog;
    }

    private boolean checkBaseInfoUpdated(DAGInfo newInfo, DAGInfo oldInfo) {
        return !Objects.equals(newInfo.getName(), oldInfo.getName());
    }

    private boolean checkScheduleUpdated(DAGSchedule newSchedule, DAGSchedule oldSchedule) {
        return !(Objects.equals(newSchedule.getBeginTime(), oldSchedule.getBeginTime())
                && Objects.equals(newSchedule.getEndTime(), oldSchedule.getEndTime())
                && Objects.equals(newSchedule.getCronExpression(), oldSchedule.getCronExpression()));
    }
}
