-- table sys_feature
-- -- first level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_SYSTEM_CONFIG', '系统配置', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_BIGDATA_RD', '数据研发', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_LABEL_MANAGE', '数据标签', 'F_MENU');

-- -- second level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_USER_FEATURE', '用户权限', 'F_MENU', 'F_MENU_SYSTEM_CONFIG');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_CONFIG_CENTER', '集成配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_LDAP_CONFIG', 'LDAP配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_METADATA_CONFIG', '元数据标签配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_DATA_DEVELOP', '数据开发', 'F_MENU', 'F_MENU_BIGDATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_MEASURE_MANAGE', '指标库', 'F_MENU', 'F_MENU_BIGDATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_DATASOURCE_CENTER', '数据源管理', 'F_MENU', 'F_MENU_BIGDATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_JOB_LIST', '任务列表', 'F_MENU', 'F_MENU_BIGDATA_RD');
