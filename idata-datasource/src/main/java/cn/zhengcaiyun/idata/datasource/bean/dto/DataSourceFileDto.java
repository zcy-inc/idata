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
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSourceFile;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:32
 **/
public class DataSourceFileDto extends BaseDto {
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
     * 文件名称
     */
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static DataSourceFileDto from(DataSourceFile file) {
        DataSourceFileDto dto = new DataSourceFileDto();
        BeanUtils.copyProperties(file, dto);

        if (StringUtils.isNotBlank(file.getEnvironments())) {
            String[] envArray = file.getEnvironments().split(",");
            dto.setEnvList(Arrays.stream(envArray)
                    .map(EnvEnum::getEnum)
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toList()));
        }
        return dto;
    }

    public DataSourceFile toModel() {
        DataSourceFile file = new DataSourceFile();
        BeanUtils.copyProperties(this, file);
        if (ObjectUtils.isNotEmpty(envList)) {
            file.setEnvironments(envList.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(",")));
        }
        return file;
    }

}
