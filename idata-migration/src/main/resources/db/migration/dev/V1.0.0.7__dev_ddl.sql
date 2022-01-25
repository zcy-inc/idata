alter table dev_job_content_di
    add merge_sql varchar(4096) null comment 'DI作业增量模式的mergeSql';

