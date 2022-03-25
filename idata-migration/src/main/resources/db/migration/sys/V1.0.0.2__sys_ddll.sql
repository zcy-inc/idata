
create table if not exists sys_duty_info
(
    id          bigint(20)  unsigned primary key auto_increment comment '主键',
    del         tinyint(4)  not null default 0 comment '是否删除，0否，1是',
    creator     varchar(20) not null comment '创建者',
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(20) not null default '' comment '修改者',
    edit_time   datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    duty_default_phone varchar(20) not null comment '默认值班电话',
    duty_info   mediumtext  not null comment '值班电话信息'
) engine = innodb
  auto_increment = 10001
  default charset = utf8mb4 comment '系统配置-值班电话信息';
