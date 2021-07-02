-- ### 新表预置数据：
-- dev_folder
-- 系统文件夹
insert into idata.dev_folder (id, creator, folder_name) values (1, '系统管理员', 'SystemFolder');
insert into idata.dev_folder (id, creator, folder_name, parent_id) values (2, '系统管理员', 'LabelSystemFolder', 1);
insert into idata.dev_folder (id, creator, folder_name, parent_id) values (3, '系统管理员', 'EnumSystemFolder', 1);
--
-- dev_label_define
-- 表：数据库名称、是否分区表、安全等级、数仓分层、表中文名称、数仓所属人、业务方所属人
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dbName:LABEL', '数据库名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedTbl:LABEL', '是否分区表', 'BOOLEAN_LABEL', 'BOOLEAN', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblSecurityLevel:LABEL', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'TABLE', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwLayer:LABEL', '数仓分层', 'ENUM_VALUE_LABEL', 'dwLayerEnum:ENUM', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblComment:LABEL', '表中文名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwOwnerId:LABEL', '数仓所属人', 'USER_LABEL', 'WHOLE', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pwOwnerId:LABEL', '业务所属人', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

-- 字段：主键、字段类型、安全等级、字段中文名称、是否分区字段
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pk:LABEL', '是否主键', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnType:LABEL', '字段类型', 'ENUM_VALUE_LABEL', 'hiveColTypeEnum:ENUM', 'COLUMN', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'colSecurityLevel:LABEL', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'COLUMN', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnComment:LABEL', '字段中文名称', 'STRING_LABEL', 'STRING', 'COLUMN', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedCol:LABEL', '是否分区字段', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 2);
--
-- dev_enum、dev_enum_value
-- 数据域、业务过程、数仓分层、安全等级、hive字段类型、聚合方式
-- 数据域
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'domainNameEnum:ENUM', '数据域', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'USER_DOMAIN:ENUM_VALUE', '用户域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'TRADE_DOMAIN:ENUM_VALUE', '交易域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'PRODUCT_DOMAIN:ENUM_VALUE', '商品域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'USER_BEHAVIOUR_DOMAIN:ENUM_VALUE', '用户行为域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'FILE_DOMAIN:ENUM_VALUE', '文件域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'GENERAL_DOMAIN:ENUM_VALUE', '通用域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'PROCURE_DOMAIN:ENUM_VALUE', '采购域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'ECOLOGY_DOMAIN:ENUM_VALUE', '金融域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum:ENUM', 'FINANCE_DOMAIN:ENUM_VALUE', '财务域');

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'bizTypeEnum:ENUM', '业务过程', 3);

-- 数仓分层
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'dwLayerEnum:ENUM', '数仓分层', 3);
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
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'securityLevelEnum:ENUM', '安全等级', 3);
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

-- hive字段类型
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'hiveColTypeEnum:ENUM', 'hive字段类型', 3);
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
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'aggregatorEnum:ENUM', '聚合方式', 3);
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

--
-- dev_enum_value
-- 同dev_enum

--
-- ### 现有表迁移方案：
--
-- table_folder -> dev_folder
-- table_info -> table_info, dev_label_define 具体字段需确认
-- column_info -> column_info, dev_label_define 具体字段需确认
-- foreign_key -> dev_foreign_key
--
-- data_domain -> dev_enum, dev_enum_value
-- business_process -> dev_enum, dev_enum_value
-- modifier_type -> dev_label_define
-- modifier -> dev_enum, dev_enum_value
-- metric -> dev_label_define
-- dimension -> dev_label_define
-- column_role -> dev_label
