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
values ('系统管理员', 'dbName', '数据库名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedTbl', '是否分区表', 'BOOLEAN_LABEL', 'BOOLEAN', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblSecurityLevel', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'TABLE', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwLayer', '数仓分层', 'ENUM_VALUE_LABEL', 'dwLayerEnum:ENUM', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'tblComment', '表中文名称', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'dwOwnerId', '数仓所属人', 'USER_LABEL', 'WHOLE', 'TABLE', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pwOwnerId', '业务所属人', 'STRING_LABEL', 'STRING', 'TABLE', 1, 2);

-- 字段：主键、字段类型、安全等级、字段中文名称、是否分区字段
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'pk', '是否主键', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnType', '字段类型', 'ENUM_VALUE_LABEL', 'hiveColTypeEnum:ENUM', 'COLUMN', 1, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'colSecurityLevel', '安全等级', 'ENUM_VALUE_LABEL', 'securityLevelEnum:ENUM', 'COLUMN', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnComment', '字段中文名称', 'STRING_LABEL', 'STRING', 'COLUMN', 0, 2);

insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'partitionedCol', '是否分区字段', 'BOOLEAN_LABEL', 'BOOLEAN', 'COLUMN', 1, 2);
--
-- dev_enum、dev_enum_value
-- 数据域、业务过程、数仓分层、安全等级、hive字段类型、聚合方式
-- 数据域
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'domainNameEnum', '数据域', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'USER_DOMAIN', '用户域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'TRADE_DOMAIN', '交易域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'PRODUCT_DOMAIN', '商品域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'USER_BEHAVIOUR_DOMAIN', '用户行为域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'FILE_DOMAIN', '文件域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'GENERAL_DOMAIN', '通用域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'PROCURE_DOMAIN', '采购域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'ECOLOGY_DOMAIN', '金融域');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'domainNameEnum', 'FINANCE_DOMAIN', '财务域');

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'bizTypeEnum', '业务过程', 3);

-- 数仓分层
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'dwLayerEnum', '数仓分层', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum', 'DW_LAYER_ADS', 'ADS');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum', 'DW_LAYER_DWS', 'DWS');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum', 'DW_LAYER_DWD', 'DWD');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum', 'DW_LAYER_DIM', 'DIM');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'dwLayerEnum', 'DW_LAYER_ODS', 'ODS');

-- 安全等级
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'securityLevelEnum', '安全等级', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_C1', '客户可公开数据(C1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_C2', '客户可共享数据(C2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_C3', '客户隐私数据(C3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_C4', '客户机密数据(C4)');

insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_S1', '业务可公开数据(S1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_S2', '业务内部数据(S2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_S3', '业务保密数据(S3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_S4', '业务机密数据(S4)');

insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_B1', '政采云可公开数据(B1)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_B2', '政采云内部数据(B2)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_B3', '政采云保密数据(B3)');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'securityLevelEnum', 'SECURITY_LEVEL_B4', '政采云机密数据(B4)');

-- hive字段类型
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'hiveColTypeEnum', 'hive字段类型', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_BIGINT', 'BIGINT');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_DOUBLE', 'DOUBLE');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_TIMESTAMP', 'TIMESTAMP');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_DATE', 'DATE');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_STRING', 'STRING');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_ARRAY_BIGINT', 'ARRAY<BIGINT>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_ARRAY_DOUBLE', 'ARRAY<DOUBLE>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_ARRAY_TIMESTAMP', 'ARRAY<TIMESTAMP>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_ARRAY_DATE', 'ARRAY<DATE>');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'hiveColTypeEnum', 'HIVE_COL_TYPE_ARRAY_STRING', 'ARRAY<STRING>');

-- 聚合方式
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'aggregatorEnum', '聚合方式', 3);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_SUM', '求和');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_AVG', '求平均');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_MAX', '最大值');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_MIN', '最小值');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_CNT', '计数');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'aggregatorEnum', 'AGGREGATOR_CNTD', '去重计数');

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
