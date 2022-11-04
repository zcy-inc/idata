
CREATE TABLE if not exists `uac_group` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `name` varchar(20) NOT NULL COMMENT '组名称',
      `owner_id` bigint(20) unsigned NOT NULL COMMENT '组负责人id',
      `remark` varchar(200) NULL COMMENT '备注',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户组';

CREATE TABLE if not exists `uac_group_user_relation` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `group_id` bigint(20) unsigned NOT NULL COMMENT '组id',
      `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
      PRIMARY KEY (`id`),
      KEY `idx_group_id` (`group_id`),
      KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户组-用户关联表';

CREATE TABLE if not exists `uac_auth_entry` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `subject_id` varchar(30) NOT NULL COMMENT '授权主体唯一标识',
      `subject_type` varchar(20) NOT NULL COMMENT '授权主体类型，users：用户，groups：用户组，apps：应用',
      `remark` varchar(200) NULL COMMENT '备注',
      PRIMARY KEY (`id`),
      KEY `idx_subject` (`subject_id`, `subject_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='授权记录表';

CREATE TABLE if not exists `uac_auth_policy` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `auth_record_id` bigint(20) unsigned NOT NULL COMMENT '授权记录id',
      `effect` varchar(20) NOT NULL COMMENT '授权类型：allow：允许，deny：拒绝',
      `actions` varchar(100) NOT NULL COMMENT '授权类型：read：读，write：写',
      `resource_type` varchar(20) NOT NULL COMMENT '资源类型：table：表',
      `remark` varchar(200) NULL COMMENT '备注',
      PRIMARY KEY (`id`),
      KEY `idx_auth_record` (`auth_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='授权策略表';

CREATE TABLE if not exists `uac_auth_resource` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `auth_record_id` bigint(20) unsigned NOT NULL COMMENT '授权记录id',
      `policy_record_id` bigint(20) unsigned NOT NULL COMMENT '授权策略记录id',
      `resource_type` varchar(20) NOT NULL COMMENT '资源类型：table：表',
      `resources` longtext NOT NULL COMMENT '资源',
      PRIMARY KEY (`id`),
      KEY `idx_auth_policy` (`auth_record_id`, `policy_record_id`),
      KEY `idx_policy` (`policy_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='授权资源表';