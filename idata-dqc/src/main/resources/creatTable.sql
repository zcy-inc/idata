CREATE TABLE `dqc_monitor_template`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(200) NOT NULL DEFAULT '' COMMENT '规则名称',
    `type`        varchar(50)  NOT NULL DEFAULT '' COMMENT '模板类型，system、custom',
    `monitor_obj` varchar(50)           DEFAULT NULL COMMENT '监控对象，table,filed',
    `content`     varchar(2048)         DEFAULT '' COMMENT 'sql',
    `category`    varchar(50)           DEFAULT NULL COMMENT '维度：及时timely，准确性accuracy，完整性integrity',
    `output_type` int(5) DEFAULT '1' COMMENT '输出类型，1数值，2文本',
    `status`      int(5) NOT NULL DEFAULT '1' COMMENT '状态:0停用，1启用',
    `del`         tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
    `creator`     varchar(20)  NOT NULL COMMENT '创建者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      varchar(20)  NOT NULL DEFAULT '' COMMENT '修改者',
    `edit_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COMMENT='数据质量模板表';

CREATE TABLE `dqc_monitor_table`
(
    `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `baseline_id`        bigint(20) NOT NULL DEFAULT '-1' COMMENT '基线id,没有基线默认-1',
    `table_name`         varchar(200) NOT NULL DEFAULT '' COMMENT '表名',
    `partition_expr`     varchar(200) NOT NULL DEFAULT '' COMMENT '分区信息',
    `latest_alarm_level` int(5) DEFAULT NULL COMMENT '告警等级，1一般，2重要，3严重',
    `access_time`        varchar(50)  NOT NULL DEFAULT '' COMMENT '最新规则执行时间',
    `del`                tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
    `creator`            varchar(20)  NOT NULL COMMENT '创建者',
    `create_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`             varchar(20)  NOT NULL DEFAULT '' COMMENT '修改者',
    `edit_time`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY                  `idx_table_name` (`table_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='数据质量被监控的表';

CREATE TABLE `dqc_monitor_rule`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `baseline_id`     bigint(20) NOT NULL COMMENT '基线id,没有基线默认-1',
    `table_name`      varchar(200) NOT NULL DEFAULT '' COMMENT '表名名称',
    `field_name`      varchar(200)          DEFAULT '' COMMENT '字段名称',
    `name`            varchar(200) NOT NULL COMMENT '规则名称',
    `rule_type`       varchar(50)  NOT NULL DEFAULT '' COMMENT '规则类型，system内置规则，template模板规则，custom自定义规则',
    `template_id`     bigint(20) NOT NULL DEFAULT '-1' COMMENT '模板规则id',
    `monitor_obj`     varchar(50)  NOT NULL DEFAULT 'table' COMMENT '监控对象，table,filed',
    `alarm_level`     int(5) NOT NULL COMMENT '告警等级，1一般，2重要，3严重',
    `alarm_receivers` varchar(2048)         DEFAULT '' COMMENT '告警接收人，逗号分隔',
    `check_type`      varchar(50)           DEFAULT '1' COMMENT '校验类型:abs绝对值，pre_period上周期',
    `compare_type`    varchar(50)           DEFAULT NULL COMMENT '比较方式：>,>=,<,<=,<>,=,range,up,down',
    `content`         varchar(2048)         DEFAULT '' COMMENT '内置规则为对应的值，自定义规则为sql',
    `output_type`     int(5) DEFAULT NULL COMMENT '输出类型，1数值，2文本',
    `fix_value`       decimal(10, 3)        DEFAULT NULL COMMENT '固定值',
    `range_start`     decimal(10, 3)        DEFAULT NULL COMMENT '开始值',
    `range_end`       decimal(10, 3)        DEFAULT NULL COMMENT '结束值',
    `status`          int(5) NOT NULL DEFAULT '1' COMMENT '状态:0关闭，1开始',
    `access_time`     varchar(100)          DEFAULT '' COMMENT '访问时间',
    `version`         varchar(100)          DEFAULT '' COMMENT '版本',
    `del`             tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
    `creator`         varchar(20)  NOT NULL COMMENT '创建者',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`          varchar(20)  NOT NULL DEFAULT '' COMMENT '修改者',
    `edit_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY               `idx_table_name` (`table_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='数据质量监控规则表';


CREATE TABLE `dqc_monitor_baseline`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(200) NOT NULL DEFAULT '' COMMENT '基线名称',
    `status`      int(5) NOT NULL DEFAULT '1' COMMENT '状态:0关闭，1开始',
    `del`         tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
    `creator`     varchar(20)  NOT NULL COMMENT '创建者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      varchar(20)  NOT NULL DEFAULT '' COMMENT '修改者',
    `edit_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量基线表';


CREATE TABLE `dqc_monitor_history`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `table_name`      varchar(200) NOT NULL DEFAULT '' COMMENT '表名',
    `partition`       varchar(200) NOT NULL DEFAULT '' COMMENT '分区',
    `rule_id`         bigint(20) NOT NULL COMMENT '监控规则id',
    `baseline_id`     bigint(20) DEFAULT NULL COMMENT '基线id',
    `template_id`     bigint(20) DEFAULT NULL COMMENT '模板id',
    `rule_name`       varchar(200) NOT NULL DEFAULT '' COMMENT '规则名称',
    `rule_type`       varchar(50)  NOT NULL DEFAULT '' COMMENT '规则类型，system内置规则，template模板规则，custom自定义规则',
    `monitor_obj`     varchar(50)           DEFAULT NULL COMMENT '监控对象，table,filed',
    `alarm_level`     int(5) DEFAULT NULL,
    `alarm_receivers` varchar(2048)         DEFAULT '' COMMENT '告警接收人，逗号分隔',
    `compare_type`    varchar(50)           DEFAULT NULL COMMENT '比较方式：>,>=,<,<=,<>,=,range,up,down',
    `check_type`      varchar(50)           DEFAULT NULL COMMENT '校验类型:abs绝对值，pre_period上周期',
    `content`         varchar(50)           DEFAULT NULL COMMENT '日期值',
    `fix_value`       decimal(10, 3)        DEFAULT NULL COMMENT '固定值',
    `range_start`     decimal(10, 3)        DEFAULT NULL COMMENT '开始值',
    `range_end`       decimal(10, 3)        DEFAULT NULL COMMENT '结束值',
    `sql`             varchar(2048)         DEFAULT '' COMMENT '查询sql',
    `alarm`           int(5) NOT NULL DEFAULT '0' COMMENT '是否告警0不告警，1告警',
    `data_value`      bigint(20) DEFAULT NULL COMMENT '查询结果',
    `rule_value`      decimal(20, 3)        DEFAULT NULL COMMENT '规则统计结果',
    `version`         varchar(50)  NOT NULL DEFAULT '' COMMENT '规则版本号（规则表的修改日期）',
    `creator`         varchar(20)           DEFAULT '' COMMENT '创建者',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`          varchar(20)  NOT NULL DEFAULT '' COMMENT '修改者',
    `edit_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量监控历史记录表';

INSERT INTO `dqc_monitor_template` (`id`, `name`, `type`, `monitor_obj`, `content`, `category`, `output_type`, `status`, `del`, `creator`, `create_time`, `editor`, `edit_time`)
VALUES
    (1, '表行数', 'system', 'table', 'table_row', 'integrity', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24'),
    (2, '表产出时间', 'system', 'table', 'table_output_time', 'timely', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24'),
    (3, '值唯一', 'system', 'field', 'field_unique', 'accuracy', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-14 16:56:07'),
    (4, '字段枚举内容', 'system', 'field', 'field_enum_content', 'integrity', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24'),
    (5, '字段枚举数量', 'system', 'field', 'field_enum_count', 'integrity', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24'),
    (6, '字段数值范围', 'system', 'field', 'field_data_range', 'accuracy', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24'),
    (7, '字段值不为空', 'system', 'field', 'field_not_null', 'accuracy', 1, 1, 0, '系统管理员', '2022-06-30 20:19:17', '系统管理员', '2022-07-04 10:28:24');
