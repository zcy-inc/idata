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

package cn.zhengcaiyun.idata.datasource.bean.dto;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.constant.enums.DataSourceTypeEnum;

import java.util.List;

/**
 * @description: 数据源
 * @author: yangjianhua
 * @create: 2021-09-15 16:07
 **/
public class DataSourceDto extends BaseDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 数据源类型
     */
    private DataSourceTypeEnum type;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 环境，多个环境用,号分隔
     */
    private List<EnvEnum> envList;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据库配置
     */
    private List<DbConfigDto> dbConfigList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataSourceTypeEnum getType() {
        return type;
    }

    public void setType(DataSourceTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnvEnum> getEnvList() {
        return envList;
    }

    public void setEnvList(List<EnvEnum> envList) {
        this.envList = envList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DbConfigDto> getDbConfigList() {
        return dbConfigList;
    }

    public void setDbConfigList(List<DbConfigDto> dbConfigList) {
        this.dbConfigList = dbConfigList;
    }
}
