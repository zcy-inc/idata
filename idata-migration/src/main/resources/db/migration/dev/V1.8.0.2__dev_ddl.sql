alter table dev_job_execute_config
    add is_open_merge_file int(5) default 0 null comment '开启小文件合并 0:否，1:是';
