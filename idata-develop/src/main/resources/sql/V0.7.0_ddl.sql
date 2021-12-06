-- auto-generated definition
create table dev_job_history
(
    id             bigint auto_increment
        primary key,
    create_time    timestamp default CURRENT_TIMESTAMP not null,
    job_id         bigint                              not null comment '作业id',
    start_time     datetime                            not null comment '作业开始时间',
    finish_time    datetime                            not null comment '作业结束时间',
    duration       bigint                              not null comment '作业持续时间（ms）',
    final_status   varchar(32)                         not null comment '作业最终状态',
    avg_vcores     double                              not null comment '作业平均消耗cpu虚拟核数',
    avg_memory     bigint                              not null comment '作业平均消耗内存（MB）',
    application_id varchar(64)                         not null comment 'yarn的application',
    constraint dev_job_history_job_id_application_id_uindex
        unique (job_id, application_id)
)
    comment '作业执行历史表';


-- auto-generated definition
create table dev_job_udf
(
    id             bigint auto_increment
        primary key,
    del            tinyint(1)  default 0                 not null,
    creator        varchar(20)                           not null,
    editor         varchar(20) default ''                null,
    create_time    timestamp   default CURRENT_TIMESTAMP not null,
    edit_time      timestamp   default CURRENT_TIMESTAMP not null,
    udf_name       varchar(64)                           not null comment '''函数名称''',
    udf_type       varchar(20)                           not null comment '函数类型',
    file_name      varchar(64)                           null comment '文件名称',
    hdfs_path      varchar(256)                          not null comment 'hdfs文件路径',
    return_type    varchar(20)                           null comment '返回类型',
    return_sample  varchar(512)                          null comment '返回值',
    folder_id      bigint                                not null comment '目标文件夹',
    description    varchar(20)                           null comment '描述',
    command_format varchar(128)                          null comment '命令格式',
    udf_sample     varchar(1024)                         null comment '示例',
    constraint dev_job_udf_udf_name_uindex
        unique (udf_name)
);


