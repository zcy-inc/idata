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

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import cn.zhengcaiyun.idata.connector.util.CreateTableListener;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.table.*;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.service.table.MetabaseService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import com.jayway.jsonpath.internal.function.ParamType;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
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
    @Autowired
    private MetabaseService metabaseService;
    @Autowired
    private DevTreeNodeLocalCache devTreeNodeLocalCache;
    @Autowired
    private EnumService enumService;
    @Autowired
    private UserAccessService userAccessService;

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
    private final String COLUMN_DESCRIPTION_LABEL = "columnDescription:LABEL";
    private final String COLUMN_PK_LABEL = "pk:LABEL";
    private final String DATA_DEVELOP_ACCESS_CODE = "F_MENU_DATA_DEVELOP";

    @Override
    public TableInfoDto getTableInfo(Long tableId) {
        return getTableInfoById(tableId);
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
    public TableInfoDto syncTableInfoByDDL(TableDdlDto tableDdlDto) {
        checkArgument(tableDdlDto.getTableId() != null, "tableId不能为空");
        checkArgument(isNotEmpty(tableDdlDto.getTableDdl()), "ddl不能为空");
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.del, isNotEqualTo(1),
                and(devTableInfo.id, isEqualTo(tableDdlDto.getTableId())))).orElse(null);
        checkArgument(tableInfo != null, "表id不存在");

        // 表相关
        Map<String, Object> tableInfoMap = getCreateTableInfo(tableDdlDto.getTableDdl());
        TableInfoDto existTableInfo = getTableInfo(tableDdlDto.getTableId());
        TableInfoDto echo = PojoUtil.copyOne(existTableInfo);
        checkArgument(tableInfoMap.containsKey("tblName"), "DDL表名解析失败");
        echo.setTableName((String) tableInfoMap.get("tblName"));
        if (tableInfoMap.get("dbName") != null) {
            echo.getTableLabels().forEach(tableLabel -> {
                if (DB_NAME_LABEL.equals(tableLabel.getLabelCode())) {
                    tableLabel.setLabelParamValue((String) tableInfoMap.get("dbName"));
                }
            });
        }
        if (tableInfoMap.get("tblComment") != null) {
            echo.getTableLabels().forEach(tableLabel -> {
                if (TABLE_COMMENT_LABEL.equals(tableLabel.getLabelCode())) {
                    tableLabel.setLabelParamValue((String) tableInfoMap.get("tblComment"));
                }
            });
        }
        // 字段相关
        List<ColumnInfoDto> ddlColumnInfoList = getColumnInfosByDdl(tableInfoMap, existTableInfo.getColumnInfos(), tableDdlDto.getTableId());
        echo.setColumnInfos(ddlColumnInfoList);

        return echo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TableInfoDto create(TableInfoDto tableInfoDto, String operator) {
        checkArgument(userAccessService.checkAddAccess(OperatorContext.getCurrentOperator().getId(), tableInfoDto.getFolderId(),
                DATA_DEVELOP_ACCESS_CODE, ResourceTypeEnum.R_DATA_DEVELOP_DIR.name()), "无添加权限");

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
        // clear cache
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_TABLE);

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
        checkArgument(userAccessService.checkUpdateAccess(OperatorContext.getCurrentOperator().getId(),
                checkTableInfo.getFolderId(), tableInfoDto.getFolderId(), ResourceTypeEnum.R_DATA_DEVELOP_DIR.name()),
                "无权限，请联系管理员");

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
        if (columnInfoDtoList == null) {
            List<Long> existColumnIdList = columnInfoService.getColumns(tableInfoDto.getId())
                    .stream().map(ColumnInfoDto::getId).collect(Collectors.toList());
            existColumnIdList.forEach(columnId -> columnInfoService.delete(columnId, operator));
        }
        else {
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
        // clear cache
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_TABLE);

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
        checkArgument(userAccessService.checkDeleteAccess(OperatorContext.getCurrentOperator().getId(),
                tableInfo.getFolderId(), ResourceTypeEnum.R_DATA_DEVELOP_DIR.name()),
                "无权限，请联系管理员");


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
        // clear cache
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_TABLE);

        return deleteSuccess;
    }

    @Override
    public String syncMetabaseInfo(Long tableId, String editor) {
        checkArgument(isNotEmpty(editor), "编辑者不能为空");
        if (METABASE_DATASOURCE_JDBCURL == null || METABASE_DATASOURCE_USERNAME == null || METABASE_DATASOURCE_PASSWORD == null) return null;
        TableInfoDto tableInfo = getTableInfo(tableId);
        TableDetailDto tableDetail = PojoUtil.copyOne(tableInfo, TableDetailDto.class, "id", "tableName", "dbName");
        tableDetail.setTableComment(tableInfo.getTableLabels().stream().filter(tableLabel ->
                TABLE_COMMENT_LABEL.equals(tableLabel.getLabelCode())).findAny().get().getLabelParamValue());
        List<TableDetailDto> syncMetabaseTblsList = new ArrayList<>();
        syncMetabaseTblsList.add(tableDetail);
        List<ColumnDetailsDto> columnsList = tableInfo.getColumnInfos().stream().map(columnInfo -> {
            ColumnDetailsDto echoColumnInfoDetail = new ColumnDetailsDto();
            echoColumnInfoDetail.setColumnName(columnInfo.getColumnName());
            echoColumnInfoDetail.setTableName(tableInfo.getTableName());
            echoColumnInfoDetail.setDbName(tableInfo.getDbName());
            String columnComment = columnInfo.getColumnLabels().stream().filter(columnLabel ->
                            columnLabel.getLabelCode().equals(COLUMN_COMMENT_LABEL)).findAny().orElse(null) != null
                    ? columnInfo.getColumnLabels().stream().filter(columnLabel ->
                    columnLabel.getLabelCode().equals(COLUMN_COMMENT_LABEL)).findAny().get().getLabelParamValue() : null;
            echoColumnInfoDetail.setColumnComment(columnComment);
            String columnDescription = columnInfo.getColumnLabels().stream().filter(columnLabel ->
                    columnLabel.getLabelCode().equals(COLUMN_DESCRIPTION_LABEL)).findAny().orElse(null) != null
                    ? columnInfo.getColumnLabels().stream().filter(columnLabel ->
                    columnLabel.getLabelCode().equals(COLUMN_DESCRIPTION_LABEL)).findAny().get().getLabelParamValue() : null;
            echoColumnInfoDetail.setColumnDescription(columnDescription);
            return echoColumnInfoDetail;
        }).collect(Collectors.toList());
        Map<String, List<ColumnDetailsDto>> syncMetabaseColMap = new HashMap<>();
        String dbTblName = tableDetail.getDbName() + "." + tableDetail.getTableName();
        syncMetabaseColMap.put(dbTblName, columnsList);

        Map<String, List<String>> syncEchoMap = metabaseService.syncMetabaseTableInfo(syncMetabaseTblsList, syncMetabaseColMap);
        // 同步字段结果
        List<String> syncMissColsList = syncEchoMap.get("syncMissCols") != null ?
                syncEchoMap.get("syncMissCols") : new ArrayList<>();
        String syncMissColNames = null;
        if (syncMissColsList != null && syncMissColsList.size() > 0) {
            syncMissColNames = String.join(",", syncMissColsList);
        }
        // 同步表结果
        List<String> syncMissTblsList = syncEchoMap.get("syncMissTbls") != null ?
                syncEchoMap.get("syncMissTbls") : new ArrayList<>();
        String syncMissTblNames = null;
        if (syncMissTblsList != null && syncMissTblsList.size() > 0) {
            syncMissTblNames = syncMissTblsList.get(0);
        }

        String colMsg = isEmpty(syncMissColNames) ?
                " columns sync successful!" : syncMissColNames + " columns not found, sync failed!";
        String tblMsg = isEmpty(syncMissTblNames) ?
                "Table sync successful!" : syncMissTblNames + " not found, sync failed!";
        String echoMsg = tblMsg + colMsg;
        return echoMsg;
    }

    @Override
    public TableTechInfoDto getTableTechInfo(Long tableId) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        return metadataQueryApi.getTableTechInfo(getDbName(tableId), tableInfo.getTableName());
    }

    private TableInfoDto getTableInfoById(Long tableId) {
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

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }

    private static Map<String, Object> getCreateTableInfo(String sql) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(sql), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        CreateTableListener listener = new CreateTableListener();
        walker.walk(listener, parser.statement());
        return listener.tableInfoMap;
    }

    private List<ColumnInfoDto> getColumnInfosByDdl(Map<String, Object> tableInfoMap,
                                                    List<ColumnInfoDto> existColumnInfoList, Long tableId) {
        List<ColumnInfoDto> echoColumnInfoList = new ArrayList<>();
        Map<String, ColumnInfoDto> existColumnInfoMap = existColumnInfoList.size() > 0 ?
                existColumnInfoList.stream().collect(Collectors.toMap(ColumnInfoDto::getColumnName, Function.identity()))
                : new HashMap<>();
        Map<String, String> hiveColumnTypeMap = enumService.getEnumValues(COLUMN_TYPE_ENUM)
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        List<Map<String, String>> tableInfoColumnList = new ArrayList<>();
        List<String> partitionColumnList = (tableInfoMap.get("partitionColumns")) != null ?
                ((List<Map<String, String>>) tableInfoMap.get("partitionColumns")).stream()
                .map(columnMap -> columnMap.get("colName")).collect(Collectors.toList()) : new ArrayList<>();
        if (tableInfoMap.get("partitionColumns") != null) {
            tableInfoColumnList.addAll((List<Map<String, String>>) tableInfoMap.get("partitionColumns"));
        }
        if (tableInfoMap.get("columns") != null) {
            tableInfoColumnList.addAll((List<Map<String, String>>) tableInfoMap.get("columns"));
        }
        if (tableInfoColumnList.size() > 0) {
            tableInfoColumnList.stream().forEach(columnMap -> {
                checkColumn(columnMap.get("colName"), columnMap.get("colType"));
                ColumnInfoDto columnInfoDto;
                if (existColumnInfoMap.containsKey(columnMap.get("colName"))) {
                    columnInfoDto = existColumnInfoMap.get(columnMap.get("colName"));
                    columnInfoDto.getColumnLabels().forEach(columnLabel -> {
                        if (COLUMN_TYPE_LABEL.equals(columnLabel.getLabelCode())) {
                            columnLabel.setLabelParamValue(hiveColumnTypeMap.get(columnMap.get("colType")));
                        }
                        if (COLUMN_COMMENT_LABEL.equals(columnLabel.getLabelCode())) {
                            columnLabel.setLabelParamValue(columnMap.get("colComment"));
                        }
                        if (COLUMN_PT_LABEL.equals(columnLabel.getLabelCode())) {
                            if (partitionColumnList.contains(columnMap.get("colName"))) {
                                columnLabel.setLabelParamValue(String.valueOf(true));
                            }
                            else {
                                columnLabel.setLabelParamValue(String.valueOf(false));
                            }
                        }
                    });
                } else {
                    columnInfoDto = new ColumnInfoDto();
                    columnInfoDto.setTableId(tableId);
                    columnInfoDto.setColumnName(columnMap.get("colName"));
                    List<LabelDto> columnLabelList = new ArrayList<>();
                    for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                        LabelDto columnLabel = new LabelDto();
                        columnLabel.setColumnName(columnMap.get("colName"));
                        columnLabel.setTableId(tableId);
                        if ("colComment".equals(entry.getKey())) {
                            columnLabel.setLabelTag(LabelTagEnum.STRING_LABEL.name());
                            columnLabel.setLabelCode(COLUMN_COMMENT_LABEL);
                            columnLabel.setLabelParamValue(columnMap.get("colComment"));
                            columnLabel.setLabelParamType(MetaTypeEnum.STRING.name());
                            columnLabelList.add(columnLabel);
                        }
                        if ("colType".equals(entry.getKey())) {
                            columnLabel.setLabelTag(LabelTagEnum.ENUM_VALUE_LABEL.name());
                            columnLabel.setLabelCode(COLUMN_TYPE_LABEL);
                            columnLabel.setLabelParamValue(hiveColumnTypeMap.get(columnMap.get("colType")));
                            columnLabel.setLabelParamType(COLUMN_TYPE_ENUM);
                            columnLabelList.add(columnLabel);
                        }
                    }
                    LabelDto partitionedLabel = new LabelDto();
                    partitionedLabel.setColumnName(columnMap.get("colName"));
                    partitionedLabel.setLabelTag(LabelTagEnum.BOOLEAN_LABEL.name());
                    partitionedLabel.setLabelCode(COLUMN_PT_LABEL);
                    partitionedLabel.setLabelParamValue(String.valueOf(partitionColumnList.contains(columnMap.get("colName"))));
                    partitionedLabel.setLabelParamType(MetaTypeEnum.BOOLEAN.name());
                    columnLabelList.add(partitionedLabel);
                    LabelDto pkLabel = new LabelDto();
                    pkLabel.setColumnName(columnMap.get("colName"));
                    pkLabel.setLabelTag(LabelTagEnum.BOOLEAN_LABEL.name());
                    pkLabel.setLabelCode(COLUMN_PK_LABEL);
                    pkLabel.setLabelParamValue(String.valueOf(false));
                    pkLabel.setLabelParamType(MetaTypeEnum.BOOLEAN.name());
                    columnLabelList.add(pkLabel);
                    columnInfoDto.setColumnLabels(columnLabelList);
                }
                echoColumnInfoList.add(columnInfoDto);
            });
        }
        for (int i = 0; i < echoColumnInfoList.size(); i++) {
            echoColumnInfoList.get(i).setColumnIndex(i);
        }
        return echoColumnInfoList;
    }

    private void checkColumn(String colName, String colType) {
        checkArgument(isNotEmpty(colName), "字段名称不能为空");
        checkArgument(isNotEmpty(colType), "字段类型不能为空");
        List<String> hiveColumnTypeList = enumService.getEnumValues(COLUMN_TYPE_ENUM)
                .stream().map(EnumValueDto::getEnumValue).collect(Collectors.toList());
        checkArgument(hiveColumnTypeList.contains(colType), colName + "字段，" + colType + "暂不支持");
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
