
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