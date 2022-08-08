create table if not exists idata.sys_resource (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是,0:否)',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `edit_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `resource_code` varchar(30) NOT NULL COMMENT '资源编码',
  `resource_name` varchar(20) NOT NULL COMMENT '资源名称',
  `resource_type` varchar(30) NOT NULL COMMENT '资源类型',
  `parent_code` varchar(30) DEFAULT NULL COMMENT '上级资源编码',
  `resource_url_path` varchar(50) NOT NULL DEFAULT '' COMMENT '资源请求路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_code` (`resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';