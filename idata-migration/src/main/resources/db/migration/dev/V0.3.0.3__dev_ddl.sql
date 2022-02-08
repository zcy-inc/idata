alter table idata.dev_label_define modify special_attribute varchar(1000) not null default '{}'
comment '特定标签属性，根据标签的标签字段变化';