create table if not exists idata.dev_folder (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  folder_name varchar(30)     not null comment '文件夹名称',
  parent_id   bigint          comment '上级文件夹ID,null表示最外层文件夹'
) engine=innodb default charset=utf8mb4 comment='文件夹表';

create table if not exists idata.dev_table_info (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  table_name  varchar(30)     not null comment '表名称',
  folder_id   bigint          comment '文件夹ID,null表示最外层文件夹'
) engine=innodb default charset=utf8mb4 comment='表信息表';

create table if not exists idata.dev_column_info (
  id            bigint unsigned not null auto_increment comment '主键', primary key(id),
  del           tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator       varchar(20)     not null comment '创建者',
  create_time   datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor        varchar(20)     not null default '' comment '修改者',
  edit_time     datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  column_name   varchar(30)     not null comment '字段名称',
  table_id      bigint          not null comment '所属表ID',
  column_index  integer         comment '字段顺序(从0开始)'
) engine=innodb default charset=utf8mb4 comment='字段信息表';

create table if not exists idata.dev_label_define (
  id                 bigint unsigned not null auto_increment comment '主键', primary key(id),
  del                tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator            varchar(20)     not null comment '创建者',
  create_time        datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor             varchar(20)     not null default '' comment '修改者',
  edit_time          datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  label_code         varchar(30)     not null comment '标签唯一标识', unique uk_label_code(label_code),
  label_name         varchar(30)     not null comment '标签名称',
  label_tag          varchar(30)     comment '标签的标签',
  label_param_type   varchar(30)     comment '标签参数类型',
  label_attributes   varchar(500)    not null default '[]' comment '标签属性',
  special_attributes varchar(500)    not null default '[]' comment '标签特定属性',
  subject_type       varchar(20)     comment '打标主体类型',
  label_index        integer         comment '标签序号',
  label_required     boolean         not null default 0 comment '是否必须打标(1:是,0:否)',
  label_scope        bigint          comment '标签作用域;null:全局,folder_id:特定文件夹域',
  folder_id          bigint          comment '文件夹ID,null表示最外层文件夹'
) engine=innodb default charset=utf8mb4 comment='标签定义表';

create table if not exists idata.dev_label (
  id                bigint unsigned not null auto_increment comment '主键', primary key(id),
  del               tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator           varchar(20)     not null comment '创建者',
  create_time       datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor            varchar(20)     not null default '' comment '修改者',
  edit_time         datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  label_code        varchar(30)     not null comment '标签唯一标识',
  subject_code      varchar(30)     not null comment '标签主体ID或标识', unique uk_labelCode_subjectCode(label_code, subject_code),
  label_param_value varchar(200)    comment '标签参数值'
) engine=innodb default charset=utf8mb4 comment='标签表';

create table if not exists idata.dev_enum (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  enum_code   varchar(30)     not null comment '枚举标识', unique uk_enumCode(enum_code),
  enum_name   varchar(30)     not null comment '枚举名称',
  folder_id   bigint          comment '文件夹ID,null表示最外层文件夹'
) engine=innodb default charset=utf8mb4 comment='枚举表';

create table if not exists idata.dev_enum_value (
  id              bigint unsigned not null auto_increment comment '主键', primary key(id),
  del             tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator         varchar(20)     not null comment '创建者',
  create_time     datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor          varchar(20)     not null default '' comment '修改者',
  edit_time       datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  enum_code       varchar(30)     not null comment '枚举标识',
  value_code      varchar(30)     not null comment '枚举值标识', unique uk_enumCode_valueCode(enum_code, value_code),
  enum_value      varchar(100)    not null comment '枚举值字面值',
  enum_attributes varchar(1000)   not null default '[]' comment '枚举值属性',
  parent_code     varchar(30)     comment '上级枚举值标识,null表示最上级'
) engine=innodb default charset=utf8mb4 comment='枚举值表';

create table if not exists idata.dev_foreign_key (
  id                 bigint unsigned not null auto_increment comment '主键', primary key(id),
  del                tinyint         not null default 0 comment '是否删除(1:是,0:否)',
  creator            varchar(20)     not null comment '创建者',
  create_time        datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor             varchar(20)     not null default '' comment '修改者',
  edit_time          datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  table_id           bigint          not null comment '外键所属表ID',
  column_names       varchar(300)    not null comment '外键列名称(支持组合)',
  refer_table_id     bigint          not null comment '外键引用表ID',
  refer_column_names varchar(300)    not null comment '外键引用列名称',
  er_type            varchar(20)     not null comment 'ER联系类型'
) engine=innodb default charset=utf8mb4 comment='表外键表';
