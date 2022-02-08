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
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import org.springframework.beans.BeanUtils;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-18 14:35
 **/
public class JobDependenceDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 环境
     */
    private String environment;

    /**
     * 上游作业id
     */
    private Long prevJobId;

    /**
     * 上游作业名称
     */
    private String prevJobName;

    /**
     * 上游作业所属dag id
     */
    private Long prevJobDagId;

    /**
     * 上游作业所属dag 名称
     */
    private String prevJobDagName;


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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Long getPrevJobId() {
        return prevJobId;
    }

    public void setPrevJobId(Long prevJobId) {
        this.prevJobId = prevJobId;
    }

    public Long getPrevJobDagId() {
        return prevJobDagId;
    }

    public void setPrevJobDagId(Long prevJobDagId) {
        this.prevJobDagId = prevJobDagId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getPrevJobName() {
        return prevJobName;
    }

    public void setPrevJobName(String prevJobName) {
        this.prevJobName = prevJobName;
    }

    public String getPrevJobDagName() {
        return prevJobDagName;
    }

    public void setPrevJobDagName(String prevJobDagName) {
        this.prevJobDagName = prevJobDagName;
    }

    public static JobDependenceDto from(JobDependence model) {
        JobDependenceDto dto = new JobDependenceDto();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }

    public JobDependence toModel() {
        JobDependence model = new JobDependence();
        BeanUtils.copyProperties(this, model);
        return model;
    }
}
