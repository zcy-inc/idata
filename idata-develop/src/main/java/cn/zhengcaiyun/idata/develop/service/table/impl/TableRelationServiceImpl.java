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
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.service.table.TableRelationService;
import cn.zhengcaiyun.idata.develop.dto.table.*;
import com.alibaba.fastjson.JSON;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
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
    private DevColumnInfoDao devColumnInfoDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private ForeignKeyService foreignKeyService;

    private final String[] foreignKeyFields = {"columnNames", "referColumnNames", "erType", "referTableId", "tableId"};
    private final String DB_NAME_LABEL = "dbName";
    private final String TBL_COMMENT_LABEL = "tblComment";
    private final String PK_LABEL = "pk";
    private final String COLUMN_TYPE_LABEL = "columnType";
    private final String COLUMN_COMMENT_LABEL = "columnComment";

    @Override
    public TableRelationDto getTableRelations(Long tableId) {
        TableRelationDto echoTableRelation = new TableRelationDto();
        List<ForeignKeyDto> foreignKeyDtoList = foreignKeyService.getForeignKeys(tableId);
        List<TableNodeDto> tables = new ArrayList<>();
        if (foreignKeyDtoList.size() == 0) {
            tables.add(findTableNodeById(tableId));
            echoTableRelation.setTables(tables);
            return echoTableRelation;
        }
        Set<Long> foreignKeyIds = new HashSet<>();
        Set<Long> tableIds = new HashSet<>();
        findTableRelationsByTableId(tableId, null, foreignKeyDtoList, tableIds, foreignKeyIds);
        foreignKeyService.getForeignKeys(tableId).stream().forEach(foreignKeyDto -> {
            foreignKeyIds.add(foreignKeyDto.getId());
            tableIds.add(foreignKeyDto.getTableId());
        });

        tables = tableIds.stream().map(tblId -> findTableNodeById(tblId)).collect(Collectors.toList());
        List<TableEdgeDto> tEdges = foreignKeyIds.stream().map(fkId -> findTableEdgeById(fkId)).collect(Collectors.toList());
        List<TableEdgeDto> edges = new ArrayList<>();
        if (tEdges.size() > 0) {
            for (TableEdgeDto tEdge : tEdges) {
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
        }
        echoTableRelation.setTables(tables);
        echoTableRelation.setEdges(edges);
        return echoTableRelation;
    }

    private TableNodeDto findTableNodeById(Long tableId) {
        DevTableInfo devTableInfoDto = devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(tableId))))
                .orElse(null);
        if (devTableInfoDto == null) { return null; }
        TableNodeDto tableNodeDto = PojoUtil.copyOne(devTableInfoDto, TableNodeDto.class);
        tableNodeDto.setKey(getDbName(tableId) + "." + tableNodeDto.getTableName());
        var builder = select(devLabel.labelParamValue, devLabel.labelCode, devLabel.columnName)
                .from(devLabel)
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(tableId)));
        List<ColumnInfoDto> columnInfoDtoList = devColumnInfoDao.selectMany(
                select(devColumnInfo.allColumns()).from(devColumnInfo)
                        .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                        .build().render(RenderingStrategies.MYBATIS3))
                        .stream().map(devColumnInfoDto -> {
                    List<DevLabel> columnLabelList = devLabelDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
                    ColumnInfoDto echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDto, ColumnInfoDto.class);
                    for (DevLabel columnLabel : columnLabelList) {
                        if (PK_LABEL.equals(columnLabel.getLabelCode())
                                && devColumnInfoDto.getColumnName().equals(columnLabel.getColumnName())) {
                            echoColumnInfoDto.setPk(Boolean.valueOf(columnLabel.getLabelParamValue()));
                        }
                        else if (COLUMN_COMMENT_LABEL.equals(columnLabel.getLabelCode())
                                && devColumnInfoDto.getColumnName().equals(columnLabel.getColumnName())) {
                            echoColumnInfoDto.setColumnComment(columnLabel.getLabelParamValue());
                        }
                        else if (COLUMN_TYPE_LABEL.equals(columnLabel.getLabelCode())
                                && devColumnInfoDto.getColumnName().equals(columnLabel.getColumnName())) {
                            echoColumnInfoDto.setColumnType(devEnumValueDao.selectOne(c ->
                                    c.where(devEnumValue.del, isNotEqualTo(1),
                                            and(devEnumValue.valueCode, isEqualTo(columnLabel.getLabelParamValue()))))
                                    .get().getEnumValue());
                        }
                    }
                    return echoColumnInfoDto;
        }).collect(Collectors.toList());
        tableNodeDto.setColumnInfos(columnInfoDtoList);
        DevLabel devLabelDto = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.tableId, isEqualTo(tableId)), and(devLabel.labelCode, isEqualTo(TBL_COMMENT_LABEL))))
                .orElse(null);
        String tableComment = devLabelDto != null ? devLabelDto.getLabelParamValue() : devTableInfoDto.getTableName();
        tableNodeDto.setTableComment(tableComment);
        return tableNodeDto;
    }

    private TableEdgeDto findTableEdgeById(Long foreignKeyId) {
        DevForeignKey devForeignKeyDto = devForeignKeyDao.selectOne(
                select(devForeignKey.allColumns())
                        .from(devForeignKey)
                        .leftJoin(devTableInfo).on(devForeignKey.tableId, equalTo(devTableInfo.id))
                        .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.id, isEqualTo(foreignKeyId)))
                        .build().render(RenderingStrategies.MYBATIS3))
                .orElse(null);
        if (devForeignKeyDto == null) { return null; }
        DevTableInfo devTableInfoDto = devTableInfoDao.selectOne(c -> c
                .where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(devForeignKeyDto.getTableId()))))
                .orElse(null);
        DevTableInfo devReferTableInfoDto = devTableInfoDao.selectOne(select(devTableInfo.tableName)
                .from(devTableInfo)
                .where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id, isEqualTo(devForeignKeyDto.getReferTableId())))
                .build().render(RenderingStrategies.MYBATIS3))
                .orElse(null);
        if (devTableInfoDto == null || devReferTableInfoDto == null) { return null; }
        ForeignKeyDto foreignKeyDto = PojoUtil.copyOne(devForeignKeyDto, ForeignKeyDto.class, foreignKeyFields);
        foreignKeyDto.setTableName(devTableInfoDto.getTableName());
        TableInfoDto referTableInfoDto = PojoUtil.copyOne(devReferTableInfoDto, TableInfoDto.class, "tableName");
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
        DevLabel devLabelDto = devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .orElse(null);
        return devLabelDto != null ? devLabelDto.getLabelParamValue() : null;
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
