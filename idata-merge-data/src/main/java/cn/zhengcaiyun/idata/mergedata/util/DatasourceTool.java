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

package cn.zhengcaiyun.idata.mergedata.util;

import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-11 17:02
 **/
public class DatasourceTool {

    public static Optional<DataSource> findDatasource(Long oldDatasourceId, List<DataSource> dataSourceList) {
        return dataSourceList.stream()
                .filter(dataSource -> dataSource.getName().indexOf(IdPadTool.padId(oldDatasourceId.toString()) + "#_") >= 0)
                .findFirst();
    }

    public static Optional<DataSource> findHiveDatasource(List<DataSource> dataSourceList) {
        return dataSourceList.stream()
                .filter(dataSource -> dataSource.getType().equalsIgnoreCase("hive"))
                .findFirst();
    }
}
