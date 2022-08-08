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

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;

import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-05 13:54
 **/
public class JobReplicationDto {

    private JobInfoDto jobInfoDto;
    private Map<EnvEnum, JobConfigCombinationDto> jobConfigCombinationDtoMap;
    private String jobContentJson;

    public JobInfoDto getJobInfoDto() {
        return jobInfoDto;
    }

    public void setJobInfoDto(JobInfoDto jobInfoDto) {
        this.jobInfoDto = jobInfoDto;
    }

    public Map<EnvEnum, JobConfigCombinationDto> getJobConfigCombinationDtoMap() {
        return jobConfigCombinationDtoMap;
    }

    public void setJobConfigCombinationDtoMap(Map<EnvEnum, JobConfigCombinationDto> jobConfigCombinationDtoMap) {
        this.jobConfigCombinationDtoMap = jobConfigCombinationDtoMap;
    }

    public String getJobContentJson() {
        return jobContentJson;
    }

    public void setJobContentJson(String jobContentJson) {
        this.jobContentJson = jobContentJson;
    }
}
