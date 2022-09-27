alter table dev_job_info
    add activity_end datetime null comment '作业有效截止时间';

alter table dev_job_execute_config
    add dest_file_type varchar(50) null comment '写入文件类型 Spark SQL类型：ORC、Parquet';

