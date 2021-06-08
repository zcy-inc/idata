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
import cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.service.table.TableRelationService;
import cn.zhengcaiyun.idata.dto.develop.table.*;
import com.alibaba.fastjson.JSON;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-05-28 15:36
 */

@Service
public class TableRelationServiceImpl implements TableRelationService {

    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private ForeignKeyService foreignKeyService;

    private final String[] foreignKeyFields = {"columnNames", "referColumnNames", "erType", "referTableId", "tableId"};
    private final String db_name = "dbName";

    @Override
    public TableRelationDto getTableRelations(Long tableId) {
        List<ForeignKeyDto> foreignKeyDtoList = foreignKeyService.getForeignKeys(null);
        Set<Long> foreignKeyIds = new HashSet<>();
        Set<Long> tableIds = new HashSet<>();
        findTableRelationsByTableId(tableId, null, foreignKeyDtoList, tableIds, foreignKeyIds);
        foreignKeyService.getForeignKeys(tableId).stream().forEach(foreignKeyDto -> {
            foreignKeyIds.add(foreignKeyDto.getId());
            tableIds.add(foreignKeyDto.getTableId());
        });

        List<TableNodeDto> tables = tableIds.stream().map(tblId -> findTableNodeById(tblId)).collect(Collectors.toList());
        List<TableEdgeDto> tEdges = foreignKeyIds.stream().map(fkId -> findTableEdgeById(fkId)).collect(Collectors.toList());
        List<TableEdgeDto> edges = new ArrayList<>();
        for (TableEdgeDto tEdge: tEdges) {
            List<String> columnNameList = Arrays.asList(tEdge.getColumnNames().split(","));
            List<String> referColumnNameList = Arrays.asList(tEdge.getReferColumnNames().split(","));
            if (columnNameList.size() != referColumnNameList.size()) continue;
            for (int i = 0; i < columnNameList.size(); i++) {
                TableEdgeDto edge = JSON.parseObject(JSON.toJSONString(tEdge), TableEdgeDto.class);
                edge.setFromPort(columnNameList.get(i));
                edge.setToPort(referColumnNameList.get(i));
                edges.add(edge);
            }
        }
        TableRelationDto echoTableRelation = new TableRelationDto();
        echoTableRelation.setTables(tables);
        echoTableRelation.setEdges(edges);
        return echoTableRelation;
    }

    private TableNodeDto findTableNodeById(Long tableId) {
        TableNodeDto tableNodeDto = PojoUtil.copyOne(devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(tableId)))).get(),
                TableNodeDto.class);
        tableNodeDto.setKey(getDbName(tableId) + "." + tableNodeDto.getTableName());
        tableNodeDto.setColumnInfos(columnInfoService.getColumns(tableId));
        return tableNodeDto;
    }

    private TableEdgeDto findTableEdgeById(Long foreignKeyId) {
//        SqlColumn tableName = devTableInfo.tableName.as("tableName");
//        SqlColumn tableId = devTableInfo.id.as("tableId");
//        SqlColumn referTableName = devTableInfo.tableName.as("referTableName");
//        SqlColumn referTableId = devTableInfo.id.as("referTableId");
//        SelectStatementProvider selectStatement = SqlBuilder.select(devForeignKey.id, devForeignKey.columnNames,
//                devForeignKey.referColumnNames, devForeignKey.erType, tableName, tableId, referTableName, referTableId)
//                .from(devForeignKey)
//                .leftJoin(devTableInfo).on(devForeignKey.tableId, equalTo(tableId))
//                .leftJoin(devTableInfo).on(devForeignKey.referTableId, equalTo(referTableId))
//                .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.id, isEqualTo(foreignKeyId)))
//                .build().render(RenderingStrategies.MYBATIS3);
//        TableEdgeDto tableEdgeDto = new TableEdgeDto();
//        tableEdgeDto.setFrom(getDbName((Long) selectStatement.getParameters().get("tableId")) + "." +
//                selectStatement.getParameters().get("tableName"));
//        tableEdgeDto.setColumnNames(String.valueOf(selectStatement.getParameters().get("columnNames")));
//        tableEdgeDto.setTo(getDbName((Long) selectStatement.getParameters().get("referTableId")) + "." +
//                selectStatement.getParameters().get("referTableName"));
//        tableEdgeDto.setReferColumnNames(String.valueOf(selectStatement.getParameters().get("referColumnNames")));
//        tableEdgeDto.setText(getTextFromErType(ERelationTypeEnum.valueOf(
//                String.valueOf(selectStatement.getParameters().get("erType")))));
//        tableEdgeDto.setText(getToTextFromErType(ERelationTypeEnum.valueOf(
//                String.valueOf(selectStatement.getParameters().get("erType")))));
        ForeignKeyDto foreignKeyDto = PojoUtil.copyOne(devForeignKeyDao.selectOne(
                select(devForeignKey.allColumns())
                        .from(devForeignKey)
                        .leftJoin(devTableInfo).on(devForeignKey.tableId, equalTo(devTableInfo.id))
                        .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.id, isEqualTo(foreignKeyId)))
                        .build().render(RenderingStrategies.MYBATIS3)).get(),
                ForeignKeyDto.class, foreignKeyFields);
        foreignKeyDto.setTableName(devTableInfoDao.selectOne(c -> c
                .where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(foreignKeyDto.getTableId())))).get()
                .getTableName());
        TableInfoDto referTableInfoDto = PojoUtil.copyOne(devTableInfoDao.selectOne(select(devTableInfo.tableName)
                .from(devTableInfo)
                .where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(foreignKeyDto.getReferTableId())))
                .build().render(RenderingStrategies.MYBATIS3)).get(), TableInfoDto.class, "tableName");
        TableEdgeDto tableEdgeDto = new TableEdgeDto();
        tableEdgeDto.setFrom(getDbName(foreignKeyDto.getTableId()) + "." + foreignKeyDto.getTableName());
        tableEdgeDto.setTo(getDbName(foreignKeyDto.getReferTableId()) + "." + referTableInfoDto.getTableName());
        tableEdgeDto.setColumnNames(foreignKeyDto.getColumnNames());
        tableEdgeDto.setReferColumnNames(foreignKeyDto.getReferColumnNames());
        tableEdgeDto.setText(getTextFromErType(ERelationTypeEnum.valueOf(foreignKeyDto.getErType())));
        tableEdgeDto.setToText(getToTextFromErType(ERelationTypeEnum.valueOf(foreignKeyDto.getErType())));
        return tableEdgeDto;
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(db_name)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }

    private String getTextFromErType(ERelationTypeEnum erType) {
        switch (erType) {
            case I2I:
            case I2N:
                return "1";
            case N2I:
                return "N";
            case M2N:
                return "M";
            default:
                break;
        }
        return null;
    }

    private String getToTextFromErType(ERelationTypeEnum erType) {
        switch (erType) {
            case I2I:
            case N2I:
                return "1";
            case I2N:
            case M2N:
                return "N";
            default:
                break;
        }
        return null;
    }

    private void findTableRelationsByTableId(Long tableId, Set<Long> cTableIds, List<ForeignKeyDto> foreignKeys,
                                             Set<Long> tableIds, Set<Long> foreignKeyIds) {
        if (tableId != null) tableIds.add(tableId);
        Set cTblIds = new HashSet();
        foreignKeys.stream().filter(foreignKeyDto -> cTableIds == null ?
                foreignKeyDto.getTableId().equals(tableId) : cTableIds.contains(foreignKeyDto.getTableId()))
                .forEach(foreignKeyDto -> {
                    foreignKeyIds.add(foreignKeyDto.getId());
                    if (!tableIds.contains(foreignKeyDto.getReferTableId())) {
                        cTblIds.add(foreignKeyDto.getReferTableId());
                    }
                    tableIds.add(foreignKeyDto.getReferTableId());
                });
        if (!cTblIds.isEmpty()) {
            findTableRelationsByTableId(null, cTblIds, foreignKeys, tableIds, foreignKeyIds);
        }
    }
}
