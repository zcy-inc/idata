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

package cn.zhengcaiyun.idata.develop.condition.opt.stream;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 16:45
 **/
public class StreamJobInstanceCondition {

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业name，支持模糊搜索
     */
    private String jobNamePattern;

    /**
     * 作业内容版本号
     */
    private Integer jobContentVersion;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 责任人
     */
    private String owner;

    /**
     * 环境
     */
    private String environment;

    /**
     * 运行实例状态，0：待启动，1：启动中，2：运行中，7：已失败，8：已停止，9：已下线
     */
    private List<Integer> statusList;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobNamePattern() {
        return jobNamePattern;
    }

    public void setJobNamePattern(String jobNamePattern) {
        this.jobNamePattern = jobNamePattern;
    }

    public Integer getJobContentVersion() {
        return jobContentVersion;
    }

    public void setJobContentVersion(Integer jobContentVersion) {
        this.jobContentVersion = jobContentVersion;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }
}
