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

import java.util.Date;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-04 14:42
 **/
public class JobAnotherHistoryDto {

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 环境
     */
    private String environment;

    /**
     * DS任务实例 id
     */
    private int taskInstanceId;

    /**
     * DS任务实例 name
     */
    private String taskInstanceName;

    /**
     * DS任务 code
     */
    private long taskCode;

    /**
     * DS工作流实例 id
     */
    private int processInstanceId;

    /**
     * DS工作流实例 name
     */
    private String processInstanceName;

    /**
     * 任务实例运行 state
     */
    private String state;

    /**
     * 任务实例提交 time
     */
    private Date submitTime;

    /**
     * 任务实例开始 time
     */
    private Date startTime;

    /**
     * 任务实例结束 time
     */
    private Date endTime;

    /**
     * 任务实例运行 host
     */
    private String host;

    /**
     * 任务实例 retry times
     */
    private int retryTimes;

    /**
     * 任务实例运行 duration
     */
    private String duration;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public int getTaskInstanceId() {
        return taskInstanceId;
    }

    public void setTaskInstanceId(int taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    public String getTaskInstanceName() {
        return taskInstanceName;
    }

    public void setTaskInstanceName(String taskInstanceName) {
        this.taskInstanceName = taskInstanceName;
    }

    public long getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(long taskCode) {
        this.taskCode = taskCode;
    }

    public int getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(int processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
