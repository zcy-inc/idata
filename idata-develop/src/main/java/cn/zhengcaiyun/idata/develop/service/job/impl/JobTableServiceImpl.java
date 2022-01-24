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

package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobTableDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.manager.TableQueryManager;
import cn.zhengcaiyun.idata.develop.service.job.JobTableService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-30 13:44
 **/
@Service
public class JobTableServiceImpl implements JobTableService {

    private final DataSourceApi dataSourceApi;
    private final TableQueryManager tableQueryManager;

    @Autowired
    public JobTableServiceImpl(DataSourceApi dataSourceApi,
                               TableQueryManager tableQueryManager) {
        this.dataSourceApi = dataSourceApi;
        this.tableQueryManager = tableQueryManager;
    }

    @Override
    public List<JobTableDto> getTable(DataSourceTypeEnum dataSourceType, Long dataSourceId) {
        DataSourceDto dataSourceDto = dataSourceApi.getDataSource(dataSourceId);
        Optional<DbConfigDto> configDtoOptional = getDbConfigDto(dataSourceDto);
        checkArgument(configDtoOptional.isPresent(), "数据库配置为空");

        List<String> tableNames = tableQueryManager.getTableNames(dataSourceDto.getType(), configDtoOptional.get());
        if (ObjectUtils.isEmpty(tableNames)) return Lists.newArrayList();

        return tableNames.stream().map(name -> {
            JobTableDto tableDto = new JobTableDto();
            tableDto.setTableName(name);
            return tableDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MappingColumnDto> getTableColumn(DataSourceTypeEnum dataSourceType, Long dataSourceId, String tableName) {
        DataSourceDto dataSourceDto = dataSourceApi.getDataSource(dataSourceId);
        Optional<DbConfigDto> configDtoOptional = getDbConfigDto(dataSourceDto);
        checkArgument(configDtoOptional.isPresent(), "数据库配置为空");
        checkArgument(StringUtils.isNotBlank(tableName), "表名为空");

        List<ColumnInfoDto> colList = tableQueryManager.getTableColumns(dataSourceDto.getType(), configDtoOptional.get(), tableName);
        List<ColumnInfoDto> pkColList = tableQueryManager.getTablePrimaryKeys(dataSourceDto.getType(), configDtoOptional.get(), tableName);
        return assemble(colList, pkColList);
    }

    private List<MappingColumnDto> assemble(List<ColumnInfoDto> colList, List<ColumnInfoDto> pkColList) {
        if (ObjectUtils.isEmpty(colList)) return Lists.newArrayList();

        Set<String> pkSet;
        if (ObjectUtils.isNotEmpty(pkColList)) {
            pkSet = pkColList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toSet());
        } else {
            pkSet = Sets.newHashSet();
        }

        return colList.stream().map(columnInfoDto -> {
            MappingColumnDto columnDto = new MappingColumnDto();
            columnDto.setName(columnInfoDto.getColumnName());
            columnDto.setDataType(columnInfoDto.getColumnType());
            columnDto.setPrimaryKey(pkSet.contains(columnInfoDto.getColumnName()));
            return columnDto;
        }).collect(Collectors.toList());
    }

    private Optional<DbConfigDto> getDbConfigDto(DataSourceDto dataSourceDto) {
        List<DbConfigDto> dbConfigDtoList = dataSourceDto.getDbConfigList();
        if (ObjectUtils.isEmpty(dbConfigDtoList)) return Optional.empty();

        DbConfigDto configDto = null;
        // 优先使用真线数据源
        for (DbConfigDto cfgDto : dbConfigDtoList) {
            if (cfgDto.getEnv() == EnvEnum.prod) {
                configDto = cfgDto;
            } else {
                if (Objects.isNull(configDto)) {
                    configDto = cfgDto;
                }
            }
        }
        return Optional.ofNullable(configDto);
    }

}
