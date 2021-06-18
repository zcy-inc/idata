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
import cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.tableId;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author caizhedong
 * @date 2021-05-28 15:34
 */

@Service
public class ColumnInfoServiceImpl implements ColumnInfoService {

    @Autowired
    private DevColumnInfoDao devColumnInfoDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevForeignKeyMyDao devForeignKeyMyDaoDao;
    @Autowired
    private LabelService labelService;

    private final String[] columnInfoFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "columnName", "tableId", "columnIndex"};
    private final String COLUMN_SUBJECT = "COLUMN";
    private final String[] columnLabelTags = {LabelTagEnum.STRING_LABEL.name(), LabelTagEnum.BOOLEAN_LABEL.name(),
    LabelTagEnum.USER_LABEL.name(), LabelTagEnum.ENUM_LABEL.name(), LabelTagEnum.ENUM_VALUE_LABEL.name()};

    @Override
    public List<ColumnInfoDto> getColumns(Long tableId) {
        List<ColumnInfoDto> echoList = new ArrayList<>();
        List<DevColumnInfo> columnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        if (columnInfoList.size() > 0) {
            echoList = PojoUtil.copyList(columnInfoList, ColumnInfoDto.class, columnInfoFields);
            List<String> columnNameList = echoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            Map<String, List<LabelDto>> columnInfoMap = labelService.findColumnLabelMap(tableId, columnNameList);
            echoList = echoList.stream().peek(columnInfoDto ->
                    columnInfoDto.setColumnLabels(columnInfoMap.get(columnInfoDto.getColumnName()))
            ).collect(Collectors.toList());
        }

        return echoList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<ColumnInfoDto> createOrEdit(List<ColumnInfoDto> columnInfoDtoList, Long tableId, List<String> columnNameList,
                                            String operator) {
//        checkArgument(isNotEmpty(operator), "创建者不能为空");
//        checkArgument(columnInfoDto.getTableId() != null, "字段所属表ID不能为空");
//        checkArgument(isNotEmpty(columnInfoDto.getColumnName()), "字段名不能为空");
//        DevColumnInfo checkDevColumn = devColumnInfoDao.selectOne(c ->
//                c.where(devColumnInfo.del, isNotEqualTo(1),
//                        and(devColumnInfo.tableId, isEqualTo(columnInfoDto.getTableId())),
//                        and(devColumnInfo.columnName, isEqualTo(columnInfoDto.getColumnName()))))
//                .orElse(null);
//        //checkArgument(checkDevColumn == null, "该表中已存在该字段");
//        List<LabelDefineDto> columnLabelDefineDtoList = labelService.findDefines(COLUMN_SUBJECT, null)
//                .stream()
//                .filter(labelDefineDto -> labelDefineDto.getLabelRequired().equals(1))
//                .filter(labelDefineDto -> Arrays.asList(columnLabelTags).contains(labelDefineDto.getLabelTag()))
//                .collect(Collectors.toList());
//        Map<String, String> columnLabelDefineMap = columnLabelDefineDtoList
//                .stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode, LabelDefineDto::getLabelName));
//        checkArgument(columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0,
//                "缺少字段必要信息");
//        List<LabelDto> columnLabelList = columnInfoDto.getColumnLabels();
//        List<String> columnLabelCodeList = columnLabelList.stream().map(LabelDto::getLabelCode).collect(Collectors.toList());
//        for (Map.Entry<String, String> entry : columnLabelDefineMap.entrySet()) {
//            checkArgument(columnLabelCodeList.contains(entry.getKey()), entry.getValue() + "不能为空");
//        }
//
//        // 插入字段表
//        columnInfoDto.setCreator(operator);
//        DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
//                "tableId", "columnName", "columnIndex", "creator");
//        devColumnInfoDao.insertSelective(columnInfo);
//        ColumnInfoDto echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
//                ColumnInfoDto.class);
//        // 插入label表
//        List<LabelDto> echoColumnLabelList = columnLabelList.stream()
//                .map(columnLabel ->  {
//                    columnLabel.setTableId(columnInfoDto.getTableId());
//                    return labelService.label(columnLabel, operator);})
//                .collect(Collectors.toList());
//        echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
//        return echoColumnInfoDto;
        checkArgument(isNotEmpty(operator), "操作者不能为空");
        checkArgument(tableId != null, "字段所属表ID不能为空");
        List<LabelDefineDto> columnLabelDefineDtoList = labelService.findDefines(COLUMN_SUBJECT, null)
                .stream()
                .filter(labelDefineDto -> labelDefineDto.getLabelRequired().equals(1))
                .filter(labelDefineDto -> Arrays.asList(columnLabelTags).contains(labelDefineDto.getLabelTag()))
                .collect(Collectors.toList());
        Map<String, String> columnLabelDefineMap = columnLabelDefineDtoList
                .stream()
                .collect(Collectors.toMap(LabelDefineDto::getLabelCode, LabelDefineDto::getLabelName));
        List<DevColumnInfo> existColumnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        Map<String, DevColumnInfo> existColumnInfoMap = existColumnInfoList.stream()
                .collect(Collectors.toMap(DevColumnInfo::getColumnName, existColumnInfo -> existColumnInfo));
        List<DevColumnInfo> deleteColumnInfoList = existColumnInfoList.stream()
                .filter(devColumnInfoDto -> !columnNameList.contains(devColumnInfoDto.getColumnName())).collect(Collectors.toList());
        // 与已存在字段比较，删除未传字段记录
        deleteColumnInfoList.forEach(deleteColumnInfo -> deleteColumnInfo(deleteColumnInfo.getId(), operator));

        List<ColumnInfoDto> echoColumnInfoList = columnInfoDtoList.stream().map(columnInfoDto -> {
            checkArgument(isNotEmpty(columnInfoDto.getColumnName()), "字段名不能为空");
            checkArgument(columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0,
                    columnInfoDto.getColumnName() + "字段缺少必要信息");
            List<LabelDto> columnLabelList = columnInfoDto.getColumnLabels();
            List<String> columnLabelCodeList = columnLabelList.stream().map(LabelDto::getLabelCode).collect(Collectors.toList());
            for (Map.Entry<String, String> entry : columnLabelDefineMap.entrySet()) {
                checkArgument(columnLabelCodeList.contains(entry.getKey()), entry.getValue() + "不能为空");
            }
            devColumnInfoDao.selectOne(c ->
                    c.where(devColumnInfo.del, isNotEqualTo(1),
                            and(devColumnInfo.tableId, isEqualTo(columnInfoDto.getTableId())),
                            and(devColumnInfo.columnName, isEqualTo(columnInfoDto.getColumnName()))))
                    .ifPresent(checkDevColumn -> columnInfoDto.setId(checkDevColumn.getId()));
            columnInfoDto.setTableId(tableId);
            boolean isCreate = existColumnInfoMap.containsKey(columnInfoDto.getColumnName());
            if (!isCreate) {
                columnInfoDto.setId(existColumnInfoMap.get(columnInfoDto.getColumnName()).getId());
            }

            return createOrUpdateColumn(columnInfoDto, columnLabelList, isCreate, operator);
        }).collect(Collectors.toList());
        return echoColumnInfoList;
    }

//    @Override
//    @Transactional(rollbackFor = Throwable.class)
//    public ColumnInfoDto edit(ColumnInfoDto columnInfoDto, String operator) {
//        checkArgument(isNotEmpty(operator), "修改者不能为空");
//        // checkArgument(columnInfoDto.getId() != null, "字段ID不能为空");
//        DevColumnInfo checkDevColumn = devColumnInfoDao.selectOne(c ->
//                c.where(devColumnInfo.del, isNotEqualTo(1),
//                        and(devColumnInfo.tableId, isEqualTo(columnInfoDto.getTableId())),
//                        and(devColumnInfo.columnName, isEqualTo(columnInfoDto.getColumnName()))))
//                .orElse(null);
//        //checkArgument(checkDevColumn != null, "该表中不存在该字段");
//
//        // 更新字段表
//        columnInfoDto.setEditor(operator);
//        DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
//                "id", "columnName", "columnIndex", "creator");
//        devColumnInfoDao.updateByPrimaryKeySelective(columnInfo);
//        ColumnInfoDto echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
//                ColumnInfoDto.class);
//        // 更新label表
//        List<LabelDto> columnLabelList = columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0
//                ? columnInfoDto.getColumnLabels() : null;
//        if (columnLabelList != null) {
//            List<LabelDto> echoColumnLabelList = columnLabelList.stream()
//                    .map(columnLabel -> {
//                        columnLabel.setTableId(columnInfoDto.getTableId());
//                        return labelService.label(columnLabel, operator);})
//                    .collect(Collectors.toList());
//            echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
//        }
//        return echoColumnInfoDto;
//    }

    private ColumnInfoDto createOrUpdateColumn(ColumnInfoDto columnInfoDto, List<LabelDto> columnLabelList,
                                               Boolean isCreate, String operator) {
        ColumnInfoDto echoColumnInfoDto;
        List<LabelDto> echoColumnLabelList;
        if (isCreate) {
            columnInfoDto.setCreator(operator);
            DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
                    "tableId", "columnName", "columnIndex", "creator");
            devColumnInfoDao.insertSelective(columnInfo);
            echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
                    ColumnInfoDto.class);
            // 插入label表
            echoColumnLabelList = columnLabelList.stream()
                    .map(columnLabel ->  {
                        columnLabel.setTableId(columnInfoDto.getTableId());
                        return labelService.label(columnLabel, operator);})
                    .collect(Collectors.toList());
            echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
        }
        else {
            columnInfoDto.setEditor(operator);
            DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
                    "id", "columnName", "columnIndex", "editor");
            devColumnInfoDao.updateByPrimaryKeySelective(columnInfo);
            echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
                    ColumnInfoDto.class);
            // 更新label表
            columnLabelList = columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0
                    ? columnInfoDto.getColumnLabels() : null;
            if (columnLabelList != null) {
                List<LabelDto> existColumnLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(tableId)),
                                and(devLabel.columnName, isEqualTo(columnInfoDto.getColumnName())))
                        .build().render(RenderingStrategies.MYBATIS3)),
                        LabelDto.class, "id", "tableId", "labelCode", "columnName");
                List<String> columnLabelCodeList = columnLabelList.stream().map(LabelDto::getLabelCode).collect(Collectors.toList());
                List<LabelDto> deleteColumnLabelList = existColumnLabelList.stream()
                        .filter(existColumnLabel -> !columnLabelCodeList.contains(existColumnLabel.getLabelCode()))
                        .collect(Collectors.toList());
                // 删除字段不再打标的记录
                deleteColumnLabelList.forEach(deleteColumnLabel -> labelService.removeLabel(deleteColumnLabel, operator));
                echoColumnLabelList = columnLabelList.stream()
                        .map(columnLabel -> {
                            columnLabel.setTableId(columnInfoDto.getTableId());
                            return labelService.label(columnLabel, operator);
                        })
                        .collect(Collectors.toList());
                echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
            }
        }
        return echoColumnInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long columnId, String operator) {
        return deleteColumnInfo(columnId, operator);
    }

    private boolean deleteColumnInfo(Long columnId, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(columnId != null, "字段ID不能为空");
        DevColumnInfo columnInfo = devColumnInfoDao.selectOne(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1),
                        and(devColumnInfo.id, isEqualTo(columnId))))
                .orElse(null);
        checkArgument(columnInfo != null, "字段不存在");
        DevForeignKey checkForeignKey = devForeignKeyMyDaoDao.selectForeignKeyByRefer(columnInfo.getTableId(),
                columnInfo.getColumnName())
                .orElse(null);
        if (checkForeignKey != null) {
            throw new IllegalArgumentException("字段被表" + checkForeignKey.getTableId() + "外键字段依赖，不能删除");
        }

        // 删除字段表记录
        devColumnInfoDao.update(c -> c.set(devColumnInfo.del).equalTo(1)
                .set(devColumnInfo.editor).equalTo(operator)
                .where(devColumnInfo.id, isEqualTo(columnId)));
        // 删除label表记录
        List<LabelDto> columnLabelList = labelService.findLabels(columnInfo.getTableId(), columnInfo.getColumnName());

        return columnLabelList.stream().allMatch(columnLabel ->
                labelService.removeLabel(columnLabel, operator));
    }
}
