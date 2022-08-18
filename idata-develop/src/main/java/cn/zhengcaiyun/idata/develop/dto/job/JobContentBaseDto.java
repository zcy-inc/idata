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

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;

/**
 * @author caizhedong
 * @date 2021-11-26 上午10:13
 */

public class JobContentBaseDto extends BaseDto {
    /**
     * 作业内容id
     */
    private Long id;
    /**
     * 作业id
     */
    private Long jobId;
    /**
     * 是否可编辑
     */
    private Integer editable;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 版本号-格式化
     */
    private String versionDisplay;
    /**
     * 作业类型
     */
    private JobTypeEnum jobType;

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

    public String getVersionDisplay() {
        return versionDisplay;
    }

    public void setVersionDisplay(String versionDisplay) {
        this.versionDisplay = versionDisplay;
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }
}
