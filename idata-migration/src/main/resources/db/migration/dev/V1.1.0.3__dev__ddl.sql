alter table dev_job_content_di
    modify merge_sql text null comment 'DI作业增量模式的mergeSql';

alter table idata.dev_job_content_sql modify source_sql longtext comment '数据来源SQL';

alter table idata.dev_job_output modify job_target_table_pk varchar(200) comment '数据来源表主键(写入模式为upsert时必填)';
