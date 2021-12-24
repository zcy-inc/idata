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
package cn.zhengcaiyun.idata.develop.dto.job.sql;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.dto.dqc.DataQueryDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-11-29 下午9:40
 */

public class SqlJobDetailsDto extends SqlJobContentDto {
    private String targetTableName;
    private String saveMode;
    private List<DevJobUdf> udfs;
    private String udfName;
    private String resourceHdfsPath;
    private String udfType;
    private String returnType;
    private List<DataQueryDto> dataQueries;

    // GaS
    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public List<DevJobUdf> getUdfs() {
        return udfs;
    }

    public void setUdfs(List<DevJobUdf> udfs) {
        this.udfs = udfs;
    }

    public String getUdfName() {
        return udfName;
    }

    public void setUdfName(String udfName) {
        this.udfName = udfName;
    }

    public String getResourceHdfsPath() {
        return resourceHdfsPath;
    }

    public void setResourceHdfsPath(String resourceHdfsPath) {
        this.resourceHdfsPath = resourceHdfsPath;
    }

    public String getUdfType() {
        return udfType;
    }

    public void setUdfType(String udfType) {
        this.udfType = udfType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<DataQueryDto> getDataQueries() {
        return dataQueries;
    }

    public void setDataQueries(List<DataQueryDto> dataQueries) {
        this.dataQueries = dataQueries;
    }
}
