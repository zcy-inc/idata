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
package cn.zhengcaiyun.idata.develop.service.table;

import cn.zhengcaiyun.idata.dto.develop.table.ColumnInfoDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-25 16:10
 */

public interface ColumnInfoService {
    ColumnInfoDto getColumnInfo(Long tableId, String columnName);
    List<ColumnInfoDto> getColumns(Long tableId);
    ColumnInfoDto create(ColumnInfoDto columnInfoDto, String creator);
    ColumnInfoDto edit(ColumnInfoDto columnInfoDto, String editor);
    boolean delete(Long columnId, String editor);
}
