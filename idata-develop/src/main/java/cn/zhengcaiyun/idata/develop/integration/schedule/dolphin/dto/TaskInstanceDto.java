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

package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto;

import java.util.Date;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-04 13:59
 **/
public class TaskInstanceDto {

    /**
     * DS任务实例 id
     */
    private int id;

    /**
     * DS任务实例 name
     */
    private String name;

    /**
     * DS工作流实例 id
     */
    private int processInstanceId;

    /**
     * DS任务 code
     */
    private long taskCode;

    /**
     * DS工作流实例 name
     */
    private String processInstanceName;

    /**
     * DS任务实例运行 state
     */
    private String state;

    /**
     * DS任务实例提交 time
     */
    private Date submitTime;

    /**
     * DS任务实例开始 time
     */
    private Date startTime;

    /**
     * DS任务实例结束 time
     */
    private Date endTime;

    /**
     * DS任务实例运行 host
     */
    private String host;

    /**
     * DS任务实例 retry times
     */
    private int retryTimes;

    /**
     * DS任务实例运行 duration
     */
    private String duration;

    /**
     * DS任务实例 max retry times
     */
    private int maxRetryTimes;

    /**
     * DS任务实例 task retry interval, unit: minute
     */
    private int retryInterval;

    /**
     * DS任务实例 delay execution time.
     */
    private int delayTime;

    /**
     * DS任务实例 task params
     */
    private String taskParams;

    /**
     * DS任务实例 dry run flag
     */
    private int dryRun;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(int processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public long getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(long taskCode) {
        this.taskCode = taskCode;
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

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public String getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(String taskParams) {
        this.taskParams = taskParams;
    }

    public int getDryRun() {
        return dryRun;
    }

    public void setDryRun(int dryRun) {
        this.dryRun = dryRun;
    }
}
