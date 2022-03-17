alter table dev_job_content_di
    modify merge_sql text null comment 'DI作业增量模式的mergeSql';

alter table idata.dev_job_content_sql modify source_sql longtext comment '数据来源SQL';

