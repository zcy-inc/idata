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

package cn.zhengcaiyun.idata.connector.api;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-20 11:43
 **/
public interface MetadataQueryApi {

    TableTechInfoDto getTableTechInfo(String db, String table);

    Boolean testConnection(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String username, String password,
                           String dbName, String schema);
}
