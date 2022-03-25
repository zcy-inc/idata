-- auto-generated definition
create table if not exists workspace
(
    id          bigint auto_increment
        primary key,
    del         tinyint(1)  default 0                 not null,
    creator     varchar(20)                           not null,
    editor      varchar(20) default ''                not null,
    create_time timestamp   default CURRENT_TIMESTAMP not null,
    edit_time   timestamp   default CURRENT_TIMESTAMP not null,
    name        varchar(64)                           not null comment '工作空间名称',
    code        varchar(64)                           not null comment '工作空间code',
    url_path    varchar(128)                          not null comment '工作空间url path前缀'
)
    comment '工作空间表';


