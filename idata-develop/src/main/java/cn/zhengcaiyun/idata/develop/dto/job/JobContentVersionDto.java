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

import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import org.jetbrains.annotations.NotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-24 15:24
 **/
public class JobContentVersionDto implements Comparable<JobContentVersionDto> {
    /**
     * 作业id
     */
    private Long jobId;
    /**
     * 作业内容版本号
     */
    private Integer version;

    /**
     * 作业内容版本号（显示）
     */
    private String versionDisplay;
    /**
     * 版本状态，1：待发布，2：已发布，4：已驳回，9：已归档，0：编辑中
     */
    private Integer versionStatus;
    /**
     * 环境，编辑中的没有环境信息
     */
    private String environment;

    /**
     * 环境下的运行状态，0：暂停，1：恢复，当版本状态为发布，运行状态为暂停时，需要和版本一起显示
     */
    private Integer envRunningState;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getVersionDisplay() {
        return versionDisplay;
    }

    public void setVersionDisplay(String versionDisplay) {
        this.versionDisplay = versionDisplay;
    }

    public Integer getEnvRunningState() {
        return envRunningState;
    }

    public void setEnvRunningState(Integer envRunningState) {
        this.envRunningState = envRunningState;
    }

    public static JobContentVersionDto from(JobContentBaseDto content) {
        JobContentVersionDto versionDto = new JobContentVersionDto();
        versionDto.setJobId(content.getJobId());
        versionDto.setVersion(content.getVersion());
        versionDto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
        versionDto.setVersionStatus(0);
        versionDto.setEnvironment("");
        return versionDto;
    }

    @Override
    public int compareTo(@NotNull JobContentVersionDto o) {
        int ret = 0;
        if (this.versionStatus == 0 && o.getVersionStatus() != 0)
            ret = -1;
        if (this.versionStatus != 0 && o.getVersionStatus() == 0)
            ret = 1;

        if (ret == 0) {
            ret = Integer.compare(o.getVersion(), this.version);
        }
        if (ret == 0) {
            ret = o.getEnvironment().compareTo(this.environment);
        }
        return ret;
    }
}
