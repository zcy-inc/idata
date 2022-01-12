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

package cn.zhengcaiyun.idata.merge.data.manager;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 10:27
 **/
@Component
public class JobMigrateManager {

    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobPublishRecordService jobPublishRecordService;
    @Autowired
    private DIJobContentService diJobContentService;
    @Autowired
    private JobExecuteConfigService jobExecuteConfigService;
    @Autowired
    private JobContentCommonService jobContentCommonService;
    @Autowired
    private JobPublishRecordRepo jobPublishRecordRepo;


    @Transactional
    public void migrateJob(JobInfoDto jobInfoDto, JobConfigCombinationDto configCombinationDto,
                           Operator jobOperator, JobMigrationDto migrationDto) {
        try {
            Long newJobId = jobInfoService.addJob(jobInfoDto, jobOperator);
            JobConfigCombinationDto combinationDto = jobExecuteConfigService.save(newJobId, EnvEnum.prod.name(), configCombinationDto, jobOperator);
            Integer contentVersion = migrateContentInfo(newJobId, jobInfoDto, migrationDto);

            jobContentCommonService.submit(newJobId, contentVersion, EnvEnum.prod.name(), "迁移自动提交", contentOperator);
            Optional<JobPublishRecord> publishRecordOptional = jobPublishRecordRepo.query(newJobId, contentVersion, EnvEnum.prod.name());
            if (publishRecordOptional.isPresent() && publishRecordOptional.get().getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val)) {
                jobPublishRecordService.approve(publishRecordOptional.get().getId(), "迁移自动发布", contentOperator);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有操作权限");
        }
    }

    private Integer migrateContentInfo(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        Integer contentVersion;
        switch (jobInfoDto.getJobType()) {
            case DI_BATCH:
                contentVersion = migrateDIContent(newJobId, jobInfoDto, migrationDto);
            case SQL_SPARK:
                // todo 迁移作业内容
            case SPARK_PYTHON:
                // todo 迁移作业内容
            case SPARK_JAR:
                // todo 迁移作业内容
            case SCRIPT_PYTHON:
                // todo 迁移作业内容
            case SCRIPT_SHELL:
                // todo 迁移作业内容
            case KYLIN:
                // todo 迁移作业内容
            default:
                contentVersion = null;
        }
        return contentVersion;
    }

    private Integer migrateDIContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }

    private Integer migrateSQLContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }

    private Integer migrateBackFlowContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }

    private Integer migrateKylinContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }

    private Integer migrateSparkContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }

    private Integer migrateScriptContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {

    }
}
