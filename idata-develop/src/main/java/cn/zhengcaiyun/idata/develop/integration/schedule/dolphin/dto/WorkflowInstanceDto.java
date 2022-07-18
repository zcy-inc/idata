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
 * @create: 2022-07-18 14:50
 **/
public class WorkflowInstanceDto {
    /**
     * id
     */
    private int id;

    /**
     * process definition code
     */
    private Long processDefinitionCode;

    /**
     * process definition version
     */
    private int processDefinitionVersion;

    /**
     * process state
     */
    private String state;

    /**
     * start time
     */
    private Date startTime;

    /**
     * end time
     */
    private Date endTime;

    /**
     * name
     */
    private String name;

    /**
     * process duration
     *
     * @return
     */
    private String duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getProcessDefinitionCode() {
        return processDefinitionCode;
    }

    public void setProcessDefinitionCode(Long processDefinitionCode) {
        this.processDefinitionCode = processDefinitionCode;
    }

    public int getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(int processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
