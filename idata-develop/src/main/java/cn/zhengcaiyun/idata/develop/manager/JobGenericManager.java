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
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobReplicationDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-05 15:05
 **/
@Component
public class JobGenericManager {

    private final JobInfoService jobInfoService;
    private final JobExecuteConfigService jobExecuteConfigService;
    private final DIJobContentService diJobContentService;
    private final SqlJobService sqlJobService;
    private final SparkJobService sparkJobService;
    private final ScriptJobService scriptJobService;
    private final KylinJobService kylinJobService;

    public JobGenericManager(JobInfoService jobInfoService,
                             JobExecuteConfigService jobExecuteConfigService,
                             DIJobContentService diJobContentService,
                             SqlJobService sqlJobService,
                             SparkJobService sparkJobService,
                             ScriptJobService scriptJobService,
                             KylinJobService kylinJobService) {
        this.jobInfoService = jobInfoService;
        this.jobExecuteConfigService = jobExecuteConfigService;
        this.diJobContentService = diJobContentService;
        this.sqlJobService = sqlJobService;
        this.sparkJobService = sparkJobService;
        this.scriptJobService = scriptJobService;
        this.kylinJobService = kylinJobService;
    }

    @Transactional
    public boolean saveJobReplication(JobReplicationDto jobReplicationDto, boolean fromImport, Operator operator) throws Exception {
        JobInfoDto jobInfoDto = jobReplicationDto.getJobInfoDto();
        Map<EnvEnum, JobConfigCombinationDto> jobConfigCombinationDtoMap = jobReplicationDto.getJobConfigCombinationDtoMap();
        String jobContentJson = jobReplicationDto.getJobContentJson();

        Long newJobId = jobInfoService.addJob(jobInfoDto, operator);
        if (jobConfigCombinationDtoMap != null && jobConfigCombinationDtoMap.size() > 0) {
            jobConfigCombinationDtoMap.forEach((envEnum, jobConfigCombinationDto) -> jobExecuteConfigService.save(newJobId, envEnum.name(), jobConfigCombinationDto, operator));
        }

        if (StringUtils.isNotBlank(jobContentJson)) {
            Integer version = saveJobContent(newJobId, jobInfoDto.getJobType(), jobContentJson, fromImport, operator);
        }
        return true;
    }

    private Integer saveJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) throws Exception {
        Integer version;
        switch (jobType) {
            case DI_BATCH:
            case BACK_FLOW:
                version = saveDIJobContent(jobId, jobType, contentJson, fromImport, operator);
                break;
            case SQL_SPARK:
            case SQL_FLINK:
                version = saveSQLJobContent(jobId, jobType, contentJson, fromImport, operator);
                break;
            case SPARK_JAR:
            case SPARK_PYTHON:
                version = saveSparkJobContent(jobId, jobType, contentJson, fromImport, operator);
                break;
            case SCRIPT_PYTHON:
            case SCRIPT_SHELL:
                version = saveScriptJobContent(jobId, jobType, contentJson, fromImport, operator);
                break;
            case KYLIN:
                version = saveKylinJobContent(jobId, jobType, contentJson, fromImport, operator);
                break;
            default:
                version = null;
                break;
        }
        return version;
    }

    private Integer saveDIJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) {
        DIJobContentContentDto contentDto = new Gson().fromJson(contentJson, DIJobContentContentDto.class);
        contentDto.setId(null);
        contentDto.setJobId(jobId);
        contentDto.setVersion(null);
        contentDto.setEditable(EditableEnum.YES.val);
        if (fromImport) {
            contentDto.setSrcDataSourceId(0L);
            contentDto.setDestDataSourceId(0L);
        }
        DIJobContentContentDto newContentDto = diJobContentService.save(jobId, contentDto, operator);
        return newContentDto.getVersion();
    }

    private Integer saveSQLJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) {
        SqlJobContentDto contentDto = new Gson().fromJson(contentJson, SqlJobContentDto.class);
        contentDto.setId(null);
        contentDto.setJobId(jobId);
        contentDto.setVersion(null);
        contentDto.setEditable(EditableEnum.YES.val);
        if (fromImport) {
            contentDto.setUdfIds(null);
            contentDto.setExternalTables(null);
            contentDto.setExtConfig(null);
        }

        SqlJobContentDto newContentDto = sqlJobService.save(contentDto, operator.getNickname());
        return newContentDto.getVersion();
    }

    private Integer saveSparkJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) throws Exception {
        SparkJobContentDto contentDto = new Gson().fromJson(contentJson, SparkJobContentDto.class);
        contentDto.setId(null);
        contentDto.setJobId(jobId);
        contentDto.setVersion(null);
        contentDto.setEditable(EditableEnum.YES.val);
        if (fromImport) {
            // none
        }
        SparkJobContentDto newContentDto = sparkJobService.save(contentDto, operator.getNickname());
        return newContentDto.getVersion();
    }

    private Integer saveScriptJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) {
        ScriptJobContentDto contentDto = new Gson().fromJson(contentJson, ScriptJobContentDto.class);
        contentDto.setId(null);
        contentDto.setJobId(jobId);
        contentDto.setVersion(null);
        contentDto.setEditable(EditableEnum.YES.val);
        if (fromImport) {
            // none
        }
        ScriptJobContentDto newContentDto = scriptJobService.save(contentDto, operator.getNickname());
        return newContentDto.getVersion();
    }

    private Integer saveKylinJobContent(Long jobId, JobTypeEnum jobType, String contentJson, boolean fromImport, Operator operator) {
        KylinJobDto contentDto = new Gson().fromJson(contentJson, KylinJobDto.class);
        contentDto.setId(null);
        contentDto.setJobId(jobId);
        contentDto.setVersion(null);
        contentDto.setEditable(EditableEnum.YES.val);
        if (fromImport) {
            // none
        }
        KylinJobDto newContentDto = kylinJobService.save(contentDto, operator.getNickname());
        return newContentDto.getVersion();
    }
}
