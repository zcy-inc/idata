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

package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.connector.util.SparkSqlUtil;
import cn.zhengcaiyun.idata.develop.constant.enums.EventStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EventTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.JobDependenceDto;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 18:04
 **/
@Component
public class JobManager {

    private final JobEventLogRepo jobEventLogRepo;
    private final JobInfoRepo jobInfoRepo;
    private final SqlJobRepo sqlJobRepo;
    private final DIJobContentRepo diJobContentRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final JobOutputRepo jobOutputRepo;
    private final JobDependenceRepo jobDependenceRepo;

    @Autowired
    public JobManager(JobEventLogRepo jobEventLogRepo,
                      JobInfoRepo jobInfoRepo,
                      SqlJobRepo sqlJobRepo,
                      DIJobContentRepo diJobContentRepo,
                      JobExecuteConfigRepo jobExecuteConfigRepo,
                      JobOutputRepo jobOutputRepo,
                      JobDependenceRepo jobDependenceRepo) {
        this.jobEventLogRepo = jobEventLogRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.sqlJobRepo = sqlJobRepo;
        this.diJobContentRepo = diJobContentRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.jobOutputRepo = jobOutputRepo;
        this.jobDependenceRepo = jobDependenceRepo;
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, Operator operator) {
        return logEvent(jobId, typeEnum, "", operator);
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, String environment, Operator operator) {
        return logEvent(jobId, typeEnum, environment, null, operator);
    }

    public JobEventLog logEvent(Long jobId, EventTypeEnum typeEnum, String environment, String eventInfo, Operator operator) {
        JobEventLog eventLog = new JobEventLog();
        eventLog.setJobId(jobId);
        eventLog.setJobEvent(typeEnum.name());
        eventLog.setEnvironment(environment);
        eventLog.setHandleStatus(EventStatusEnum.PENDING.val);
        eventLog.setCreator(operator.getNickname());
        eventLog.setEditor(operator.getNickname());
        eventLog.setEventInfo(eventInfo);
        jobEventLogRepo.create(eventLog);
        return eventLog;
    }

    public List<JobDependenceDto> deriveDependenciesForSparkSql(JobInfo jobInfo, String environment, Integer version) {
        DevJobContentSql contentSql;
        if (Objects.isNull(version)) {
            Optional<DevJobContentSql> contentSqlOptional = sqlJobRepo.queryLatest(jobInfo.getId());
            checkArgument(contentSqlOptional.isPresent(), "请先输入作业SQL脚本");
            contentSql = contentSqlOptional.get();
        } else {
            contentSql = sqlJobRepo.query(jobInfo.getId(), version);
            checkArgument(Objects.nonNull(contentSql), "当前版本的作业SQL脚本不存在");
        }
        String sql = contentSql.getSourceSql();
        checkArgument(StringUtils.isNotBlank(sql), "作业的SQL脚本为空");

        List<String> fromTables;
        try {
            fromTables = SparkSqlUtil.getFromTables(sql, null);
        } catch (Exception ex) {
            return Lists.newArrayList();
        }
        return deriveJobDependencies(jobInfo, environment, fromTables);
    }

    public List<JobDependenceDto> deriveDependenciesForBackFlow(JobInfo jobInfo, String environment, Integer version) {
        Optional<DIJobContent> contentOptional;
        if (Objects.isNull(version)) {
            contentOptional = diJobContentRepo.queryLatest(jobInfo.getId());
            checkArgument(contentOptional.isPresent(), "请先保存作业内容");
        } else {
            contentOptional = diJobContentRepo.query(jobInfo.getId(), version);
            checkArgument(contentOptional.isPresent(), "当前版本作业内容不存在");
        }
        DIJobContent content = contentOptional.get();
        String srcTables = content.getSrcTables();
        return deriveJobDependencies(jobInfo, environment, Lists.newArrayList(srcTables));
    }

    private List<JobDependenceDto> deriveJobDependencies(final JobInfo jobInfo, final String environment, final List<String> tables) {
        if (CollectionUtils.isEmpty(tables)){
            return Lists.newArrayList();
        }
        List<JobOutput> outputList = jobOutputRepo.queryByDestTable(tables, environment);
        List<Long> derivedDepJobIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(outputList)) {
            derivedDepJobIds.addAll(outputList.stream()
                    .map(JobOutput::getJobId)
                    .distinct()
                    .collect(Collectors.toList()));
        }

        List<DIJobContent> diJobContents = diJobContentRepo.queryListByDestTable(tables);
        if (CollectionUtils.isNotEmpty(diJobContents)) {
            List<Long> tempDepJobIds = diJobContents.stream()
                    .map(DIJobContent::getJobId)
                    .distinct()
                    .collect(Collectors.toList());
            derivedDepJobIds.addAll(jobInfoRepo.queryJobInfo(tempDepJobIds)
                    .stream()
                    .filter(tempJobInfo -> JobTypeEnum.DI_BATCH.getCode().equals(tempJobInfo.getJobType())
                            || JobTypeEnum.DI_STREAM.getCode().equals(tempJobInfo.getJobType()))
                    .map(JobInfo::getId)
                    .collect(Collectors.toList()));
        }

        List<JobDependence> existDepList = jobDependenceRepo.queryPrevJob(jobInfo.getId(), environment);
        Set<Long> existDepJobIdSet = existDepList.stream()
                .map(JobDependence::getPrevJobId)
                .collect(Collectors.toSet());

        List<JobExecuteConfig> derivedDepJobConfigList = jobExecuteConfigRepo.queryList(derivedDepJobIds, environment);
        List<JobDependenceDto> finalDepDtoList = Lists.newArrayList();
        for (JobExecuteConfig derivedDepJobConfig : derivedDepJobConfigList) {
            if (!existDepJobIdSet.contains(derivedDepJobConfig.getJobId())) {
                JobDependenceDto dependenceDto = new JobDependenceDto();
                dependenceDto.setJobId(jobInfo.getId());
                dependenceDto.setEnvironment(environment);
                dependenceDto.setPrevJobId(derivedDepJobConfig.getJobId());
                dependenceDto.setPrevJobDagId(derivedDepJobConfig.getSchDagId());
                dependenceDto.setFresh(Boolean.TRUE);
                finalDepDtoList.add(dependenceDto);
            }
        }
        finalDepDtoList.addAll(existDepList.stream()
                .map(JobDependenceDto::from)
                .collect(Collectors.toList()));
        return finalDepDtoList;
    }

}
