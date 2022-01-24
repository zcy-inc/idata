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
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.connector.api.DataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceFileCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceFileDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSourceFile;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceFileRepo;
import cn.zhengcaiyun.idata.datasource.manager.DataSourceFileManager;
import cn.zhengcaiyun.idata.datasource.service.DataSourceFileService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:45
 **/
@Service
public class DataSourceFileServiceImpl implements DataSourceFileService {

    private final DataSourceFileRepo dataSourceFileRepo;
    private final DataSourceFileManager dataSourceFileManager;
    private final DataQueryApi dataQueryApi;

    @Autowired
    public DataSourceFileServiceImpl(DataSourceFileRepo dataSourceFileRepo,
                                     DataSourceFileManager dataSourceFileManager,
                                     DataQueryApi dataQueryApi) {
        this.dataSourceFileRepo = dataSourceFileRepo;
        this.dataSourceFileManager = dataSourceFileManager;
        this.dataQueryApi = dataQueryApi;
    }

    @Override
    public Page<DataSourceFileDto> pagingDataSourceFile(DataSourceFileCondition condition, PageParam pageParam) {
        Page<DataSourceFile> page = dataSourceFileRepo.pagingDataSourceFile(condition, pageParam);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }
        return Page.newOne(
                page.getContent().stream()
                        .map(DataSourceFileDto::from)
                        .collect(Collectors.toList()),
                page.getTotal());
    }

    @Override
    public DataSourceFileDto getDataSourceFile(Long id) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSourceFile> optional = dataSourceFileRepo.queryDataSourceFile(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSourceFile file = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_YES.val, file.getDel()), "数据源已删除");
        return DataSourceFileDto.from(file);
    }

    @Override
    public DataSourceFileDto addDataSourceFile(DataSourceFileDto dto, Operator operator) {
        checkDataSourceFile(dto);
        dto.setDel(DeleteEnum.DEL_NO.val);
        dto.setOperator(operator);
        Long id = dataSourceFileRepo.createDataSourceFile(dto.toModel());
        checkState(nonNull(id), "创建数据源失败");
        Optional<DataSourceFile> optional = dataSourceFileRepo.queryDataSourceFile(id);
        if (optional.isEmpty()) return null;
        return DataSourceFileDto.from(optional.get());
    }

    @Override
    public QueryResultDto getDataSourceFileData(DataSourceFileDto dto, PageParam pageParam) {
        checkDataSourceFile(dto);
        List<String> db_table = Splitter.on(".").omitEmptyStrings().splitToList(dto.getName());
        checkArgument(db_table.size() == 2, "名称格式不正确");

        EnvEnum prodEnvEnum = null;
        for (EnvEnum envEnum : dto.getEnvList()) {
            if (EnvEnum.prod == envEnum) prodEnvEnum = envEnum;
        }
        String envPrefix = prodEnvEnum != null ? "" : dto.getEnvList().get(0).name() + "_";
        QueryResultDto resultDto = dataQueryApi.queryData(envPrefix + db_table.get(0), db_table.get(1), pageParam.getLimit(), pageParam.getOffset());
        return resultDto;
    }

    @Override
    public String importSourceFileData(InputStream originFileStream, String originFileName, String destTableName, String environments) {
        checkArgument(Objects.nonNull(originFileStream), "文件未上传成功");
        checkArgument(StringUtils.isNotBlank(originFileName), "文件名称为空");
        checkArgument(StringUtils.isNotBlank(destTableName), "目标表名为空");
        checkArgument(StringUtils.isNotBlank(environments), "环境名称为空");
        List<String> db_table = Splitter.on(".").omitEmptyStrings().splitToList(destTableName);
        checkArgument(db_table.size() == 2, "目标表名称格式不正确");

        List<String> envList = Splitter.on(",").omitEmptyStrings().splitToList(environments);
        List<EnvEnum> envEnumList = envList.stream()
                .map(EnvEnum::getEnum)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        checkArgument(envEnumList.size() == envList.size(), "环境名称不正确");

        dataSourceFileManager.importSourceFileData(originFileStream, originFileName, destTableName, envEnumList);
        return destTableName;
    }

    private void checkDataSourceFile(DataSourceFileDto dto) {
        checkArgument(nonNull(dto.getType()), "数据源类型为空");
        checkArgument(StringUtils.isNotBlank(dto.getName()), "数据源名称为空");
        checkArgument(ObjectUtils.isNotEmpty(dto.getEnvList()), "数据源环境为空");
        checkArgument(StringUtils.isNotBlank(dto.getFileName()), "文件名称为空");
    }
}
