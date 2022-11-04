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

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-22 上午10:15
 */

public class SqlJobContentDto extends JobContentBaseDto {
    private String sourceSql;
    private String udfIds;
    /**
     * 外部表配置
     */
    private List<SqlJobExternalTableDto> extTables;
    private SqlJobExtendConfigDto extConfig;

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

    public List<SqlJobExternalTableDto> getExtTables() {
        return extTables;
    }

    public void setExtTables(List<SqlJobExternalTableDto> extTables) {
        this.extTables = extTables;
    }

    public SqlJobExtendConfigDto getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(SqlJobExtendConfigDto extConfig) {
        this.extConfig = extConfig;
    }

    public DevJobContentSql toModel() {
        DevJobContentSql contentSql = new DevJobContentSql();
        BeanUtils.copyProperties(this, contentSql);
        if (!Objects.isNull(this.extConfig)) {
            contentSql.setExtendConfigs(new Gson().toJson(this.extConfig));
        } else {
            contentSql.setExtendConfigs("");
        }
        if (Objects.nonNull(this.extTables)) {
            contentSql.setExternalTables(JSON.toJSONString(this.extTables));
        } else {
            contentSql.setExternalTables("");
        }
        return contentSql;
    }

    public static SqlJobContentDto from(DevJobContentSql contentSql) {
        SqlJobContentDto contentDto = new SqlJobContentDto();
        BeanUtils.copyProperties(contentSql, contentDto);
        if (StringUtils.isNotBlank(contentSql.getExtendConfigs())) {
            contentDto.setExtConfig(new Gson().fromJson(contentSql.getExtendConfigs(), SqlJobExtendConfigDto.class));
        }
        if (StringUtils.isNotBlank(contentSql.getExternalTables())) {
            contentDto.setExtTables(JSON.parseArray(contentSql.getExternalTables(), SqlJobExternalTableDto.class));
        }
        return contentDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SqlJobContentDto that = (SqlJobContentDto) o;
        return Objects.equals(sourceSql, that.sourceSql) && Objects.equals(udfIds, that.udfIds) && Objects.equals(extTables, that.extTables) && Objects.equals(extConfig, that.extConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sourceSql, udfIds, extTables, extConfig);
    }
}
