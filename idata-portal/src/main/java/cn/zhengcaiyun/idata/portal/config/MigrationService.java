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
package cn.zhengcaiyun.idata.portal.config;

import cn.zhengcaiyun.idata.commons.encrypt.RandomUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.ForeignKeyDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionDataService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import cn.zhengcaiyun.idata.develop.service.measure.ModifierService;
import cn.zhengcaiyun.idata.develop.service.table.DwMetaService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.inject.internal.cglib.reflect.$FastMember;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.devEnum;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-08-15 11:50
 */

@Service
public class MigrationService {

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
    private DimensionService dimensionService;
    @Autowired
    private DevEnumDao devEnumDao;
    @Autowired
    private ModifierService modifierService;
    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private MetricService metricService;
    @Autowired
    private DevColumnInfoDao devColumnInfoDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;

    public List<TableInfoDto> syncTableData(){
        List<Map<String, Object>> tableList = dwMetaService.getTables();
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
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));
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
        List<TableInfoDto> echoList = new ArrayList<>();
        TableInfoDto echoTable;
        tableList.forEach(tableRecord -> {
            TableInfoDto tableInfoDto = new TableInfoDto();
            tableInfoDto.setTableName((String) tableRecord.get("tbl_name"));
            if (idataTableMap.containsKey(tableInfoDto.getTableName())) {
                tableInfoDto.setId(idataTableMap.get(tableInfoDto.getTableName()));
            }
            tableInfoDto.setFolderId(folderMap.get(tableRecord.get("layer").toString()));
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
            echoTable = tableInfoService.create(tableInfoDto, "系统管理员");
            System.out.println("Table Sync Success : tableName " + tableInfoDto.getTableName());
            echoList.add(echoTable);
        }
        return echoList;
    }

    public boolean syncForeignKeys() {
        List<Map<String, Object>> foreignKeyList = dwMetaService.getForeignKeys();
        List<Map<String, Object>> columnList = dwMetaService.getColumns();
        Map<String, String> columnMap = columnList.stream().collect(Collectors.toMap(
                record -> record.get("id").toString(), record -> (String) record.get("col_name")));
        Map<String, String> tableMap = dwMetaService.getTables().stream()
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

    public EnumDto syncBizProcess() {
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

    public List<MeasureDto> syncDimensions() {
        List<Map<String, Object>> dimensionList = dwMetaService.getDimensions();
        Map<String, List<Map<String, Object>>> columnRoleMap = dwMetaService.getColumnRoles("dimension");
        List<Map<String, Object>> tableList = dwMetaService.getTables();
        Map<String, String> tableMap = tableList.stream()
                .collect(Collectors.toMap(record -> record.get("id").toString(), record -> (String) record.get("tbl_name")));
        Map<String, Long> idataTableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevTableInfo::getTableName, DevTableInfo::getId));
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));
        List<MeasureDto> echo = dimensionList.stream().map(dimensionMap -> {
            MeasureDto dimension = new MeasureDto();
            dimension.setLabelTag(LabelTagEnum.DIMENSION_LABEL.name());
            dimension.setSubjectType(SubjectTypeEnum.COLUMN.name());
            dimension.setLabelName((String) dimensionMap.get("cn_name"));
//            dimension.setLabelCode(((String) dimensionMap.get("en_name")) + ":LABEL");
            SpecialAttributeDto specialAttributeDto = new SpecialAttributeDto();
            specialAttributeDto.setDegradeDim(false);
            dimension.setSpecialAttribute(specialAttributeDto);

            List<AttributeDto> measureAttributeList = new ArrayList<>();
            AttributeDto enNameAttribute = new AttributeDto();
            enNameAttribute.setAttributeKey("enName");
            enNameAttribute.setAttributeType("STRING");
            enNameAttribute.setAttributeValue((String) dimensionMap.get("en_name"));
            measureAttributeList.add(enNameAttribute);
            AttributeDto idAttribute = new AttributeDto();
            idAttribute.setAttributeKey("dimensionId");
            idAttribute.setAttributeType("STRING");
            idAttribute.setAttributeValue((String) dimensionMap.get("identity"));
            measureAttributeList.add(idAttribute);
            AttributeDto defineAttribute = new AttributeDto();
            defineAttribute.setAttributeKey("dimensionDefine");
            defineAttribute.setAttributeType("STRING");
            defineAttribute.setAttributeValue("");
            measureAttributeList.add(defineAttribute);
            AttributeDto commentAttribute = new AttributeDto();
            commentAttribute.setAttributeKey("comment");
            commentAttribute.setAttributeType("STRING");
            commentAttribute.setAttributeValue("");
            measureAttributeList.add(commentAttribute);
            dimension.setLabelAttributes(measureAttributeList);

            List<LabelDto> dimensionLabelList = new ArrayList<>();
            LabelDto dimensionLabel = new LabelDto();
            dimensionLabel.setTableId(idataTableMap.get(tableMap.get(dimensionMap.get("dim_tbl_id").toString())));
            dimensionLabel.setColumnName((String) dimensionMap.get("dim_tbl_column"));
            dimensionLabel.setLabelParamValue("true");
            dimensionLabelList.add(dimensionLabel);

            if (columnRoleMap.containsKey(dimensionMap.get("id").toString())) {
                List<Map<String, Object>> roleList = columnRoleMap.get(dimensionMap.get("id").toString());
                roleList.forEach(roleRecord -> {
                    LabelDto roleLabel = new LabelDto();
                    roleLabel.setTableId(idataTableMap.get(tableMap.get(roleRecord.get("table_id").toString())));
                    roleLabel.setColumnName((String) roleRecord.get("column_name"));
                    roleLabel.setLabelParamValue("false");
                    dimensionLabelList.add(roleLabel);
                });
            }
            dimension.setMeasureLabels(dimensionLabelList);
            dimension.setFolderId(folderMap.get("DimensionFolder"));
            return dimensionService.create(dimension, "系统管理员");
        }).collect(Collectors.toList());
        return echo;
    }

    public List<EnumDto> syncModifierEnum() {
        Map<String, Map<String, Object>> modifierTypeMap = dwMetaService.getModifierTypes().stream()
                .collect(Collectors.toMap(record -> record.get("id").toString(), Function.identity()));
        Map<String, List<Map<String, Object>>> modifierMap = dwMetaService.getModifiers()
                .stream().collect(Collectors.groupingBy(record -> record.get("type_id").toString()));
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));
        Map<String, String> enumMap = devEnumDao.select(c -> c.where(devEnum.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevEnum::getEnumName, DevEnum::getEnumCode));
        List<EnumDto> echo = new ArrayList<>();
        modifierTypeMap.forEach((key, value) -> {
            if (modifierMap.containsKey(key)) {
                EnumDto enumDto = new EnumDto();
                String enumName = (String) value.entrySet().stream().filter(map -> "modifier_type".equals(map.getKey()))
                        .findFirst().get().getValue();
                if (enumMap.containsKey(enumName)) {
                    enumDto.setEnumCode(enumMap.get(enumName));
                }
                enumDto.setEnumName(enumName);
                //enumDto.setEnumCode(RandomUtil.randomStr(10) + ":ENUM");
                List<Map<String, Object>> modifierList = modifierMap.get(key);
                List<EnumValueDto> echoEnumValueList = modifierList.stream().map(record -> {
                    EnumValueDto enumValue = new EnumValueDto();
                    enumValue.setEnumValue((String) record.get("modifier"));
                    enumValue.setValueCode(RandomUtil.randomStr(10) + ":ENUM_VALUE");
                    return enumValue;
                }).collect(Collectors.toList());
                enumDto.setEnumValues(echoEnumValueList);
                enumDto.setFolderId(folderMap.get("EnumSystemFolder"));
                echo.add(enumService.createOrEdit(enumDto, "系统管理员"));
            }
        });
        return echo;
    }

    public List<MeasureDto> syncModifiers() {
        Map<String, String> modifierEnumMap = devEnumDao.select(c -> c.where(devEnum.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevEnum::getEnumName, DevEnum::getEnumCode));
        List<Map<String, Object>> modifierTypeList = dwMetaService.getModifierTypes();
        Map<String, List<Map<String, Object>>> columnRoleMap = dwMetaService.getColumnRoles("modifier");
        Map<String, String> tableMap = dwMetaService.getTables().stream()
                .collect(Collectors.toMap(record -> record.get("id").toString(), record -> (String) record.get("tbl_name")));
        Map<String, Long> idataTableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevTableInfo::getTableName, DevTableInfo::getId));
        Map<String, List<String>> idataColumnMap = new HashMap<>();
        devColumnInfoDao.select(c -> c.where(devColumnInfo.del,
                isNotEqualTo(1))).forEach(record -> {
                    List<String> columnNameList;
                    if (idataColumnMap.containsKey(record.getTableId().toString())) {
                        columnNameList = idataColumnMap.get(record.getTableId().toString());
                    }
                    else {
                        columnNameList = new ArrayList<>();
                    }
                    columnNameList.add(record.getColumnName());
                    idataColumnMap.put(record.getTableId().toString(), columnNameList);
        });
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));
        List<MeasureDto> echo = modifierTypeList.stream().map(modifierTypeMap -> {
            MeasureDto modifier = new MeasureDto();
            modifier.setLabelTag(LabelTagEnum.MODIFIER_LABEL.name());
            modifier.setSubjectType(SubjectTypeEnum.COLUMN.name());
            modifier.setLabelName((String) modifierTypeMap.get("modifier_type"));
            //modifier.setLabelCode(RandomUtil.randomStr(10) + ":LABEL");

            List<AttributeDto> measureAttributeList = new ArrayList<>();
            AttributeDto enNameAttribute = new AttributeDto();
            enNameAttribute.setAttributeKey("enName");
            enNameAttribute.setAttributeType("STRING");
            String enName = modifierTypeMap.get("en_name") != null ? (String) modifierTypeMap.get("en_name") : "";
            enNameAttribute.setAttributeValue(enName);
            measureAttributeList.add(enNameAttribute);
            AttributeDto enumAttribute = new AttributeDto();
            enumAttribute.setAttributeKey("modifierEnum");
            enumAttribute.setAttributeType("STRING");
            enumAttribute.setAttributeValue(modifierEnumMap.getOrDefault((String) modifierTypeMap.get("modifier_type"), ""));
            measureAttributeList.add(enumAttribute);
            AttributeDto defineAttribute = new AttributeDto();
            defineAttribute.setAttributeKey("modifierDefine");
            defineAttribute.setAttributeType("STRING");
            String modifierDefine = modifierTypeMap.get("description") != null
                    ? (String) modifierTypeMap.get("description") : "";
            defineAttribute.setAttributeValue(modifierDefine);
            measureAttributeList.add(defineAttribute);
            AttributeDto commentAttribute = new AttributeDto();
            commentAttribute.setAttributeKey("comment");
            commentAttribute.setAttributeType("STRING");
            commentAttribute.setAttributeValue("");
            measureAttributeList.add(commentAttribute);
            modifier.setLabelAttributes(measureAttributeList);

            List<LabelDto> modifierLabelList = new ArrayList<>();

            if (columnRoleMap.containsKey(modifierTypeMap.get("id").toString())) {
                List<Map<String, Object>> roleList = columnRoleMap.get(modifierTypeMap.get("id").toString());
                roleList.forEach(roleRecord -> {
                    LabelDto roleLabel = new LabelDto();
                    roleLabel.setTableId(idataTableMap.get(tableMap.get(roleRecord.get("table_id").toString())));
                    roleLabel.setColumnName((String) roleRecord.get("column_name"));
                    if (idataColumnMap.containsKey(idataTableMap.get(tableMap.get(roleRecord.get("table_id").toString())).toString())
                            && idataColumnMap.get(idataTableMap.get(tableMap.get(roleRecord.get("table_id").toString())).toString())
                            .contains((String) roleRecord.get("column_name"))) {
                        modifierLabelList.add(roleLabel);
                    }
                });
            }
            modifier.setMeasureLabels(modifierLabelList);
            modifier.setFolderId(folderMap.get("ModifierFolder"));
            return modifierService.create(modifier, "系统管理员");
        }).collect(Collectors.toList());
        return echo;
    }

    public List<MeasureDto> syncMetrics(String metricType) {
        List<Map<String, Object>> metricList = dwMetaService.getMetrics(metricType);
        Map<String, List<Map<String, Object>>> atomicColumnRoleMap = dwMetaService.getColumnRoles("atomic_metric");
        Map<String, String> bizProcessMap = dwMetaService.getBizProcesses();
        Map<String, String> idataBizProcessMap = enumService.getEnumValues("bizProcessEnum:ENUM")
                .stream().collect(Collectors.toMap(EnumValueDto::getEnumValue, EnumValueDto::getValueCode));
        Map<String, Long> folderMap = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevFolder::getFolderName, DevFolder::getId));

        Map<String, String> aggregatorMap = new HashMap<>();
        aggregatorMap.put("SUM", "AGGREGATOR_SUM:ENUM_VALUE");
        aggregatorMap.put("AVG", "AGGREGATOR_AVG:ENUM_VALUE");
        aggregatorMap.put("MAX", "AGGREGATOR_MAX:ENUM_VALUE");
        aggregatorMap.put("MIN", "AGGREGATOR_MIN:ENUM_VALUE");
        aggregatorMap.put("CNT", "AGGREGATOR_CNT:ENUM_VALUE");
        aggregatorMap.put("CNTD", "AGGREGATOR_CNTD:ENUM_VALUE");
        List<MeasureDto> echo = metricList.stream().map(record -> {
            MeasureDto metric = new MeasureDto();
            //metric.setLabelCode(RandomUtil.randomStr(10) + ":LABEL");
            metric.setLabelName((String) record.get("cn_name"));
            metric.setSubjectType(SubjectTypeEnum.COLUMN.name());

            List<AttributeDto> attributeList = new ArrayList<>();
            AttributeDto idAttribute = new AttributeDto();
            idAttribute.setAttributeKey("metricId");
            idAttribute.setAttributeType("STRING");
            idAttribute.setAttributeValue((String) record.get("identity"));
            attributeList.add(idAttribute);
            AttributeDto enAttribute = new AttributeDto();
            enAttribute.setAttributeKey("enName");
            enAttribute.setAttributeType("STRING");
            enAttribute.setAttributeValue((String) record.get("en_name"));
            attributeList.add(enAttribute);
            AttributeDto defineAttribute = new AttributeDto();
            defineAttribute.setAttributeKey("metricDefine");
            defineAttribute.setAttributeType("STRING");
            String metricDefine = record.get("description") != null ? (String) record.get("description") : "";
            defineAttribute.setAttributeValue(metricDefine);
            attributeList.add(defineAttribute);
            AttributeDto commentAttribute = new AttributeDto();
            commentAttribute.setAttributeKey("comment");
            commentAttribute.setAttributeType("STRING");
            String metricComment = record.get("note") != null ? (String) record.get("note") : "";
            commentAttribute.setAttributeValue(metricComment);
            attributeList.add(commentAttribute);
            AttributeDto bizAttribute = new AttributeDto();
            bizAttribute.setAttributeKey("bizProcessCode");
            bizAttribute.setAttributeType("STRING");
            bizAttribute.setAttributeValue(idataBizProcessMap.get(bizProcessMap.get(record.get("process_id").toString())));
            attributeList.add(bizAttribute);
            if ("atomic".equals((String) record.get("metric_type"))) {
                Map<String, String> tableMap =  dwMetaService.getTables().stream()
                        .collect(Collectors.toMap(table -> table.get("id").toString(), table -> (String) table.get("tbl_name")));
                Map<String, Long> idataTableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                        .stream().collect(Collectors.toMap(DevTableInfo::getTableName, DevTableInfo::getId));
                metric.setLabelTag(LabelTagEnum.ATOMIC_METRIC_LABEL.name());
                metric.setFolderId(folderMap.get("AtomicMetricFolder"));
                List<Map<String, Object>> roleList = atomicColumnRoleMap.get(record.get("id").toString());
                if (roleList != null && roleList.size() > 0) {
                    String aggregator = aggregatorMap.get((String) roleList.get(0).get("aggregator"));
                    SpecialAttributeDto specialAttributeDto = new SpecialAttributeDto();
                    specialAttributeDto.setAggregatorCode(aggregator);
                    metric.setSpecialAttribute(specialAttributeDto);
                    List<LabelDto> atomicLabelList = new ArrayList<>();
                    LabelDto atomicLabelDto = new LabelDto();
                    atomicLabelDto.setTableId(idataTableMap.get(tableMap.get(roleList.get(0).get("table_id").toString())));
                    atomicLabelDto.setColumnName((String) roleList.get(0).get("column_name"));
                    atomicLabelList.add(atomicLabelDto);
                    metric.setMeasureLabels(atomicLabelList);
                }
            }
            else if ("derive".equals((String) record.get("metric_type"))) {
                Map<String, DevLabelDefine> measureMap = devLabelDefineDao.select(c ->
                        c.where(devLabelDefine.del, isNotEqualTo(1),
                                and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL.name()),
                                        or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name())))))
                        .stream().collect(Collectors.toMap(DevLabelDefine::getLabelName, Function.identity()));
                Map<String, String> atomicMetricMap = dwMetaService.getMetrics("atomic").stream().collect(Collectors.toMap(
                        metricRecord -> metricRecord.get("id").toString(), metricRecord -> (String) metricRecord.get("cn_name")));
                Map<String, String> modifierEnumMap = devEnumDao.select(c -> c.where(devEnum.del, isNotEqualTo(1)))
                        .stream().collect(Collectors.toMap(DevEnum::getEnumName, DevEnum::getEnumCode));
                Map<String, String> modifierTypeMap = dwMetaService.getModifierTypes().stream()
                        .collect(Collectors.toMap(modifierType -> modifierType.get("id").toString(),
                                modifierType -> (String) modifierType.get("modifier_type").toString()));
                Map<String, Map<String, Object>> modifierMap = dwMetaService.getModifiers().stream()
                        .collect(Collectors.toMap(modifierRecord -> modifierRecord.get("id").toString(), Function.identity()));
                Map<String, List<String>> modifierTypeIdMap = dwMetaService.getModifierTypeIdMap();

                metric.setLabelTag(LabelTagEnum.DERIVE_METRIC_LABEL.name());
                SpecialAttributeDto specialAttributeDto = new SpecialAttributeDto();
                if (measureMap.containsKey(atomicMetricMap.get(record.get("atomic_id").toString()))) {
                    specialAttributeDto.setAtomicMetricCode(measureMap.get(atomicMetricMap
                            .get(record.get("atomic_id").toString())).getLabelCode());
                }
                List<String> modifierIdList = Arrays.stream((record.get("modifier_ids").toString()).split("[{,}]"))
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                Map<String, List<String>> modifierTypeAndModifierMap = getModifiers(modifierIdList, modifierMap, modifierTypeIdMap);
                List<ModifierDto> modifierList = new ArrayList<>();
                modifierTypeAndModifierMap.forEach((key, value) -> {
                    ModifierDto echoModifier = new ModifierDto();
                    if (measureMap.containsKey(modifierTypeMap.get(key))) {
                        echoModifier.setModifierCode(measureMap.get(modifierTypeMap.get(key)).getLabelCode());
                    }
                    List<String> modifierEnumValueCodeList =
                            enumService.getEnumValues(modifierEnumMap.get(modifierTypeMap.get(key)))
                            .stream().map(EnumValueDto::getValueCode).collect(Collectors.toList());
                    echoModifier.setEnumValueCodes(modifierEnumValueCodeList);
                    if (!modifierList.contains(echoModifier)) {
                        modifierList.add(echoModifier);
                    }
                });
                specialAttributeDto.setModifiers(modifierList);
                metric.setSpecialAttribute(specialAttributeDto);
                metric.setFolderId(folderMap.get("DeriveMetricFolder"));
            }
            else if ("complex".equals((String) record.get("metric_type"))) {
                metric.setLabelTag(LabelTagEnum.COMPLEX_METRIC_LABEL.name());
                SpecialAttributeDto specialAttributeDto = new SpecialAttributeDto();
                String complexMetricFormula = record.get("formula") != null ? (String) record.get("formula") : "";
                specialAttributeDto.setComplexMetricFormula(complexMetricFormula);
                metric.setSpecialAttribute(specialAttributeDto);
                metric.setFolderId(folderMap.get("ComplexMetricFolder"));
            }
            else {
                return null;
            }
            metric.setLabelAttributes(attributeList);
            return metricService.create(metric, "系统管理员");
        }).collect(Collectors.toList());

        return echo;
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

    private Map<String, List<String>> getModifiers(List<String> modifierIds, Map<String, Map<String, Object>> modifierMap,
                                                   Map<String, List<String>> modifierTypeIdMap) {
        Set<String> modifierTypeIds = modifierIds.stream().map(record -> modifierMap.get(record).get("type_id").toString())
                .collect(Collectors.toSet());
        Map<String, List<String>> echoMap = new HashMap<>();
        modifierTypeIds.forEach(modifierTypeId -> {
            List<String> modifierIdList = modifierTypeIdMap.get(modifierTypeId);
            List<String> echo = modifierIds.stream().filter(modifierIdList::contains).collect(Collectors.toList());
            echoMap.put(modifierTypeId, echo);
        });
        return echoMap;
    }

    private Map<String, String> changeBizProcessCodes(Map<String, String> bizProcessCodeMap) {
        Map<String, String> echoMap = new HashMap<>();
        bizProcessCodeMap.forEach((key, value) -> echoMap.put(key + ":ENUM_VALUE", value));
        return echoMap;
    }

}
