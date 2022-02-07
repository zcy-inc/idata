alter table dev_job_content_di
    add direct varchar(5) null comment '数据流向，IN-数据集成、OUT-数据回流' after version;

alter table dev_job_content_di
    add src_topic varchar(128) null comment '来源为kafka数据类型的topic' after src_data_source_id;

alter table dev_job_content_di modify src_columns text not null comment '数据来源-字段信息，json格式' after src_read_mode;

alter table dev_job_content_di
    add src_query varchar(2048) null comment '补充迁移旧版Idata的di_query字段。用于复杂sql或函数sql，解决除了单表的简单列映射之外的场景。' after src_read_filter;

alter table dev_job_content_di
    add dest_topic varchar(128) null comment '目标为kafka的数据类的topic' after dest_data_source_id;

alter table dev_job_content_di
    add script_select_columns varchar(1024) null comment '脚本模式，作用同可视化src_columns';

alter table dev_job_content_di
    add script_key_columns varchar(512) null comment '脚本模式，同可视化src_columns中primaryKey字段';

alter table dev_job_content_di
    add script_merge_sql varchar(2048) null comment '脚本模式，同可视化merge_sql';

alter table dev_job_content_di
    add script_query varchar(2048) null comment '脚本模式，同可视化src_query';

alter table dev_job_content_di
    add config_mode tinyint(2) null comment '配置模式，1：可视化模式，2：脚本模式';

