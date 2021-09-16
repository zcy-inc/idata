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

package cn.zhengcaiyun.idata.datasource.service;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-15 16:47
 **/
public interface DataSourceService {

    Page<DataSourceDto> pagingDataSource(DataSourceCondition condition, PageParam pageParam);

    DataSourceDto getDataSource(Long id);

    DataSourceDto addDataSource(DataSourceDto dto, Operator operator);

    DataSourceDto editDataSource(DataSourceDto dto, Operator operator);

    Boolean removeDataSource(Long id, Operator operator);

    Boolean testConnection(DataSourceTypeEnum dataSourceType, DbConfigDto dto);
}
