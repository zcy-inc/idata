alter table dev_job_content_di
    add src_topic varchar(128) null comment '来源为kafka数据类型的topic' after src_data_source_id;

alter table dev_job_content_di modify src_columns text not null comment '数据来源-字段信息，json格式' after src_read_mode;

alter table dev_job_content_di
    add src_query text null comment '补充迁移旧版Idata的di_query字段。用于复杂sql或函数sql，解决除了单表的简单列映射之外的场景。' after src_read_filter;

alter table dev_job_content_di
    add dest_topic varchar(128) null comment '目标为kafka的数据类的topic' after dest_data_source_id;

alter table dev_job_content_di
    add script_select_columns varchar(2048) null comment '脚本模式，作用同可视化src_columns';

alter table dev_job_content_di
    add script_key_columns varchar(1024) null comment '脚本模式，同可视化src_columns中primaryKey字段';

alter table dev_job_content_di
    add script_merge_sql text null comment '脚本模式，同可视化merge_sql';

alter table dev_job_content_di
    add script_query text null comment '脚本模式，同可视化src_query';

alter table dev_job_content_di
    add config_mode tinyint(2) null comment '配置模式，1：可视化模式，2：脚本模式';

alter table dev_job_content_di
    add dest_properties varchar(512) null comment '目标库中间件的内置属性' after dest_columns;

alter table dev_job_content_di
    add dest_bulk_num bigint null comment '单次批量写入数据条数' after dest_properties;

alter table dev_job_content_di
    add dest_sharding_num int null comment '多分片写入（并行度）' after dest_properties;

alter table dev_job_content_di
    add script_merge_sql_param varchar(256) null comment 'merge_sql的参数，json格式' after script_key_columns;




