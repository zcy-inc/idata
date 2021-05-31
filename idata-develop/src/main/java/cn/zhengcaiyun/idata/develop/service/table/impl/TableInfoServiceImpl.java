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
package cn.zhengcaiyun.idata.develop.service.table.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDefineDto;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDto;
import cn.zhengcaiyun.idata.dto.develop.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.dto.develop.table.ForeignKeyDto;
import cn.zhengcaiyun.idata.dto.develop.table.TableInfoDto;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-05-28 15:21
 */

@Service
public class TableInfoServiceImpl implements TableInfoService {

    @Autowired
    private LabelService labelService;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private DevColumnInfoDao devColumnInfoDao;

    private final String[] tableInfoFields = {"id", "del", "creator", "create_time", "editor", "edit_time",
            "table_name", "folder_id"};
    private final String[] columnInfoFields = {"id", "del", "creator", "create_time", "editor", "edit_time",
            "column_name", "table_id", "column_index"};

    @Override
    public TableInfoDto getTableInfo(Long tableId) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        TableInfoDto echo = PojoUtil.copyOne(tableInfo, TableInfoDto.class, tableInfoFields);

        List<DevForeignKey> foreignKeyList = devForeignKeyDao.selectMany(
                select(devForeignKey.allColumns()).from(devForeignKey)
                        .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        List<ForeignKeyDto> foreignKeyDtoList = PojoUtil.copyList(foreignKeyList, ForeignKeyDto.class, tableInfoFields);
        List<LabelDto> tableLabelList = getTableLabels(tableId);
        List<ColumnInfoDto> columnInfoDtoList = columnInfoService.getColumns(tableId);
//        List<DevColumnInfo> columnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.columnName)
//                .from(devColumnInfo)
//                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
//                .build().render(RenderingStrategies.MYBATIS3));
//        List<String> columnNameList = columnInfoList.stream().map(DevColumnInfo::getColumnName).collect(Collectors.toList());
//        Map<String, List<LabelDto>> columnInfoMap = getColumnLabels(tableId, columnNameList);
//        List<ColumnInfoDto> columnInfoDtoList = columnInfoList.stream().map(columnInfo -> {
//            ColumnInfoDto columnInfoDto = PojoUtil.copyOne(columnInfo, ColumnInfoDto.class, columnInfoFields);
//            columnInfoDto.setColumnLabels(columnInfoMap.get(columnInfo.getColumnName()));
//            return columnInfoDto;
//        }).collect(Collectors.toList());

        echo.setTableLabels(tableLabelList);
        echo.setColumnInfos(columnInfoDtoList);
        echo.setForeignKeys(foreignKeyDtoList);

        return echo;
    }

    private List<LabelDto> getTableLabels(Long tableId) {
        List<LabelDto> tableLabelList = new ArrayList<>();
//        tableLabelList = labelService.findLabels(tableId); TODO
        return tableLabelList;

    }

//    private Map<String, List<LabelDto>> getColumnLabels(Long tableId, List<String> columnNames) {
//        Map<String, List<LabelDto>> columnLabelMap = new HashMap<>();
//        columnLabelMap = labelService.findColumnLabelMap(tableId, columnNames);
//        return columnLabelMap;
//    }

    @Override
    public List<TableInfoDto> getTables(String labelCode, String database) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TableInfoDto create(TableInfoDto tableInfoDto, String operator) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TableInfoDto edit(TableInfoDto tableInfoDto, String editor) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long tableId, String editor) {
        return false;
    }
}
