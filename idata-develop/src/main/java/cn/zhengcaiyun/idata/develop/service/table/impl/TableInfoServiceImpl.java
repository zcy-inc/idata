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
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.ForeignKeyDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-05-28 15:21
 */

@Service
public class TableInfoServiceImpl implements TableInfoService {

    @Value("${metabase.datasource.jdbcUrl:#{null}}")
    private String METABASE_DATASOURCE_JDBCURL;
    @Value("${metabase.datasource.username:#{null}}")
    private String METABASE_DATASOURCE_USERNAME;
    @Value("${metabase.datasource.password:#{null}}")
    private String METABASE_DATASOURCE_PASSWORD;

    @Autowired
    private LabelService labelService;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private ForeignKeyService foreignKeyService;
    @Autowired
    private MetadataQueryApi metadataQueryApi;

    private final String[] tableInfoFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "tableName", "folderId"};
    private String[] foreignKeyFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "tableId", "columnNames", "referTableId", "referColumnNames", "erType"};
    private final String DB_NAME_LABEL = "dbName:LABEL";
    private final String TABLE_SUBJECT = "TABLE";
    private final String TABLE_COMMENT_LABEL = "tblComment:LABEL";
    private final String COLUMN_TYPE_ENUM = "hiveColTypeEnum:ENUM";
    private final String COLUMN_COMMENT_LABEL = "columnComment:LABEL";
    private final String COLUMN_TYPE_LABEL = "columnType:LABEL";
    private final String COLUMN_PT_LABEL = "partitionedCol:LABEL";

    @Override
    public TableInfoDto getTableInfo(Long tableId) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        TableInfoDto echoTableInfo = PojoUtil.copyOne(tableInfo, TableInfoDto.class, tableInfoFields);

        List<DevForeignKey> foreignKeyList = devForeignKeyDao.selectMany(
                select(devForeignKey.allColumns()).from(devForeignKey)
                        .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        List<ForeignKeyDto> foreignKeyDtoList = PojoUtil.copyList(foreignKeyList, ForeignKeyDto.class, foreignKeyFields);
        foreignKeyDtoList = foreignKeyDtoList.stream().map(foreignKeyDto -> {
                foreignKeyDto.setReferTableName(devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.id,
                        isEqualTo(foreignKeyDto.getReferTableId())))).get().getTableName());
                foreignKeyDto.setReferDbName(getDbName(tableId));
                return foreignKeyDto;
        }).collect(Collectors.toList());
        List<LabelDto> tableLabelList = labelService.findLabels(tableId, null);
        List<ColumnInfoDto> columnInfoDtoList = columnInfoService.getColumns(tableId);

        echoTableInfo.setTableLabels(tableLabelList);
        echoTableInfo.setColumnInfos(columnInfoDtoList);
        echoTableInfo.setForeignKeys(foreignKeyDtoList);
        echoTableInfo.setDbName(tableLabelList
                .stream().filter(tableLabel -> DB_NAME_LABEL.equals(tableLabel.getLabelCode()))
                .findAny().get().getLabelParamValue());

        return echoTableInfo;
    }

    @Override
    public List<TableInfoDto> getTablesByDataBase(String labelValue) {
        List<DevTableInfo> tableInfoList = devTableInfoDao.selectMany(select(devTableInfo.allColumns())
                .from(devTableInfo)
                .leftJoin(devLabel).on(devTableInfo.id, equalTo(devLabel.tableId))
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.labelParamValue, isEqualTo(labelValue)))
                .build().render(RenderingStrategies.MYBATIS3));
        return PojoUtil.copyList(tableInfoList, TableInfoDto.class);
    }

    @Override
    public List<LabelDto> getDbNames() {
        List<LabelDto> dbLabelDtoList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.labelCode, devLabel.labelParamValue)
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)))
                        .groupBy(devLabel.labelParamValue)
                        .build().render(RenderingStrategies.MYBATIS3)),
                LabelDto.class, "labelCode", "labelParamValue");
        return dbLabelDtoList;
    }

    @Override
    public String getTableDDL(Long tableId) {
        StringBuilder ddl = new StringBuilder("");
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.del, isNotEqualTo(1),
                and(devTableInfo.id, isEqualTo(tableId)))).orElse(null);
        if (tableInfo == null) { return ddl.toString(); }
        String tableComment = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.tableId, isEqualTo(tableId)), and(devLabel.labelCode, isEqualTo(TABLE_COMMENT_LABEL))))
                .get().getLabelParamValue();
        String tableDbName = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.tableId, isEqualTo(tableId)), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL))))
                .get().getLabelParamValue();
        Map<String, String> columnTypeEnumMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                and(devEnumValue.enumCode, isEqualTo(COLUMN_TYPE_ENUM))))
                .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
        List<ColumnInfoDto> columnInfoList = columnInfoService.getColumns(tableId);
        if (columnInfoList.size() == 0) { return ddl.toString(); }
        columnInfoList.forEach(columnInfoDto -> columnInfoDto.getColumnLabels().forEach(columnLabel -> {
            if (COLUMN_COMMENT_LABEL.equals(columnLabel.getLabelCode())) {
                columnInfoDto.setColumnComment(columnLabel.getLabelParamValue());
            }
            else if (COLUMN_TYPE_LABEL.equals(columnLabel.getLabelCode())) {
                columnInfoDto.setColumnType(columnTypeEnumMap.get(columnLabel.getLabelParamValue()));
            }
            else if (COLUMN_PT_LABEL.equals(columnLabel.getLabelCode())) {
                columnInfoDto.setPartitionedColumn(columnLabel.getLabelParamValue());
            }
        }));
        List<ColumnInfoDto> columnInfoDtoList = columnInfoList.stream()
                .filter(columnInfoDto -> "false".equals(columnInfoDto.getPartitionedColumn()))
                .collect(Collectors.toList());
        List<ColumnInfoDto> columnInfoPtList = columnInfoList.stream()
                .filter(columnInfoDto -> "true".equals(columnInfoDto.getPartitionedColumn()))
                .collect(Collectors.toList());

        ddl.append("create external table ").append("`").append(tableDbName).append("`.`").append(tableInfo.getTableName()).append("`(\n");
        for (int i = 0; i < columnInfoDtoList.size(); i++) {
            if ("false".equals(columnInfoDtoList.get(i).getPartitionedColumn())) {
                ddl.append("  `").append(columnInfoDtoList.get(i).getColumnName()).append("` ").append(columnInfoDtoList.get(i).getColumnType());
                if (StringUtils.isNotEmpty(columnInfoDtoList.get(i).getColumnComment())) {
                    ddl.append(" ").append("comment").append(" ").append("'").append(columnInfoDtoList.get(i).getColumnComment()).append("'");
                }
                if (i < columnInfoDtoList.size() - 1) ddl.append(",\n");
                if (i == columnInfoDtoList.size() - 1) ddl.append(") \n");
            }
        }
        ddl.append("comment '").append(tableComment).append("' \n");
        if (columnInfoPtList.size() > 0) {
            ddl.append("partitioned by (\n");
            for (int i = 0; i < columnInfoPtList.size(); i++) {
                ddl.append("  `").append(columnInfoPtList.get(i).getColumnName()).append("` ").append(columnInfoPtList.get(i).getColumnType());
                if (StringUtils.isNotEmpty(columnInfoPtList.get(i).getColumnComment())) {
                    ddl.append(" ").append("comment").append(" ").append("'").append(columnInfoPtList.get(i).getColumnComment()).append("'");
                }
                if (i < columnInfoPtList.size() - 1) ddl.append(",\n");
                if (i == columnInfoPtList.size() - 1) ddl.append(") \n");
            }
        }
        ddl.append("stored as orc \n");
        ddl.append(String.format("location 'hdfs://nameservice1/hive/%s.db/%s' \n", tableDbName, tableInfo.getTableName()));
        return ddl.toString();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TableInfoDto create(TableInfoDto tableInfoDto, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(tableInfoDto.getTableName()), "表名称不能为空");
        DevTableInfo checkTableInfo = devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.del, isNotEqualTo(1),
                        and(devTableInfo.tableName, isEqualTo(tableInfoDto.getTableName()))))
                .orElse(null);
        checkArgument(checkTableInfo == null, "表已存在，新建失败");
        List<LabelDefineDto> tableLabelDefineDtoList = labelService.findDefines(TABLE_SUBJECT, null)
                .stream()
                .filter(labelDefineDto -> labelDefineDto.getLabelRequired().equals(1))
                .collect(Collectors.toList());
        Map<String, String> tableLabelDefineMap = tableLabelDefineDtoList
                .stream()
                .collect(Collectors.toMap(LabelDefineDto::getLabelCode, LabelDefineDto::getLabelName));
        checkArgument(tableInfoDto.getTableLabels() != null && tableInfoDto.getTableLabels().size() > 0, "缺少表必要信息");
        List<LabelDto> tableLabelDtoList = tableInfoDto.getTableLabels();
        List<String> tableLabelCodeList = tableLabelDtoList.stream().map(LabelDto::getLabelCode).collect(Collectors.toList());
        for (Map.Entry<String, String> entry : tableLabelDefineMap.entrySet()) {
            checkArgument(tableLabelCodeList.contains(entry.getKey()), entry.getValue() + "不能为空");
        }

        // 插入tableInfo表
        tableInfoDto.setCreator(operator);
        DevTableInfo tableInfo = PojoUtil.copyOne(tableInfoDto, DevTableInfo.class,
                "tableName", "folderId", "creator");
        devTableInfoDao.insertSelective(tableInfo);
        TableInfoDto echoTableInfoDto = PojoUtil.copyOne(devTableInfoDao.selectByPrimaryKey(tableInfo.getId()).get(),
                TableInfoDto.class, tableInfoFields);
        // 插入label表
        List<LabelDto> echoTableLabelDtoList = tableLabelDtoList.stream().map(tableLabelDto -> {
            tableLabelDto.setTableId(tableInfo.getId());
            return labelService.label(tableLabelDto, operator);
        }).collect(Collectors.toList());
        echoTableInfoDto.setTableLabels(echoTableLabelDtoList);
        // 字段相关表操作
        List<ColumnInfoDto> columnInfoDtoList = tableInfoDto.getColumnInfos() != null && tableInfoDto.getColumnInfos().size() > 0
                        ? tableInfoDto.getColumnInfos() : null;
        if (columnInfoDtoList != null) {
            List<String> columnNameList = columnInfoDtoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            List<ColumnInfoDto> echoColumnInfoDtoList = columnInfoService.createOrEdit(columnInfoDtoList,
                    tableInfo.getId(), columnNameList, operator);
            echoTableInfoDto.setColumnInfos(echoColumnInfoDtoList);
            // 外键表操作
            List<ForeignKeyDto> foreignKeyDtoList = tableInfoDto.getForeignKeys() != null && tableInfoDto.getForeignKeys().size() > 0
                    ? tableInfoDto.getForeignKeys() : null;
            if (foreignKeyDtoList != null) {
                List<ForeignKeyDto> echoForeignKeyDtoList = foreignKeyService.createOrEdit(foreignKeyDtoList,
                        tableInfo.getId(), columnNameList, operator);
                echoTableInfoDto.setForeignKeys(echoForeignKeyDtoList);
            }
        }

        return echoTableInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TableInfoDto edit(TableInfoDto tableInfoDto, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(tableInfoDto.getId() != null, "表ID不能为空");
        DevTableInfo checkTableInfo = devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.id, isEqualTo(tableInfoDto.getId()), and(devTableInfo.del, isNotEqualTo(1))))
                .orElse(null);
        checkArgument(checkTableInfo != null, "表不存在");

        // 更新tableInfo表
        tableInfoDto.setEditor(operator);
        DevTableInfo tableInfo = PojoUtil.copyOne(tableInfoDto, DevTableInfo.class, "id",
                "tableName", "folderId", "editor");
        devTableInfoDao.updateByPrimaryKeySelective(tableInfo);
        TableInfoDto echoTableInfoDto = PojoUtil.copyOne(devTableInfoDao.selectByPrimaryKey(tableInfo.getId()).get(),
                TableInfoDto.class, tableInfoFields);
        // 插入label表
        List<LabelDto> tableLabelDtoList = tableInfoDto.getTableLabels() != null && tableInfoDto.getTableLabels().size() > 0 ?
                tableInfoDto.getTableLabels() : null;
        if (tableLabelDtoList != null) {
            List<LabelDto> existTableLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                    .from(devLabel).where(devLabel.del, isNotEqualTo(1),
                            and(devLabel.tableId, isEqualTo(tableInfo.getId())),
                            and(devLabel.columnName, isNull(), or(devLabel.columnName, isEqualTo(""))))
                            .build().render(RenderingStrategies.MYBATIS3)),
                    LabelDto.class, "id", "tableId", "labelCode");
            List<String> tableLabelCodeList = tableLabelDtoList.stream().map(LabelDto::getLabelCode).collect(Collectors.toList());
            List<LabelDto> deleteTableLabelList = existTableLabelList.stream()
                    .filter(existColumnLabel -> !tableLabelCodeList.contains(existColumnLabel.getLabelCode()))
                    .collect(Collectors.toList());
            // 删除表不再打标的记录
            deleteTableLabelList.forEach(deleteTableLabel -> labelService.removeLabel(deleteTableLabel, operator));
            List<LabelDto> echoTableLabelDtoList = tableLabelDtoList.stream().map(tableLabelDto -> {
                tableLabelDto.setTableId(tableInfo.getId());
                return labelService.label(tableLabelDto, operator);
            }).collect(Collectors.toList());
            echoTableInfoDto.setTableLabels(echoTableLabelDtoList);
        }
        // 字段表相关操作
        List<ColumnInfoDto> columnInfoDtoList = tableInfoDto.getColumnInfos() != null && tableInfoDto.getColumnInfos().size() > 0
                ? tableInfoDto.getColumnInfos() : null;
        if (columnInfoDtoList != null) {
            List<String> columnNameList = columnInfoDtoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            List<ColumnInfoDto> echoColumnInfoDtoList = columnInfoService.createOrEdit(columnInfoDtoList,
                    tableInfo.getId(), columnNameList, operator);
            echoTableInfoDto.setColumnInfos(echoColumnInfoDtoList);
            // 外键表操作
            List<ForeignKeyDto> foreignKeyDtoList = tableInfoDto.getForeignKeys() != null && tableInfoDto.getForeignKeys().size() > 0
                    ? tableInfoDto.getForeignKeys() : null;
            if (foreignKeyDtoList != null) {
                List<ForeignKeyDto> echoForeignKeyDtoList = foreignKeyService.createOrEdit(foreignKeyDtoList,
                        tableInfo.getId(), columnNameList, operator);
                echoTableInfoDto.setForeignKeys(echoForeignKeyDtoList);
            }
            else {
                List<ForeignKeyDto> existForeignKeyList = foreignKeyService.getForeignKeys(tableInfoDto.getId());
                existForeignKeyList.forEach(existForeignKey -> foreignKeyService.delete(existForeignKey.getId(), operator));
            }
        }

        return echoTableInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long tableId, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(tableId != null, "表ID不能为空");
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c ->
                c.where(devTableInfo.del, isNotEqualTo(1),
                        and(devTableInfo.id, isEqualTo(tableId))))
                .orElse(null);
        checkArgument(tableInfo != null, "表不存在");

        // 校验指标系统依赖
        List<DevLabel> measureList = devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(tableId)),
                        and(devLabelDefine.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name()),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name())),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL.name()))))
                .build().render(RenderingStrategies.MYBATIS3));
        if (measureList.size() != 0) {
            throw new IllegalArgumentException(labelService.findDefine(measureList.get(0).getLabelCode()).getLabelName() + "依赖该表，不能删除");
        }
        // 删除label表记录
        List<LabelDto> tableLabelDtoList = labelService.findLabels(tableId, null);
        boolean deleteSuccess = tableLabelDtoList.stream().allMatch(tableLabelDto -> labelService.removeLabel(tableLabelDto, operator));
        // 删除columnInfo表记录
        List<ColumnInfoDto> columnInfoDtoList = columnInfoService.getColumns(tableId);
        deleteSuccess = deleteSuccess && columnInfoDtoList.stream().allMatch(columnInfoDto ->
                columnInfoService.delete(columnInfoDto.getId(), operator));
        // 删除外键表记录
        List<ForeignKeyDto> foreignKeyDtoList = foreignKeyService.getForeignKeys(tableId);
        deleteSuccess = deleteSuccess && foreignKeyDtoList.stream().allMatch(foreignKeyDto ->
                foreignKeyService.delete(foreignKeyDto.getId(), operator));
        // 删除tableInfo表记录
        devTableInfoDao.update(c -> c.set(devTableInfo.del).equalTo(1).set(devTableInfo.editor).equalTo(operator)
                .where(devTableInfo.id, isEqualTo(tableId)));

        return deleteSuccess;
    }

    @Override
    public String syncMetabaseInfo(Long tableId, String editor) {
        checkArgument(isNotEmpty(editor), "编辑者不能为空");
        TableInfoDto tableInfo = getTableInfo(tableId);

    }

    @Override
    public TableTechInfoDto getTableTechInfo(Long tableId) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        return metadataQueryApi.getTableTechInfo(getDbName(tableId), tableInfo.getTableName());
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }
//
//    private List<String> getAssetCatalogues(String assetCatalogueCode) {
//        List<DevEnumValue> assetCatalogueList = devEnumValueDao.select(c ->
//                c.where(devEnumValue.del, isNotEqualTo(1)).and(devEnumValue.enumCode, isEqualTo(ASSET_CATALOGUE_ENUM)));
//        Map<String, DevEnumValue> assetCatalogueMap = assetCatalogueList.stream()
//                .collect(Collectors.toMap(DevEnumValue::getValueCode, Function.identity()));
//        List<DevEnumValue> echo = new ArrayList<>();
//        echo.add(assetCatalogueMap.get(assetCatalogueCode));
//        return getTree(echo, assetCatalogueMap).stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
//    }
//
//    private List<DevEnumValue> getTree(List<DevEnumValue> assetCatalogues, Map<String, DevEnumValue> assetCatalogueMap) {
//        if (isNotEmpty(assetCatalogues.get(0).getParentCode())) {
//            assetCatalogues.add(0, assetCatalogueMap.get(assetCatalogues.get(0).getParentCode()));
//            getTree(assetCatalogues, assetCatalogueMap);
//        }
//        return assetCatalogues;
//    }
}
