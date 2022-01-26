alter table dev_table_info
    add hive_table_name varchar(200) null comment '同步至hive的表' after table_name;


alter table dev_label
    add hidden tinyint default 0 null comment '是否隐藏不展示';


alter table dev_label
    add column_id bigint null after table_id;

alter table dev_label drop index uk_labelCode_tableId_columnName;
alter table dev_label add unique key uk_labelCode_tableId_columnId(label_code, table_id, column_id);
