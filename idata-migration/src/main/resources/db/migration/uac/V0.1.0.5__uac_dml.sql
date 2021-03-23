insert into idata.uac_user (id, creator, username, sys_admin, auth_type, password, nickname)
values (1, '', 'admin', '1', 'REGISTER', '09e7ee58adc68d5399b35ea5b4f96731', '系统管理员');

insert into idata.uac_role (id, creator, role_code, role_name)
values (1, '系统管理员', 'ADMIN', '管理员');
insert into idata.uac_role(id, creator, role_code, role_name)
values (2, '系统管理员', 'DW_DEVELOPMENT', '数仓开发');
insert into idata.uac_role(id, creator, role_code, role_name)
values (3, '系统管理员', 'BIZ_DEVELOPMENT', '业务开发');

-- insert into uac_access
-- first level menu
insert into idata.uac_access (id, access_code, access_type, access_key)
values (1, 'F_MENU_DATA_RD', 'F_MENU', 'DATA_RD');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (2, 'F_MENU_DATA_QUALITY', 'F_MENU', 'DATA_QUALITY');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (3, 'F_MENU_DATAPI', 'F_MENU', 'DATAPI');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (4, 'F_MENU_DATASOURCE_MANAGE', 'F_MENU', 'DATASOURCE_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (5, 'F_MENU_DATA_METRIC_MANAGE', 'F_MENU', 'METRIC_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (6, 'F_MENU_OPS_CENTER', 'F_MENU', 'OPS_CENTER');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (7, 'F_MENU_XIAOCAI_BI', 'F_MENU', 'XIAOCAI_BI');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (8, 'F_MENU_USER_MANAGE', 'F_MENU', 'USER_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (9, 'F_MENU_ROLE_MANAGE', 'F_MENU', 'ROLE_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (10, 'F_MENU_AD_HOC_QUERY', 'F_MENU', 'HOC_QUERY');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (11, 'F_MENU_ETL_TOOL', 'F_MENU', 'ETL_TOOL');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (12, 'F_MENU_CATEGORY_PREDICTION', 'F_MENU', 'CATEGORY_PREDICTION');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (13, 'F_MENU_IREPORT', 'F_MENU', 'IREPORT');

-- second level menu
insert into idata.uac_access (id, access_code, access_type, access_key)
values (14, 'F_MENU_DW_DESIGN', 'F_MENU', 'DW_DESIGN');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (15, 'F_MENU_JOB_MANAGE', 'F_MENU', 'JOB_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (16, 'F_MENU_RESOURCE_MANAGE', 'F_MENU', 'RESOURCE_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (17, 'F_MENU_FUNCTION_MANAGE', 'F_MENU', 'FUNCTION_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (18, 'F_MENU_API_DEVELOP', 'F_MENU', 'API_DEVELOP');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (19, 'F_MENU_API_APP_MANAGE', 'F_MENU', 'API_APP_MANAGE');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (20, 'F_MENU_API_APP_ACCESS', 'F_MENU', 'API_APP_ACCESS');

-- icon
insert into idata.uac_access (id, access_code, access_type, access_key)
values (21, 'F_ICON_DW_DESIGN_ROOT_DIR', 'F_ICON', 'DW_DESIGN_ROOT_DIR');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (22, 'F_ICON_JOB_ROOT_DIR', 'F_ICON', 'JOB_ROOT_DIR');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (23, 'F_ICON_RELEASE_JOB', 'F_ICON', 'RELEASE_JOB');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (24, 'F_ICON_RESOURCE_ROOT_DIR', 'F_ICON', 'RESOURCE_ROOT_DIR');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (25, 'F_ICON_FUNCTION_ROOT_DIR', 'F_ICON', 'FUNCTION_ROOT_DIR');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (26, 'F_ICON_API_ROOT_DIR', 'F_ICON', 'API_ROOT_DIR');
insert into idata.uac_access (id, access_code, access_type, access_key)
values (27, 'F_ICON_RELEASE_API', 'F_ICON', 'RELEASE_API');

-- insert into uac_role_access
-- ADMIN
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DATA_QUALITY');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DATASOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DATA_METRIC_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_OPS_CENTER');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_XIAOCAI_BI');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_USER_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_AD_HOC_QUERY');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_ETL_TOOL');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_CATEGORY_PREDICTION');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_IREPORT');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_ROLE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_DW_DESIGN');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENO_JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_API_DEVELOP');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_API_APP_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_MENU_API_APP_ACCESS');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_DW_DESIGN_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_JOB_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_RELEASE_JOB');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_RESOURCE_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_FUNCTION_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_API_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'ADMIN', 'F_ICON_RELEASE_API');

-- DW_DEVELOPMENT
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DATA_QUALITY');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DATASOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DATA_METRIC_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_OPS_CENTER');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_XIAOCAI_BI');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_AD_HOC_QUERY');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_ETL_TOOL');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_CATEGORY_PREDICTION');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_IREPORT');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_DW_DESIGN');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENO_JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_API_DEVELOP');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_API_APP_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_MENU_API_APP_ACCESS');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_ICON_DW_DESIGN_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_ICON_JOB_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_ICON_RESOURCE_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_ICON_FUNCTION_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'DW_DEVELOPMENT', 'F_ICON_API_ROOT_DIR');

-- BIZ_DEVELOPMENT
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_API_DEVELOP');
insert into idata.uac_role_access (creator, role_code, access_code)
values ('系统管理员', 'BIZ_DEVELOPMENT', 'F_MENU_API_APP_ACCESS');
