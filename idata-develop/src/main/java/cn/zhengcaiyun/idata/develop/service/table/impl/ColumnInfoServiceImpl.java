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
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoDTO;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.label.SubjectTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import jdk.jfr.Label;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.labelTag;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.tableId;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
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
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private TableInfoService tableInfoService;

    @Autowired
    private DevTableInfoDao devTableInfoDao;

    private final String[] columnInfoFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "columnName", "tableId", "columnIndex"};
    private final String COLUMN_ATTRIBUTE = "columnAttribute:LABEL";
    private final String COLUMN_DIMENSION = "DIMENSION:ENUM_VALUE";
    private final String COLUMN_SUBJECT = "COLUMN";
    private final String COLUMN_TYPE_ENUM = "hiveColTypeEnum:ENUM";
    private final String COLUMN_COMMENT_LABEL = "columnComment:LABEL";
    private final String COLUMN_TYPE_LABEL = "columnType:LABEL";
    private final String COLUMN_PK_LABEL = "pk:LABEL";
    private final String COLUMN_SECURITY_LABEL = "colSecurityLevel:LABEL";
    private final String COLUMN_SECURITY_ENUM = "securityLevelEnum:ENUM";
    private final String COLUMN_DESCRIPTION_LABEL = "columnDescription:LABEL";
    private final String COLUMN_PARTITION_LABEL = "partitionedCol:LABEL";
    private final String[] columnLabelTags = {LabelTagEnum.STRING_LABEL.name(), LabelTagEnum.BOOLEAN_LABEL.name(),
    LabelTagEnum.USER_LABEL.name(), LabelTagEnum.ENUM_LABEL.name(), LabelTagEnum.ENUM_VALUE_LABEL.name()};

    @Override
    public List<ColumnInfoDto> getColumns(Long tableId) {
        List<ColumnInfoDto> echoList = new ArrayList<>();
        List<DevColumnInfo> devColumnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        if (devColumnInfoList.size() > 0) {
            List<ColumnInfoDto> columnInfoList = PojoUtil.copyList(devColumnInfoList, ColumnInfoDto.class, columnInfoFields);
            List<String> columnNameList = columnInfoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            Map<String, List<LabelDto>> columnInfoMap = labelService.findColumnLabelMap(tableId, columnNameList);
            Map<Integer, ColumnInfoDto> columnIndexMap = columnInfoList
                    .stream().collect(Collectors.toMap(ColumnInfoDto::getColumnIndex, Function.identity()));
            List<ColumnInfoDto> indexColumnList = new ArrayList<>();
            for (int i = 0; i < columnIndexMap.size(); i++) {
                indexColumnList.add(columnIndexMap.get(i));
            }
            echoList = indexColumnList.stream().peek(columnInfoDto -> {
                List<LabelDto> columnLabelList = columnInfoMap.get(columnInfoDto.getColumnName());
                columnInfoDto.setColumnLabels(columnLabelList);
                String isPk = columnLabelList.stream().filter(columnLabel -> COLUMN_PK_LABEL.equals(columnLabel.getLabelCode()))
                        .findFirst().get().getLabelParamValue();
                columnInfoDto.setPk(Boolean.valueOf(isPk));
                String columnAttributeCode = columnLabelList.stream().filter(columnLabel -> COLUMN_ATTRIBUTE.equals(columnLabel.getLabelCode()))
                        .findFirst().get().getLabelParamValue();
                columnInfoDto.setColumnAttributeCode(columnAttributeCode);
            }).collect(Collectors.toList());
        }

        return echoList;
    }

    @Override
    public List<ColumnInfoDto> getDimensionColumns(String metricCode, Long tableId) {
        List<DevLabel> existLabelList = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(metricCode))));
        if (existLabelList.size() == 0) return new ArrayList<>();
        List<ColumnInfoDto> columnList = getColumns(tableId);
        if (!existLabelList.get(0).getTableId().equals(tableId)) return columnList;

        return columnList.stream().filter(column -> StringUtils.isNotEmpty(column.getColumnAttributeCode())
                && COLUMN_ATTRIBUTE.equals(column.getColumnAttributeCode())).collect(Collectors.toList());
    }

//    @Override
//    @Deprecated
//    public void compareColumns(TableInfoDto tableInfo) {
//        Long tableId = tableInfo.getId();
//        if (tableInfo.getHiveTableName() != null) {
//            // 同步过hive的表才进行比较
//            // 将多的字段、少的字段、不同的字段都获取到
//            List<ColumnInfoDto> localColumnInfos = tableInfo.getColumnInfos();
//            Set<String> diffColumnNameList = new HashSet<>();
//            CompareInfoDTO compareInfoDTO = tableInfoService.compareHiveInfo(tableId);
//            diffColumnNameList.addAll(compareInfoDTO.getLessList().stream().map(CompareInfoDTO.BasicColumnInfo::getColumnName).collect(Collectors.toSet()));
//            diffColumnNameList.addAll(compareInfoDTO.getMoreList().stream().map(CompareInfoDTO.BasicColumnInfo::getColumnName).collect(Collectors.toSet()));
//            diffColumnNameList.addAll(compareInfoDTO.getDifferentList().stream().map(CompareInfoDTO.ChangeColumnInfo::getColumnName).collect(Collectors.toSet()));
//            localColumnInfos.stream().forEach(e -> {
//                e.setEnableCompare(true);
//                e.setHiveDiff(diffColumnNameList.contains(e.getColumnName()));
//            });
//        }
//    }

    @Override
    public List<ColumnDetailsDto> getColumnDetails(Long tableId) {
        List<ColumnDetailsDto> echoList = new ArrayList<>();
        List<DevColumnInfo> devColumnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        if (devColumnInfoList.size() > 0) {
            Map<String, String> columnTypeEnumMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                    and(devEnumValue.enumCode, isEqualTo(COLUMN_TYPE_ENUM))))
                    .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
            Map<String, String> columnSecurityEnumMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                    and(devEnumValue.enumCode, isEqualTo(COLUMN_SECURITY_ENUM))))
                    .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
            List<ColumnInfoDto> columnInfoList = PojoUtil.copyList(devColumnInfoList, ColumnInfoDto.class, columnInfoFields);
            List<String> columnNameList = columnInfoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            Map<String, List<LabelDto>> columnInfoMap = labelService.findColumnLabelMap(tableId, columnNameList);
            columnInfoList = columnInfoList.stream().peek(columnInfoDto ->
                    columnInfoDto.setColumnLabels(columnInfoMap.get(columnInfoDto.getColumnName()))
            ).collect(Collectors.toList());
            Map<Integer, ColumnInfoDto> columnIndexMap = columnInfoList
                    .stream().collect(Collectors.toMap(ColumnInfoDto::getColumnIndex, Function.identity()));
            List<ColumnInfoDto> indexColumnList = new ArrayList<>();
            for (int i = 0; i < columnIndexMap.size(); i++) {
                indexColumnList.add(columnIndexMap.get(i));
            }
            echoList = indexColumnList.stream().map(columnInfo -> {
                ColumnDetailsDto echo = PojoUtil.copyOne(columnInfo, ColumnDetailsDto.class);
                columnInfo.getColumnLabels().forEach(columnLabel -> {
                    if (columnLabel.getLabelCode().equals(COLUMN_COMMENT_LABEL)) {
                        echo.setColumnComment(columnLabel.getLabelParamValue());
                    }
                    else if (columnLabel.getLabelCode().equals(COLUMN_TYPE_LABEL)) {
                        echo.setColumnType(columnTypeEnumMap.get(columnLabel.getLabelParamValue()));
                    }
                    else if (columnLabel.getLabelCode().equals(COLUMN_PK_LABEL)) {
                        echo.setPk(Boolean.valueOf(columnLabel.getLabelParamValue()));
                    }
                    else if (columnLabel.getLabelCode().equals(COLUMN_SECURITY_LABEL)) {
                        echo.setSecurityLevel(columnSecurityEnumMap.get(columnLabel.getLabelParamValue()));
                    }
                    else if (columnLabel.getLabelCode().equals(COLUMN_DESCRIPTION_LABEL)) {
                        echo.setColumnDescription(columnLabel.getLabelParamValue());
                    }
                    else if (columnLabel.getLabelCode().equals(COLUMN_PARTITION_LABEL)) {
                        echo.setPartitionedColumn(columnLabel.getLabelParamValue());
                    }
                });
                return echo;
            }).collect(Collectors.toList());
        }

        return echoList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<ColumnInfoDto> createOrEdit(List<ColumnInfoDto> columnInfoDtoList, Long tableId, List<Long> columnIdList,
                                            String operator) {
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
        Map<Long, DevColumnInfo> existColumnInfoMap = existColumnInfoList.stream()
                .collect(Collectors.toMap(DevColumnInfo::getId, existColumnInfo -> existColumnInfo));
        List<DevColumnInfo> deleteColumnInfoList = existColumnInfoList.stream()
                .filter(devColumnInfoDto -> !columnIdList.contains(devColumnInfoDto.getId())).collect(Collectors.toList());
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
            boolean isCreate = !existColumnInfoMap.containsKey(columnInfoDto.getId());
            if (!isCreate) {
                columnInfoDto.setId(existColumnInfoMap.get(columnInfoDto.getId()).getId());
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
                        columnLabel.setColumnId(echoColumnInfoDto.getId());
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
                List<String> columnLabelDefineCodeList = labelService.findDefines(SubjectTypeEnum.COLUMN.name(), null)
                        .stream().map(LabelDefineDto::getLabelCode).collect(Collectors.toList());
                List<LabelDto> existColumnLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1),
                                and(devLabel.hidden, isEqualTo(0)),
                                and(devLabel.tableId, isEqualTo(columnInfoDto.getTableId())),
                                and(devLabel.columnName, isEqualTo(columnInfoDto.getColumnName())),
                                and(devLabel.labelCode, isIn(columnLabelDefineCodeList)))
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
                            columnLabel.setColumnId(columnInfoDto.getId());
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

    @Override
    public boolean checkColumn(String columnName, Long tableId) {
        List<DevColumnInfo> columnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        return columnInfoList.stream().map(DevColumnInfo::getColumnName).collect(Collectors.toList()).contains(columnName);
    }

    @Override
    public List<String> getColumnNames(Long tableId) {
        return devColumnInfoDao.selectMany(
                select(devColumnInfo.columnName)
                .from(devColumnInfo)
                .where(devColumnInfo.tableId, isEqualTo(tableId), and(devColumnInfo.del, isEqualTo(DEL_NO.val)))
                        .build().render(RenderingStrategies.MYBATIS3))
                .stream()
                .map(DevColumnInfo::getColumnName)
                .collect(Collectors.toList());
    }


    @Override
    public List<DevColumnInfo> getColumnInfo(Long tableId) {
        return devColumnInfoDao.selectMany(
                        select(devColumnInfo.allColumns())
                                .from(devColumnInfo)
                                .where(devColumnInfo.tableId, isEqualTo(tableId), and(devColumnInfo.del, isEqualTo(DEL_NO.val)))
                                .build().render(RenderingStrategies.MYBATIS3))
                .stream()
                .collect(Collectors.toList());
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
            throw new IllegalArgumentException(columnInfo.getColumnName() + "字段被表" + checkForeignKey.getTableId() + "外键字段依赖，不能删除");
        }
        // 校验指标系统依赖
        Map<String, List<DevLabel>> measureColumnMap = devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(columnInfo.getTableId())),
                        and(devLabelDefine.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name()),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name())),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL.name()))))
                .build().render(RenderingStrategies.MYBATIS3))
                .stream().collect(Collectors.groupingBy(DevLabel::getColumnName));
        if (measureColumnMap.size() > 0) {
            checkArgument(!measureColumnMap.containsKey(columnInfo.getColumnName()),
                    labelService.findDefine(measureColumnMap.get(columnInfo.getColumnName()).get(0).getLabelCode())
                            .getLabelName() + "依赖" + columnInfo.getColumnName() + "字段，不能删除");
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
