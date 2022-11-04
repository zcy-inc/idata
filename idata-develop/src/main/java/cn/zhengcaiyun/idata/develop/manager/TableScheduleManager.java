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
package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.api.DataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.service.QueryService;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DiJobDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.SensitiveColumnDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import org.apache.avro.generic.GenericData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

/**
 * @author caizhedong
 * @date 2022-06-08 上午10:22
 */

@Component
public class TableScheduleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoDto.class);

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private JobPublishRecordService jobPublishRecordService;
    @Autowired
    private DIJobContentRepo diJobContentRepo;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DataSourceRepo dataSourceRepo;
    @Autowired
    private DataQueryApi dataQueryApi;
    @Autowired
    private UacUserDao uacUserDao;

    private final Long DEFAULT_LIMIT = 20000L;
    private final String DW_LAYER_ODS = "DW_LAYER_ODS:ENUM_VALUE";
    private final String[] tableLabelCodes = {"dbName:LABEL", "partitionedTbl:LABEL", "tblSecurityLevel:LABEL", "dwLayer:LABEL", "tblComment:LABEL", "dwOwnerId:LABEL",
            "pwOwnerId:LABEL", "domainId:LABEL", "dwOwnerId:LABEL"};
    private final String[] columnLabelCodes = {"pk:LABEL", "columnType:LABEL", "colSecurityLevel:LABEL", "partitionedCol:LABEL"};
    private final String SECURITY_LEVEL_LOW_CODE = "SECURITY_LEVEL_LOW:ENUM_VALUE";

    @Transactional(rollbackFor = Throwable.class)
    public Boolean syncTableColumnsSecurity() throws IllegalAccessException {
        LOGGER.info("******************** 同步ODS字段安全等级开始 ********************");
        // 获取DI作业同步表及字段（表名、字段名、字段类型）（暂不通过hive获取）
        JobPublishRecordCondition publishCondition = new JobPublishRecordCondition();
        publishCondition.setEnvironment(EnvEnum.prod.name());
        publishCondition.setJobTypeCode(JobTypeEnum.DI_BATCH.getCode());
        publishCondition.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        List<JobPublishRecord> diJobPublishList = jobPublishRecordService.findJobs(publishCondition);
        // 获取所有DI线上作业
        Map<Long, Integer> jobIdVersionMap = diJobPublishList.stream()
                .collect(Collectors.toMap(JobPublishRecord::getJobId, JobPublishRecord::getJobContentVersion));
        Set<Long> diJobContentIds = diJobPublishList.stream().map(JobPublishRecord::getJobContentId).collect(Collectors.toSet());
        List<DIJobContent> diJobList = diJobContentRepo.queryList(new ArrayList<>(diJobContentIds));
        List<DIJobContent> diContentPublishList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : jobIdVersionMap.entrySet()) {
            DIJobContent diJobContentRecord = diJobList.stream().filter(c ->
                    entry.getKey().equals(c.getJobId()) && entry.getValue().equals(c.getVersion())).findFirst()
                    .orElse(null);
            diContentPublishList.add(diJobContentRecord);
        }
        // 获取字段安全等级信息，依赖DI作业同步表名，根据sourceName过滤ods作业是否需要打标
        List<SensitiveColumnDto> sensitiveColumnList = queryColumnSecurity();
        Map<String, List<SensitiveColumnDto>> sensitiveSourceColumnMap = sensitiveColumnList.stream()
                .collect(Collectors.groupingBy(SensitiveColumnDto::getSchemaName));
        Set<String> sensitiveSources = sensitiveSourceColumnMap.keySet();
        Map<String, List<String>> sensitiveSourceTblMap = new HashMap<>();
        for (String sensitiveSource : sensitiveSources) {
            List<String> tableNameList = sensitiveSourceColumnMap.get(sensitiveSource).stream()
                    .map(SensitiveColumnDto::getTableName).collect(Collectors.toList());
            sensitiveSourceTblMap.put(sensitiveSource, tableNameList);
        }
        // 获取所有ods作业
        List<DataSource> allDataSourceList = dataSourceRepo.getDataSources();
        Map<Long, String> dataSourceNameMap = allDataSourceList
                .stream().collect(Collectors.toMap(DataSource::getId, DataSource::getName));
        Map<Long, List<DIJobContent>> diMap = diContentPublishList.stream().collect(Collectors.groupingBy(DIJobContent::getSrcDataSourceId));
        Map<String, List<DIJobContent>> nonSensitiveDiSourceJobMap = new HashMap<>();
        Map<String, List<DIJobContent>> sensitiveDiSourceJobMap = new HashMap<>();
        List<DIJobContent> nonSensitiveDiJobList = new ArrayList<>();
        List<DIJobContent> sensitiveDiJobList = new ArrayList<>();
        for (Map.Entry<Long, List<DIJobContent>> entry : diMap.entrySet()) {
            if (dataSourceNameMap.containsKey(entry.getKey())) {
                // 未打标安全等级ods表
                if (!sensitiveSources.contains(dataSourceNameMap.get(entry.getKey()))) {
                    nonSensitiveDiSourceJobMap.put(dataSourceNameMap.get(entry.getKey()), entry.getValue());
                    nonSensitiveDiJobList.addAll(entry.getValue());
                }
                else {
                    List<DIJobContent> diJobContentList = entry.getValue();
                    // 打标的数据源，按表分类是否需要被打标
                    List<DIJobContent> nonDiJobContentList = diJobContentList.stream()
                            .filter(c -> !sensitiveSourceTblMap.get(dataSourceNameMap.get(entry.getKey())).contains(c.getSrcTables()))
                            .collect(Collectors.toList());
                    List<DIJobContent> sensitiveDiJobContentList = diJobContentList.stream()
                            .filter(c -> sensitiveSourceTblMap.get(dataSourceNameMap.get(entry.getKey())).contains(c.getSrcTables()))
                            .collect(Collectors.toList());
                    // 无需打标
                    nonSensitiveDiSourceJobMap.put(dataSourceNameMap.get(entry.getKey()), nonDiJobContentList);
                    nonSensitiveDiJobList.addAll(nonDiJobContentList);
                    // 打标
                    sensitiveDiSourceJobMap.put(dataSourceNameMap.get(entry.getKey()), sensitiveDiJobContentList);
                    sensitiveDiJobList.addAll(sensitiveDiJobContentList);
                }
            }
        }
        // 获取数仓设计ods层所有表
        List<TableInfoDto> existOdsTableList = tableInfoService.getRequiredTablesInfoByDataBase(DW_LAYER_ODS);
        Map<String, TableInfoDto> existOdsTableMap = existOdsTableList.stream().collect(Collectors.toMap(TableInfoDto::getTableName, Function.identity()));
        Set<String> existOdsTableNames = existOdsTableMap.keySet();
        // 与需要同步的表比较，获取新增表list、修改表list
        List<TableInfoDto> odsNonSensitiveTableList = changeDiToTableList(nonSensitiveDiJobList, SECURITY_LEVEL_LOW_CODE, dataSourceNameMap);
        List<TableInfoDto> odsSensitiveTableList = changeDiToTableList(sensitiveDiJobList, null, dataSourceNameMap);
        // 新增无需打标的ods表
        List<TableInfoDto> newOdsNonSensitiveTableList = odsNonSensitiveTableList.stream()
                .filter(c -> !existOdsTableNames.contains(c.getTableName())).collect(Collectors.toList());
        // 新增需要打标的ods表
        List<TableInfoDto> newOdsSensitiveTableList = odsSensitiveTableList.stream()
                .filter(c -> !existOdsTableNames.contains(c.getTableName())).collect(Collectors.toList());
        // 修改的ods表（当前仅比较字段数量）TODO 后续增加比较字段重命名、安全等级变化
        // 无需打标的ods变更表
        List<TableInfoDto> changeOdsNonSensitiveTableList = odsNonSensitiveTableList.stream()
                .filter(c -> existOdsTableNames.contains(c.getTableName())
                        && existOdsTableMap.get(c.getTableName()).getColumnInfos().size() != c.getColumnInfos().size()).
                        collect(Collectors.toList());
        // 需打标的ods表更表
        List<TableInfoDto> changeOdsSensitiveTableList = odsSensitiveTableList.stream()
                .filter(c -> existOdsTableNames.contains(c.getTableName())
                        && existOdsTableMap.get(c.getTableName()).getColumnInfos().size() != c.getColumnInfos().size()).
                        collect(Collectors.toList());

        LOGGER.info("******************** 数仓设计新增无敏感字段ODS表，共{}张 ********************", newOdsNonSensitiveTableList.size());
        LOGGER.info("******************** 数仓设计新增敏感字段ODS表，共{}张 ********************", newOdsSensitiveTableList.size());
        LOGGER.info("******************** 数仓设计修改无敏感字段ODS表，共{}张 ********************", changeOdsNonSensitiveTableList.size());
        LOGGER.info("******************** 数仓设计修改敏感字段ODS表，共{}张 ********************", changeOdsSensitiveTableList.size());

        // 新增ods表
        List<TableInfoDto> addList = new ArrayList<>();
        addList.addAll(newOdsNonSensitiveTableList);
        addList.addAll(newOdsSensitiveTableList);
        TableInfoDto echoTable;
        for (TableInfoDto tableInfoDto : addList) {
//            LOGGER.info("******************** 数仓设计新增ODS表，表名为：{} ********************", tableInfoDto.getTableName());
            echoTable = tableInfoService.create(tableInfoDto, "系统管理员");
        }
        LOGGER.info("******************** 数仓设计新增ODS表，共{}张 ********************", addList.size());
        // 修改ods表
        List<TableInfoDto> updateList = getUpdateTableList(changeOdsNonSensitiveTableList, existOdsTableMap);
        updateList.addAll(getUpdateTableList(changeOdsSensitiveTableList, existOdsTableMap));
        for (TableInfoDto tableInfoDto : updateList) {
//            LOGGER.info("******************** 数仓设计修改ODS表，表名为：{} ********************", tableInfoDto.getTableName());
            echoTable = tableInfoService.edit(tableInfoDto, "系统管理员");
        }
        LOGGER.info("******************** 数仓设计修改ODS表，共{}张 ********************", updateList.size());
        return true;
    }

    // dbName和tableName待数据落数仓后修改，获取数仓字段安全等级
    private List<SensitiveColumnDto> queryColumnSecurity() {
        String dbName = "ods";
        String tableName = "ods_db_data_porter_sensitive_column";
        QueryResultDto resultDto = dataQueryApi.queryData(dbName, tableName, null, DEFAULT_LIMIT, 0L);
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < resultDto.getMeta().size(); i++) {
            columnIndexMap.put(resultDto.getMeta().get(i).getColumnName(), i);
        }
        List<SensitiveColumnDto> sensitiveColumnList = resultDto.getData().stream().map(data -> {
            SensitiveColumnDto sensitiveColumn = new SensitiveColumnDto();
            sensitiveColumn.setColumnName(data.get(columnIndexMap.get("columnname")));
            sensitiveColumn.setTableName(data.get(columnIndexMap.get("tablename")));
            sensitiveColumn.setSchemaName(data.get(columnIndexMap.get("schemaname")));
            sensitiveColumn.setSecurityLevel(changeColSecurityLevel(data.get(columnIndexMap.get("securitylevel"))));
            return sensitiveColumn;
        }).collect(Collectors.toList());
        return sensitiveColumnList;
    }

    // 将diJob修改为tableInfo，securityLevelCode为空则动态查询
    private List<TableInfoDto> changeDiToTableList(List<DIJobContent> diJobList, String securityLevelCode,
                                                   Map<Long, String> dataSourceMap) {
        Map<String, String> tableSecurityMap = getTableSecurityMap();
        Map<String, String> columnSecurityMap = getColumnSecurityMap();
        Map<String, Long> userMap = uacUserDao.select(c -> c.where(uacUser.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(UacUser::getNickname, UacUser::getId));
        List<TableInfoDto> echoList = diJobList.stream().map(diJob -> {
            TableInfoDto tableInfoDto = new TableInfoDto();
            // 线上ODS的folderId
            tableInfoDto.setFolderId(3L);
            tableInfoDto.setTableName(diJob.getDestTable().split("\\.")[1]);
            List<LabelDto> tableLabelList = new ArrayList<>();
            List<String> tableLabelCodeList = Arrays.asList(tableLabelCodes);
            // 表相关
            for (String labelCode : tableLabelCodeList) {
                LabelDto tableLabel = new LabelDto();
                if ("dbName:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("dbName:LABEL");
                    tableLabel.setLabelParamValue("ods");
                }
                else if ("partitionedTbl:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("partitionedTbl:LABEL");
                    tableLabel.setLabelParamValue("false");
                }
                else if ("pwOwnerId:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("pwOwnerId:LABEL");
                    tableLabel.setLabelParamValue(diJob.getCreator());
                }
                else if ("tblSecurityLevel:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("tblSecurityLevel:LABEL");
                    if (securityLevelCode != null || !dataSourceMap.containsKey(diJob.getSrcDataSourceId())) {
                        tableLabel.setLabelParamValue(securityLevelCode);
                    }
                    else {
                        tableLabel.setLabelParamValue(tableSecurityMap.getOrDefault(dataSourceMap.get(diJob.getSrcDataSourceId()) + "." + diJob.getSrcTables(), SECURITY_LEVEL_LOW_CODE));
                    }
                }
                else if ("dwLayer:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("dwLayer:LABEL");
                    tableLabel.setLabelParamValue("DW_LAYER_ODS:ENUM_VALUE");
                }
                else if ("tblComment:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("tblComment:LABEL");
                    tableLabel.setLabelParamValue(diJob.getDestTable());
                }
                else if ("dwOwnerId:LABEL".equals(labelCode)) {
                    tableLabel.setLabelCode("dwOwnerId:LABEL");
                    // 若找不到用户，则默认大时
                    Long userId = userMap.getOrDefault(StringUtils.isNotEmpty(diJob.getEditor()) ? diJob.getEditor() : diJob.getCreator(), 45L);
                    tableLabel.setLabelParamValue(userId.toString());
                }
                else {
                    tableLabel.setLabelCode("domainId:LABEL");
                    // 元数据域，仅用于打标，需手动插入
                    tableLabel.setLabelParamValue("ODS_DOMAIN:ENUM_VALUE");
                }
                tableLabelList.add(tableLabel);
            }
            tableInfoDto.setTableLabels(tableLabelList);
            // 字段相关
            // 获取字段列表，组成dto
            List<String> columnNameList = StringUtils.isNotEmpty(diJob.getScriptSelectColumns())
                    ? Arrays.asList(diJob.getScriptSelectColumns().split(","))
                    : DIJobContentContentDto.from(diJob).getDestCols().stream().map(MappingColumnDto::getName).collect(Collectors.toList());
            List<ColumnInfoDto> columnList = columnNameList.stream().map(columnName -> {
                ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                List<LabelDto> columnLabelList = new ArrayList<>();
                List<String> columnLabelCodeList = Arrays.asList(columnLabelCodes);
                for (String columnLabelCode : columnLabelCodeList) {
                    LabelDto columnLabel = new LabelDto();
                    columnLabel.setColumnName(columnName);
                    if ("pk:LABEL".equals(columnLabelCode)) {
                        columnLabel.setLabelCode("pk:LABEL");
                        columnLabel.setLabelParamValue("false");
                    }
                    // 暂时默认给STRING
                    else if ("columnType:LABEL".equals(columnLabelCode)) {
                        columnLabel.setLabelCode("columnType:LABEL");
                        columnLabel.setLabelParamValue("HIVE_COL_TYPE_STRING:ENUM_VALUE");
                    }
                    else if ("colSecurityLevel:LABEL".equals(columnLabelCode)) {
                        columnLabel.setLabelCode("colSecurityLevel:LABEL");
                        if (dataSourceMap.containsKey(diJob.getSrcDataSourceId())) {
                            columnLabel.setLabelParamValue(columnSecurityMap
                                    .getOrDefault(dataSourceMap.get(diJob.getSrcDataSourceId()) + "." + diJob.getSrcTables() + "." + columnName,
                                            "SECURITY_LEVEL_LOW:ENUM_VALUE"));
                        }
                        else {
                            columnLabel.setLabelParamValue("SECURITY_LEVEL_LOW:ENUM_VALUE");
                        }
                    }
                    else {
                        columnLabel.setLabelCode("partitionedCol:LABEL");
                        columnLabel.setLabelParamValue("false");
                    }
                    columnLabelList.add(columnLabel);
                }
                columnInfoDto.setColumnName(columnName);
                columnInfoDto.setColumnLabels(columnLabelList);
                columnInfoDto.setColumnIndex(columnNameList.indexOf(columnName));
                return columnInfoDto;
            }).collect(Collectors.toList());
            tableInfoDto.setColumnInfos(columnList);
            return tableInfoDto;
        }).collect(Collectors.toList());
        return echoList;
    }
    
    private List<TableInfoDto> getUpdateTableList(List<TableInfoDto> tableInfoList, Map<String, TableInfoDto> existTableMap) {
        List<TableInfoDto> echoList = new ArrayList<>();
        for (TableInfoDto tableInfoDto : tableInfoList) {
            TableInfoDto existTableInfoDto = existTableMap.get(tableInfoDto.getTableName());
            List<ColumnInfoDto> existColumnInfoList = existTableInfoDto.getColumnInfos();
            Set<String> existColumnNames = existColumnInfoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toSet());
            for (ColumnInfoDto columnInfoDto : tableInfoDto.getColumnInfos()) {
                if (!existColumnNames.contains(columnInfoDto.getColumnName())) {
                    existColumnInfoList.add(columnInfoDto);
                }
            }
            echoList.add(existTableInfoDto);
        }
        return echoList;
    }

    private Map<String, String> getTableSecurityMap() {
        List<SensitiveColumnDto> sensitiveColumnList = queryColumnSecurity();
        Map<String, List<SensitiveColumnDto>> sensitiveColumnMap = sensitiveColumnList.stream()
                .collect(Collectors.groupingBy(sensitiveColumnDto -> sensitiveColumnDto.getSchemaName() + "." + sensitiveColumnDto.getTableName()));
        Map<String, String> tableSecurityMap = new HashMap<>();
        for (Map.Entry<String, List<SensitiveColumnDto>> entry : sensitiveColumnMap.entrySet()) {
            List<SensitiveColumnDto> sensitiveColList = entry.getValue();
            Set<String> sensitiveLevels = sensitiveColList.stream().map(SensitiveColumnDto::getSecurityLevel).collect(Collectors.toSet());
            if (sensitiveLevels.size() == 1) {
                tableSecurityMap.put(entry.getKey(), new ArrayList<>(sensitiveLevels).get(0));
            }
            else {
                // 当前只可能有2种枚举
                tableSecurityMap.put(entry.getKey(), "SECURITY_LEVEL_HIGH:ENUM_VALUE");
            }
        }
        return tableSecurityMap;
    }

    private Map<String, String> getColumnSecurityMap() {
        List<SensitiveColumnDto> sensitiveColumnList = queryColumnSecurity();
        return sensitiveColumnList.stream()
                .collect(Collectors.toMap(
                        sensitiveColumnDto -> sensitiveColumnDto.getSchemaName() + "." + sensitiveColumnDto.getTableName() + "." + sensitiveColumnDto.getColumnName(),
                        SensitiveColumnDto::getSecurityLevel));
    }

    private String changeColSecurityLevel(String securityLevel) {
        if ("CONFIDENTIAL".equals(securityLevel)) {
            return "SECURITY_LEVEL_HIGH:ENUM_VALUE";
        }
        else if ("SENSITIVE".equals(securityLevel)) {
            return "SECURITY_LEVEL_MEDIUM:ENUM_VALUE";
        }
        else {
            return "SECURITY_LEVEL_LOW:ENUM_VALUE";
        }
    }
}
