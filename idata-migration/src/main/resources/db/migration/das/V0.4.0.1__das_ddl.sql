
create table if not exists das_data_source
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    type          varchar(30)         not null comment '数据源类型',
    name          varchar(50)         not null comment '数据源名称',
    environments  varchar(50)         not null comment '环境，多个环境用,号分隔',
    remark        varchar(200)        null comment '备注',
    db_configs    varchar(2000)       not null comment '数据库配置，json字符串',
    key idx_name(name(16))
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据库数据源表';

create table if not exists das_data_source_file
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    type          varchar(30)         not null comment '数据源类型',
    name          varchar(50)         not null comment '数据源名称',
    environments  varchar(50)         not null comment '环境，多个环境用,号分隔',
    remark        varchar(200)        null comment '备注',
    file_name     varchar(200)        not null comment '文件名称',
    key idx_name(name(16))
) engine = innodb
  auto_increment = 10000
  default charset = utf8mb4 comment '文件型数据源表';



