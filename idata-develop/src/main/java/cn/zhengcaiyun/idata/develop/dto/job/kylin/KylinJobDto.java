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
package cn.zhengcaiyun.idata.develop.dto.job.kylin;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;

import java.util.Date;
import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-18 下午8:05
 */
public class KylinJobDto extends JobContentBaseDto {
    private String cubeName;
    private String buildType;
    private Date startTime;
    private Date endTime;

    // GaS
    public String getCubeName() {
        return cubeName;
    }

    public void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KylinJobDto kylinJobDto = (KylinJobDto) o;
        return Objects.equals(cubeName, kylinJobDto.cubeName) && Objects.equals(buildType, kylinJobDto.buildType)
                && Objects.equals(startTime, kylinJobDto.startTime) && Objects.equals(endTime, kylinJobDto.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cubeName, buildType, startTime, endTime);
    }
}
