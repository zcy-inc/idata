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
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.commons.filter.KeywordFilter;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.PaginationInMemory;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDetailDto;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.cache.job.OverhangJobCacheValue;
import cn.zhengcaiyun.idata.develop.cache.job.OverhangJobLocalCache;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.*;
import cn.zhengcaiyun.idata.develop.dal.dao.job.*;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import cn.zhengcaiyun.idata.develop.dal.query.JobOutputQuery;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.event.job.publisher.JobEventPublisher;
import cn.zhengcaiyun.idata.develop.manager.JobManager;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private DevJobInfoMyDao devJobInfoMyDao;

    @Autowired
    private JobOutputMyDao jobOutputMyDao;

    private final JobInfoRepo jobInfoRepo;
    private final JobOutputRepo jobOutputRepo;
    private final DevJobUdfMyDao devJobUdfMyDao;
    private final JobPublishRecordMyDao jobPublishRecordMyDao;
    private final JobDependenceRepo jobDependenceRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final DAGRepo dagRepo;
    private final JobManager jobManager;
    private final JobScheduleManager jobScheduleManager;
    private final JobEventPublisher jobEventPublisher;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;
    private final OverhangJobLocalCache overhangJobLocalCache;
    private final DevAccessService devAccessService;
    private final DataSourceApi dataSourceApi;

    @Autowired
    public JobInfoServiceImpl(JobInfoRepo jobInfoRepo,
                              JobOutputRepo jobOutputRepo,
                              DevJobUdfMyDao devJobUdfMyDao,
                              JobPublishRecordMyDao jobPublishRecordMyDao,
                              JobDependenceRepo jobDependenceRepo,
                              JobExecuteConfigRepo jobExecuteConfigRepo,
                              JobPublishRecordRepo jobPublishRecordRepo,
                              DAGRepo dagRepo,
                              JobManager jobManager,
                              JobScheduleManager jobScheduleManager,
                              JobEventPublisher jobEventPublisher,
                              DevTreeNodeLocalCache devTreeNodeLocalCache,
                              OverhangJobLocalCache overhangJobLocalCache,
                              DevAccessService devAccessService,
                              DataSourceApi dataSourceApi) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobOutputRepo = jobOutputRepo;
        this.devJobUdfMyDao = devJobUdfMyDao;
        this.jobPublishRecordMyDao = jobPublishRecordMyDao;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.dagRepo = dagRepo;
        this.jobManager = jobManager;
        this.jobDependenceRepo = jobDependenceRepo;
        this.jobScheduleManager = jobScheduleManager;
        this.jobEventPublisher = jobEventPublisher;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
        this.overhangJobLocalCache = overhangJobLocalCache;
        this.devAccessService = devAccessService;
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    @Transactional
    public Long addJob(JobInfoDto dto, Operator operator) throws IllegalAccessException {
        checkJobInfo(dto);
        devAccessService.checkAddAccess(operator.getId(), dto.getFolderId());

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
    public List<JobInfo> getJobListByName(String searchName) {
        JobInfoCondition condition = new JobInfoCondition();
        condition.setName(searchName);
        return jobInfoRepo.queryJobInfo(condition);
    }

    @Override
    public Map<Long, String> getNameMapByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfoByIds(ids);
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
    public JobInfoExecuteDetailDto getJobInfoExecuteDetail(Long id, String env) {
        JobInfoExecuteDetailDto jobInfoExecuteDetailDto = devJobInfoMyDao.selectJobInfoExecuteDetail(id, env);
        checkArgument(jobInfoExecuteDetailDto != null, String.format("任务不存在或配置不存在, jobId:%d，环境:%s", id, env));

        String jobType = jobInfoExecuteDetailDto.getJobType();
        checkArgument(JobTypeEnum.getEnum(jobType).isPresent(), String.format("任务类型未匹配, jobType:%s", jobType));

        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobType).get();
        jobInfoExecuteDetailDto.setJobTypeEnum(jobTypeEnum);
        switch (jobTypeEnum) {
            case BACK_FLOW:
                JobInfoExecuteDetailDto.BackFlowDetailDto backFlowResponse = new JobInfoExecuteDetailDto.BackFlowDetailDto(jobInfoExecuteDetailDto);
                backFlowResponse.setJobTypeEnum(JobTypeEnum.BACK_FLOW);
                backFlowResponse.setJobType(JobTypeEnum.BACK_FLOW.getCode());

                // 封装di_job_content
                DIJobContent bfJobContent = jobPublishRecordMyDao.getPublishedDiJobContent(id, env);
                checkArgument(Objects.nonNull(bfJobContent), String.format("发布记录不存在或di_content_id未匹配, jobId:%d，环境:%s", id, env));

                BeanUtils.copyProperties(bfJobContent, backFlowResponse);

//                backFlowResponse.setUpdateKey(bfJobOutput.getJobTargetTablePk());
//                backFlowResponse.setDestWriteMode(JobWriteModeEnum.valueOf(bfJobOutput.getDestWriteMode()));
//
//                // 根据回流启用模式，填充数据
//                if (DiConfigModeEnum.VISIBLE.value.equals(bfJobContent.getConfigMode())) {
//                    backFlowResponse.setSrcSql(bfJobContent.getSrcQuery());
//                } else if (DiConfigModeEnum.SCRIPT.value.equals(bfJobContent.getConfigMode())) {
//                    backFlowResponse.setSrcSql(bfJobContent.getScriptQuery());
//                }
//                backFlowResponse.setParallelism(bfJobContent.getSrcShardingNum());
//
//                // 封装连接信息
//                DataSourceDetailDto bfSourceDetail = dataSourceApi.getDataSourceDetail(bfJobOutput.getDestDataSourceId());
//                backFlowResponse.setDestUrlPath(bfSourceDetail.getJdbcUrl());
//                backFlowResponse.setDestUserName(bfSourceDetail.getUserName());
//                backFlowResponse.setDestPassword(bfSourceDetail.getPassword());
//                backFlowResponse.setDestDriverType(bfSourceDetail.getDriverTypeEnum());
                return backFlowResponse;
            case DI_BATCH:
                JobInfoExecuteDetailDto.DiJobDetailsDto diResponse = new JobInfoExecuteDetailDto.DiJobDetailsDto(jobInfoExecuteDetailDto);

                // 封装di_job_content
                DIJobContent diJobContent = jobPublishRecordMyDao.getPublishedDiJobContent(id, env);
                checkArgument(Objects.nonNull(diJobContent), String.format("发布记录不存在或di_content_id未匹配, jobId:%d，环境:%s", id, env));
                BeanUtils.copyProperties(diJobContent, diResponse);
                if (StringUtils.isNotBlank(diJobContent.getSrcColumns())) {
                    diResponse.setSrcCols(JSON.parseArray(diJobContent.getSrcColumns(), MappingColumnDto.class));
                }

                //兼容数据库数据错误
                if (diResponse.getSrcShardingNum() == null || diResponse.getSrcShardingNum() < 1) {
                    diResponse.setSrcShardingNum(1);
                }

                // 封装连接信息
                DataSourceDetailDto srcSourceDetail = dataSourceApi.getDataSourceDetail(diJobContent.getSrcDataSourceId());
                diResponse.setSrcDataType(srcSourceDetail.getDataSourceTypeEnum().name());
                diResponse.setSrcJdbcUrl(srcSourceDetail.getJdbcUrl());
                diResponse.setSrcUsername(srcSourceDetail.getUserName());
                diResponse.setSrcPassword(srcSourceDetail.getPassword());
                diResponse.setSrcDbName(srcSourceDetail.getDbName());
                diResponse.setSrcDriverType(srcSourceDetail.getDriverTypeEnum());

                diResponse.setDiQuery(generateSrcQuery(diResponse.getSrcCols(), diResponse.getSrcReadFilter(), diResponse.getSrcTables(), diResponse.getSrcDbName()));

                // 字段类型转换
                diResponse.setDestWriteMode(WriteModeEnum.DiEnum.valueOf(diJobContent.getDestWriteMode()));
                diResponse.setSrcReadMode(SrcReadModeEnum.getByValue(diJobContent.getSrcReadMode()));

                String writeMode = diJobContent.getDestWriteMode();
                if (StringUtils.equalsIgnoreCase(writeMode, WriteModeEnum.SqlEnum.UPSERT.name())) {
                    // TODO 岛端不需要增量逻辑
                }
                return diResponse;
            case SQL_SPARK:
                JobOutputQuery query = new JobOutputQuery();
                query.setJobId(id);
                query.setEnvironment(env);
                JobOutput jobOutput = Optional.ofNullable(jobOutputMyDao.queryOne(query))
                        .orElseThrow(() -> new IllegalArgumentException(String.format("任务输出表不存在，jobId:%d，环境:%s", id, env)));

                // 封装sql_job_content
                DevJobContentSql contentSql = jobPublishRecordMyDao.getPublishedSqlJobContent(id, env);
                checkArgument(Objects.nonNull(contentSql), String.format("发布记录不存在或sql_content_id未匹配, jobId:%d，环境:%s", id, env));


                List<DevJobUdf> udfList = new ArrayList<>();
                String udfIds = contentSql.getUdfIds();
                if (StringUtils.isNotBlank(udfIds)) {
                    List<Long> idList = Arrays.stream(udfIds.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
                    udfList = devJobUdfMyDao.getByIds(idList);
                }

                // 额外判断：旧版idata中sql作业中涉及回流作业，htool对回流作业处理不一样
                DataSourceDto dataSource = dataSourceApi.getDataSource(jobOutput.getDestDataSourceId());
                if (!StringUtils.equalsIgnoreCase(dataSource.getType().name(), "hive")) {
                    JobInfoExecuteDetailDto.BackFlowDetailDto oldBackFlowResponse = new JobInfoExecuteDetailDto.BackFlowDetailDto(jobInfoExecuteDetailDto);
                    oldBackFlowResponse.setJobTypeEnum(JobTypeEnum.BACK_FLOW);
                    oldBackFlowResponse.setJobType(JobTypeEnum.BACK_FLOW.getCode());

                    BeanUtils.copyProperties(contentSql, oldBackFlowResponse);
                    BeanUtils.copyProperties(jobOutput, oldBackFlowResponse);
                    oldBackFlowResponse.setUdfList(udfList);

                    // 封装连接信息
                    DataSourceDetailDto destSourceDetail = dataSourceApi.getDataSourceDetail(jobOutput.getDestDataSourceId());
                    oldBackFlowResponse.setSrcSql(contentSql.getSourceSql());
                    oldBackFlowResponse.setDestUrlPath(destSourceDetail.getJdbcUrl());
                    oldBackFlowResponse.setDestUserName(destSourceDetail.getUserName());
                    oldBackFlowResponse.setDestPassword(destSourceDetail.getPassword());
                    oldBackFlowResponse.setDestWriteMode(WriteModeEnum.BackFlowEnum.valueOf(jobOutput.getDestWriteMode()));
                    oldBackFlowResponse.setDestDriverType(destSourceDetail.getDriverTypeEnum());
                    oldBackFlowResponse.setUpdateKey(jobOutput.getJobTargetTablePk());
                    return oldBackFlowResponse;
                }

                JobInfoExecuteDetailDto.SqlJobDetailsDto sqlResponse = new JobInfoExecuteDetailDto.SqlJobDetailsDto(jobInfoExecuteDetailDto);
                BeanUtils.copyProperties(contentSql, sqlResponse);
                BeanUtils.copyProperties(jobOutput, sqlResponse);
                sqlResponse.setUdfList(udfList);

                // 字段类型转换
                sqlResponse.setDestWriteMode(WriteModeEnum.SqlEnum.valueOf(jobOutput.getDestWriteMode()));

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
                        JobArgumentDto dto = (JobArgumentDto)script;
                        jarArgs.append(dto.getArgumentValue() + " ");
                    }
                    sparkResponse.setAppArguments(jarArgs.toString());
                }

                return sparkResponse;
            case KYLIN:
                JobInfoExecuteDetailDto.KylinDetailJob kylinResponse = new JobInfoExecuteDetailDto.KylinDetailJob(jobInfoExecuteDetailDto);

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
                        JobArgumentDto dto = (JobArgumentDto)script;
                        shellArgs.append(dto.getArgumentValue() + " ");
                    }
                    scriptResponse.setSourceResource(shellArgs.toString());
                }

                return scriptResponse;
            default:
                throw new IllegalArgumentException(String.format("不支持该任务类型, jobType:%s", jobType));
        }
    }

    /**
     * 生成src query
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
