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

package cn.zhengcaiyun.idata.datasource.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import cn.zhengcaiyun.idata.datasource.manager.DataSourceManager;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-15 16:48
 **/
@Service
public class DataSourceServiceImpl implements DataSourceService {

    private final DataSourceRepo dataSourceRepo;
    private final DataSourceManager dataSourceManager;

    @Autowired
    public DataSourceServiceImpl(DataSourceRepo dataSourceRepo,
                                 DataSourceManager dataSourceManager) {
        this.dataSourceRepo = dataSourceRepo;
        this.dataSourceManager = dataSourceManager;
    }

    @Override
    public Page<DataSourceDto> pagingDataSource(DataSourceCondition condition, PageParam pageParam) {
        Page<DataSource> page = dataSourceRepo.pagingDataSource(condition, pageParam);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }
        return Page.newOne(
                page.getContent().stream()
                        .map(DataSourceDto::from)
                        .collect(Collectors.toList()),
                page.getTotal());
    }

    @Override
    public DataSourceDto getDataSource(Long id) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");
        return DataSourceDto.from(source);
    }

    @Override
    public DataSourceDto addDataSource(DataSourceDto dto, Operator operator) {
        checkDataSource(dto);
        List<DataSource> dupNameDataSources = dataSourceRepo.queryDataSource(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameDataSources), "数据源名称已存在");

        dto.setDel(DeleteEnum.DEL_NO.val);
        dto.setOperator(operator);
        Long id = dataSourceRepo.createDataSource(dto.toModel());
        checkState(nonNull(id), "创建数据源失败");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        if (optional.isEmpty()) return null;
        return DataSourceDto.from(optional.get());
    }

    @Override
    public DataSourceDto editDataSource(DataSourceDto dto, Operator operator) {
        checkArgument(nonNull(dto.getId()), "数据源编号为空");
        checkDataSource(dto);
        List<DataSource> dupNameDataSources = dataSourceRepo.queryDataSource(dto.getName());
        if (ObjectUtils.isNotEmpty(dupNameDataSources)) {
            DataSource dupNameDataSource = dupNameDataSources.get(0);
            checkArgument(Objects.equals(dto.getId(), dupNameDataSource.getId()), "数据源名称已存在");
        }

        Optional<DataSource> optional = dataSourceRepo.queryDataSource(dto.getId());
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");

        dto.setEditor(operator.getNickname());
        boolean ret = dataSourceRepo.updateDataSource(dto.toModel());
        checkState(ret, "更新数据源失败");
        Optional<DataSource> newOptional = dataSourceRepo.queryDataSource(dto.getId());
        if (newOptional.isEmpty()) return null;
        return DataSourceDto.from(newOptional.get());
    }

    @Override
    public Boolean removeDataSource(Long id, Operator operator) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        if (Objects.equals(DeleteEnum.DEL_YES.val, source.getDel())) return true;

        boolean isUsing = dataSourceManager.checkInUsing(DataSourceTypeEnum.getEnum(source.getType()).orElse(null),
                id);
        checkArgument(!isUsing, "数据源正在被使用，不能删除");
        boolean ret = dataSourceRepo.deleteDataSource(id, operator.getNickname());
        checkState(ret, "删除数据源失败");
        return true;
    }

    @Override
    public Boolean testConnection(DataSourceTypeEnum dataSourceType, DbConfigDto dto) {
        checkArgument(nonNull(dataSourceType), "数据源类型为空");
        if (!supportTestConnection(dataSourceType)) return false;

        checkArgument(nonNull(dto.getEnv()), "数据库所属环境为空");
        checkArgument(StringUtils.isNotBlank(dto.getHost()), "数据库地址为空");
        checkArgument(nonNull(dto.getPort()), "数据库端口为空");
        checkArgument(StringUtils.isNotBlank(dto.getDbName()), "数据库名称为空");

        if (DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType
                || DataSourceTypeEnum.presto == dataSourceType) {
            checkArgument(StringUtils.isNotBlank(dto.getUsername()), "数据库用户名为空");
        }
        if (DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType) {
            checkArgument(StringUtils.isNotBlank(dto.getPassword()), "数据库密码为空");
        }
        return dataSourceManager.testConnectionWithJDBC(dataSourceType, dto);
    }

    private boolean supportTestConnection(DataSourceTypeEnum dataSourceType) {
        return DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType
                || DataSourceTypeEnum.presto == dataSourceType
                || DataSourceTypeEnum.hive == dataSourceType;
    }

    private void checkDataSource(DataSourceDto dto) {
        checkArgument(nonNull(dto.getType()), "数据源类型为空");
        checkArgument(StringUtils.isNotBlank(dto.getName()), "数据源名称为空");
        checkArgument(ObjectUtils.isNotEmpty(dto.getEnvList()), "数据源环境为空");
        checkArgument(ObjectUtils.isNotEmpty(dto.getDbConfigList()), "数据库配置为空");
        checkArgument(Objects.equals(dto.getEnvList().size(), dto.getDbConfigList().size()), "数据库配置与环境不匹配");

        Map<EnvEnum, DbConfigDto> cfgMap = Maps.newHashMap();
        dto.getDbConfigList().forEach(cfg -> {
            checkArgument(nonNull(cfg.getEnv()), "数据库所属环境为空");
            checkArgument(StringUtils.isNotBlank(cfg.getHost()), "数据库地址为空");
            checkArgument(nonNull(cfg.getPort()), "数据库端口为空");

            if (DataSourceTypeEnum.mysql == dto.getType()
                    || DataSourceTypeEnum.postgresql == dto.getType()) {
                checkArgument(StringUtils.isNotBlank(cfg.getDbName()), "数据库名称为空");
                checkArgument(StringUtils.isNotBlank(cfg.getUsername()), "数据库用户名为空");
                checkArgument(StringUtils.isNotBlank(cfg.getPassword()), "数据库密码为空");
            }

            cfgMap.put(cfg.getEnv(), cfg);
        });
        dto.getEnvList().forEach(env -> {
            checkArgument(nonNull(cfgMap.get(env)), "数据库所属环境不正确");
        });
    }

}
