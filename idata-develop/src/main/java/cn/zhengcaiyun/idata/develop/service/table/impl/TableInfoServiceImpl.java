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
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.ForeignKeyDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
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
    private ForeignKeyService foreignKeyService;

    private final String[] tableInfoFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "tableName", "folderId"};
    private String[] foreignKeyFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "tableId", "columnNames", "referTableId", "referColumnNames", "erType"};
    private final String DB_NAME_LABEL = "dbName";
    private final String TABLE_SUBJECT = "TABLE";

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

        return echoTableInfo;
    }

    @Override
    public List<TableInfoDto> getTables(String labelValue) {
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
        return PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)))
                        .build().render(RenderingStrategies.MYBATIS3)),
                LabelDto.class, "labelCode", "labelParamValue");
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
            List<ColumnInfoDto> echoColumnInfoDtoList = columnInfoDtoList.stream().map(columnInfoDto ->{
                columnInfoDto.setTableId(tableInfo.getId());
                return columnInfoService.create(columnInfoDto, operator);
            }).collect(Collectors.toList());
            echoTableInfoDto.setColumnInfos(echoColumnInfoDtoList);
        }
        // 外键表操作
        List<ForeignKeyDto> foreignKeyDtoList = tableInfoDto.getForeignKeys() != null && tableInfoDto.getForeignKeys().size() > 0
                ? tableInfoDto.getForeignKeys() : null;
        if (foreignKeyDtoList != null) {
            List<ForeignKeyDto> echoForeignKeyDtoList = foreignKeyDtoList.stream().map(foreignKeyDto -> {
                foreignKeyDto.setTableId(tableInfo.getId());
                return foreignKeyService.create(foreignKeyDto, operator);
            }).collect(Collectors.toList());
            echoTableInfoDto.setForeignKeys(echoForeignKeyDtoList);
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
            List<ColumnInfoDto> echoColumnInfoDtoList = columnInfoDtoList.stream().map(columnInfoDto ->
                    columnInfoService.edit(columnInfoDto, operator)).collect(Collectors.toList());
            echoTableInfoDto.setColumnInfos(echoColumnInfoDtoList);
        }
        // 外键表操作
        List<ForeignKeyDto> foreignKeyDtoList = tableInfoDto.getForeignKeys() != null && tableInfoDto.getForeignKeys().size() > 0
                ? tableInfoDto.getForeignKeys() : null;
        if (foreignKeyDtoList != null) {
            List<ForeignKeyDto> echoForeignKeyDtoList = foreignKeyDtoList.stream().map(foreignKeyDto ->
                    foreignKeyService.edit(foreignKeyDto, operator)).collect(Collectors.toList());
            echoTableInfoDto.setForeignKeys(echoForeignKeyDtoList);
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

        // 删除tableInfo表记录
        devTableInfoDao.update(c -> c.set(devTableInfo.del).equalTo(1).set(devTableInfo.editor).equalTo(operator)
                .where(devTableInfo.id, isEqualTo(tableId)));
        // 删除label表记录
        List<LabelDto> tableLabelDtoList = labelService.findLabels(tableId, null);
        boolean deleteSuccess = tableLabelDtoList.stream().allMatch(tableLabelDto ->
                labelService.removeLabel(tableLabelDto, operator));
        // 删除columnInfo表记录
        List<ColumnInfoDto> columnInfoDtoList = columnInfoService.getColumns(tableId);
        deleteSuccess = deleteSuccess && columnInfoDtoList.stream().allMatch(columnInfoDto ->
                columnInfoService.delete(columnInfoDto.getId(), operator));
        // 删除外键表记录
        List<ForeignKeyDto> foreignKeyDtoList = foreignKeyService.getForeignKeys(tableId);
        deleteSuccess = deleteSuccess && foreignKeyDtoList.stream().allMatch(foreignKeyDto ->
                foreignKeyService.delete(foreignKeyDto.getId(), operator));

        return deleteSuccess;
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }
}
