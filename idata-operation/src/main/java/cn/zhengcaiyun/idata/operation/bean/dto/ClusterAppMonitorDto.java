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

package cn.zhengcaiyun.idata.operation.bean.dto;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 17:39
 **/
public class ClusterAppMonitorDto {

    /**
     * 集群应用 id
     */
    private String appId;

    /**
     * 集群应用 name
     */
    private String appName;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 集群应用 user
     */
    private String user;

    /**
     * 集群应用队列
     */
    private String queue;

    /**
     * 集群应用状态: NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED
     */
    private String state;

    /**
     * 集群应用运行进度
     */
    private Float progress;

    /**
     * 集群应用类型
     */
    private String applicationType;

    /**
     * 集群应用开始时间
     */
    private LocalDateTime startedTime;

    /**
     * 集群应用总分配内存，单位：GB
     */
    private Integer allocatedMem;

    /**
     * 集群应用总分配核数
     */
    private Integer allocatedVCores;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public LocalDateTime getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(LocalDateTime startedTime) {
        this.startedTime = startedTime;
    }

    public Integer getAllocatedMem() {
        return allocatedMem;
    }

    public void setAllocatedMem(Integer allocatedMem) {
        this.allocatedMem = allocatedMem;
    }

    public Integer getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(Integer allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }
}
