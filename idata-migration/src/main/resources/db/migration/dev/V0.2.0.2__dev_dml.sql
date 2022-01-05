-- ### 新表预置数据：
-- dev_folder
-- 系统文件夹
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (1, '系统管理员', 'LabelSystemFolder', 'LABEL');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (2, '系统管理员', 'EnumSystemFolder', 'ENUM');

insert into idata.dev_folder (id, creator, folder_name, folder_type) values (3, '系统管理员', 'ODS', 'TABLE');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (4, '系统管理员', 'DWD', 'TABLE');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (5, '系统管理员', 'DWS', 'TABLE');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (6, '系统管理员', 'DIM', 'TABLE');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (7, '系统管理员', 'ADS', 'TABLE');

insert into idata.dev_folder (id, creator, folder_name, folder_type) values (8, '系统管理员', 'DimensionFolder',
'DIMENSION');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (9, '系统管理员', 'ModifierFolder', 'MODIFIER');
insert into idata.dev_folder (id, creator, folder_name, folder_type) values (10, '系统管理员', 'MetricFolder', 'METRIC');
insert into idata.dev_folder (id, creator, folder_name, parent_id, folder_type) values (11, '系统管理员', 'AtomicMetricFolder', 10, 'METRIC');
insert into idata.dev_folder (id, creator, folder_name, parent_id, folder_type) values (12, '系统管理员', 'DeriveMetricFolder', 10, 'METRIC');
insert into idata.dev_folder (id, creator, folder_name, parent_id, folder_type) values (13, '系统管理员', 'ComplexMetricFolder', 10, 'METRIC');

--
-- dev_label_define
-- 表：数据库名称、是否分区表、安全等级、数仓分层、表中文名称、数仓所属人、业务方所属人、数据资产、数据域、metabase地址、描述、业务过程
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dbName:LABEL', '数据库名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedTbl:LABEL', '是否分区表', 'BOOLEAN_LABEL', 'BOOLEAN', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblSecurityLevel:LABEL', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'TABLE', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwLayer:LABEL', '数仓分层', 'ENUM_VALUE_LABEL', 'dwLayerEnum:ENUM', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblComment:LABEL', '表中文名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwOwnerId:LABEL', '数仓所属人', 'USER_LABEL', 'WHOLE', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pwOwnerId:LABEL', '业务所属人', 'STRING_LABEL', 'STRING', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'assetCatalogue:LABEL', '数据资产', 'ENUM_VALUE_LABEL', 'assetCatalogueEnum:ENUM', 'TABLE', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'domainId:LABEL', '数据域', 'ENUM_VALUE_LABEL', 'domainIdEnum:ENUM', 'TABLE', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'metabaseUrl:LABEL', 'Metabase地址', 'STRING_LABEL', 'STRING', 'TABLE', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblDescription:LABEL', '描述', 'STRING_LABEL', 'STRING', 'TABLE', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'bizProcess:LABEL', '业务过程', 'ENUM_VALUE_LABEL', 'bizProcessEnum:ENUM', 'TABLE', 0, 1);

-- 字段：主键、字段类型、安全等级、字段中文名称、是否分区字段、描述
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pk:LABEL', '是否主键', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnType:LABEL', '字段类型', 'ENUM_VALUE_LABEL', 'hiveColTypeEnum:ENUM', 'COLUMN', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'colSecurityLevel:LABEL', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'COLUMN', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnComment:LABEL', '字段中文名称', 'STRING_LABEL', 'STRING', 'COLUMN', 0, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedCol:LABEL', '是否分区字段', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 1);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnDescription:LABEL', '描述', 'STRING_LABEL', 'STRING', 'COLUMN', 0, 1);
--
-- dev_enum、dev_enum_value
-- 数据域、业务过程、数仓分层、安全等级、hive字段类型、聚合方式、数据资产
-- 数据域
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'domainIdEnum:ENUM', '数据域', 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'USER_DOMAIN:ENUM_VALUE', '用户域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'TRADE_DOMAIN:ENUM_VALUE', '交易域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'PRODUCT_DOMAIN:ENUM_VALUE', '商品域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'USER_BEHAVIOUR_DOMAIN:ENUM_VALUE', '用户行为域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'FILE_DOMAIN:ENUM_VALUE', '文件域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'GENERAL_DOMAIN:ENUM_VALUE', '通用域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'PROCURE_DOMAIN:ENUM_VALUE', '采购域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'ECOLOGY_DOMAIN:ENUM_VALUE', '金融域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainIdEnum:ENUM', 'FINANCE_DOMAIN:ENUM_VALUE', '财务域');

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'bizProcessEnum:ENUM', '业务过程', 2);

-- 数仓分层
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'dwLayerEnum:ENUM', '数仓分层', 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum:ENUM', 'DW_LAYER_ADS:ENUM_VALUE', 'ADS');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum:ENUM', 'DW_LAYER_DWS:ENUM_VALUE', 'DWS');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum:ENUM', 'DW_LAYER_DWD:ENUM_VALUE', 'DWD');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum:ENUM', 'DW_LAYER_DIM:ENUM_VALUE', 'DIM');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum:ENUM', 'DW_LAYER_ODS:ENUM_VALUE', 'ODS');

-- 安全等级
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'securityLevelEnum:ENUM', '安全等级', 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_C1:ENUM_VALUE', '客户可公开数据(C1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_C2:ENUM_VALUE', '客户可共享数据(C2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_C3:ENUM_VALUE', '客户隐私数据(C3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_C4:ENUM_VALUE', '客户机密数据(C4)');

insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_S1:ENUM_VALUE', '业务可公开数据(S1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_S2:ENUM_VALUE', '业务内部数据(S2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_S3:ENUM_VALUE', '业务保密数据(S3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_S4:ENUM_VALUE', '业务机密数据(S4)');

insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_B1:ENUM_VALUE', '政采云可公开数据(B1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_B2:ENUM_VALUE', '政采云内部数据(B2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_B3:ENUM_VALUE', '政采云保密数据(B3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_B4:ENUM_VALUE', '政采云机密数据(B4)');

insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_LOW:ENUM_VALUE', '低');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_MEDIUM:ENUM_VALUE', '中');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum:ENUM', 'SECURITY_LEVEL_HIGH:ENUM_VALUE', '高');

-- hive字段类型
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'hiveColTypeEnum:ENUM', 'hive字段类型', 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_BIGINT:ENUM_VALUE', 'BIGINT');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_DOUBLE:ENUM_VALUE', 'DOUBLE');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_TIMESTAMP:ENUM_VALUE', 'TIMESTAMP');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_DATE:ENUM_VALUE', 'DATE');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_STRING:ENUM_VALUE', 'STRING');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_ARRAY_BIGINT:ENUM_VALUE', 'ARRAY<BIGINT>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_ARRAY_DOUBLE:ENUM_VALUE', 'ARRAY<DOUBLE>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_ARRAY_TIMESTAMP:ENUM_VALUE', 'ARRAY<TIMESTAMP>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_ARRAY_DATE:ENUM_VALUE', 'ARRAY<DATE>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum:ENUM', 'HIVE_COL_TYPE_ARRAY_STRING:ENUM_VALUE', 'ARRAY<STRING>');

-- 聚合方式
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'aggregatorEnum:ENUM', '聚合方式', 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_SUM:ENUM_VALUE', '求和');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_AVG:ENUM_VALUE', '求平均');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_MAX:ENUM_VALUE', '最大值');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_MIN:ENUM_VALUE', '最小值');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_CNT:ENUM_VALUE', '计数');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum:ENUM', 'AGGREGATOR_CNTD:ENUM_VALUE', '去重计数');

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'assetCatalogueEnum:ENUM', '数据资产', 2);
--
-- dev_enum_value
-- 同dev_enum

-- 告警等级
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required)
values ('系统管理员', 'alarmLayer:LABEL', '告警等级', 'ENUM_VALUE_LABEL', 'alarmLayerEnum:ENUM', 'TABLE', 0);
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'alarmLayerEnum:ENUM', '告警等级',
 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_HIGH:ENUM_VALUE', '高');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_MEDIUM:ENUM_VALUE', '中');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_LOW:ENUM_VALUE', '低');

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'assetCatalogueEnum:ENUM', '数据资产');
