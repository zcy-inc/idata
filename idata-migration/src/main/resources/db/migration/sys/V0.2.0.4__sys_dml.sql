-- table sys_feature
-- -- first level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_OBJECT_LABEL', '数据标签', 'F_MENU');

-- presto 地址，待数据源配置功能具备后删除
insert into sys_config(key_one, value_one)
values ('trino-info','{"port":80,"host":"presto-new.bigdata.cai-inc.com","type":"presto","dbCatalog":"hive","username":"presto"}')