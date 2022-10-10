
CREATE TABLE `uac_group` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `name` varchar(20) NOT NULL COMMENT '组名称',
      `owner_id` bigint(20) unsigned NOT NULL COMMENT '组负责人id',
      `remark` varchar(200) NOT NULL COMMENT '备注',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户组';

CREATE TABLE `uac_group_user_relation` (
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