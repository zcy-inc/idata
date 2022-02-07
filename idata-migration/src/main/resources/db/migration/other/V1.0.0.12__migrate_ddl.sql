
create table if not exists oth_migrate_result
(
    id              bigint(20) unsigned primary key auto_increment comment '主键',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    edit_time       datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    migrate_type    varchar(100)        NULL COMMENT '迁移操作类型',
    reason          text                NULL COMMENT '原因',
    data            mediumtext          NULL COMMENT '具体数据'
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '其他-数据迁移结果表';


