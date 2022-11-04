CREATE TABLE `table_sibship` (
          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
          `job_id` bigint(20) NOT NULL COMMENT '作业id',
          `db` varchar(64) NOT NULL COMMENT '输出库',
          `table_name` varchar(128) NOT NULL COMMENT '输出表',
          `source_db` varchar(64) NOT NULL COMMENT '输入库',
          `source_table_name` varchar(128) NOT NULL COMMENT '输入表',
          `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是,0:否)',
          `creator` varchar(20) NOT NULL COMMENT '创建人',
          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          `editor` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
          `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='表血缘关系';