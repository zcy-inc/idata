alter table dev_job_udf
    add global_fun tinyint default 1 null comment '是否是全局类型函数，1：是，0：否';

