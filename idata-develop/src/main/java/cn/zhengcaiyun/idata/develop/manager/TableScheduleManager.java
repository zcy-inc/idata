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
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.SensitiveColumnDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author caizhedong
 * @date 2022-06-08 上午10:22
 */

@Component
public class TableScheduleManager {

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

    private final Long DEFAULT_LIMIT = 20000L;
    private final String DW_LAYER_ODS = "DW_LAYER_ODS:ENUM_VALUE";
    private final String[] tableLabelCodes = {"dbName:LABEL", "partitionedTbl:LABEL", "tblSecurityLevel:LABEL", "dwLayer:LABEL", "tblComment:LABEL", "dwOwnerId:LABEL",
            "pwOwnerId:LABEL", "domainId:LABEL"};
    private final String[] columnLabelCodes = {"pk:LABEL", "columnType:LABEL", "colSecurityLevel:LABEL", "partitionedCol:LABEL"};
    private final String SECURITY_LEVEL_LOW_CODE = "SECURITY_LEVEL_LOW:ENUM_VALUE";

    public List<TableInfoDto> syncTableColumnsSecurity() {
        // 获取DI作业同步表及字段（表名、字段名、字段类型）（暂不通过hdfs获取）
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
        Map<Long, List<DIJobContent>> diMap = diContentPublishList.stream().collect(Collectors.groupingBy(DIJobContent::getId));
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
        // 与需要同步的表比较，获取新增表list、修改表list（比较字段数量变化、字段名变化、安全等级变化）
        List<TableInfoDto> odsNonSensitiveTableList = getTableList(nonSensitiveDiJobList, SECURITY_LEVEL_LOW_CODE);
        List<TableInfoDto> odsSensitiveTableList = getTableList(sensitiveDiJobList, null);
        return null;
    }

    // TODO dbName和tableName待数据落数仓后修改
    private List<SensitiveColumnDto> queryColumnSecurity() {
        String dbName = "dbName";
        String tableName = "tableName";
        QueryResultDto resultDto = dataQueryApi.queryData(dbName, tableName, DEFAULT_LIMIT, 0L);
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < resultDto.getMeta().size(); i++) {
            columnIndexMap.put(resultDto.getMeta().get(i).getColumnName(), i);
        }
        List<SensitiveColumnDto> sensitiveColumnList = resultDto.getData().stream().map(data -> {
            SensitiveColumnDto sensitiveColumn = new SensitiveColumnDto();
            sensitiveColumn.setColumnName(data.get(columnIndexMap.get("columnname")));
            sensitiveColumn.setTableName(data.get(columnIndexMap.get("tablename")));
            sensitiveColumn.setSchemaName(data.get(columnIndexMap.get("schemaname")));
            sensitiveColumn.setSecurityLevel(data.get(columnIndexMap.get("securitylevel")));
            return sensitiveColumn;
        }).collect(Collectors.toList());
        return sensitiveColumnList;
    }

    // TODO，securityLevelCode为空则动态查询
    private List<TableInfoDto> getTableList(List<DIJobContent> diJobList, String securityLevelCode) {
        return new ArrayList<>();
    }

    private String changeColSecurityLevel(String securityLevel) {
        if ("SENSITIVE".equals(securityLevel)) {
            return "SECURITY_LEVEL_MEDIUM:ENUM_VALUE";
        }
        else if ("CONFIDENTIAL".equals(securityLevel)) {
            return "SECURITY_LEVEL_HIGH:ENUM_VALUE";
        }
        else {
            return "SECURITY_LEVEL_LOW:ENUM_VALUE";
        }
    }
}
