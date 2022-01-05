insert into idata.dev_label_define (creator, label_code, label_name, label_tag, label_param_type, subject_type,
label_required, folder_id)
values ('系统管理员', 'alarmLayer:LABEL', '告警等级', 'ENUM_VALUE_LABEL', 'alarmLayerEnum:ENUM', 'TABLE', 0, 1);

insert into idata.dev_enum (creator, enum_code, enum_name, folder_id) values ('系统管理员', 'alarmLayerEnum:ENUM', '告警等级',
 2);
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_HIGH:ENUM_VALUE', '高');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_MEDIUM:ENUM_VALUE', '中');
insert into idata.dev_enum_value (creator, enum_code, value_code, enum_value)
values ('系统管理员', 'alarmLayerEnum:ENUM', 'ALARM_LEVEL_LOW:ENUM_VALUE', '低');