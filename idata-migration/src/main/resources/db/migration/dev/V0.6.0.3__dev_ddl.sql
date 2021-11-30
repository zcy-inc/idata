alter table dev_job_info add column job_language varchar(20) comment '作业使用语言' after job_type;
alter table dev_job_info add column job_execution_engine varchar(20) comment '作业执行引擎' after job_language;

create table if not exists idata.dev_job_content_sql (
  id                bigint unsigned     not null auto_increment comment '主键', primary key(id),
  del               tinyint             not null default 0 comment '是否删除(1:是,0:否)',
  creator           varchar(20)         not null comment '创建者',
  create_time       datetime(3)         not null default current_timestamp(3) comment '创建时间',
  editor            varchar(20)         not null default '' comment '修改者',
  edit_time         datetime(3)         not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  job_id            bigint unsigned     not null comment '作业ID',
  editable          tinyint(4)          not null comment '是否可编辑，0否，1是',
  version           int(11) unsigned    not null comment '作业版本号',
  source_sql        text                comment '数据来源SQL',
  udf_ids           varchar(20)         default null comment 'UDF ID列表',
  external_tables   varchar(100)        default null comment '外部表配置',
  unique key idx_job_id_version(job_id, version)
) engine=innodb auto_increment=10000 default charset=utf8mb4 comment='数据开发-SQL作业表';

create table if not exists idata.dev_job_content_spark (
  id                    bigint unsigned     not null auto_increment comment '主键', primary key(id),
  del                   tinyint             not null default 0 comment '是否删除(1:是,0:否)',
  creator               varchar(20)         not null comment '创建者',
  create_time           datetime(3)         not null default current_timestamp(3) comment '创建时间',
  editor                varchar(20)         not null default '' comment '修改者',
  edit_time             datetime(3)         not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  job_id                bigint unsigned     not null comment '作业ID',
  editable              tinyint(4)          not null comment '是否可编辑，0否，1是',
  version               int(11) unsigned    not null comment '作业版本号',
  resource_hdfs_path    varchar(200)        not null comment '执行文件HDFS地址',
  app_arguments         varchar(200)        not null default '{}' comment '执行参数',
  main_class            varchar(100)        comment '执行类(JAR类型)',
  unique key idx_job_id_version(job_id, version)
) engine=innodb auto_increment=500 default charset=utf8mb4 comment='数据开发-SPARK作业表';

create table if not exists idata.dev_job_content_script (
  id                    bigint unsigned     not null auto_increment comment '主键', primary key(id),
  del                   tinyint             not null default 0 comment '是否删除(1:是,0:否)',
  creator               varchar(20)         not null comment '创建者',
  create_time           datetime(3)         not null default current_timestamp(3) comment '创建时间',
  editor                varchar(20)         not null default '' comment '修改者',
  edit_time             datetime(3)         not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  job_id                bigint unsigned     not null comment '作业ID',
  editable              tinyint(4)          not null comment '是否可编辑，0否，1是',
  version               int(11) unsigned    not null comment '作业版本号',
  source_resource       text                comment '脚本资源内容',
  script_arguments      varchar(200)        not null default '{}' comment '执行参数',
  unique key idx_job_id_version(job_id, version)
) engine=innodb auto_increment=200 default charset=utf8mb4 comment='数据开发-SCRIPT作业表';

create table if not exists idata.dev_job_content_kylin (
  id                    bigint unsigned     not null auto_increment comment '主键', primary key(id),
  del                   tinyint             not null default 0 comment '是否删除(1:是,0:否)',
  creator               varchar(20)         not null comment '创建者',
  create_time           datetime(3)         not null default current_timestamp(3) comment '创建时间',
  editor                varchar(20)         not null default '' comment '修改者',
  edit_time             datetime(3)         not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  job_id                bigint unsigned     not null comment '作业ID',
  editable              tinyint(4)          not null comment '是否可编辑，0否，1是',
  version               int(11) unsigned    not null comment '作业版本号',
  cube_name             varchar(20)         not null comment 'CUBE名称',
  build_type            varchar(10)         not null comment 'CUBE构建类型(BUILD/MERGE/REFRESH)',
  start_time            datetime(3)         not null default current_timestamp(3) comment '数据开始时间',
  end_time              datetime(3)         not null default current_timestamp(3) comment '数据结束时间',
  unique key idx_job_id_version(job_id, version)
) engine=innodb auto_increment=200 default charset=utf8mb4 comment='数据开发-KYLIN作业表';