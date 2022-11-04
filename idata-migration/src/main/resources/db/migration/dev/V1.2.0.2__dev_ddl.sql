alter table dev_job_udf
    add source_name varchar(256) null comment 'java类名称（JavaFunction、JavaUDAF）或module（PythonFunction）|沐泽|2019-12-19';

