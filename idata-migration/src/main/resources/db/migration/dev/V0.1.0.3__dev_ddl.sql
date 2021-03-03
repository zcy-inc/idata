create table if not exists idata.dev_folder (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  folder_type varchar(20)     not null comment '文件夹类型',
  folder_name varchar(30)     not null comment '文件夹名称',
  parent_id   bigint unsigned comment '父文件夹ID'
) engine=innodb default charset=utf8mb4 comment='文件夹表';

create table if not exists idata.dev_table (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  table_type  varchar(20)     not null comment '表类型',
  table_name  varchar(30)     not null comment '表名称',
  folder_id   bigint unsigned comment '所属文件夹ID'
) engine=innodb default charset=utf8mb4 comment='表信息表';
