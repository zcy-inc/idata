
create table if not exists map_view_count
(
    id          bigint(20) unsigned primary key auto_increment comment '主键',
    del         tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator     varchar(20)         not null comment '创建者',
    create_time datetime            not null default current_timestamp comment '创建时间',
    editor      varchar(20)         not null default '' comment '修改者',
    edit_time   datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    entity_code        varchar(50)        NOT NULL COMMENT '实体记录唯一标识',
    entity_source     varchar(30)        NOT NULL COMMENT '实体记录数据源：数仓表（table） or 数据指标（indicator）',
    user_id bigint(20) unsigned NOT NULL COMMENT '用户编号，0表示全局所有用户的统计数据',
    view_count     bigint(20) unsigned    NOT NULL COMMENT '浏览次数',
    key idx_user_entity(user_id, entity_source, entity_code, del)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据地图用户-数据记录浏览次数统计表';


