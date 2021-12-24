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
package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.di.DiJobDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobDetailsDto;

/**
 * @author caizhedong
 * @date 2021-11-29 上午9:16
 */

public class JobDetailsDto {
    private JobTypeEnum jobType;
    private String driverMemory;
    private String executorMemory;
    private int executorCores;
    private String yarnQueue;
    // JobTypeEnum.engine
    private String engineType;

    private DiJobDetailsDto diJobDetails;
    private SqlJobDetailsDto sqlJobDetails;
    private SparkJobDetailsDto sparkJobDetails;
    private KylinJobDto kylinJobDetails;
    private ScriptJobContentDto scriptJobDetails;

    // GaS
    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public int getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(int executorCores) {
        this.executorCores = executorCores;
    }

    public String getYarnQueue() {
        return yarnQueue;
    }

    public void setYarnQueue(String yarnQueue) {
        this.yarnQueue = yarnQueue;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public DiJobDetailsDto getDiJobDetails() {
        return diJobDetails;
    }

    public void setDiJobDetails(DiJobDetailsDto diJobDetails) {
        this.diJobDetails = diJobDetails;
    }

    public SqlJobDetailsDto getSqlJobDetails() {
        return sqlJobDetails;
    }

    public void setSqlJobDetails(SqlJobDetailsDto sqlJobDetails) {
        this.sqlJobDetails = sqlJobDetails;
    }

    public SparkJobDetailsDto getSparkJobDetails() {
        return sparkJobDetails;
    }

    public void setSparkJobDetails(SparkJobDetailsDto sparkJobDetails) {
        this.sparkJobDetails = sparkJobDetails;
    }

    public KylinJobDto getKylinJobDetails() {
        return kylinJobDetails;
    }

    public void setKylinJobDetails(KylinJobDto kylinJobDetails) {
        this.kylinJobDetails = kylinJobDetails;
    }

    public ScriptJobContentDto getScriptJobDetails() {
        return scriptJobDetails;
    }

    public void setScriptJobDetails(ScriptJobContentDto scriptJobDetails) {
        this.scriptJobDetails = scriptJobDetails;
    }
}
