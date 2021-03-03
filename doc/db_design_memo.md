项目数据库设计的一些约定
====

项目中建表除了满足阿里开发规范之外，还须要满足以下约定。两者有不一致的地方，以本文档为准。   

* 须要使用表前缀。格式：所属模块缩写名_表名    
保留表前缀:     
sys\_：系统表前缀。系统会初始化系统表中的数据，表结构和内容可能会随着系统版本变化。项目代码中一般只读系统表，特定需求可以修改除主键外的字段值，一般不做物理删除。   
* 一般基于主键和逻辑外键设计实体关系。重要实体需要有可读唯一键字段。    
* 表名不需要复数名词命名，字段名按需使用复数名词命名。    
* 关于通用字段    
每个表都须要有id，create\_time，edit\_time三个通用字段。所有通用字段须要按照表格中的字段名、字段类型约束和表格中字段顺序出现在所有表字段的最前面。

|通用字段名|是否必须|字段类型约束|使用场景|
|---|---|---|---|
|id|是<div style="width:100px"/>|id bigint unsigned not null auto\_increment comment '主键', primary key(id)|自增主键，必须有|
|del|否|del smallint not null default 0 comment '是否删除(1:是,其他:否)'|用户在系统生成的数据一般需要逻辑删除|
|creator|否|creator varchar(20) not null comment '创建者'|用户在系统生成的数据一般需要记录创建者名称（这里不是用户id，忽略修改用户名的情况）|
|create\_time|是|create\_time datetime(3) not null default current\_timestamp(3) comment '创建时间'|数据行创建时间，必须有|
|editor|否|editor varchar(20) not null default '' comment '修改者'|用户在系统生成的数据一般需要记录修改者（这里不是用户id，忽略修改用户名的情况）|
|edit\_time|是|edit\_time datetime(3) not null default current\_timestamp(3) on update current\_timestamp(3) comment '修改时间'|数据行修改时间，必须有|

* DDL模板

````
create table if not exists db_name.ddl_example (
  id          bigint unsigned not null auto_increment comment '主键', primary key(id),
  del         smallint        not null default 0 comment '是否删除(1:是,其他:否)',
  creator     varchar(20)     not null comment '创建者',
  create_time datetime(3)     not null default current_timestamp(3) comment '创建时间',
  editor      varchar(20)     not null default '' comment '修改者',
  edit_time   datetime(3)     not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间'
) engine=innodb default charset=utf8mb4 comment='DDL模板示例表';
````
