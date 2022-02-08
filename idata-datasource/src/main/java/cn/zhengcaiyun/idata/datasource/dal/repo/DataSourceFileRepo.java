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

package cn.zhengcaiyun.idata.datasource.dal.repo;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceFileCondition;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSourceFile;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:36
 **/
public interface DataSourceFileRepo {
    Page<DataSourceFile> pagingDataSourceFile(DataSourceFileCondition condition, PageParam pageParam);

    List<DataSourceFile> queryDataSourceFile(DataSourceFileCondition condition, long limit, long offset);

    long countDataSourceFile(DataSourceFileCondition condition);

    Optional<DataSourceFile> queryDataSourceFile(Long id);

    Long createDataSourceFile(DataSourceFile file);
}
