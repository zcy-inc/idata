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
package cn.zhengcaiyun.idata.develop.dto.dqc;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author caizhedong
 * @date 2021-12-23 下午9:26
 */

public class DataQueryDto {
    private Long alarmId;
    private Long tableId;
    private String monitorType;
    @ApiModelProperty(value = "TABLE | COLUMN")
    private String monitorGranularity;
    private String monitorSql;
    private String pkColumnName;
    private String columnName;
    @ApiModelProperty(value = "ACCURACY | INTEGRITY | TIMELINESS")
    private String metricType;
    @ApiModelProperty(value = "WHOLE | SET | BOOLEAN | TIME | DATETIME | ARRAY | DECIMAL | STRING")
    private String outputType;
    private String output;

    // GaS
    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorGranularity() {
        return monitorGranularity;
    }

    public void setMonitorGranularity(String monitorGranularity) {
        this.monitorGranularity = monitorGranularity;
    }

    public String getMonitorSql() {
        return monitorSql;
    }

    public void setMonitorSql(String monitorSql) {
        this.monitorSql = monitorSql;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
