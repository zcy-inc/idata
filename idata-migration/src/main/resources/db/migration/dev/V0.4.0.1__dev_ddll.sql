
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

create table if not exists dev_dag_info
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    name          varchar(50)         not null comment '名称',
    dw_layer_code varchar(50)         not null comment '数仓分层',
    status        int(11)             not null comment '状态，1启用，0停用',
    remark        varchar(200)        null comment '备注',
    folder_id     bigint(20) unsigned not null comment '文件夹id',
    key idx_name(name(16))
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-dag';

create table if not exists dev_dag_schedule
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    dag_id        bigint(20) unsigned not null comment 'dag id',
    begin_time    datetime            not null comment '开始时间',
    end_time      datetime            not null comment '结束时间',
    period_range  varchar(20)         not null comment '周期范围，year, month, week, day, hour, minute',
    trigger_mode  varchar(20)         not null comment '触发方式，interval: 时间间隔，point: 指定时间',
    cron_expression   varchar(200)        null comment 'cron表达式',
    key idx_dag_id(dag_id)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-dag-调度配置表';

create table if not exists dev_job_info
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    name          varchar(50)         not null comment '作业名称',
    job_type      varchar(20)         not null comment '作业类型',
    dw_layer_code varchar(50)         not null comment '数仓分层',
    status        int(11)             not null comment '状态，1启用，0停用',
    remark        varchar(200)        null comment '备注',
    folder_id     bigint(20) unsigned not null comment '文件夹id',
    key idx_name(name(16))
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-作业基本信息表';

create table if not exists dev_job_content_di
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
    src_tables             text                not null comment '数据来源-表',
    src_read_mode          varchar(20)         not null comment '数据来源-读取模式，all：全量，incremental：增量',
    src_read_filter        varchar(1000)       not null default '' comment '数据来源-过滤条件',
    src_read_shard_key     varchar(50)         not null default '' comment '数据来源-切分键',
    src_columns            text                not null comment '数据来源-字段信息，json格式',
    dest_data_source_type  varchar(20)         not null comment '数据去向-数据源类型',
    dest_data_source_id    bigint(20) unsigned not null comment '数据去向-数据源id',
    dest_table_id          bigint(20) unsigned not null comment '数据去向-数仓表id',
    src_write_mode         varchar(20)         not null comment '数据去向-写入模式，override，upsert',
    dest_before_write      varchar(1000)       not null default '' comment '数据去向-写入前语句',
    dest_after_write       varchar(1000)       not null default '' comment '数据去向-写入后语句',
    dest_columns           text                not null comment '数据去向-字段信息，json格式',
    content_hash           varchar(50)         not null default '' comment '作业内容hash',
    key idx_job_id_version(job_id, version)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-DI作业内容';

create table if not exists dev_job_execute_config
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    editor                 varchar(20)         not null default '' comment '修改者',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    environment            varchar(20)         not null comment '环境',
    sch_dag_id             bigint(20) unsigned not null comment '调度配置-dag编号',
    sch_rerun_mode         varchar(20)         not null comment '调度配置-重跑配置，always：皆可重跑，failed：失败后可重跑，never：皆不可重跑',
    sch_time_out           int(11)             not null comment '调度配置-超时时间，单位：秒',
    sch_dry_run            tinyint(4)          not null comment '调度配置-是否空跑，0否，1是',
    exec_queue             varchar(30)         not null comment '运行配置-队列',
    exec_max_parallelism   int(11) unsigned    not null default 0 comment '运行配置-作业最大并发数，配置为0时表示使用默认并发数',
    exec_warn_level        varchar(30)         not null default '' comment '运行配置-告警等级',
    key idx_job_id(job_id)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-作业运行配置';

create table if not exists dev_job_publish_record
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    editor                 varchar(20)         not null default '' comment '修改者',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    job_content_id         bigint(20) unsigned not null comment '作业内容id',
    job_content_version    int(11) unsigned    not null comment '作业内容版本号',
    job_type_code          varchar(20)         not null comment '作业类型',
    dw_layer_code          varchar(50)         not null comment '数仓分层',
    environment            varchar(20)         not null comment '环境',
    publish_status         int(11)             not null comment '发布状态，1：待发布，2：已发布，4：已驳回，9：已归档',
    submit_remark          varchar(200)        not null default '' comment '提交备注',
    approve_operator       varchar(20)         not null default '' comment '审批人',
    approve_time           datetime            null comment '审批时间',
    approve_remark         varchar(200)        not null default '' comment '审批备注',
    key idx_job_id_version(job_id, job_content_id, job_content_version)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-作业发布记录';