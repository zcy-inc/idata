create table if not exists idata.uac_app_info (
  id            bigint unsigned not null auto_increment comment '主键', primary key(id),
  del           tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator       varchar(20)     not null comment '创建者',
  create_time   datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor        varchar(20)     not null default '' comment '修改者',
  edit_time     datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  app_name      varchar(200)    not null comment '应用名称',
  app_key       varchar(200)    not null comment '应用编码',
  app_secret    varchar(200)    not null comment '应用秘钥',
  description   varchar(500)    not null comment '应用描述'
) engine=innodb default charset=utf8mb4 comment='应用信息表';

create table if not exists idata.uac_app_feature (
  id            bigint unsigned not null auto_increment comment '主键', primary key(id),
  del           tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator       varchar(20)     not null comment '创建者',
  create_time   datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor        varchar(20)     not null default '' comment '修改者',
  edit_time     datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  app_key       varchar(200)    not null comment '应用编码',
  feature_codes varchar(200)    not null comment '功能编码(英文逗号分隔)'
) engine=innodb default charset=utf8mb4 comment='应用权限表';