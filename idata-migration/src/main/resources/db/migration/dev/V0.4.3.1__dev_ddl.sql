alter table dev_table_info
    add hive_table_name varchar(200) null comment '同步至hive的表' after table_name;


alter table dev_label
    add hidden tinyint default 0 null comment '是否隐藏不展示';


