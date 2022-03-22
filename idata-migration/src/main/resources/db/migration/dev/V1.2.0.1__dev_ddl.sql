alter table dev_job_execute_config
    add exec_cores int(11) null comment 'executor cores' after exec_worker_mem;

alter table dev_job_execute_config
    add ext_properties varchar(1000) null comment '自定义参数' after exec_engine;

alter table dev_job_content_di
    add src_tables_fashion varchar(10) not null default 'S' comment '来源表方式，S：下拉选择；E：手动编辑' after src_tables;




