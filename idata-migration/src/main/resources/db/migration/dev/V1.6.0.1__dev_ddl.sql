
create table if not exists dev_job_content_di_stream
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    editor                 varchar(20)         not null default '' comment '修改者',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    editable               tinyint(4)          not null comment '是否可编辑，0否，1是',
    version                int(11) unsigned    not null comment '作业版本号',
    src_data_source_type   varchar(20)         not null comment '数据来源-数据源类型',
    src_data_source_id     bigint(20) unsigned not null comment '数据来源-数据源id',
    dest_data_source_type  varchar(20)         not null comment '数据去向-数据源类型',
    dest_data_source_id    bigint(20) unsigned not null comment '数据去向-数据源id',
    cdc_tables             text                not null comment 'cdc抽数配置',
    unique key idx_job_id_version(job_id, version)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-DI-STREAM作业内容';