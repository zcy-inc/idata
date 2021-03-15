-- create database if not exists idata default character set utf8mb4;

create table if not exists idata.sys_config (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  key_one     varchar(30)     not null comment '系统配置键1',
  value_one   varchar(200)    not null comment '系统配置值1'
) engine=innodb default charset=utf8mb4 comment='系统配置表';

create table if not exists idata.sys_feature (
  id           bigint unsigned not null auto_increment comment '主键', primary key(id),
  del          smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  create_time  datetime(3)     not null default current_timestamp(3) comment '创建时间',
  edit_time    datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  feature_code varchar(30)     not null comment '功能编码', unique uk_feature_code(feature_code),
  feature_name varchar(20)     not null comment '功能名称',
  feature_type varchar(30)     not null comment '功能类型',
  parent_code  varchar(30)     comment '上级功能编码'
) engine=innodb default charset=utf8mb4 comment='功能表';
