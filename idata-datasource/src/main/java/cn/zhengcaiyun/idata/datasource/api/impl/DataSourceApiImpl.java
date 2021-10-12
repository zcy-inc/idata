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

package cn.zhengcaiyun.idata.datasource.api.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-29 16:58
 **/
@Service
public class DataSourceApiImpl implements DataSourceApi {

    private final DataSourceRepo dataSourceRepo;

    @Autowired
    public DataSourceApiImpl(DataSourceRepo dataSourceRepo) {
        this.dataSourceRepo = dataSourceRepo;
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
}
