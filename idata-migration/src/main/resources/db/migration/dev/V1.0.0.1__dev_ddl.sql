alter table dev_job_content_di
    add src_sharding_num int null comment '数据来源-分片数量（并行度）' after src_read_shard_key;

alter table dev_job_execute_config drop column exec_max_parallelism;

alter table dev_job_execute_config
    add extraction_type varchar(30) null comment '抽数配置';
