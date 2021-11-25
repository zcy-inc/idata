alter table idata.sys_config add column creator varchar(20) not null comment '创建者' after del;
alter table idata.sys_config add column editor varchar(20) comment '修改者' after create_time;
alter table idata.sys_config add column type varchar(20) comment '配置类型';
alter table idata.sys_config modify key_one varchar(50) not null comment '系统配置键1';
alter table idata.sys_config modify value_one varchar(1000) not null comment '系统配置值1';
alter table idata.sys_feature add column feature_url_path varchar(50) not null default '' comment '功能请求路径';