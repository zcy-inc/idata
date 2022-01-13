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
package cn.zhengcaiyun.idata.merge.data.service.impl;

import cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevFolder;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.folder.CompositeFolderDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.folder.CompositeFolderService;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.table.DwMetaService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.merge.data.service.ModelMigrationService;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

/**
 * @author caizhedong
 * @date 2022-01-13 上午9:59
 */

@Service
public class ModelMigrationServiceImpl implements ModelMigrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMigrationServiceImpl.class);

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private DwMetaService dwMetaService;
    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private EnumService enumService;
    @Autowired
    private DevFolderDao devFolderDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private CompositeFolderService compositeFolderService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean syncModelMigration() {
        // 同步业务过程
        LOGGER.info("*** *** 开始业务过程数据迁移... ... *** ***");
        syncBizProcess();
        LOGGER.info("*** *** 业务过程数据迁移完成... ... *** ***");
        // 同步表和字段
        LOGGER.info("*** *** 开始数仓设计数据迁移... ... *** ***");
        List<String> errorTableNameList = syncTables();
        if (ObjectUtils.isEmpty(errorTableNameList)) {
            LOGGER.info("*** *** 数仓设计数据迁移完成... ... *** ***");
        }
        else {
            LOGGER.info("*** *** 数仓设计同步有误表名： " + String.join(",", errorTableNameList));
        }
        // 同步表外键
        LOGGER.info("*** *** 开始外键数据迁移... ... *** ***");
        syncForeignKeys();
        LOGGER.info("*** *** 外键数据迁移完成... ... *** ***");
        return true;
    }

    private EnumDto syncBizProcess() {
        Map<String, String> bizProcessMap = changeBizProcessCodes(dwMetaService.getBizProcessNames());
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));
        EnumDto enumDto = new EnumDto();
        enumDto.setEnumCode("bizProcessEnum:ENUM");
        enumDto.setEnumName("业务过程");
        enumDto.setFolderId(3L);
        List<EnumValueDto> enumValueList = bizProcessMap.entrySet()
                .stream().map(entry -> {
                    EnumValueDto echoEnumValue = new EnumValueDto();
                    echoEnumValue.setValueCode(entry.getKey());
                    echoEnumValue.setEnumValue(entry.getValue());
                    echoEnumValue.setEnumAttributes(new ArrayList<>());
                    return echoEnumValue;
                }).collect(Collectors.toList());
        enumDto.setEnumValues(enumValueList);
        enumDto.setFolderId(folderMap.get("EnumSystemFolder"));
        return enumService.createOrEdit(enumDto, "系统管理员");
    }

    private List<String> syncTables() {
        List<Map<String, Object>> tableList = dwMetaService.getTables(null);
        List<Map<String, Object>> columnList = dwMetaService.getColumns();
        Map<String, String> userMap = dwMetaService.getUsers();
        Map<String, String> domainMap = dwMetaService.getDomains();
        Map<String, String> bizProcessMap = dwMetaService.getBizProcesses();

        Map<String, Long> idataUserMap = uacUserDao.select(c -> c.where(uacUser.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(UacUser::getNickname, UacUser::getId));
        Map<String, String> idataDomainMap = enumService.getEnumValues("domainIdEnum:ENUM")
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        Map<String, String> idataLayerMap = enumService.getEnumValues("dwLayerEnum:ENUM")
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        Map<String, String> idataBizProcessMap = enumService.getEnumValues("bizProcessEnum:ENUM")
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        Map<Long, Long> folderMap = new HashMap<>();
        // 文件夹新逻辑
        folderMap = compositeFolderService.getFolders("DESIGN.TABLE")
                .stream().collect(Collectors.toMap(tableFolder -> Long.valueOf(tableFolder.getName().split("#_")[0]),
                        CompositeFolderDto::getId));

        Map<String, String> idataColTypeMap = enumService.getEnumValues("hiveColTypeEnum:ENUM")
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        Map<String, Long> idataTableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevTableInfo::getTableName, DevTableInfo::getId));

        Map<String, Map<String, Object>> tableMap = new HashMap<>();
        Map<String, List<Map<String, Object>>> columnMap = new HashMap<>();
        tableList.forEach(record -> {
            if (record != null) {
                if (record.get("biz_process_ids") != null) {
                    record.replace("biz_process_ids", record.get("biz_process_ids").toString());
                }
                if (record.get("metabase_urls") != null) {
                    record.replace("metabase_urls", record.get("metabase_urls").toString());
                }
            }
            tableMap.put(record.get("id").toString(), record);
        });
        columnList.forEach(record -> {
            List<Map<String, Object>> columns;
            if (!columnMap.containsKey(record.get("table_id").toString())) {
                columns = new ArrayList<>();
                columns.add(record);
                columnMap.put(record.get("table_id").toString(), columns);
            }
            else {
                columns = columnMap.get(record.get("table_id").toString());
                columns.add(record);
                columnMap.replace(record.get("table_id").toString(), columns);
            }
        });
        List<TableInfoDto> addList = new ArrayList<>();
        List<String> errorTableNameList = new ArrayList<>();
        TableInfoDto echoTable;
        Map<Long, Long> finalFolderMap = folderMap;
        tableList.forEach(tableRecord -> {
            TableInfoDto tableInfoDto = new TableInfoDto();
            tableInfoDto.setTableName((String) tableRecord.get("tbl_name"));
            if (idataTableMap.containsKey(tableInfoDto.getTableName())) {
                tableInfoDto.setId(idataTableMap.get(tableInfoDto.getTableName()));
            }
            tableInfoDto.setFolderId(finalFolderMap.get(Long.valueOf(tableRecord.get("folder_id").toString())));
            // 表信息
            List<LabelDto> tableLabels = new ArrayList<>();
            for (Map.Entry<String, Object> entry : tableRecord.entrySet()) {
                LabelDto tableLabel = new LabelDto();
                String colKey = entry.getKey();
                if ("db_name".equals(colKey) && tableRecord.get("db_name") != null) {
                    tableLabel.setLabelCode("dbName:LABEL");
                    tableLabel.setLabelParamValue((String) tableRecord.get("db_name"));
                }
                else if ("tbl_comment".equals(colKey) && tableRecord.get("tbl_comment") != null) {
                    tableLabel.setLabelCode("tblComment:LABEL");
                    tableLabel.setLabelParamValue((String) tableRecord.get("tbl_comment"));
                }
                else if ("pw_owner_name".equals(colKey) && tableRecord.get("pw_owner_name") != null) {
                    tableLabel.setLabelCode("pwOwnerId:LABEL");
                    tableLabel.setLabelParamValue((String) tableRecord.get("pw_owner_name"));
                }
                else if ("dw_owner_id".equals(colKey) && tableRecord.get("dw_owner_id") != null) {
                    tableLabel.setLabelCode("dwOwnerId:LABEL");
                    if (userMap.containsKey(tableRecord.get("dw_owner_id").toString())
                            && idataUserMap.containsKey(userMap.get(tableRecord.get("dw_owner_id").toString()))) {
                        tableLabel.setLabelParamValue(idataUserMap.get(userMap.get(tableRecord.get("dw_owner_id").toString())).toString());
                    }
                    else {
                        tableLabel.setLabelParamValue(idataUserMap.get("大时").toString());
                    }
                }
                else if ("domain_id".equals(colKey) && tableRecord.get("domain_id") != null) {
                    tableLabel.setLabelCode("domainId:LABEL");
                    tableLabel.setLabelParamValue(idataDomainMap.get(domainMap.get(tableRecord.get("domain_id").toString())));
                }
                else if ("description".equals(colKey) && tableRecord.get("description") != null) {
                    tableLabel.setLabelCode("tblDescription:LABEL");
                    tableLabel.setLabelParamValue((String) tableRecord.get("description"));
                }
                else if ("partitioned".equals(colKey) && tableRecord.get("partitioned") != null) {
                    String partitioned = tableRecord.get("partitioned").toString();
                    tableLabel.setLabelCode("partitionedTbl:LABEL");
                    if ("false".equals(partitioned)) {
                        tableLabel.setLabelParamValue("false");
                    }
                    else {
                        tableLabel.setLabelParamValue("true");
                    }
                }
                else if ("layer".equals(colKey) && tableRecord.get("layer") != null) {
                    tableLabel.setLabelCode("dwLayer:LABEL");
                    tableLabel.setLabelParamValue(idataLayerMap.get((String) tableRecord.get("layer")));
                }
                else if ("metabase_urls".equals(colKey) && tableRecord.get("metabase_urls") != null) {
                    tableLabel.setLabelCode("metabaseUrl:LABEL");
                    List<String> metabaseUrlList = Arrays.stream(((String) tableRecord.get("metabase_urls")).split("[{,}]"))
                            .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                    tableLabel.setLabelParamValue(String.join(",", metabaseUrlList));
                }
                else if ("security_level".equals(colKey) && tableRecord.get("security_level") != null) {
                    tableLabel.setLabelCode("tblSecurityLevel:LABEL");
                    tableLabel.setLabelParamValue(syncSecurityLevel((String) tableRecord.get("security_level")));
                }
                else if ("biz_process_ids".equals(colKey) && tableRecord.get("biz_process_ids") != null) {
                    tableLabel.setLabelCode("bizProcess:LABEL");
                    List<String> bizProcessIdList = Arrays.stream(((String) tableRecord.get("biz_process_ids")).split("[{,}]"))
                            .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                    List<String> bizProcessCodeList = new ArrayList<>();
                    bizProcessIdList.forEach(bizProcessId -> {
                        bizProcessCodeList.add(idataBizProcessMap.get(bizProcessMap.get(bizProcessId)));
                    });
                    tableLabel.setLabelParamValue(String.join(",", bizProcessCodeList));
                }
                else {
                    continue;
                }
                tableLabels.add(tableLabel);
            }
            tableInfoDto.setTableLabels(tableLabels);
            // 字段信息
            List<Map<String, Object>> syncColumnList = columnMap.get(tableRecord.get("id").toString());
            if (syncColumnList != null && syncColumnList.size() > 0) {
                tableInfoDto.setColumnInfos(syncColumns(syncColumnList, idataColTypeMap));
            }
            addList.add(tableInfoDto);
        });

        for (TableInfoDto tableInfoDto : addList) {
            try {
                echoTable = tableInfoService.create(tableInfoDto, "系统管理员");
                errorTableNameList.remove(tableInfoDto.getTableName());
            } catch (IllegalAccessException ignore) {}
        }
        return errorTableNameList;
    }

    private String syncSecurityLevel(String securityLevel) {
        if ("High".equals(securityLevel)) {
            return "SECURITY_LEVEL_HIGH:ENUM_VALUE";
        }
        else if ("Low".equals(securityLevel)) {
            return "SECURITY_LEVEL_MEDIUM:ENUM_VALUE";
        }
        else {
            return "SECURITY_LEVEL_LOW:ENUM_VALUE";
        }
    }

    private List<ColumnInfoDto> syncColumns(List<Map<String, Object>> columnList, Map<String, String> idataColTypeMap) {
        List<ColumnInfoDto> echoList = new ArrayList<>();
        for (int i = 0; i < columnList.size(); i++) {
            Map<String, Object> columnMap = columnList.get(i);
            ColumnInfoDto echoColumn = new ColumnInfoDto();
            String columnName = (String) columnMap.get("col_name");
            echoColumn.setColumnName(columnName);
            echoColumn.setColumnIndex(i);
            List<LabelDto> columnLabelList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
                LabelDto columnLabel = new LabelDto();
                String colKey = entry.getKey();
                if ("col_type".equals(colKey) && columnMap.get("col_type") != null) {
                    columnLabel.setColumnName(columnName);
                    columnLabel.setLabelCode("columnType:LABEL");
                    columnLabel.setLabelParamValue(idataColTypeMap.get((String) columnMap.get("col_type")));
                }
                else if ("col_comment".equals(colKey) && columnMap.get("col_comment") != null) {
                    columnLabel.setColumnName(columnName);
                    columnLabel.setLabelCode("columnComment:LABEL");
                    columnLabel.setLabelParamValue((String) columnMap.get("col_comment"));
                }
                else if ("description".equals(colKey) && columnMap.get("description") != null) {
                    columnLabel.setColumnName(columnName);
                    columnLabel.setLabelCode("columnDescription:LABEL");
                    columnLabel.setLabelParamValue((String) columnMap.get("description"));
                }
                else if ("partitioned".equals(colKey) && columnMap.get("partitioned") != null) {
                    columnLabel.setColumnName(columnName);
                    String partitioned = columnMap.get("partitioned").toString();
                    columnLabel.setLabelCode("partitionedCol:LABEL");
                    if ("false".equals(partitioned)) {
                        columnLabel.setLabelParamValue("false");
                    }
                    else {
                        columnLabel.setLabelParamValue("true");
                    }
                }
                else if ("pk".equals(colKey) && columnMap.get("pk") != null) {
                    columnLabel.setColumnName(columnName);
                    String pk = columnMap.get("pk").toString();
                    columnLabel.setLabelCode("pk:LABEL");
                    if ("false".equals(pk)) {
                        columnLabel.setLabelParamValue("false");
                    }
                    else {
                        columnLabel.setLabelParamValue("true");
                    }
                }
                else if ("security_level".equals(colKey) && columnMap.get("security_level") != null) {
                    columnLabel.setColumnName(columnName);
                    columnLabel.setLabelCode("tblSecurityLevel:LABEL");
                    columnLabel.setLabelParamValue(syncSecurityLevel((String) columnMap.get("security_level")));
                }
                else {
                    continue;
                }
                columnLabelList.add(columnLabel);
            }
            echoColumn.setColumnLabels(columnLabelList);
            echoList.add(echoColumn);
        }
        return echoList;
    }

    private boolean syncForeignKeys() {
        List<Map<String, Object>> foreignKeyList = dwMetaService.getForeignKeys(null);
        List<Map<String, Object>> columnList = dwMetaService.getColumns();
        Map<String, String> columnMap = columnList.stream().collect(Collectors.toMap(
                record -> record.get("id").toString(), record -> (String) record.get("col_name")));
        Map<String, String> tableMap = dwMetaService.getTables(null).stream()
                .collect(Collectors.toMap(record -> record.get("id").toString(), record -> (String) record.get("tbl_name")));
        Map<String, Long> idataTableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevTableInfo::getTableName, DevTableInfo::getId));
        List<Integer> echoList = foreignKeyList.stream().map(foreignKeyMap -> {
            DevForeignKey echo = new DevForeignKey();
            echo.setCreator("系统管理员");
            echo.setTableId(idataTableMap.get(tableMap.get(foreignKeyMap.get("table_id").toString())));
            String colIdStr = foreignKeyMap.get("col_ids").toString();
            String[] colIdStrs = colIdStr.split("[{,}]");
            List<String> colIdList = Arrays.stream((foreignKeyMap.get("col_ids")).toString().split("[{,}]"))
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<String> columnNameList = colIdList.stream().map(columnMap::get).collect(Collectors.toList());
            echo.setColumnNames(String.join(",", columnNameList));
            echo.setReferTableId(idataTableMap.get(tableMap.get(foreignKeyMap.get("refer_tbl_id").toString())));
            List<String> referColIdList = Arrays.stream((foreignKeyMap.get("col_ids")).toString().split("[{,}]"))
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<String> referColumnNameList = referColIdList.stream().map(columnMap::get).collect(Collectors.toList());
            echo.setReferColumnNames(String.join(",", referColumnNameList));
            echo.setErType((String) foreignKeyMap.get("er_type"));
            return devForeignKeyDao.insertSelective(echo);
        }).collect(Collectors.toList());
        return true;
    }

    private Map<String, String> changeBizProcessCodes(Map<String, String> bizProcessCodeMap) {
        Map<String, String> echoMap = new HashMap<>();
        bizProcessCodeMap.forEach((key, value) -> echoMap.put(key + ":ENUM_VALUE", value));
        return echoMap;
    }
}
