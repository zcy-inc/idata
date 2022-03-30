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
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentVersionDto;
import cn.zhengcaiyun.idata.develop.dto.job.SubmitJobDto;
import cn.zhengcaiyun.idata.develop.manager.JobPublishManager;
import cn.zhengcaiyun.idata.develop.service.job.JobContentCommonService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.datasource.dal.dao.DataSourceFileDynamicSqlSupport.dataSourceFile;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
@Service
public class JobContentCommonServiceImpl implements JobContentCommonService {

    private final DIJobContentRepo diJobContentRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobPublishManager jobPublishManager;
    private final JobInfoRepo jobInfoRepo;
    private final JobInfoService jobInfoService;
    private final SqlJobRepo sqlJobRepo;
    private final SparkJobRepo sparkJobRepo;
    private final ScriptJobRepo scriptJobRepo;
    private final KylinJobRepo kylinJobRepo;
    private final JobContentSqlRepo jobContentSqlRepo;

    @Autowired
    public JobContentCommonServiceImpl(DIJobContentRepo diJobContentRepo,
                                       JobExecuteConfigRepo jobExecuteConfigRepo,
                                       JobPublishRecordRepo jobPublishRecordRepo,
                                       JobPublishManager jobPublishManager,
                                       JobInfoRepo jobInfoRepo,
                                       JobInfoService jobInfoService,
                                       SqlJobRepo sqlJobRepo,
                                       SparkJobRepo sparkJobRepo,
                                       ScriptJobRepo scriptJobRepo,
                                       KylinJobRepo kylinJobRepo,
                                       JobContentSqlRepo jobContentSqlRepo) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobPublishManager = jobPublishManager;
        this.jobInfoRepo = jobInfoRepo;
        this.jobInfoService = jobInfoService;
        this.sqlJobRepo = sqlJobRepo;
        this.sparkJobRepo = sparkJobRepo;
        this.scriptJobRepo = scriptJobRepo;
        this.kylinJobRepo = kylinJobRepo;
        this.jobContentSqlRepo = jobContentSqlRepo;
    }

    @Override
    public Boolean submit(Long jobId, Integer version, String env, String remark, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");
        Optional<JobInfo> jobInfoDto = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoDto.isPresent(), "作业不存在");
        // 支持不同作业
        JobContentBaseDto content = getJobContent(jobId, version, jobInfoDto.get().getJobType());
        Optional<EnvEnum> envEnumOptional = EnvEnum.getEnum(env);
        checkArgument(envEnumOptional.isPresent(), "提交环境为空");

        JobPublishContent publishContent = assemblePublishContent(content);
        return jobPublishManager.submit(publishContent, envEnumOptional.get(), remark, operator);
    }

    private JobPublishContent assemblePublishContent(JobContentBaseDto content) {
        return new JobPublishContent(content.getId(), content.getJobId(), content.getVersion());
    }

    @Override
    public List<JobContentVersionDto> getVersions(Long jobId) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        Optional<JobInfo> jobInfoDto = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoDto.isPresent(), "作业不存在");

        List<JobPublishRecord> publishRecordList = jobPublishRecordRepo.queryList(jobId);

        List<JobContentBaseDto> contentList = getJobContents(jobId, jobInfoDto.get().getJobType());

        List<JobContentVersionDto> contentVersionDtoList = assembleContentVersion(publishRecordList, contentList);

        List<JobExecuteConfig> executeConfigList = jobExecuteConfigRepo.queryList(jobId, new JobExecuteConfigCondition());
        fillRunningState(contentVersionDtoList, executeConfigList);
        return contentVersionDtoList;
    }

    @Override
    public JobContentBaseDto getJobContent(Long jobId, Integer version, String jobType) {
        try {
            JobTypeEnum.valueOf(jobType);
        } catch (Exception e) {
            throw new IllegalArgumentException("作业类型有误");
        }
        JobContentBaseDto content = new JobContentBaseDto();
        content.setJobId(jobId);
        content.setVersion(version);
        if (JobTypeEnum.DI_BATCH.getCode().equals(jobType) || JobTypeEnum.DI_STREAM.getCode().equals(jobType) || JobTypeEnum.BACK_FLOW.getCode().equals(jobType) ) {
            Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
            checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
            content.setId(jobContentOptional.get().getId());
        } else if (JobTypeEnum.SQL_SPARK.getCode().equals(jobType)) {
            checkArgument(sqlJobRepo.query(jobId, version) != null, "作业版本不存在");
            content.setId(sqlJobRepo.query(jobId, version).getId());
        } else if (JobTypeEnum.SPARK_PYTHON.getCode().equals(jobType) || JobTypeEnum.SPARK_JAR.getCode().equals(jobType)) {
            checkArgument(sparkJobRepo.query(jobId, version) != null, "作业版本不存在");
            content.setId(sparkJobRepo.query(jobId, version).getId());
        } else if (JobTypeEnum.SCRIPT_PYTHON.getCode().equals(jobType) || JobTypeEnum.SCRIPT_SHELL.getCode().equals(jobType)) {
            checkArgument(scriptJobRepo.query(jobId, version) != null, "作业版本不存在");
            content.setId(scriptJobRepo.query(jobId, version).getId());
        } else {
            checkArgument(kylinJobRepo.query(jobId, version) != null, "作业版本不存在");
            content.setId(kylinJobRepo.query(jobId, version).getId());
        }
        return content;
    }

    @Override
    public List<JobContentBaseDto> getJobContents(Long jobId, String jobType) {
        List<JobContentBaseDto> contentList;
        try {
            JobTypeEnum.valueOf(jobType);
        } catch (Exception e) {
            throw new IllegalArgumentException("作业类型有误");
        }
        if (JobTypeEnum.DI_BATCH.getCode().equals(jobType) || JobTypeEnum.DI_STREAM.getCode().equals(jobType)|| JobTypeEnum.BACK_FLOW.getCode().equals(jobType)) {
            contentList = PojoUtil.copyList(diJobContentRepo.queryList(jobId), JobContentBaseDto.class,
                    "jobId", "version", "createTime");
        } else if (JobTypeEnum.SQL_SPARK.getCode().equals(jobType)
                || JobTypeEnum.SQL_FLINK.getCode().equals(jobType)
                || JobTypeEnum.SQL_DORIS.getCode().equals(jobType)) {
            contentList = PojoUtil.copyList(sqlJobRepo.queryList(jobId), JobContentBaseDto.class,
                    "jobId", "version", "createTime");
        } else if (JobTypeEnum.SPARK_PYTHON.getCode().equals(jobType) || JobTypeEnum.SPARK_JAR.getCode().equals(jobType)) {
            contentList = PojoUtil.copyList(sparkJobRepo.queryList(jobId), JobContentBaseDto.class,
                    "jobId", "version", "createTime");
        } else if (JobTypeEnum.SCRIPT_PYTHON.getCode().equals(jobType) || JobTypeEnum.SCRIPT_SHELL.getCode().equals(jobType)) {
            contentList = PojoUtil.copyList(scriptJobRepo.queryList(jobId), JobContentBaseDto.class,
                    "jobId", "version", "createTime");
        } else {
            contentList = PojoUtil.copyList(kylinJobRepo.queryList(jobId), JobContentBaseDto.class,
                    "jobId", "version", "createTime");
        }
        return contentList;
    }

    @Override
    public Boolean ifBindUDF(Long id) {
        return jobContentSqlRepo.ifBindUDF(id);
    }

    private void fillRunningState(List<JobContentVersionDto> contentVersionDtoList, List<JobExecuteConfig> executeConfigList) {
        if (CollectionUtils.isEmpty(executeConfigList)) return;

        Map<String, JobExecuteConfig> envMap = executeConfigList.stream().collect(Collectors.toMap(JobExecuteConfig::getEnvironment, Function.identity()));
        for (JobContentVersionDto versionDto : contentVersionDtoList) {
            if (versionDto.getVersionStatus().equals(PublishStatusEnum.PUBLISHED.val)) {
                JobExecuteConfig config = envMap.get(versionDto.getEnvironment());
                versionDto.setEnvRunningState(config == null ? null : config.getRunningState());
            }
        }
    }

    private List<JobContentVersionDto> assembleContentVersion(List<JobPublishRecord> publishRecordList,
                                                              List<JobContentBaseDto> contentList) {
        Map<Integer, List<JobPublishRecord>> versionMap;
        if (ObjectUtils.isNotEmpty(publishRecordList)) {
            versionMap = publishRecordList.stream()
                    .collect(Collectors.groupingBy(JobPublishRecord::getJobContentVersion));
        } else {
            versionMap = Maps.newHashMap();
        }

        List<JobContentVersionDto> versionDtoList = Lists.newArrayList();
        if (ObjectUtils.isNotEmpty(contentList)) {
            for (JobContentBaseDto content : contentList) {
                List<JobPublishRecord> publishRecords = versionMap.get(content.getVersion());
                if (publishRecords == null || publishRecords.size() == 0) {
                    versionDtoList.add(JobContentVersionDto.from(content));
                    continue;
                }

                for (JobPublishRecord record : publishRecords) {
                    JobContentVersionDto versionDto = JobContentVersionDto.from(content);
                    versionDto.setEnvironment(record.getEnvironment());
                    versionDto.setVersionStatus(record.getPublishStatus());
                    versionDtoList.add(versionDto);
                }
            }
        }
        return versionDtoList.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
