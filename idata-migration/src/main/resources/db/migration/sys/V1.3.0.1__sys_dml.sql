update idata.sys_feature set parent_code = null, feature_name = '数据指标' where feature_code = 'F_MENU_MEASURE_MANAGE';

insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_MEASURE_METRIC', '指标', 'F_MENU', 'F_MENU_MEASURE_MANAGE', '');
insert into idata.sys_feature (feature_code, feature_name, feature_type, parent_code, feature_url_path)
values ('F_MENU_MEASURE_MODIFIER', '修饰词', 'F_MENU', 'F_MENU_MEASURE_MANAGE', '');