alter table dev_job_execute_config
    add dest_file_type varchar(50) null comment '写入文件类型 Spark SQL类型：ORC、Parquet';

CREATE TABLE `map_user_favourite` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
      `creator` varchar(20) NOT NULL COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改者',
      `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `entity_code` varchar(50) NOT NULL COMMENT '实体记录唯一标识',
      `entity_source` varchar(30) NOT NULL COMMENT '实体记录数据源：数仓表（table） or 数据指标（indicator）',
      `user_id` bigint(20) unsigned NOT NULL COMMENT '用户编号，0表示全局所有用户的统计数据',
      PRIMARY KEY (`id`),
      KEY `idx_user_entity` (`user_id`,`entity_source`,`entity_code`,`del`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='数据地图用户-收藏表';