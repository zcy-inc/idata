alter table dev_job_history
    add state varchar(32) null comment '作业当前状态' after duration;