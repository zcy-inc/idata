-- table sys_feature
-- -- first level menu
-- 旧菜单
-- insert into idata.sys_feature (feature_code, feature_name, feature_type)
-- values ('F_MENU_DATA_RD', '数据研发', 'F_MENU');
-- insert into idata.sys_feature (feature_code, feature_name, feature_type)
-- values ('F_MENU_DATASOURCE_MANAGE', '数据源管理', 'F_MENU');
-- insert into idata.sys_feature (feature_code, feature_name, feature_type)
-- values ('F_MENU_METRIC_MANAGE', '指标库', 'F_MENU');

insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_BIGDATA_RD', '数据研发', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_LABEL_MANAGE', '数据标签', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_OPS_CENTER', '运维中心', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATA_QUALITY', '数据质量', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATAPI', 'Datapi', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_XIAOCAI_BI', '小采BI', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_USER_MANAGE', '用户管理', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_ROLE_MANAGE', '角色管理', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_AD_HOC_QUERY', '临时查询', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_ETL_TOOL', '小工具', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_CATEGORY_PREDICTION', '类目预测工作台', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_SYSTEM_CONFIG', '系统配置', 'F_MENU');
-- -- second level menu
-- parentCode: F_MENU_SYSTEM_CONFIG
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_USER_FEATURE', '用户权限', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/uac/roles');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_CONFIG_CENTER', '集成配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/sys/configs');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_LDAP_CONFIG', 'LDAP配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/sys/configs');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_METADATA_CONFIG', '元数据标签配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/dev/labelDefines');
-- parentCode: F_MENU_BIGDATA_RD
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_DATA_DEVELOP', '数据开发', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/dev/compositeFolders/functions/tree');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_DATASOURCE_CENTER', '数据源管理', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/das/datasources');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_JOB_LIST', '任务列表', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/dev/jobs/publishRecords/page');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_MEASURE_MANAGE', '指标库', 'F_MENU', 'F_MENU_BIGDATA_RD', '');
-- parentCode: F_MENU_OPS_CENTER
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_OPS_DASHBOARD', '运维看板', 'F_MENU', 'F_MENU_OPS_CENTER', '/api/p1/ops/dashboard/jobSchedule/overview');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_JOB_HISTORY', '作业历史', 'F_MENU', 'F_MENU_OPS_CENTER', '/api/p1/ops/dashboard/page/jobHistory');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_CLUSTER_MONITORING', '集群监控', 'F_MENU', 'F_MENU_OPS_CENTER', '/api/p1/ops/clusters/apps');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_JOB_MONITORING', '任务监控', 'F_MENU', 'F_MENU_OPS_CENTER', '/api/p1/dev/jobs/overhangPage');
-- parentCode: F_MENU_DATAPI
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_DEVELOP', 'API开发', 'F_MENU', 'F_MENU_DATAPI');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_APP_MANAGE', '应用', 'F_MENU', 'F_MENU_DATAPI');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_APP_ACCESS', '应用权限', 'F_MENU', 'F_MENU_DATAPI');

insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_DW_DESIGN', '数仓设计', 'F_MENU', 'F_MENU_DATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_JOB_MANAGE', '作业管理', 'F_MENU', 'F_MENU_DATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_RESOURCE_MANAGE', '资源管理', 'F_MENU', 'F_MENU_DATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_FUNCTION_MANAGE', '函数管理', 'F_MENU', 'F_MENU_DATA_RD');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_DEVELOP', 'API开发', 'F_MENU', 'F_MENU_DATAPI');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_APP_MANAGE', '应用', 'F_MENU', 'F_MENU_DATAPI');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_MENU_API_APP_ACCESS', '应用权限', 'F_MENU', 'F_MENU_DATAPI');
-- -- icon
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_DATA_DEVELOP_ROOT_DIR', '数据开发文件夹根目录添加', 'F_ICON', 'F_MENU_BIGDATA_RD', '/api/p1/dev/compositeFolders');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_RELEASE_DATA_JOB', '作业发布', 'F_ICON', 'F_MENU_JOB_LIST', '/api/p1/dev/jobs/publishRecords/approve');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_REJECT_DATA_JOB', '作业驳回', 'F_ICON', 'F_MENU_JOB_LIST', '/api/p1/dev/jobs/publishRecords/reject');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_API_ROOT_DIR', 'API开发根目录添加', 'F_ICON', 'F_MENU_API_DEVELOP');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_RELEASE_API', 'Datapi接口发布真线', 'F_ICON', 'F_MENU_API_DEVELOP');

-- table sys_config
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'ds-config','{"url":{},"token":{}}', 'DS');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'hive-config','{"url":{},"username":{},"password":{}}', 'HIVE_METASTORE');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'htool-config','{"jobDetails.apiUrl":{},"sqoop.log.path":{},"hive2.jdbc.url":{},"hive2.jdbc.username":{},"hive2.jdbc.password":{},"hdfs.addr":{},"yarn.addr":{},"sqlRewrite.apiUrl":{},"kylin.auth":{},"kylin.apiUrl":{},"htool.addr":{}}', 'HTOOL');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'livy-config','{"url":{},"livy.sessionMax":{}}', 'LIVY');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'ldap-config','{"ldap.url":{},"ldap.base":{},"ldap.userDn":{},"ldap.password":{},"ldap.domain":{}}', 'LDAP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'autocompletion-config','{}', 'SQL');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'autocompletion-config','{}', 'PYTHON');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'core-site','{}', 'HADOOP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'hdfs-site','{}', 'HADOOP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'yarn-site','{}', 'HADOOP');

insert into sys_config(key_one, value_one) values ('{}');
insert into sys_config(key_one, value_one)
values ('{"hive-info":{"configValue":"jdbc:hive2://bigdata-master3.cai-inc.com:10000/default"}}');
