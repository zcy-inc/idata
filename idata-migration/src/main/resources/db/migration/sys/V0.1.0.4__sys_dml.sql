-- insert into sys_feature
-- first level menu
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATA_RD', '数据研发', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATA_QUALITY', '数据质量', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATAPI', 'Datapi', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATASOURCE_MANAGE', '数据源管理', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_DATA_METRIC_MANAGE', '指标库', 'F_MENU');
insert into idata.sys_feature (feature_code, feature_name, feature_type)
values ('F_MENU_OPS_CENTER', '运维中心', 'F_MENU');
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
values ('F_MENU_IREPORT', 'IREPORT', 'F_MENU');

-- second level menu
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

-- icon
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_DW_DESIGN_ROOT_DIR', '数仓设计根目录添加', 'F_ICON', 'F_MENU_DW_DESIGN');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_JOB_ROOT_DIR', '作业管理根目录添加', 'F_ICON', 'F_MENU_JOB_MANAGE');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_RELEASE_JOB', '作业发布真线', 'F_ICON', 'F_MENU_JOB_MANAGE');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_RESOURCE_ROOT_DIR', '资源管理根目录添加', 'F_ICON', 'F_MENU_RESOURCE_MANAGE');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_FUNCTION_ROOT_DIR', '函数管理根目录添加', 'F_ICON', 'F_MENU_FUNCTION_MANAGE');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_API_ROOT_DIR', 'API开发根目录添加', 'F_ICON', 'F_MENU_API_DEVELOP');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code)
values ('F_ICON_RELEASE_API', 'Datapi接口发布真线', 'F_ICON', 'F_MENU_API_DEVELOP');