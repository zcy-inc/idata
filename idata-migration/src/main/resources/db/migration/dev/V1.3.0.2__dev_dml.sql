-- dev_label_define
insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'columnAttribute:LABEL', '字段属性', 'ENUM_VALUE_LABEL', 'columnAttributeEnum:ENUM', 'COLUMN', 0, 1);

-- dev_enum
insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'columnAttributeEnum:ENUM', '字段属性', 2);
-- dev_enum_value
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'columnAttributeEnum:ENUM', 'DIMENSION:ENUM_VALUE', '维度');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'columnAttributeEnum:ENUM', 'METRIC:ENUM_VALUE', '指标');
