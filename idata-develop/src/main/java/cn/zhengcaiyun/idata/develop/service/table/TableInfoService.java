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

import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoDTO;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.SyncHiveDTO;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableDdlDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-25 16:07
 */

public interface TableInfoService {
    TableInfoDto getTableInfo(Long tableId);
    List<TableInfoDto> getTablesByDataBase(String database);
    List<LabelDto> getDbNames();

    /**
     * 获取table的创建语句DDL
     * @param tableId
     * @return
     */
    String getTableDDL(Long tableId);
    TableInfoDto syncTableInfoByDDL(TableDdlDto tableDdlDto);
    TableInfoDto create(TableInfoDto tableInfoDto, String creator);
    TableInfoDto edit(TableInfoDto tableInfoDto, String editor);
    boolean delete(Long tableId, String editor);
    String syncMetabaseInfo(Long tableId, String editor);

    /**
     * 获取表技术相关信息
     * @param tableId
     * @return
     */
    TableTechInfoDto getTableTechInfo(Long tableId);

    /**
     * 更新hive表名字
     * @param tableId
     * @param hiveTableName
     * @param operator
     */
    void updateHiveTableName(Long tableId, String hiveTableName, String operator);

    /**
     * 获取简单的表数据信息（非连表）
     * @param tableId
     * @return
     */
    DevTableInfo getSimpleById(Long tableId);
}
