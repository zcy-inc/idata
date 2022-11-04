
create table if not exists dev_metric_approval_record
(
    id                     bigint(20) unsigned primary key auto_increment comment '主键',
    del                    tinyint(4)          not null default 0 comment '是否删除，0否，1是',
    creator                varchar(20)         not null comment '创建/提交者',
    create_time            datetime            not null default current_timestamp comment '创建/提交时间',
    editor                 varchar(20)         not null default '' comment '修改者',
    edit_time              datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
    metric_id              varchar(50)         not null comment '数据指标id',
    metric_name            varchar(100)        not null comment '数据指标名称',
    metric_tag             varchar(100)        not null comment '数据指标类型',
    biz_domain             varchar(20)         not null comment '数据域',
    biz_process            varchar(50)         not null comment '业务过程',
    approval_status        int(11)             not null comment '审批状态，1：待审批，2：已审批，3：已撤回，4：已驳回',
    submit_remark          varchar(200)        null comment '提交备注',
    approve_operator       varchar(20)         null comment '审批人',
    approve_time           datetime            null comment '审批时间',
    approve_remark         varchar(200)        null comment '审批备注',
    key idx_metric_id_approval_status(metric_id, approval_status)
) engine = innodb
  auto_increment = 1000
  default charset = utf8mb4 comment '数据开发-指标审批记录';