-- table uac_user
insert into idata.uac_user (id, creator, username, sys_admin, auth_type, password, nickname)
values (1, '', 'admin', '1', 'REGISTER', '09e7ee58adc68d5399b35ea5b4f96731', '系统管理员');

-- table uac_role
insert into idata.uac_role (id, creator, role_code, role_name)
values (1, '系统管理员', 'ADMIN', '管理员');
insert into idata.uac_role(id, creator, role_code, role_name)
values (2, '系统管理员', 'DW_DEVELOPER', '数仓开发');
insert into idata.uac_role(id, creator, role_code, role_name)
values (3, '系统管理员', 'BIZ_DEVELOPER', '业务开发');

-- table uac_role_access
-- -- ADMIN
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_DATA_RD', 'F_MENU', 'DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_DW_DESIGN', 'F_MENU', 'DW_DESIGN');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENO_JOB_MANAGE', 'F_MENU', 'JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_RESOURCE_MANAGE', 'F_MENU', 'RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_FUNCTION_MANAGE', 'F_MENU', 'FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_DATAPI', 'F_MENU', 'DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_API_DEVELOP', 'F_MENU', 'API_DEVELOP');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_API_APP_MANAGE', 'F_MENU', 'API_APP_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_API_APP_ACCESS', 'F_MENU', 'API_APP_ACCESS');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_DATA_QUALITY', 'F_MENU', 'DATA_QUALITY');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_DATASOURCE_MANAGE', 'F_MENU', 'DATASOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_METRIC_MANAGE', 'F_MENU', 'METRIC_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_OPS_CENTER', 'F_MENU', 'OPS_CENTER');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_XIAOCAI_BI', 'F_MENU', 'XIAOCAI_BI');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_AD_HOC_QUERY', 'F_MENU', 'AD_HOC_QUERY');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_ETL_TOOL', 'F_MENU', 'ETL_TOOL');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_CATEGORY_PREDICTION', 'F_MENU', 'CATEGORY_PREDICTION');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_IREPORT', 'F_MENU', 'IREPORT');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_USER_MANAGE', 'F_MENU', 'USER_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_MENU_ROLE_MANAGE', 'F_MENU', 'ROLE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_DW_DESIGN_ROOT_DIR', 'F_ICON', 'DW_DESIGN_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_JOB_ROOT_DIR', 'F_ICON', 'JOB_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_RESOURCE_ROOT_DIR', 'F_ICON', 'RESOURCE_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_FUNCTION_ROOT_DIR', 'F_ICON', 'FUNCTION_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_API_ROOT_DIR', 'F_ICON', 'API_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_RELEASE_API', 'F_ICON', 'RELEASE_API');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'ADMIN', 'F_ICON_RELEASE_JOB', 'F_ICON', 'RELEASE_JOB');
-- -- DW_DEVELOPER
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_DATA_RD', 'F_MENU', 'DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_DW_DESIGN', 'F_MENU', 'DW_DESIGN');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENO_JOB_MANAGE', 'F_MENU', 'JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_RESOURCE_MANAGE', 'F_MENU', 'RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_FUNCTION_MANAGE', 'F_MENU', 'FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_DATAPI', 'F_MENU', 'DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_API_DEVELOP', 'F_MENU', 'API_DEVELOP');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_API_APP_MANAGE', 'F_MENU', 'API_APP_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_API_APP_ACCESS', 'F_MENU', 'API_APP_ACCESS');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_DATA_QUALITY', 'F_MENU', 'DATA_QUALITY');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_DATASOURCE_MANAGE', 'F_MENU', 'DATASOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_METRIC_MANAGE', 'F_MENU', 'METRIC_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_OPS_CENTER', 'F_MENU', 'OPS_CENTER');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_XIAOCAI_BI', 'F_MENU', 'XIAOCAI_BI');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_AD_HOC_QUERY', 'F_MENU', 'AD_HOC_QUERY');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_ETL_TOOL', 'F_MENU', 'ETL_TOOL');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_CATEGORY_PREDICTION', 'F_MENU', 'CATEGORY_PREDICTION');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_MENU_IREPORT', 'F_MENU', 'IREPORT');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_ICON_DW_DESIGN_ROOT_DIR', 'F_ICON', 'DW_DESIGN_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_ICON_JOB_ROOT_DIR', 'F_ICON', 'JOB_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_ICON_RESOURCE_ROOT_DIR', 'F_ICON', 'RESOURCE_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_ICON_FUNCTION_ROOT_DIR', 'F_ICON', 'FUNCTION_ROOT_DIR');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'DW_DEVELOPER', 'F_ICON_API_ROOT_DIR', 'F_ICON', 'API_ROOT_DIR');
-- -- BIZ_DEVELOPER
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_DATA_RD', 'F_MENU', 'DATA_RD');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_JOB_MANAGE', 'F_MENU', 'JOB_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_RESOURCE_MANAGE', 'F_MENU', 'RESOURCE_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_FUNCTION_MANAGE', 'F_MENU', 'FUNCTION_MANAGE');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_DATAPI', 'F_MENU', 'DATAPI');
insert into idata.uac_role_access (creator, role_code, access_code, access_type, access_key)
values ('系统管理员', 'BIZ_DEVELOPER', 'F_MENU_API_DEVELOP', 'F_MENU', 'API_DEVELOP');
