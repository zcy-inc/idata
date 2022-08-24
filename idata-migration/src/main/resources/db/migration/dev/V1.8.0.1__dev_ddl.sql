
ALTER TABLE `dev_job_content_di_stream`
    CHANGE `cdc_tables` `cdc_config` text NOT NULL COMMENT 'cdc配置';

ALTER TABLE dev_job_content_di_stream
    add enable_sharding int(4) default 0 not null comment '是否开启分表支持，0：否，1：是' after dest_data_source_id;

create table if not exists dev_job_content_di_stream_table
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    job_id                 bigint(20) unsigned not null comment '作业id',
    job_content_id         bigint(20) unsigned not null comment '作业内容id',
    job_content_version    int(11) unsigned    not null comment '作业内容版本号',
    src_table              varchar(200)        not null comment '数据来源-表',
    dest_table             varchar(200)        not null comment '数据去向-表',
    sharding               int(4) default 0    not null comment '是否分表',
    force_init             int(4)              not null comment '是否强制初始化，0：否，1：是',
    table_cdc_props        text                not null comment '表cdc配置'
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-DI-STREAM 作业表配置';

create table if not exists dev_stream_job_instance
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    editor                 varchar(20)         not null default '' comment '修改者',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    job_name               varchar(128)        not null comment '作业name',
    job_content_id         bigint(20) unsigned not null comment '作业内容id',
    job_content_version    int(11) unsigned    not null comment '作业内容版本号',
    job_type_code          varchar(20)         not null comment '作业类型',
    dw_layer_code          varchar(50)         not null comment '数仓分层',
    owner                  varchar(20)         not null comment '责任人',
    environment            varchar(20)         not null comment '环境',
    status                 int(11)             not null comment '运行实例状态，0：待启动，1：启动中，2：运行中，6：启动失败，7：运行异常，8：已停止，9：已下线',
    run_start_time         datetime            null comment '运行开始时间',
    external_url           varchar(500)        not null default '' comment '外部链接',
    run_params             varchar(4000)       not null default '' comment '运行参数配置',
    key idx_job_version_env(job_id, job_content_version, environment)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-实时作业运行实例';

create table if not exists dev_stream_job_flink_info
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    environment            varchar(20)         not null comment '环境',
    secondary_id           varchar(150)         not null comment '作业类型',
    flink_job_id           varchar(50)         not null comment '数仓分层',
    key idx_env(job_id, environment)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-Flink Job运行信息';