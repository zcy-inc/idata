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
package cn.zhengcaiyun.idata.develop.dto.job.spark;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.JobArgumentDto;

import java.util.List;
import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-19 下午3:27
 */

public class SparkJobDto extends BaseDto {
    private Long id;
    private Long jobId;
    private Integer editable;
    private Integer version;
    private JobTypeEnum jobType;
    private String resourceHdfsPath;
    private List<JobArgumentDto> appArguments;
    private String mainClass;
    private String pythonResource;

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

    public String getResourceHdfsPath() {
        return resourceHdfsPath;
    }

    public void setResourceHdfsPath(String resourceHdfsPath) {
        this.resourceHdfsPath = resourceHdfsPath;
    }

    public List<JobArgumentDto> getAppArguments() {
        return appArguments;
    }

    public void setAppArguments(List<JobArgumentDto> appArguments) {
        this.appArguments = appArguments;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getPythonResource() {
        return pythonResource;
    }

    public void setPythonResource(String pythonResource) {
        this.pythonResource = pythonResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SparkJobDto that = (SparkJobDto) o;
        return Objects.equals(resourceHdfsPath, that.resourceHdfsPath) &&
                Objects.equals(appArguments, that.appArguments) &&
                Objects.equals(mainClass, that.mainClass)
                && Objects.equals(pythonResource, that.pythonResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resourceHdfsPath, appArguments, mainClass, pythonResource);
    }
}
