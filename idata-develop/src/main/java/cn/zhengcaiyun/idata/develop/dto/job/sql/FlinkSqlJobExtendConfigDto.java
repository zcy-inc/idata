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

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-03-23 14:14
 **/
public class FlinkSqlJobExtendConfigDto {

    private List<FlinkDataSourceConfigDto> flinkSourceConfigs;
    private List<FlinkDataSourceConfigDto> flinkSinkConfigs;

    public List<FlinkDataSourceConfigDto> getFlinkSourceConfigs() {
        return flinkSourceConfigs;
    }

    public void setFlinkSourceConfigs(List<FlinkDataSourceConfigDto> flinkSourceConfigs) {
        this.flinkSourceConfigs = flinkSourceConfigs;
    }

    public List<FlinkDataSourceConfigDto> getFlinkSinkConfigs() {
        return flinkSinkConfigs;
    }

    public void setFlinkSinkConfigs(List<FlinkDataSourceConfigDto> flinkSinkConfigs) {
        this.flinkSinkConfigs = flinkSinkConfigs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlinkSqlJobExtendConfigDto that = (FlinkSqlJobExtendConfigDto) o;
        return Objects.equals(flinkSourceConfigs, that.flinkSourceConfigs) && Objects.equals(flinkSinkConfigs, that.flinkSinkConfigs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flinkSourceConfigs, flinkSinkConfigs);
    }

    public static class FlinkDataSourceConfigDto {
        private String dataSourceType;
        private Long dataSourceId;
        private String dataSourceUDCode;

        public String getDataSourceType() {
            return dataSourceType;
        }

        public void setDataSourceType(String dataSourceType) {
            this.dataSourceType = dataSourceType;
        }

        public Long getDataSourceId() {
            return dataSourceId;
        }

        public void setDataSourceId(Long dataSourceId) {
            this.dataSourceId = dataSourceId;
        }

        public String getDataSourceUDCode() {
            return dataSourceUDCode;
        }

        public void setDataSourceUDCode(String dataSourceUDCode) {
            this.dataSourceUDCode = dataSourceUDCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FlinkDataSourceConfigDto that = (FlinkDataSourceConfigDto) o;
            return Objects.equals(dataSourceType, that.dataSourceType) && Objects.equals(dataSourceId, that.dataSourceId) && Objects.equals(dataSourceUDCode, that.dataSourceUDCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dataSourceType, dataSourceId, dataSourceUDCode);
        }
    }
}
