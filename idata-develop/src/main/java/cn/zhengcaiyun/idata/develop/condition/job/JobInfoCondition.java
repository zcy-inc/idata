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

package cn.zhengcaiyun.idata.develop.condition.job;

import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 16:53
 **/
public class JobInfoCondition {
    /**
     * 作业名称
     */
    private String name;
    /**
     * 作业类型
     */
    private JobTypeEnum jobType;
    /**
     * 数仓分层
     */
    private String dwLayerCode;
    /**
     * 状态，1启用，0停用
     */
    private Integer status;
    /**
     * 文件夹id
     */
    private Long folderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}
