alter table dev_job_content_di modify src_read_mode varchar(20) null comment '数据来源-读取模式，all：全量，incremental：增量';

alter table dev_job_content_di modify dest_table varchar(100) null comment '数据去向-目标表';

