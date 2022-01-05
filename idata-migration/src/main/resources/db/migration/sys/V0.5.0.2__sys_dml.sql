-- table sys_feature
-- -- first level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_SYSTEM_CONFIG', '系统配置', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_BIGDATA_RD', '数据研发', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type, feature_url_path)
values ('F_MENU_LABEL_MANAGE', '数据标签', 'F_MENU', '/api/p1/lab/labFolder/tree');

-- -- second level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_USER_FEATURE', '用户权限', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/uac/roles');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_CONFIG_CENTER', '集成配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/sys/configs');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_LDAP_CONFIG', 'LDAP配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/sys/configs');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_METADATA_CONFIG', '元数据标签配置', 'F_MENU', 'F_MENU_SYSTEM_CONFIG', '/api/p1/dev/labelDefines');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_DATA_DEVELOP', '数据开发', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/dev/compositeFolders/functions/tree');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_MEASURE_MANAGE', '指标库', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/dev/devFolderTree');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_DATASOURCE_CENTER', '数据源管理', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/das/datasources');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_JOB_LIST', '任务列表', 'F_MENU', 'F_MENU_BIGDATA_RD', '/api/p1/dev/jobs/publishRecords/page');

-- -- icon
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_DATA_DEVELOP_ROOT_DIR', '数据开发文件夹根目录添加', 'F_ICON', 'F_MENU_BIGDATA_RD', '/api/p1/dev/compositeFolders');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_RELEASE_DATA_JOB', '作业发布', 'F_ICON', 'F_MENU_JOB_LIST', '/api/p1/dev/jobs/publishRecords/approve');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_ICON_REJECT_DATA_JOB', '作业驳回', 'F_ICON', 'F_MENU_JOB_LIST', '/api/p1/dev/jobs/publishRecords/reject');

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
values ('系统管理员', 'ldap-config','{"ldap.url":{},"ldap.base":{},"ldap.userDn":{},"ldap.password":{}}', 'LDAP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'core-site','{}', 'HADOOP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'hdfs-site','{}', 'HADOOP');
insert into sys_config(creator ,key_one, value_one, type)
values ('系统管理员', 'yarn-site','{}', 'HADOOP');

update sys_config set value_one = '{"port":{"configValue":"80"},"host":{"configValue":"presto-new.bigdata.cai-inc.com"},"type":{"configValue":"presto"},"dbCatalog":{"configValue":"hive"},"username":{"configValue":"presto"}}'
where key_one = 'trino-info';
update sys_config set value_one = '{"hive-info":{"configValue":"jdbc:hive2://bigdata-master3.cai-inc.com:10000/default"}}'
where key_one = 'hive-info';
