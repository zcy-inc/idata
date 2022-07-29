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

package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.dto.general.KeyValuePair;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.enums.FolderTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.commons.filter.KeywordFilter;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.PaginationInMemory;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDetailDto;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.cache.job.OverhangJobCacheValue;
import cn.zhengcaiyun.idata.develop.cache.job.OverhangJobLocalCache;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.*;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobInfoMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import cn.zhengcaiyun.idata.develop.dal.query.JobOutputQuery;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.FlinkSqlJobExtendConfigDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobExtendConfigDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobExternalTableDto;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.helper.rule.DIRuleHelper;
import cn.zhengcaiyun.idata.develop.helper.rule.EnvRuleHelper;
import cn.zhengcaiyun.idata.develop.manager.JobManager;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.develop.util.FlinkSqlUtil;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import cn.zhengcaiyun.idata.develop.util.MyBeanUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 17:07
 **/
@Service
public class JobInfoServiceImpl implements JobInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    private DevJobInfoMyDao devJobInfoMyDao;
    private JobOutputMyDao jobOutputMyDao;
    private final JobInfoRepo jobInfoRepo;
    private final JobOutputRepo jobOutputRepo;
    private final DevJobUdfMyDao devJobUdfMyDao;
    private final JobPublishRecordMyDao jobPublishRecordMyDao;
    private final JobDependenceRepo jobDependenceRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final SqlJobRepo sqlJobRepo;
    private final DAGRepo dagRepo;
    private final CompositeFolderRepo compositeFolderRepo;
    private final JobManager jobManager;
    private final JobScheduleManager jobScheduleManager;
    private final JobEventPublisher jobEventPublisher;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;
    private final OverhangJobLocalCache overhangJobLocalCache;
    private final DevAccessService devAccessService;
    private final DataSourceApi dataSourceApi;
    private final DIJobContentRepo diJobContentRepo;
    private final SparkJobRepo sparkJobRepo;
    private final ScriptJobRepo scriptJobRepo;
    private final KylinJobRepo kylinJobRepo;

    private final DIStreamJobContentRepo diStreamJobContentRepo;

    @Autowired
    public JobInfoServiceImpl(DevJobInfoMyDao devJobInfoMyDao,
                              JobOutputMyDao jobOutputMyDao,
                              JobInfoRepo jobInfoRepo,
                              JobOutputRepo jobOutputRepo,
                              DevJobUdfMyDao devJobUdfMyDao,
                              JobPublishRecordMyDao jobPublishRecordMyDao,
                              JobDependenceRepo jobDependenceRepo,
                              JobExecuteConfigRepo jobExecuteConfigRepo,
                              JobPublishRecordRepo jobPublishRecordRepo,
                              SqlJobRepo sqlJobRepo, DAGRepo dagRepo,
                              CompositeFolderRepo compositeFolderRepo,
                              JobManager jobManager,
                              JobScheduleManager jobScheduleManager,
                              JobEventPublisher jobEventPublisher,
                              DevTreeNodeLocalCache devTreeNodeLocalCache,
                              OverhangJobLocalCache overhangJobLocalCache,
                              DevAccessService devAccessService,
                              DataSourceApi dataSourceApi,
                              DIJobContentRepo diJobContentRepo,
                              SparkJobRepo sparkJobRepo,
                              ScriptJobRepo scriptJobRepo,
                              KylinJobRepo kylinJobRepo,
                              DIStreamJobContentRepo diStreamJobContentRepo) {
        this.devJobInfoMyDao = devJobInfoMyDao;
        this.jobOutputMyDao = jobOutputMyDao;
        this.jobInfoRepo = jobInfoRepo;
        this.jobOutputRepo = jobOutputRepo;
        this.devJobUdfMyDao = devJobUdfMyDao;
        this.jobPublishRecordMyDao = jobPublishRecordMyDao;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.sqlJobRepo = sqlJobRepo;
        this.dagRepo = dagRepo;
        this.compositeFolderRepo = compositeFolderRepo;
        this.jobManager = jobManager;
        this.jobDependenceRepo = jobDependenceRepo;
        this.jobScheduleManager = jobScheduleManager;
        this.jobEventPublisher = jobEventPublisher;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
        this.overhangJobLocalCache = overhangJobLocalCache;
        this.devAccessService = devAccessService;
        this.dataSourceApi = dataSourceApi;
        this.diJobContentRepo = diJobContentRepo;
        this.sparkJobRepo = sparkJobRepo;
        this.scriptJobRepo = scriptJobRepo;
        this.kylinJobRepo = kylinJobRepo;
        this.diStreamJobContentRepo = diStreamJobContentRepo;
    }

    @Override
    @Transactional
    public Long addJob(JobInfoDto dto, Operator operator) throws IllegalAccessException {
        checkJobInfo(dto);
        devAccessService.checkAddAccess(operator.getId(), dto.getFolderId());

        Optional<CompositeFolder> folderOptional = compositeFolderRepo.queryFolder(dto.getFolderId());
        checkArgument(folderOptional.isPresent(), "作业所属文件夹不存在");
        checkArgument(!FolderTypeEnum.FUNCTION.name().equals(folderOptional.get().getType()), "作业不能建在模块根目录下");

        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameRecords), "作业名称已存在");
        dto.setStatus(UsingStatusEnum.ONLINE.val);
        dto.setOperator(operator);

        JobInfo info = dto.toModel();
        Long jobId = jobInfoRepo.saveJobInfo(info);
        // 发布job创建事件
        JobEventLog eventLog = jobManager.logEvent(jobId, EventTypeEnum.CREATED, operator);
        jobEventPublisher.whenCreated(eventLog);

        FunctionModuleEnum belong = dto.getJobType().belong();
        if (belong == null && dto.getJobType() == JobTypeEnum.BACK_FLOW) {
            belong = FunctionModuleEnum.DI;
        }
        devTreeNodeLocalCache.invalidate(belong);
        return jobId;
    }

    @Override
    @Transactional
    public Boolean editJobInfo(JobInfoDto dto, Operator operator) throws IllegalAccessException {
        checkJobInfo(dto);
        JobInfo oldJobInfo = tryFetchJobInfo(dto.getId());
        devAccessService.checkUpdateAccess(operator.getId(), oldJobInfo.getFolderId(), dto.getFolderId());

        List<JobInfo> dupNameRecords = jobInfoRepo.queryJobInfoByName(dto.getName());
        if (ObjectUtils.isNotEmpty(dupNameRecords)) {
            checkArgument(Objects.equals(dto.getId(), dupNameRecords.get(0).getId()), "作业名称已存在");
        }

        dto.setStatus(null);
        dto.resetEditor(operator);
        JobInfo newJobInfo = dto.toModel();
        Boolean ret = jobInfoRepo.updateJobInfo(newJobInfo);
        if (BooleanUtils.isTrue(ret)) {
            if (checkJobInfoUpdated(newJobInfo, oldJobInfo)) {
                List<JobExecuteConfig> executeConfigs = jobExecuteConfigRepo.queryList(dto.getId(), new JobExecuteConfigCondition());
                // 检查是否已停用，只有停用后才能更改
                checkArgument(!isRunning(executeConfigs), "先在所有环境下暂停作业，再修改作业名称");
                // 保存后发布job更新事件
                JobEventLog eventLog = jobManager.logEvent(newJobInfo.getId(), EventTypeEnum.UPDATED, operator);
                jobEventPublisher.whenUpdated(eventLog);
            }
        }
        devTreeNodeLocalCache.invalidate(dto.getJobType().belong());
        return ret;
    }

    @Override
    public JobInfoDto getJobInfo(Long id) {
        JobInfo jobInfo = tryFetchJobInfo(id);
        return JobInfoDto.from(jobInfo);
    }

    @Override
    public JobDetailsDto getJobDetails(Long jobId, Integer version, Boolean isDryRun) {
        return null;
    }

    @Override
    @Transactional
    public Boolean removeJob(Long id, Operator operator) throws IllegalAccessException {
        JobInfo jobInfo = tryFetchJobInfo(id);
        List<JobExecuteConfig> executeConfigs = jobExecuteConfigRepo.queryList(id, new JobExecuteConfigCondition());
        devAccessService.checkDeleteAccess(operator.getId(), jobInfo.getFolderId());
        // 检查是否已停用，只有停用后才能更改
        checkArgument(!isRunning(executeConfigs), "先在所有环境下暂停作业，再删除作业");

        List<JobDependence> postJobs = jobDependenceRepo.queryPostJob(id);
        List<JobDependence> prevJobs = jobDependenceRepo.queryPrevJob(id);
        checkArgument(ObjectUtils.isEmpty(postJobs) && ObjectUtils.isEmpty(prevJobs), "请删除所有环境作业上下游依赖，再删除作业");

        // 发布job删除事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.DELETED, operator);
        jobEventPublisher.whenDeleted(eventLog);

        Boolean ret = jobInfoRepo.deleteJobAndSubInfo(jobInfo, operator.getNickname());
        JobTypeEnum.getEnum(jobInfo.getJobType()).ifPresent(jobTypeEnum -> devTreeNodeLocalCache.invalidate(jobTypeEnum.belong()));
        return ret;
    }

    @Override
    @Transactional
    public Boolean resumeJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能恢复运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.equals(RunningStateEnum.pause.val, executeConfig.getRunningState()), "作业在%s环境已运行，勿重复操作", environment);
        //作业未发布，不能恢复运行
        checkState(isJobPublished(id, environment), "作业未发布，不能恢复");

        jobExecuteConfigRepo.switchRunningState(executeConfig.getId(), RunningStateEnum.resume, operator.getNickname());
        // 发布job恢复事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.JOB_RESUME, environment, operator);
        jobEventPublisher.whenResumed(eventLog);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean pauseJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能暂停运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.equals(RunningStateEnum.resume.val, executeConfig.getRunningState()), "作业在%s环境已暂停，勿重复操作", environment);

        jobExecuteConfigRepo.switchRunningState(executeConfig.getId(), RunningStateEnum.pause, operator.getNickname());
        // 发布job暂停事件
        JobEventLog eventLog = jobManager.logEvent(id, EventTypeEnum.JOB_PAUSE, environment, operator);
        jobEventPublisher.whenPaused(eventLog);
        return Boolean.TRUE;
    }

    @Override
    public Boolean runJob(Long id, String environment, Operator operator) {
        checkArgument(Objects.nonNull(id), "作业编号参数为空");
        checkArgument(StringUtils.isNotBlank(environment), "作业环境参数为空");
        Optional<JobExecuteConfig> configOptional = jobExecuteConfigRepo.query(id, environment);
        checkArgument(configOptional.isPresent(), "%s环境未配置调度配置，不能运行", environment);
        JobExecuteConfig executeConfig = configOptional.get();
        checkState(Objects.nonNull(executeConfig.getSchDagId()), "作业在%s环境未关联DAG，请先关联DAG", environment);
        //作业未发布，不能恢复运行
        checkState(isJobPublished(id, environment), "作业未发布，不能运行");
        checkState(Objects.equals(RunningStateEnum.resume.val, executeConfig.getRunningState()), "作业在%s环境已暂停，请先恢复", environment);
        // dag 必须上线
        checkState(isDAGOnline(executeConfig.getSchDagId()), "作业关联DAG未上线，请先上线DAG");

        jobScheduleManager.runJob(id, environment, false);
        return Boolean.TRUE;
    }

    // TODO 进勇脚本支持
    @Override
    public JobDryRunDto dryRunJob(Long jobId, Integer version) {
        return null;
    }

    @Override
    @Transactional
    public Boolean moveJob(List<Long> jobIds, Long destFolderId, Operator operator) {
        checkArgument(!CollectionUtils.isEmpty(jobIds), "请选择作业");
        checkArgument(Objects.nonNull(destFolderId), "请选择目标文件夹");
        final List<Long> finalJobIds = jobIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Optional<CompositeFolder> folderOptional = compositeFolderRepo.queryFolder(destFolderId);
        checkArgument(folderOptional.isPresent(), "目标文件夹不存在");
        checkArgument(!FolderTypeEnum.FUNCTION.name().equals(folderOptional.get().getType()), "目标文件夹不能是模块根目录");

        jobInfoRepo.updateJobFolder(finalJobIds, destFolderId, operator.getNickname());
        devTreeNodeLocalCache.invalidate(Lists.newArrayList(FunctionModuleEnum.DI, FunctionModuleEnum.DEV_JOB));
        return Boolean.TRUE;
    }

    @Override
    public List<JobInfo> getJobListByName(String searchName) {
        JobInfoCondition condition = new JobInfoCondition();
        condition.setName(searchName);
        return jobInfoRepo.queryJobInfo(condition);
    }

    @Override
    public Map<Long, String> getNameMapByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(ids);
        Map<Long, String> map = new HashMap<>();
        jobInfoList.forEach(e -> map.put(e.getId(), e.getName()));
        return map;
    }

    @Override
    public OverhangJobWrapperDto pagingOverhangJob(JobInfoCondition condition, PageParam pageParam) {
        Optional<OverhangJobCacheValue> cacheValueOptional = overhangJobLocalCache.getOverhangJobs();
        if (cacheValueOptional.isEmpty())
            return new OverhangJobWrapperDto(LocalDateTime.now(), Page.empty());

        JobTypeEnum jobType = condition.getJobType();
        String name = condition.getName();
        KeywordFilter nameFilter = StringUtils.isNotBlank(name) ? new KeywordFilter(name.trim()) : null;

        List<OverhangJobDto> overhangJobDtoList = cacheValueOptional.get().getOverhangJobDtoList().stream()
                .filter(dto -> jobType == null || jobType == dto.getJobType())
                .filter(dto -> nameFilter == null || nameFilter.match(dto.getName()))
                .collect(Collectors.toList());
        if (overhangJobDtoList.isEmpty())
            return new OverhangJobWrapperDto(cacheValueOptional.get().getFetchTime(), Page.empty());

        return new OverhangJobWrapperDto(cacheValueOptional.get().getFetchTime(),
                PaginationInMemory.of(overhangJobDtoList).paging(pageParam));
    }

    @Override
    public <T> void fillJobName(List<T> content, Class klass, String jobIdFieldName, String jobNameFieldName) throws NoSuchFieldException {
        List<Long> jobIdList = new ArrayList<>();
        Map<T, Long> objectMap = new HashMap<>();
        content.forEach(e -> {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(jobIdFieldName, klass);
                Method readMethod = propertyDescriptor.getReadMethod();
                Long jobId = (Long) readMethod.invoke(e);
                jobIdList.add(jobId);
                objectMap.put(e, jobId);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IntrospectionException introspectionException) {
                introspectionException.printStackTrace();
            }
        });

        if (CollectionUtils.isEmpty(jobIdList)) {
            return;
        }

        Map<Long, String> nameMap = new HashMap<>();
        jobInfoRepo.queryJobInfo(jobIdList).forEach(e -> nameMap.put(e.getId(), e.getName()));

        content.forEach(e -> {
            Long jobId = objectMap.get(e);
            String jobName = nameMap.get(jobId);
            try {
                PropertyDescriptor pd = new PropertyDescriptor(jobNameFieldName, klass);
                Method wM = pd.getWriteMethod();//获得写方法
                wM.invoke(e, jobName);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IntrospectionException introspectionException) {
                introspectionException.printStackTrace();
            }
        });
    }

    @Override
    public JobInfoExecuteDetailDto getJobInfoExecuteDetail(Long id, String env, Integer jobVersion) {
        JobInfoExecuteDetailDto jobInfoExecuteDetailDto = devJobInfoMyDao.selectJobInfoExecuteDetail(id, env);
        checkArgument(jobInfoExecuteDetailDto != null, String.format("任务不存在或配置不存在, jobId:%d，环境:%s", id, env));

        String jobType = jobInfoExecuteDetailDto.getJobType();
        checkArgument(JobTypeEnum.getEnum(jobType).isPresent(), String.format("任务类型未匹配, jobType:%s", jobType));

        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobType).get();
        jobInfoExecuteDetailDto.setJobTypeEnum(jobTypeEnum);

        Map<String, String> confProp = Maps.newHashMap();
        if (StringUtils.isNotBlank(jobInfoExecuteDetailDto.getExtProperties())) {
            List<KeyValuePair<String, String>> extendProperties = new Gson().fromJson(jobInfoExecuteDetailDto.getExtProperties(), new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType());
            extendProperties.stream()
                    .forEach(keyValPair -> confProp.put(keyValPair.getKey(), keyValPair.getValue()));
        }
        jobInfoExecuteDetailDto.setConfProp(confProp);

        switch (jobTypeEnum) {
            case BACK_FLOW:
                JobInfoExecuteDetailDto.BackFlowDetailDto backFlowResponse = new JobInfoExecuteDetailDto.BackFlowDetailDto(jobInfoExecuteDetailDto);
                backFlowResponse.setJobTypeEnum(JobTypeEnum.BACK_FLOW);
                backFlowResponse.setJobType(JobTypeEnum.BACK_FLOW.getCode());

                // 封装di_job_content
                DIJobContent bfJobContent = jobPublishRecordMyDao.getPublishedDiJobContent(id, env);
                checkArgument(Objects.nonNull(bfJobContent), String.format("发布记录不存在或di_content_id未匹配, jobId:%d，环境:%s", id, env));

                BeanUtils.copyProperties(bfJobContent, backFlowResponse);

                backFlowResponse.setSrcTable(bfJobContent.getSrcTables());
                backFlowResponse.setDestWriteMode(WriteModeEnum.BackFlowEnum.valueOf(bfJobContent.getDestWriteMode()));

                // 根据回流启用模式，填充数据
                if (DiConfigModeEnum.VISIBLE.value.equals(bfJobContent.getConfigMode())) {
                    backFlowResponse.setSrcSql(bfJobContent.getSrcQuery());
                    // 抽取关联映射列
                    List<MappingColumnDto> columnDtoList = JSON.parseArray(bfJobContent.getSrcColumns(), MappingColumnDto.class);
                    List<String> columnNameList = columnDtoList.stream().filter(e -> e.getMappedColumn() != null).map(e -> e.getName()).collect(Collectors.toList());
                    backFlowResponse.setDestColumnNames(StringUtils.join(columnNameList, ","));
                    //抽取主键key，此处使用destColumns字段，原因：1. 业务逻辑正确需要 2. 使用srcColumns的mappingColumn 前端传入的primaryKey错误都是false，无法使用 3. 如果字段维护都没有bug的话，可以统一使用destColumns字段，为了保证之前问题数据作业不影响，上面仍旧使用srcColumns
                    columnDtoList = JSON.parseArray(bfJobContent.getDestColumns(), MappingColumnDto.class);
                    List<String> keyNameList = columnDtoList.stream()
                            .filter(e -> e.getMappedColumn() != null)
                            .filter(e -> (e.getPrimaryKey() != null && e.getPrimaryKey()))
                            .map(e -> e.getName())
                            .collect(Collectors.toList());
                    backFlowResponse.setUpdateKey(StringUtils.join(keyNameList, ","));
                } else if (DiConfigModeEnum.SCRIPT.value.equals(bfJobContent.getConfigMode())) {
                    backFlowResponse.setSrcSql(bfJobContent.getScriptQuery());
                    backFlowResponse.setDestColumnNames(bfJobContent.getScriptSelectColumns());
                    backFlowResponse.setUpdateKey(bfJobContent.getScriptKeyColumns());
                }
                backFlowResponse.setParallelism(bfJobContent.getDestShardingNum());

                String properties = bfJobContent.getDestProperties();
                if (StringUtils.isNotBlank(properties)) {
                    List<KeyValuePair<String, String>> mapList = new Gson().fromJson(properties, new TypeToken<List<KeyValuePair<String, String>>>() {
                    }.getType());
                    Map<String, String> map = new HashMap<>();
                    mapList.forEach(e -> map.put(e.getKey(), e.getValue()));
                    backFlowResponse.setDestPropMap(map);
                }

                // 封装连接信息
                DataSourceDetailDto bfSourceDetail = dataSourceApi.getDataSourceDetail(bfJobContent.getDestDataSourceId(), env);
                backFlowResponse.setDestUrlPath(bfSourceDetail.getJdbcUrl());
                backFlowResponse.setDestUserName(bfSourceDetail.getUserName());
                backFlowResponse.setDestPassword(bfSourceDetail.getPassword());
                backFlowResponse.setDestDriverType(bfSourceDetail.getDriverTypeEnum());
                backFlowResponse.setDestDRDS(StringUtils.startsWithIgnoreCase(bfSourceDetail.getName(), "drds"));

                // 是否支持columns，不支持不返回给htool
                if (!DIRuleHelper.supportColumns(JobTypeEnum.BACK_FLOW, backFlowResponse.getExecEngine())) {
                    backFlowResponse.setDestColumnNames(null);
                }
                // 是否支持query sql，不支持不返回给htool
                DIRuleHelper.SupportQuerySqlParam bfParam = new DIRuleHelper.SupportQuerySqlParam();
                bfParam.setDiJobContent(bfJobContent);
                bfParam.setJobTypeEnum(JobTypeEnum.BACK_FLOW);
                bfParam.setEngineTypeEnum(backFlowResponse.getExecEngine());
                if (!DIRuleHelper.supportQuerySQL(bfParam)) {
                    backFlowResponse.setSrcSql(null);
                }

                // 根据规则定位真正的表
                DataSourceTypeEnum srcBfDsTypeEnum = dataSourceApi.getDataSourceDetail(bfJobContent.getSrcDataSourceId(), env).getDataSourceTypeEnum();
                String bfSrcRawTable = backFlowResponse.getSrcTable();
                String bfSrcTables = EnvRuleHelper.handlerDbTableName(srcBfDsTypeEnum, bfSrcRawTable, env);
                backFlowResponse.setSrcTable(bfSrcTables);
                backFlowResponse.setDestTable(EnvRuleHelper.handlerDbTableName(bfSourceDetail.getDataSourceTypeEnum(), backFlowResponse.getDestTable(), env));

                return backFlowResponse;
            case DI_BATCH:
                JobInfoExecuteDetailDto.DiJobDetailsDto diResponse = new JobInfoExecuteDetailDto.DiJobDetailsDto(jobInfoExecuteDetailDto);

                // 封装di_job_content
                DIJobContent diJobContent = jobPublishRecordMyDao.getPublishedDiJobContent(id, env);
                checkArgument(Objects.nonNull(diJobContent), String.format("发布记录不存在或di_content_id未匹配, jobId:%d，环境:%s", id, env));

                // 拷贝基础数据
                MyBeanUtils.copyDiProperties(diJobContent, diResponse);

                //兼容数据库数据错误
                if (diResponse.getSrcShardingNum() == null || diResponse.getSrcShardingNum() < 1) {
                    diResponse.setSrcShardingNum(1);
                }

                DataSourceDetailDto srcSourceDetail = dataSourceApi.getDataSourceDetail(diJobContent.getSrcDataSourceId(), env);
                String srcDiDsName = srcSourceDetail.getDataSourceTypeEnum().name();

                // 封装连接信息
                diResponse.setSrcDataType(srcDiDsName);
                diResponse.setSrcJdbcUrl(srcSourceDetail.getJdbcUrl());
                diResponse.setSrcUsername(srcSourceDetail.getUserName());
                diResponse.setSrcPassword(srcSourceDetail.getPassword());
                diResponse.setSrcDbName(srcSourceDetail.getDbName());
                diResponse.setSrcDriverType(srcSourceDetail.getDriverTypeEnum());

                // 字段类型转换
                diResponse.setDestWriteMode(WriteModeEnum.DiEnum.valueOf(diJobContent.getDestWriteMode()));
                diResponse.setSrcReadMode(SrcReadModeEnum.getByValue(diJobContent.getSrcReadMode()));

                // 是否支持query sql，不支持不返回给htool
                DIRuleHelper.SupportQuerySqlParam diParam = new DIRuleHelper.SupportQuerySqlParam();
                diParam.setDiJobContent(diJobContent);
                diParam.setJobTypeEnum(JobTypeEnum.DI_BATCH);
                diParam.setEngineTypeEnum(diResponse.getExecEngine());
                if (!DIRuleHelper.supportQuerySQL(diParam)) {
                    diResponse.setDiQuery(null);
                }

                // 根据规则定位真正的表
                String diSrcRawTable = diResponse.getSrcTables();
                String diSrcTables = EnvRuleHelper.handlerDbTableName(srcDiDsName, diSrcRawTable, env);
                diResponse.setSrcTables(diSrcTables);
                DataSourceTypeEnum destDiDsTypeEnum = dataSourceApi.getDataSourceDetail(diJobContent.getDestDataSourceId(), env).getDataSourceTypeEnum();
                diResponse.setDestTable(EnvRuleHelper.handlerDbTableName(destDiDsTypeEnum, diResponse.getDestTable(), env));


                return diResponse;
            case SQL_SPARK:
                JobOutputQuery query = new JobOutputQuery();
                query.setJobId(id);
                query.setEnvironment(env);
                JobOutput jobOutput = Optional.ofNullable(jobOutputMyDao.queryOne(query))
                        .orElseThrow(() -> new IllegalArgumentException(String.format("任务输出表不存在，jobId:%d，环境:%s", id, env)));

                // 封装sql_job_content
                // dryRun不需要发布，正常调用jobVersion > 0
                DevJobContentSql contentSql;
                if (jobVersion != null && jobVersion > 0) {
                    contentSql = sqlJobRepo.query(id, jobVersion);
                    checkArgument(Objects.nonNull(contentSql), String.format("sql_content_id未匹配, jobId:%d，环境:%s", id, env));
                } else {
                    contentSql = jobPublishRecordMyDao.getPublishedSqlJobContent(id, env);
                    checkArgument(Objects.nonNull(contentSql), String.format("发布记录不存在或sql_content_id未匹配, jobId:%d，环境:%s", id, env));
                }

                List<DevJobUdf> udfList = new ArrayList<>();
                String udfIds = contentSql.getUdfIds();
                if (StringUtils.isNotBlank(udfIds)) {
                    List<Long> idList = Arrays.stream(udfIds.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
                    udfList = devJobUdfMyDao.getByIds(idList);
                }

                // 额外判断：旧版idata中sql作业中涉及回流作业，htool对回流作业处理不一样
                DataSourceDto dataSource = dataSourceApi.getDataSource(jobOutput.getDestDataSourceId());
                String sqlDsName = dataSource.getType().name();
                if (!StringUtils.equalsIgnoreCase(sqlDsName, "hive")) {
                    JobInfoExecuteDetailDto.BackFlowDetailDto oldBackFlowResponse = new JobInfoExecuteDetailDto.BackFlowDetailDto(jobInfoExecuteDetailDto);
                    oldBackFlowResponse.setJobTypeEnum(JobTypeEnum.BACK_FLOW);
                    oldBackFlowResponse.setJobType(JobTypeEnum.BACK_FLOW.getCode());

                    BeanUtils.copyProperties(contentSql, oldBackFlowResponse);
                    BeanUtils.copyProperties(jobOutput, oldBackFlowResponse);
                    oldBackFlowResponse.setUdfList(udfList.stream().map(e -> JobUdfDto.fromModel(e)).collect(Collectors.toList()));

                    // 封装连接信息
                    DataSourceDetailDto destSourceDetail = dataSourceApi.getDataSourceDetail(jobOutput.getDestDataSourceId(), env);
                    oldBackFlowResponse.setSrcSql(contentSql.getSourceSql());
                    oldBackFlowResponse.setDestUrlPath(destSourceDetail.getJdbcUrl());
                    oldBackFlowResponse.setDestUserName(destSourceDetail.getUserName());
                    oldBackFlowResponse.setDestPassword(destSourceDetail.getPassword());
                    oldBackFlowResponse.setDestWriteMode(WriteModeEnum.BackFlowEnum.valueOf(jobOutput.getDestWriteMode()));
                    oldBackFlowResponse.setDestDriverType(destSourceDetail.getDriverTypeEnum());
                    oldBackFlowResponse.setUpdateKey(jobOutput.getJobTargetTablePk());
                    oldBackFlowResponse.setDestDRDS(StringUtils.startsWithIgnoreCase(destSourceDetail.getName(), "drds"));

                    // 根据规则定位真正的表
                    oldBackFlowResponse.setDestTable(EnvRuleHelper.handlerDbTableName(sqlDsName, oldBackFlowResponse.getDestTable(), env));

                    return oldBackFlowResponse;
                }

                JobInfoExecuteDetailDto.SqlJobDetailsDto sqlResponse = new JobInfoExecuteDetailDto.SqlJobDetailsDto(jobInfoExecuteDetailDto);
                BeanUtils.copyProperties(contentSql, sqlResponse);
                BeanUtils.copyProperties(jobOutput, sqlResponse);
                sqlResponse.setUdfList(udfList.stream().map(e -> JobUdfDto.fromModel(e)).collect(Collectors.toList()));

                // 字段类型转换
                sqlResponse.setDestWriteMode(WriteModeEnum.SqlEnum.valueOf(jobOutput.getDestWriteMode()));
                // 根据规则定位真正的表
                sqlResponse.setDestTable(EnvRuleHelper.handlerDbTableName(sqlDsName, sqlResponse.getDestTable(), env));

                //处理外部表
                List<JobInfoExecuteDetailDto.SqlJobDetailsDto.ExternalTableDto> externalTableList = new ArrayList<>();
                String extTables = contentSql.getExternalTables();
                if (StringUtils.isNotBlank(extTables)) {
                    List<SqlJobExternalTableDto> extTableDtoList = null;
                    try {
                        extTableDtoList = JSON.parseArray(extTables, SqlJobExternalTableDto.class);
                    } catch (Exception ex) {
                        LOGGER.warn("Parse ExternalTables to Array for SQL_SPARK job {} in env {} failed. ex {}.", id, env, Throwables.getStackTraceAsString(ex));
                    }
                    if (!CollectionUtils.isEmpty(extTableDtoList)) {
                        for (SqlJobExternalTableDto extTableDto : extTableDtoList) {
                            checkArgument(DataSourceTypeEnum.doris.name().equals(extTableDto.getDataSourceType())
                                            || DataSourceTypeEnum.starrocks.name().equals(extTableDto.getDataSourceType()),
                                    String.format("作业外部表目前支持doris或starrocks, jobId:%s，环境:%s", id.toString(), env));

                            DataSourceDetailDto extDataSource = dataSourceApi.getDataSourceDetail(extTableDto.getDataSourceId(), env);
                            JobInfoExecuteDetailDto.SqlJobDetailsDto.ExternalTableDto externalTableDto = new JobInfoExecuteDetailDto.SqlJobDetailsDto.ExternalTableDto();
                            externalTableDto.setExtSrcType("StarRocks");
                            externalTableDto.setExtSrcUrl(extDataSource.getHost() + ":8030");
                            externalTableDto.setExtSrcUsername(extDataSource.getUserName());
                            externalTableDto.setExtSrcPassword(extDataSource.getPassword());

                            List<SqlJobExternalTableDto.ExternalTableInfo> tables = extTableDto.getTables();
                            checkArgument(!CollectionUtils.isEmpty(tables), "外部表信息为空，jobId:%s，环境:%s", id.toString(), env);
                            List<String> extSrcTables = new ArrayList<>();
                            for (SqlJobExternalTableDto.ExternalTableInfo extTableInfo : tables) {
                                extSrcTables.add(extTableInfo.getTableName());
                            }
                            externalTableDto.setExtSrcTables(extSrcTables);
                            externalTableList.add(externalTableDto);
                        }
                    }
                }
                sqlResponse.setExternalTableList(externalTableList);

                return sqlResponse;
            case SPARK_PYTHON:
            case SPARK_JAR:
                JobInfoExecuteDetailDto.SparkJobDetailsDto sparkResponse = new JobInfoExecuteDetailDto.SparkJobDetailsDto(jobInfoExecuteDetailDto);

                // 封装spark_job_content
                DevJobContentSpark contentSpark = jobPublishRecordMyDao.getPublishedSparkJobContent(id, env);
                checkArgument(Objects.nonNull(contentSpark), String.format("发布记录不存在或sql_content_id未匹配, jobId:%d，环境:%s", id, env));
                BeanUtils.copyProperties(contentSpark, sparkResponse);

                // 封装shell参数　
                StringBuffer jarArgs = new StringBuffer("");
                if (!CollectionUtils.isEmpty(contentSpark.getAppArguments())) {
                    for (Object script : contentSpark.getAppArguments()) {
                        JobArgumentDto dto = (JobArgumentDto) script;
                        jarArgs.append(dto.getArgumentValue() + " ");
                    }
                    sparkResponse.setAppArguments(jarArgs.toString());
                }

                return sparkResponse;
            case KYLIN:
                JobInfoExecuteDetailDto.KylinDetailJobDto kylinResponse = new JobInfoExecuteDetailDto.KylinDetailJobDto(jobInfoExecuteDetailDto);

                // 封装kylin_job_content
                DevJobContentKylin contentKylin = jobPublishRecordMyDao.getPublishedKylinJobContent(id, env);
                checkArgument(Objects.nonNull(contentKylin), String.format("发布记录不存在或sql_content_id未匹配, jobId:%d，环境:%s", id, env));
                BeanUtils.copyProperties(contentKylin, kylinResponse);

                return kylinResponse;
            case SCRIPT_SHELL:
            case SCRIPT_PYTHON:
                JobInfoExecuteDetailDto.ScriptJobDetailsDto scriptResponse = new JobInfoExecuteDetailDto.ScriptJobDetailsDto(jobInfoExecuteDetailDto);

                // 封装script_job_content
                DevJobContentScript contentScript = jobPublishRecordMyDao.getPublishedScriptJobContent(id, env);
                checkArgument(Objects.nonNull(contentScript), String.format("发布记录不存在或sql_content_id未匹配, jobId:%d，环境:%s", id, env));
                BeanUtils.copyProperties(contentScript, scriptResponse);

                // 封装shell参数　
                StringBuffer shellArgs = new StringBuffer("");
                if (!CollectionUtils.isEmpty(contentScript.getScriptArguments())) {
                    for (Object script : contentScript.getScriptArguments()) {
                        JobArgumentDto dto = (JobArgumentDto) script;
                        shellArgs.append(dto.getArgumentValue() + " ");
                    }
                    scriptResponse.setScriptArguments(shellArgs.toString());
                }

                return scriptResponse;
            case SQL_FLINK:
                return getFlinkSqlJobDetail(id, env, jobInfoExecuteDetailDto, jobVersion);
            case DI_STREAM:
                return getFlinkCDCJobDetail(id, env, jobInfoExecuteDetailDto, jobVersion);
            default:
                throw new IllegalArgumentException(String.format("不支持该任务类型, jobType:%s", jobType));

        }
    }

    private JobInfoExecuteDetailDto getFlinkSqlJobDetail(Long jobId, String env, JobInfoExecuteDetailDto baseJobDetailDto,
                                                         Integer jobVersion) {
        // 封装sql_job_content
        // dryRun不需要发布，正常调用jobVersion > 0
        DevJobContentSql flinkSqlContent;
        boolean published = true;
        if (jobVersion != null && jobVersion > 0) {
            flinkSqlContent = sqlJobRepo.query(jobId, jobVersion);
            published = false;
        } else {
            flinkSqlContent = jobPublishRecordMyDao.getPublishedSqlJobContent(jobId, env);
        }
        checkArgument(Objects.nonNull(flinkSqlContent), String.format("未查询到可用的Flink作业内容, jobId:%s，环境:%s", jobId, env));
        String jobExtendConfigs = flinkSqlContent.getExtendConfigs();
        checkArgument(StringUtils.isNotBlank(jobExtendConfigs), "Flink作业数据源配置为空, jobId:%s，环境:%s", jobId, env);
        SqlJobExtendConfigDto extendConfigDto = new Gson().fromJson(jobExtendConfigs, SqlJobExtendConfigDto.class);
        FlinkSqlJobExtendConfigDto flinkExtConfig = extendConfigDto.getFlinkExtConfig();
        checkArgument(Objects.nonNull(flinkExtConfig), "Flink作业数据源配置为空, jobId:%s，环境:%s", jobId, env);
        checkArgument(!CollectionUtils.isEmpty(flinkExtConfig.getFlinkSinkConfigs()) && !CollectionUtils.isEmpty(flinkExtConfig.getFlinkSourceConfigs()), "Flink作业数据源配置为空, jobId:%d，环境:%s", jobId, env);

        List<DevJobUdf> udfList = new ArrayList<>();
        String udfIds = flinkSqlContent.getUdfIds();
        if (StringUtils.isNotBlank(udfIds)) {
            List<Long> idList = Arrays.stream(udfIds.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
            udfList = devJobUdfMyDao.getByIds(idList);
        }

        Map<String, String> privacyProps = Maps.newHashMap();
        flinkExtConfig.getFlinkSourceConfigs().stream().forEach(dataSourceConfigDto -> {
            DataSourceDto dataSourceDto = dataSourceApi.getDataSource(dataSourceConfigDto.getDataSourceId());
            DbConfigDto dbConfigDto = dataSourceDto.getDbConfigList().stream()
                    .filter(dbConfig -> dbConfig.getEnv().name().equals(env))
                    .findFirst().orElse(null);
            checkArgument(Objects.nonNull(dbConfigDto), "Flink作业数据源不合法, jobId:%s，环境:%s", jobId, env);
            privacyProps.putAll(FlinkSqlUtil.generateProperties(dataSourceConfigDto.getDataSourceType(), StringUtils.isBlank(dataSourceConfigDto.getDataSourceUDCode()) ? "-" : dataSourceConfigDto.getDataSourceUDCode(),
                    dataSourceDto.getType(), dbConfigDto));
        });

        flinkExtConfig.getFlinkSinkConfigs().stream().forEach(dataSourceConfigDto -> {
            DataSourceDto dataSourceDto = dataSourceApi.getDataSource(dataSourceConfigDto.getDataSourceId());
            DbConfigDto dbConfigDto = dataSourceDto.getDbConfigList().stream()
                    .filter(dbConfig -> dbConfig.getEnv().name().equals(env))
                    .findFirst().orElse(null);
            checkArgument(Objects.nonNull(dbConfigDto), "Flink作业数据源不合法, jobId:%s，环境:%s", jobId, env);
            privacyProps.putAll(FlinkSqlUtil.generateProperties(dataSourceConfigDto.getDataSourceType(), StringUtils.isBlank(dataSourceConfigDto.getDataSourceUDCode()) ? "-" : dataSourceConfigDto.getDataSourceUDCode(),
                    dataSourceDto.getType(), dbConfigDto));
        });

        if (!baseJobDetailDto.getConfProp().containsKey("logLevel")) {
            // 给个默认值
            baseJobDetailDto.getConfProp().put("logLevel", "warn");
        }
        JobInfoExecuteDetailDto.FlinkSqlJobDetailsDto flinkSqlResponse = new JobInfoExecuteDetailDto.FlinkSqlJobDetailsDto(baseJobDetailDto);
        flinkSqlResponse.setSourceSql(flinkSqlContent.getSourceSql());
        flinkSqlResponse.setUdfList(udfList.stream().map(e -> JobUdfDto.fromModel(e)).collect(Collectors.toList()));
        flinkSqlResponse.setJobPrivacyProp(privacyProps);

        flinkSqlResponse.setPublished(published);
        flinkSqlResponse.setJobVersion(flinkSqlContent.getVersion().toString());

        String flinkVersion = flinkSqlResponse.getConfProp().get("Flink-Version");
        flinkSqlResponse.getConfProp().remove("Flink-Version");
        if (StringUtils.isNotBlank(flinkVersion)) {
            // flink-1.10
            flinkSqlResponse.setFlinkVersion(flinkVersion);
        }

        // todo IData待支持
        flinkSqlResponse.setStartFromSavePoint(Boolean.FALSE);
        flinkSqlResponse.setFlinkJobId("");

        Optional<JobOutput> outputOptional = jobOutputRepo.query(jobId, env);
        checkArgument(outputOptional.isPresent(), "Flink SQL 作业输出表未配置, jobId:%s，环境:%s", jobId, env);
        JobOutput jobOutput = outputOptional.get();
        flinkSqlResponse.setDestTableName(jobOutput.getDestTable());
        return flinkSqlResponse;
    }

    private JobInfoExecuteDetailDto getFlinkCDCJobDetail(Long jobId, String env, JobInfoExecuteDetailDto baseJobDetailDto,
                                                         Integer jobVersion) {
        // dryRun不需要发布，正常调用jobVersion > 0
        Integer version = jobVersion;
        boolean published = false;
        if (version == null || version < 1) {
            // 查询发布版本
            JobPublishRecordCondition recordCond = new JobPublishRecordCondition();
            recordCond.setJobId(jobId);
            recordCond.setEnvironment(env);
            recordCond.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
            List<JobPublishRecord> publishRecordList = jobPublishRecordRepo.queryList(recordCond);
            checkArgument(!CollectionUtils.isEmpty(publishRecordList), String.format("FlinkCDC作业未发布，请先发布在运行, jobId:%s，环境:%s", jobId, env));
            version = publishRecordList.get(0).getJobContentVersion();
            published = true;
        }
        Optional<DIStreamJobContent> contentOptional = diStreamJobContentRepo.query(jobId, version);
        checkArgument(contentOptional.isPresent(), String.format("未查询到可用的FlinkCDC作业内容, jobId:%s，环境:%s，版本:%s", jobId, env, version));
        DIStreamJobContent jobContent = contentOptional.get();
        checkArgument(StringUtils.isNotBlank(jobContent.getCdcTables()), String.format("FlinkCDC作业表配置不合法, jobId:%s，环境:%s，版本:%s", jobId, env, version));
//        JobInfoExecuteDetailDto.FlinkCDCJobDetailDto cdcJobDetailDto = new JobInfoExecuteDetailDto.FlinkCDCJobDetailDto(baseJobDetailDto);
        JobInfoExecuteDetailDto.FlinkCDCJobDetailDto cdcJobDetailDto = JSON.parseObject(jobContent.getCdcTables(), JobInfoExecuteDetailDto.FlinkCDCJobDetailDto.class);
        BeanUtils.copyProperties(baseJobDetailDto, cdcJobDetailDto);
        cdcJobDetailDto.setJobVersion(version.toString());
        cdcJobDetailDto.setPublished(published);

        return cdcJobDetailDto;
    }

    @Override
    public List<JobExtInfoDto> getJobExtInfo(List<Long> jobIds) {
        if (CollectionUtils.isEmpty(jobIds)) {
            return Lists.newArrayList();
        }
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(jobIds);
        if (CollectionUtils.isEmpty(jobInfoList)) {
            return Lists.newArrayList();
        }
        List<JobExtInfoDto> extInfoDtoList = jobInfoList.stream()
                .map(JobExtInfoDto::from)
                .collect(Collectors.toList());
        Map<JobTypeEnum, List<JobExtInfoDto>> jobTypeMap = extInfoDtoList.stream()
                .collect(Collectors.groupingBy(JobExtInfoDto::getJobType));
        for (Map.Entry<JobTypeEnum, List<JobExtInfoDto>> entry : jobTypeMap.entrySet()) {
            List<Long> subJobIds = entry.getValue().stream()
                    .map(JobExtInfoDto::getId)
                    .collect(Collectors.toList());
            Map<Long, JobContentVersionDto> jobVersionMap = fetchJobVersions(entry.getKey(), subJobIds);
            entry.getValue().forEach(jobExtInfoDto -> {
                JobContentVersionDto versionDto = jobVersionMap.get(jobExtInfoDto.getId());
                if (Objects.nonNull(versionDto)) {
                    jobExtInfoDto.setVersion(versionDto.getVersion());
                    if (StringUtils.isNotBlank(versionDto.getVersionDisplay())) {
                        String envTag = "";
                        if (StringUtils.isNotBlank(versionDto.getEnvironment())) {
                            envTag = "-" + versionDto.getEnvironment();
                        }
                        jobExtInfoDto.setVersionDisplay(versionDto.getVersionDisplay() + envTag);
                    }
                }
            });
        }
        return extInfoDtoList;
    }

    private Map<Long, JobContentVersionDto> fetchJobVersions(JobTypeEnum typeEnum, List<Long> jobIds) {
        if (CollectionUtils.isEmpty(jobIds)) {
            return Maps.newHashMap();
        }

        JobPublishRecordCondition condition = new JobPublishRecordCondition();
        condition.setJobIds(jobIds);
        condition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        condition.setEnvironment(EnvEnum.prod.name());
        List<JobPublishRecord> prodPublishRecordList = jobPublishRecordRepo.queryList(condition);
        List<Long> prodPublishContentIds = prodPublishRecordList.stream()
                .map(JobPublishRecord::getJobContentId)
                .collect(Collectors.toList());
        Map<Long, JobContentVersionDto> jobVersionMap = Maps.newHashMap();
        putJobVersions(typeEnum, prodPublishContentIds, EnvEnum.prod, jobVersionMap);

        condition.setEnvironment(EnvEnum.stag.name());
        List<JobPublishRecord> stagPublishRecordList = jobPublishRecordRepo.queryList(condition);
        List<Long> stagPublishContentIds = stagPublishRecordList.stream()
                .map(JobPublishRecord::getJobContentId)
                .collect(Collectors.toList());
        putJobVersions(typeEnum, stagPublishContentIds, EnvEnum.stag, jobVersionMap);
        return jobVersionMap;
    }

    private void putJobVersions(JobTypeEnum typeEnum, List<Long> jobPublishContentIds, EnvEnum publishedEnvEnum, Map<Long, JobContentVersionDto> jobVersionMap) {
        if (CollectionUtils.isEmpty(jobPublishContentIds)) {
            return;
        }
        if (JobTypeEnum.DI_BATCH == typeEnum || JobTypeEnum.BACK_FLOW == typeEnum) {
            List<DIJobContent> contentList = diJobContentRepo.queryList(jobPublishContentIds);
            for (DIJobContent content : contentList) {
                if (!jobVersionMap.containsKey(content.getJobId())) {
                    JobContentVersionDto versionDto = new JobContentVersionDto();
                    versionDto.setJobId(content.getJobId());
                    versionDto.setVersion(content.getVersion());
                    versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                    versionDto.setEnvironment(publishedEnvEnum.name());
                    jobVersionMap.put(content.getJobId(), versionDto);
                }
            }
        } else if (JobTypeEnum.SQL_SPARK == typeEnum || JobTypeEnum.SQL_FLINK == typeEnum) {
            List<DevJobContentSql> contentList = sqlJobRepo.queryList(jobPublishContentIds);
            for (DevJobContentSql content : contentList) {
                if (!jobVersionMap.containsKey(content.getJobId())) {
                    JobContentVersionDto versionDto = new JobContentVersionDto();
                    versionDto.setJobId(content.getJobId());
                    versionDto.setVersion(content.getVersion());
                    versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                    versionDto.setEnvironment(publishedEnvEnum.name());
                    jobVersionMap.put(content.getJobId(), versionDto);
                }
            }
        } else if (JobTypeEnum.SPARK_JAR == typeEnum || JobTypeEnum.SPARK_PYTHON == typeEnum) {
            List<DevJobContentSpark> contentList = sparkJobRepo.queryList(jobPublishContentIds);
            for (DevJobContentSpark content : contentList) {
                if (!jobVersionMap.containsKey(content.getJobId())) {
                    JobContentVersionDto versionDto = new JobContentVersionDto();
                    versionDto.setJobId(content.getJobId());
                    versionDto.setVersion(content.getVersion());
                    versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                    versionDto.setEnvironment(publishedEnvEnum.name());
                    jobVersionMap.put(content.getJobId(), versionDto);
                }
            }
        } else if (JobTypeEnum.SCRIPT_PYTHON == typeEnum || JobTypeEnum.SCRIPT_SHELL == typeEnum) {
            List<DevJobContentScript> contentList = scriptJobRepo.queryList(jobPublishContentIds);
            for (DevJobContentScript content : contentList) {
                if (!jobVersionMap.containsKey(content.getJobId())) {
                    JobContentVersionDto versionDto = new JobContentVersionDto();
                    versionDto.setJobId(content.getJobId());
                    versionDto.setVersion(content.getVersion());
                    versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                    versionDto.setEnvironment(publishedEnvEnum.name());
                    jobVersionMap.put(content.getJobId(), versionDto);
                }
            }
        } else if (JobTypeEnum.KYLIN == typeEnum) {
            List<DevJobContentKylin> contentList = kylinJobRepo.queryList(jobPublishContentIds);
            for (DevJobContentKylin content : contentList) {
                if (!jobVersionMap.containsKey(content.getJobId())) {
                    JobContentVersionDto versionDto = new JobContentVersionDto();
                    versionDto.setJobId(content.getJobId());
                    versionDto.setVersion(content.getVersion());
                    versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
                    versionDto.setEnvironment(publishedEnvEnum.name());
                    jobVersionMap.put(content.getJobId(), versionDto);
                }
            }
        }
    }

    /**
     * 生成src query
     *
     * @param mappingColumnList
     * @param srcReadFilter
     * @param srcTables
     * @return
     */
    private String generateSrcQuery(List<MappingColumnDto> mappingColumnList, String srcReadFilter, String srcTables, String srcDbName) {
        if (CollectionUtils.isEmpty(mappingColumnList)) {
            return null;
        }
        boolean generate = mappingColumnList.stream().anyMatch(e -> StringUtils.isNotEmpty(e.getMappingSql()));
        if (!generate) {
            return null;
        }

        List<String> columns = mappingColumnList.stream().map(e -> {
            String name = e.getName();
            String mappingSql = e.getMappingSql();
            return StringUtils.isNotEmpty(mappingSql) ? mappingSql : name;
        }).collect(Collectors.toList());

        String selectColumns = StringUtils.join(columns, ",");
        String srcQuery = String.format("select %s from %s.%s ", selectColumns, srcDbName, srcTables);
        if (StringUtils.isNotEmpty(srcReadFilter)) {
            srcQuery += (" where " + srcReadFilter);
        }
        return srcQuery;
    }

    private JobInfo tryFetchJobInfo(Long id) {
        checkArgument(Objects.nonNull(id), "作业编号不存在");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(id);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        return jobInfoOptional.get();
    }

    private void checkJobInfo(JobInfoDto dto) {
        checkArgument(StringUtils.isNotBlank(dto.getName()), "作业名为空");
        checkArgument(StringUtils.isNotBlank(dto.getDwLayerCode()), "作业分层为空");
        checkArgument(Objects.nonNull(dto.getFolderId()), "作业所属文件夹为空");
        checkArgument(Objects.nonNull(dto.getJobType()), "作业类型为空");
        checkArgument(JobTypeEnum.getEnum(dto.getJobType().toString()).isPresent(), "作业类型有误");
    }

    private boolean isJobPublished(Long jobId, String environment) {
        JobPublishRecordCondition condition = new JobPublishRecordCondition();
        condition.setJobId(jobId);
        condition.setEnvironment(environment);
        condition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        return jobPublishRecordRepo.count(condition) > 0;
    }

    private boolean isDAGOnline(Long dagId) {
        Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(dagId);
        if (dagInfoOptional.isEmpty()) return false;
        return Objects.equals(dagInfoOptional.get().getStatus(), UsingStatusEnum.ONLINE.val);
    }

    private boolean checkJobInfoUpdated(JobInfo newJobInfo, JobInfo oldJobInfo) {
        return !Objects.equals(newJobInfo.getName(), oldJobInfo.getName());
    }

    private boolean isRunning(JobExecuteConfig config) {
        return Objects.equals(RunningStateEnum.resume.val, config.getRunningState());
    }

    private boolean isRunning(List<JobExecuteConfig> executeConfigs) {
        if (CollectionUtils.isEmpty(executeConfigs)) return false;

        for (JobExecuteConfig config : executeConfigs) {
            if (isRunning(config)) return true;
        }
        return false;
    }
}
