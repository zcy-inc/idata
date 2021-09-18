
create table if not exists dev_composite_folder
(
    id          bigint(20)  unsigned primary key auto_increment comment '主键',
    del         tinyint(4)  not null default 0 comment '是否删除，0否，1是',
    creator     varchar(20) not null comment '创建者',
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(20) not null default '' comment '修改者',
    edit_time   datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    name        varchar(30) not null comment '名称',
    type        varchar(20) not null comment '功能型：FUNCTION，普通型：FOLDER',
    belong      varchar(20) not null comment '文件夹所属业务功能：DESIGN, DESIGN.TABLE, DESIGN.LABEL, DESIGN.ENUM, DAG, DI, DEV, DEV.JOB',
    parent_id   bigint(20)  not null comment '父文件夹编号，第一级文件夹父编号为0',
    key idx_name(name(16)),
    key idx_belong(belong(16)),
    key idx_parent_id(parent_id)
) engine = innodb
  auto_increment = 10001
  default charset = utf8mb4 comment '数据开发-复合文件夹表';