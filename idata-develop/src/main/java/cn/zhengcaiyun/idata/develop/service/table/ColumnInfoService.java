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

import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-25 16:10
 */

public interface ColumnInfoService {
    List<ColumnInfoDto> getColumns(Long tableId);

    /**
     * 获取列
     * @param tableId 表id
     * @param checkCompare 是否校验（远端hive表）字段一致性
     * @return
     */
    List<ColumnInfoDto> getColumns(Long tableId, boolean checkCompare);

    /**
     * 和远端（hive）表进行列比较，差异信息封装在tableInfo字段中
     * @param tableInfo
     */
    void compareColumns(TableInfoDto tableInfo);

    List<ColumnDetailsDto> getColumnDetails(Long tableId);
    List<ColumnInfoDto> createOrEdit(List<ColumnInfoDto> columnInfoDtoList, Long tableId, List<String> columnNameList, String operator);
//    ColumnInfoDto edit(ColumnInfoDto columnInfoDto, String operator);
    boolean delete(Long columnId, String operator);
    boolean checkColumn(String columnName, Long tableId);
}
