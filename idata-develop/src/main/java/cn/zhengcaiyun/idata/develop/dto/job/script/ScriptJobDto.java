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
package cn.zhengcaiyun.idata.develop.dto.job.script;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.JobArgumentDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-11-19 下午3:19
 */

public class ScriptJobDto extends BaseDto {
    private Long id;
    private Long jobId;
    private Integer editable;
    private Integer version;
    private JobTypeEnum jobType;
    private String sourceResource;
    private List<JobArgumentDto> scriptArguments;

    // GaS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String getSourceResource() {
        return sourceResource;
    }

    public void setSourceResource(String sourceResource) {
        this.sourceResource = sourceResource;
    }

    public List<JobArgumentDto> getScriptArguments() {
        return scriptArguments;
    }

    public void setScriptArguments(List<JobArgumentDto> scriptArguments) {
        this.scriptArguments = scriptArguments;
    }
}
