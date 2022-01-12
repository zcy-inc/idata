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
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 10:27
 **/
@Component
public class JobMigrateManager {

    @Transactional
    public void migrateJob(JobInfoDto jobInfoDto, JobConfigCombinationDto configCombinationDto,
                           Operator jobOperator, JobMigrationDto migrationDto){

    }

    void migrateBaseInfo() {

    }

    void migrateConfigInfo(){

    }

    void migrateContentInfo() {

    }

    void migratePublishInfo() {

    }

    void migrateDIContent() {

    }

    void migrateSQLContent() {

    }

    void migrateBackFlowContent() {

    }

    void migrateKylinContent() {

    }

    void migrateSparkContent() {

    }

    void migrateScriptContent() {

    }
}
