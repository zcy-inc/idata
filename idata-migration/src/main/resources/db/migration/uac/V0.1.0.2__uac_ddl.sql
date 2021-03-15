create table if not exists idata.uac_user (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  username    varchar(20)     not null comment '用户名', unique uk_username(username),
  sys_admin   smallint        not null default 0 comment '是否系统管理员(0:否,1:是,2:其他系统管理员)',
  auth_type   varchar(20)     not null comment '认证方式',
  password    varchar(50)     comment '注册密码',
  nickname    varchar(20)     not null comment '昵称',
  employee_id varchar(20)     not null default '' comment '工号',
  department  varchar(50)     not null default '' comment '部门',
  real_name   varchar(20)     not null default '' comment '真实姓名',
  avatar      varchar(200)    not null default '' comment '头像信息',
  email       varchar(100)    not null default '' comment '电子邮箱',
  mobile      varchar(20)     not null default '' comment '手机号'
) engine=innodb auto_increment=11 default charset=utf8mb4 comment='用户表';

create table if not exists idata.uac_user_token (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  user_id     bigint unsigned not null comment '用户ID',
  token       varchar(50)     not null comment '用户令牌'
) engine=innodb default charset=utf8mb4 comment='用户令牌表';

create table if not exists idata.uac_role (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  role_code   varchar(30)     not null comment '角色编码', unique uk_role_code(role_code),
  role_name   varchar(20)     not null comment '角色名称'
) engine=innodb auto_increment=1001 default charset=utf8mb4 comment='角色表';

create table if not exists idata.uac_user_role (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  user_id     bigint unsigned not null comment '用户ID',
  role_code   varchar(30)     not null comment '角色编码'
) engine=innodb default charset=utf8mb4 comment='用户角色关系表';

create table if not exists idata.uac_access (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  access_code varchar(30)     not null comment '权限编码', unique uk_access_code(access_code),
  access_type varchar(30)     not null comment '权限类型',
  access_key  varchar(40)     comment '权限对应的ID或编码', unique uk_accessType_accessKey(access_type, access_key)
) engine=innodb auto_increment=100001 default charset=utf8mb4 comment='权限表';

create table if not exists idata.uac_role_access (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
  role_code   varchar(30)     not null comment '角色编码',
  access_code varchar(30)     not null comment '权限编码'
) engine=innodb default charset=utf8mb4 comment='角色权限关系表';
