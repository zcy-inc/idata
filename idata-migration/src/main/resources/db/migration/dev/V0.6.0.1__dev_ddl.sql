
alter table dev_dag_info add column environment varchar(20) not null default "" comment '环境';

alter table dev_job_execute_config add column sch_time_out_strategy varchar(20) not null comment '调度配置-超时策略，alarm：超时告警，fail：超时失败，都有时用,号分隔';
alter table dev_job_execute_config add column sch_priority int(11) not null comment '调度配置-优先级，1：低，2：中，3：高';
alter table dev_job_execute_config add column sch_fail_strategy varchar(10) not null comment '调度配置-失败策略';
alter table dev_job_execute_config add column exec_driver_mem int(11) unsigned not null default 0 comment '运行配置-驱动器内存';
alter table dev_job_execute_config add column exec_worker_mem int(11) unsigned not null default 0 comment '运行配置-执行器内存';
alter table dev_job_execute_config add column extension_cfg varchar(300) not null default '' comment '扩展配置字段';

create table if not exists dev_dag_dependence
(
    id                  bigint(20) unsigned primary key auto_increment comment '主键',
    creator             varchar(20)         not null comment '创建者',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    dag_id              bigint(20) unsigned not null comment 'dag id',
    prev_dag_id   bigint(20) unsigned not null comment '依赖的前置 dag id',
    unique key idx_dag_prev(dag_id, prev_dag_id),
    unique key idx_prev_dag(prev_dag_id, dag_id)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据开发-dag-DAG依赖关系';

create table if not exists dev_dag_event_log
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    dag_id        bigint(20) unsigned not null comment 'dag id',
    dag_event     varchar(20)         not null comment '事件，created, updated, deleted ...',
    event_info    varchar(300)        null comment '事件信息，用于事件重放处理时使用',
    handle_status int(11)             not null comment '事件处理状态，0: 待处理，1: 处理成功，9：处理失败',
    handle_msg    varchar(150)        null comment '处理结果信息',
    key idx_dag_id_event(dag_id, dag_event)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据开发-dag-DAG事件日志表';

create table if not exists dev_job_event_log
(
    id            bigint(20) unsigned primary key auto_increment comment '主键',
    del           tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator       varchar(20)         not null comment '创建者',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    editor        varchar(20)         not null default '' comment '修改者',
    edit_time     datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    job_id        bigint(20) unsigned not null comment 'dag id',
    job_event     varchar(20)         not null comment '事件，created, updated, deleted ...',
    event_info    varchar(300)        null comment '事件信息，用于事件重放处理时使用',
    handle_status int(11)             not null comment '事件处理状态，0: 待处理，1: 处理成功，9：处理失败',
    handle_msg    varchar(150)        null comment '处理结果信息',
    key idx_job_id_event(job_id, job_event)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据开发-作业事件日志表';

create table if not exists dev_job_dependence
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    environment            varchar(20)         not null comment '环境',
    prev_job_id             bigint(20) unsigned not null comment '上游作业id',
    prev_job_dag_id         varchar(20)         not null comment '上游作业所属dag id',
    key idx_job_env_prev (job_id, environment, prev_job_id),
    key idx_prev_env_job (prev_job_id, environment, job_id)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据开发-作业依赖';

create table if not exists dev_job_output
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建者',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    job_id                 bigint(20) unsigned not null comment '作业id',
    environment            varchar(20)         not null comment '环境',
    dest_data_source_type  varchar(20)         not null comment '数据去向-数据源类型',
    dest_data_source_id    bigint(20) unsigned not null comment '数据去向-数据源id',
    dest_table             varchar(100)        not null comment '数据去向-目标表',
    dest_write_mode        varchar(20)         not null comment '数据去向-写入模式，init: 新建表，override: 覆盖表',
    key idx_job_env (job_id, environment),
    key idx_table (dest_table(40))
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '数据开发-作业输出';