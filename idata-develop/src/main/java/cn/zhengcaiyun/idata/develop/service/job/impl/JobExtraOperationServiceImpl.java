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
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobContentDto;
import cn.zhengcaiyun.idata.develop.manager.JobExtraOperationManager;
import cn.zhengcaiyun.idata.develop.service.job.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-04-28 14:35
 **/
@Service
public class JobExtraOperationServiceImpl implements JobExtraOperationService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final JobInfoService jobInfoService;
    private final JobExecuteConfigService jobExecuteConfigService;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final DIJobContentService diJobContentService;
    private final SqlJobService sqlJobService;
    private final SparkJobService sparkJobService;
    private final ScriptJobService scriptJobService;
    private final KylinJobService kylinJobService;
    private final JobExtraOperationManager jobExtraOperationManager;

    public JobExtraOperationServiceImpl(JobInfoService jobInfoService,
                                        JobExecuteConfigService jobExecuteConfigService,
                                        JobPublishRecordRepo jobPublishRecordRepo,
                                        DIJobContentService diJobContentService,
                                        SqlJobService sqlJobService,
                                        SparkJobService sparkJobService,
                                        ScriptJobService scriptJobService,
                                        KylinJobService kylinJobService,
                                        JobExtraOperationManager jobExtraOperationManager) {
        this.jobInfoService = jobInfoService;
        this.jobExecuteConfigService = jobExecuteConfigService;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.diJobContentService = diJobContentService;
        this.sqlJobService = sqlJobService;
        this.sparkJobService = sparkJobService;
        this.scriptJobService = scriptJobService;
        this.kylinJobService = kylinJobService;
        this.jobExtraOperationManager = jobExtraOperationManager;
    }

    @Override
    public List<JobExtraOperateResult> copyJobTo(List<Long> jobIds, Long destFolderId, Operator operator) {
        List<JobExtraOperateResult> resultList = Lists.newArrayList();
        for (Long jobId : jobIds) {
            JobExtraOperateResult result = copyJobTo(jobId, destFolderId, operator);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public JobExtraOperateResult copyJobTo(Long jobId, Long destFolderId, Operator operator) {
        JobExtraOperateResult result = new JobExtraOperateResult();
        try {
            boolean fromImport = false;
            JobReplicationDto jobReplicationDto = getJobReplication(jobId);
            String jobOriginName = jobReplicationDto.getJobInfoDto().getName();
            cleanOutJobReplication(jobReplicationDto, destFolderId, fromImport);
            jobExtraOperationManager.saveJobReplication(jobReplicationDto, fromImport, operator);

            result.setJobName(jobOriginName);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception ex) {
            result.setSuccess(Boolean.FALSE);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    @Override
    public String exportJobJson(List<Long> jobIds) {
        List<JobReplicationDto> replicationDtoList = Lists.newArrayList();
        for (Long jobId : jobIds) {
            JobReplicationDto jobReplicationDto = getJobReplication(jobId);
            replicationDtoList.add(jobReplicationDto);
        }
        return new GsonBuilder().create().toJson(replicationDtoList);
    }

    @Override
    public List<JobExtraOperateResult> importJob(String jobJson, Long destFolderId, Operator operator) {
        List<JobReplicationDto> replicationDtoList = new GsonBuilder().create().fromJson(jobJson, new TypeToken<List<JobReplicationDto>>() {
        }.getType());
        List<JobExtraOperateResult> results = Lists.newArrayList();
        for (JobReplicationDto replicationDto : replicationDtoList) {
            JobExtraOperateResult result = new JobExtraOperateResult();
            try {
                boolean fromImport = true;
                cleanOutJobReplication(replicationDto, destFolderId, fromImport);
                jobExtraOperationManager.saveJobReplication(replicationDto, fromImport, operator);

                result.setJobName(replicationDto.getJobInfoDto().getName());
                result.setSuccess(Boolean.TRUE);
            } catch (Exception ex) {
                result.setSuccess(Boolean.FALSE);
                result.setMsg(ex.getMessage());
            }
            results.add(result);
        }
        return results;
    }

    private JobReplicationDto getJobReplication(Long jobId) {
        JobReplicationDto jobReplicationDto = new JobReplicationDto();

        JobInfoDto jobInfoDto = jobInfoService.getJobInfo(jobId);
        jobReplicationDto.setJobInfoDto(jobInfoDto);

        Map<EnvEnum, JobConfigCombinationDto> jobConfigCombinationDtoMap = Maps.newHashMap();
        Arrays.stream(EnvEnum.values())
                .forEach(enumObj -> {
                    JobConfigCombinationDto configCombinationDto = jobExecuteConfigService.getCombineConfig(jobId, enumObj.name());
                    if (Objects.nonNull(configCombinationDto)) {
                        jobConfigCombinationDtoMap.put(enumObj, configCombinationDto);
                    }
                });

        String contentJson;
        JobPublishRecordCondition condition = new JobPublishRecordCondition();
        condition.setJobId(jobId);
        condition.setEnvironment(EnvEnum.prod.name());
        condition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        List<JobPublishRecord> publishRecordList = jobPublishRecordRepo.queryList(condition, 1, 0);
        if (CollectionUtils.isNotEmpty(publishRecordList)) {
            JobPublishRecord publishRecord = publishRecordList.get(0);
            contentJson = getJobContentJson(jobInfoDto, publishRecord.getJobContentVersion());
        } else {
            condition.setEnvironment(EnvEnum.stag.name());
            publishRecordList = jobPublishRecordRepo.queryList(condition, 1, 0);
            if (CollectionUtils.isNotEmpty(publishRecordList)) {
                JobPublishRecord publishRecord = publishRecordList.get(0);
                contentJson = getJobContentJson(jobInfoDto, publishRecord.getJobContentVersion());
            } else {
                contentJson = null;
            }
        }

        jobReplicationDto.setJobConfigCombinationDtoMap(jobConfigCombinationDtoMap);
        jobReplicationDto.setJobContentJson(contentJson);
        return jobReplicationDto;
    }

    private String getJobContentJson(JobInfoDto jobInfoDto, Integer version) {
        JobTypeEnum jobType = jobInfoDto.getJobType();
        String contentJson;
        switch (jobType) {
            case DI_BATCH:
            case BACK_FLOW:
                contentJson = getDIJobContentJson(jobInfoDto.getId(), version);
                break;
            case SQL_SPARK:
            case SQL_FLINK:
            case SQL_DORIS:
                contentJson = getSQLJobContentJson(jobInfoDto.getId(), version);
                break;
            case SPARK_JAR:
            case SPARK_PYTHON:
                contentJson = getSparkJobContentJson(jobInfoDto.getId(), version);
                break;
            case SCRIPT_PYTHON:
            case SCRIPT_SHELL:
                contentJson = getScriptJobContentJson(jobInfoDto.getId(), version);
                break;
            case KYLIN:
                contentJson = getKylinJobContentJson(jobInfoDto.getId(), version);
                break;
            default:
                contentJson = null;
                break;
        }
        return contentJson;
    }

    private String getDIJobContentJson(Long jobId, Integer version) {
        DIJobContentContentDto contentDto = diJobContentService.get(jobId, version);
        return new Gson().toJson(contentDto);
    }

    private String getSQLJobContentJson(Long jobId, Integer version) {
        SqlJobContentDto contentDto = sqlJobService.find(jobId, version);
        return new Gson().toJson(contentDto);
    }

    private String getSparkJobContentJson(Long jobId, Integer version) {
        SparkJobContentDto contentDto = sparkJobService.find(jobId, version);
        return new Gson().toJson(contentDto);
    }

    private String getScriptJobContentJson(Long jobId, Integer version) {
        ScriptJobContentDto contentDto = scriptJobService.find(jobId, version);
        return new Gson().toJson(contentDto);
    }

    private String getKylinJobContentJson(Long jobId, Integer version) {
        KylinJobDto contentDto = kylinJobService.find(jobId, version);
        return new Gson().toJson(contentDto);
    }

    private void cleanOutJobReplication(JobReplicationDto jobReplicationDto, Long destFolderId, boolean fromImport) {
        JobInfoDto jobInfoDto = jobReplicationDto.getJobInfoDto();
        Map<EnvEnum, JobConfigCombinationDto> jobConfigCombinationDtoMap = jobReplicationDto.getJobConfigCombinationDtoMap();
        jobInfoDto.setName(getJobCopyName(jobInfoDto.getName()));
        if (Objects.nonNull(destFolderId)) {
            jobInfoDto.setFolderId(destFolderId);
        }
        jobInfoDto.setId(null);

        if (jobConfigCombinationDtoMap != null && jobConfigCombinationDtoMap.size() > 0) {
            jobConfigCombinationDtoMap.forEach((envEnum, jobConfigCombinationDto) -> {
                JobExecuteConfigDto executeConfig = jobConfigCombinationDto.getExecuteConfig();
                if (Objects.nonNull(executeConfig)) {
                    executeConfig.setId(null);
                    executeConfig.setJobId(null);
                    if (fromImport) {
                        executeConfig.setSchDagId(0L);
                    }
                }
                List<JobDependenceDto> dependencies = jobConfigCombinationDto.getDependencies();
                if (CollectionUtils.isNotEmpty(dependencies)) {
                    if (fromImport) {
                        jobConfigCombinationDto.setDependencies(null);
                    } else {
                        for (JobDependenceDto dependenceDto : dependencies) {
                            dependenceDto.setId(null);
                            dependenceDto.setJobId(null);
                        }
                    }
                }
                JobOutputDto output = jobConfigCombinationDto.getOutput();
                if (Objects.nonNull(output)) {
                    output.setId(null);
                    output.setJobId(null);
                    if (fromImport) {
                        output.setDestDataSourceId(0L);
                    }
                }
            });
        }
    }

    private String getJobCopyName(String originName) {
        return originName + "-copy-" + LocalDateTime.now().format(dateTimeFormatter);
    }

}
