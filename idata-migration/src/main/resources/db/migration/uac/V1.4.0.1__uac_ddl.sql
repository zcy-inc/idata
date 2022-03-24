-- auto-generated definition
create table sys_resource
(
    id                bigint unsigned auto_increment comment '主键'
        primary key,
    del               tinyint     default 0                    not null comment '是否删除(1:是,0:否)',
    create_time       datetime(3) default CURRENT_TIMESTAMP(3) not null comment '创建时间',
    edit_time         datetime(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3) comment '修改时间',
    resource_code     varchar(30)                              not null comment '资源编码',
    resource_name     varchar(20)                              not null comment '资源名称',
    resource_type     varchar(30)                              not null comment '资源类型',
    parent_code       varchar(30)                              null comment '上级资源编码',
    resource_url_path varchar(50) default ''                   not null comment '资源请求路径',
    constraint uk_resource_code
        unique (resource_code)
)
    comment '资源表';

