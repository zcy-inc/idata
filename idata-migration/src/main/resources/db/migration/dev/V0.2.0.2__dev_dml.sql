-- ### 新表预置数据：
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

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'assetCatalogueEnum:ENUM',
'数据资产', 2);
