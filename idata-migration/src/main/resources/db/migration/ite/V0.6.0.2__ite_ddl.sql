
create table if not exists ite_ds_entity_mapping
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    entity_id              bigint(20) unsigned not null comment '业务实体id',
    environment            varchar(20)         not null comment '环境',
    ds_entity_type         varchar(10)         not null comment '业务实体type: workflow, task',
    ds_entity_code         bigint(20) unsigned not null comment 'ds 业务实体code',
    unique key idx_id_code(entity_id, ds_entity_type, environment,ds_entity_code),
    unique key idx_code_id(ds_entity_code, ds_entity_type, environment,entity_id)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '系统集成-ds业务实体映射';

create table if not exists ite_ds_dependence_node
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    create_time            datetime            not null default current_timestamp comment '创建时间',
    task_code              bigint(20) unsigned not null comment 'ds任务code',
    workflow_code          bigint(20) unsigned not null comment 'ds工作流code',
    dependence_node_code   bigint(20) unsigned not null comment 'ds依赖节点code',
    prev_task_code         bigint(20) unsigned not null comment '上游ds任务code',
    prev_workflow_code     bigint(20) unsigned not null comment '上游ds工作流code',
    key idx_workflow_prev_task(workflow_code, prev_task_code),
    key idx_dependence_node(dependence_node_code),
    key idx_task(task_code)
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4 comment '系统集成-ds依赖节点';