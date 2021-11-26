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

import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;

import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-22 上午10:15
 */

public class SqlJobContentDto extends JobContentBaseDto {
    private String sourceSql;
    private String udfIds;
    private String externalTables;

    // GaS
    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public String getUdfIds() {
        return udfIds;
    }

    public void setUdfIds(String udfIds) {
        this.udfIds = udfIds;
    }

    public String getExternalTables() {
        return externalTables;
    }

    public void setExternalTables(String externalTables) {
        this.externalTables = externalTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SqlJobContentDto sqlJobDto = (SqlJobContentDto) o;
        return Objects.equals(sourceSql, sqlJobDto.sourceSql) &&
                Objects.equals(udfIds, sqlJobDto.udfIds) &&
                Objects.equals(externalTables, sqlJobDto.externalTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sourceSql, udfIds, externalTables);
    }
}
