-- table sys_feature
-- -- first level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_OBJECT_LABEL', '数据标签', 'F_MENU');

insert into sys_config(key_one, value_one)
values ('trino-info','{"port":18080,"host":"172.29.108.184","type":"presto","dbCatalog":"hive","username":"presto"}')