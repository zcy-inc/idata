alter table dev_job_history
    add user varchar(32) null comment '启动应用的user';

alter table dev_job_history
    add am_container_logs_url varchar(512) null comment 'application master container url地址';

