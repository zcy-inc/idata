create table if not exists lab_folder
(
    id          bigint(20) unsigned primary key auto_increment comment '主键',
    del         tinyint(4)  not null default 0 comment '是否删除，0否，1是',
    creator     varchar(20) not null comment '创建者',
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(20) not null default '' comment '修改者',
    edit_time   datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    name        varchar(30) not null comment '名称',
    parent_id   bigint(20)  not null comment '父文件夹编号，第一级文件夹父编号为0',
    belong      varchar(20) not null comment '所属业务标识'
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '标签文件夹表';

create table if not exists lab_object_label
(
    id          bigint(20) unsigned primary key auto_increment comment '主键',
    del         tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator     varchar(20)         not null comment '创建者',
    create_time datetime            not null default current_timestamp comment '创建时间',
    editor      varchar(20)         not null default '' comment '修改者',
    edit_time   datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    name        varchar(100)        NOT NULL COMMENT '名称',
    name_en     varchar(100)        NOT NULL COMMENT '英文名称',
    object_type varchar(10)         NOT NULL COMMENT '主体类型',
    remark      varchar(200)        NOT NULL COMMENT '备注',
    rules       mediumtext COMMENT '标签规则，json格式',
    version     int(11) unsigned    NOT NULL COMMENT '版本',
    folder_id   bigint(20) unsigned NOT NULL COMMENT '文件夹id'
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '对象标签定义表';


