alter table dev_job_content_di
    add src_table_pt varchar(32) null comment '回流数据源（hive）的分区目录信息 例如pt=20220801';

alter table dev_job_content_di
    add dest_table_pt varchar(32) null comment '目标数据源（doris）的分区别名，例如p20220801';

