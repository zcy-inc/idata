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

package cn.zhengcaiyun.idata.develop.dto.opt.stream;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 17:34
 **/
public class StreamJobInstanceDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业name
     */
    private String jobName;

    /**
     * 作业内容id
     */
    private Long jobContentId;

    /**
     * 作业内容版本号
     */
    private Integer jobContentVersion;
    /**
     * 版本号（页面显示）
     */
    private String jobContentVersionDisplay;

    /**
     * 作业类型
     */
    private String jobTypeCode;

    /**
     * 数仓分层
     */
    private String dwLayerCode;

    /**
     * 责任人
     */
    private String owner;

    /**
     * 环境
     */
    private String environment;

    /**
     * 运行实例状态，0：待启动，1：启动中，2：运行中，6：启动失败，7：运行异常，8：已停止，9：已下线
     */
    private Integer status;

    /**
     * 运行开始时间
     */
    private Date runStartTime;

    /**
     * 外部链接
     */
    private String externalUrl;

    /**
     * 运行参数配置
     */
    private StreamJobRunParamDto runParamDto;

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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getJobContentId() {
        return jobContentId;
    }

    public void setJobContentId(Long jobContentId) {
        this.jobContentId = jobContentId;
    }

    public Integer getJobContentVersion() {
        return jobContentVersion;
    }

    public void setJobContentVersion(Integer jobContentVersion) {
        this.jobContentVersion = jobContentVersion;
    }

    public String getJobTypeCode() {
        return jobTypeCode;
    }

    public void setJobTypeCode(String jobTypeCode) {
        this.jobTypeCode = jobTypeCode;
    }

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(Date runStartTime) {
        this.runStartTime = runStartTime;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public StreamJobRunParamDto getRunParamDto() {
        return runParamDto;
    }

    public String getJobContentVersionDisplay() {
        return jobContentVersionDisplay;
    }

    public void setJobContentVersionDisplay(String jobContentVersionDisplay) {
        this.jobContentVersionDisplay = jobContentVersionDisplay;
    }

    public void setRunParamDto(StreamJobRunParamDto runParamDto) {
        this.runParamDto = runParamDto;
    }

    public static StreamJobInstanceDto from(StreamJobInstance instance) {
        StreamJobInstanceDto dto = new StreamJobInstanceDto();
        BeanUtils.copyProperties(instance, dto);

        if (StringUtils.isNotBlank(instance.getRunParams())) {
            StreamJobRunParamDto paramDto = new Gson().fromJson(instance.getRunParams(), StreamJobRunParamDto.class);
            dto.setRunParamDto(paramDto);
        }
        return dto;
    }

    public StreamJobInstance toModel() {
        StreamJobInstance instance = new StreamJobInstance();
        BeanUtils.copyProperties(this, instance);

        if (Objects.nonNull(this.runParamDto)) {
            instance.setRunParams(new Gson().toJson(this.runParamDto));
        }
        return instance;
    }
}
