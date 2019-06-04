-- 登陆用户表
DROP TABLE IF EXISTS `c_user`;
CREATE TABLE `c_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loginname` VARCHAR(100) NOT NULL COMMENT '登陆账户',
  `loginpw` VARCHAR(100) NOT NULL COMMENT '登陆密码',
  `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(50) NOT NULL COMMENT '邮箱',
  `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `depart_ment` VARCHAR(200) DEFAULT NULL COMMENT '部门名称',
  `role_code` VARCHAR(100) DEFAULT NULL COMMENT '角色id',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态 1正常，0禁止登陆，-1删除',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='登陆用户表';
-- 菜单目录表
DROP TABLE IF EXISTS `c_menu`;
CREATE TABLE `c_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` VARCHAR(10) DEFAULT NULL COMMENT '编号',
  `fathercode` VARCHAR(10) DEFAULT '-1' COMMENT '父类编号',
  `name` VARCHAR(100) DEFAULT NULL COMMENT '目录名称',
  `menu_url` VARCHAR(200) DEFAULT '#' COMMENT '目录路径',
  `menu_image` VARCHAR(200) DEFAULT '#' COMMENT '图片',
  `level` INT(5) DEFAULT '-1' COMMENT '目录等级',
  `sort` INT(5) DEFAULT '0' COMMENT '目录排序',
  `isleaf` int(2) DEFAULT NULL COMMENT '是否叶子 1是 0不是',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `type` INT(5) DEFAULT '1' COMMENT '类型',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='菜单目录表';
-- 角色表
DROP TABLE IF EXISTS `c_role`;
CREATE TABLE `c_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_code` VARCHAR(100) DEFAULT NULL COMMENT '角色编号',
  `role_name` VARCHAR(100) DEFAULT NULL COMMENT '角色名称',
  `sort` INT(5) DEFAULT '0' COMMENT '角色排序',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `type` INT(5) DEFAULT '1' COMMENT '类型',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表';
-- 角色菜单关联表
DROP TABLE IF EXISTS `c_role_menu`;
CREATE TABLE `c_role_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_code` VARCHAR(100) DEFAULT NULL COMMENT '角色id',
  `menu_code` VARCHAR(100) DEFAULT NULL COMMENT '菜单code',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';
-- 数据权限控制表
DROP TABLE IF EXISTS `c_data_authority`;
CREATE TABLE `c_data_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `object_table` char(30) DEFAULT null COMMENT '目标表',
  `object_id` bigint(20) DEFAULT '-1' COMMENT '目标id',  
  `owner_level` int(2) DEFAULT '1' COMMENT '权限控制类型(1:人2:部门3:角色组)',
  `owner_id` bigint(20) DEFAULT '-1' COMMENT '所有者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` bigint(20) DEFAULT '-1' COMMENT '操作人',
  `type` int(5) DEFAULT '1' COMMENT '类型(1：权限所有人 2：权限共享者)',
  `iscancel` int(2) DEFAULT '0' COMMENT '是否作废',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数据权限控制表';
-- 数据源信息表
DROP TABLE IF EXISTS `b_data_source`;
CREATE TABLE `b_data_source` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '数据源名称',
  `data_type` VARCHAR(100) NOT NULL COMMENT '数据源类型',
  `creater` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `iscancel` INT(5) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数据源信息表';
-- 数据源字段信息表
DROP TABLE IF EXISTS `b_data_source_plugin`;
CREATE TABLE `b_data_source_plugin` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` BIGINT(20) NOT NULL COMMENT '数据源id',
  `field_name` VARCHAR(100) DEFAULT NULL COMMENT '页面字段名称',
  `field_code` VARCHAR(100) DEFAULT NULL COMMENT '字段code',
  `field_value` VARCHAR(100) DEFAULT NULL COMMENT '页面传入的实际值',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数据源字段信息表';
-- 数据表组信息表
DROP TABLE IF EXISTS `b_data_table`;
CREATE TABLE `b_data_table` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` BIGINT(20) NOT NULL COMMENT '数据源id',
  `data_type` VARCHAR(20) NOT NULL COMMENT '数据源类型(继承)',
  `bank_name` VARCHAR(500) DEFAULT NULL COMMENT '结构名、库名、前缀名、分组名',
  `table_name` VARCHAR(500) NOT NULL COMMENT '表名(多个，隔开)',
  `creater` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数据表组信息表';
-- 节点信息表
DROP TABLE IF EXISTS `b_nodes`;
CREATE TABLE `b_nodes` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_id` VARCHAR(100) DEFAULT NULL COMMENT '节点id',
  `machine_name` VARCHAR(100) DEFAULT NULL COMMENT '机器名',
  `ip_address` VARCHAR(100) DEFAULT NULL COMMENT 'ip地址',
  `pid_number` VARCHAR(100) DEFAULT NULL COMMENT '进程号',
  `heart_beat_time` VARCHAR(100) DEFAULT '0000-00-00 00:00:00' COMMENT '心跳时间',
  `state` INT(5) DEFAULT '-1' COMMENT '状态 1:在线 -1:离线',
  `node_type` VARCHAR(20) DEFAULT NULL COMMENT '节点类型1预配置2主动注册 先隐藏',
  `task_push_state` VARCHAR(20) DEFAULT NULL COMMENT '节点任务推送状态',
  `creater` BIGINT(20) DEFAULT '-1' COMMENT '创建人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `node_id` (`node_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='节点信息表';
-- 节点所有权控制表
DROP TABLE IF EXISTS `b_nodes_owner`;
CREATE TABLE `b_nodes_owner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_id` bigint(20) DEFAULT '-1' COMMENT '任务id',
  `owner_level` int(2) DEFAULT '1' COMMENT '权限控制类型(1:人2:部门3:角色组)',
  `owner_id` bigint(20) DEFAULT '-1' COMMENT '所有者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` bigint(20) DEFAULT '-1' COMMENT '操作人',
  `type` int(5) DEFAULT '1' COMMENT '类型(1：权限所有人 2：权限共享者)',
  `iscancel` int(2) DEFAULT '0' COMMENT '是否作废',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8 COMMENT='节点所有权控制表';
-- 告警字段字典
DROP TABLE IF EXISTS `d_alarm_plugin`;
CREATE TABLE `d_alarm_plugin` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alert_type` VARCHAR(100) NOT NULL COMMENT '告警策略',
  `field_name` VARCHAR(100) DEFAULT NULL COMMENT '页面字段名称',
  `field_code` VARCHAR(100) DEFAULT NULL COMMENT '字段实际名',
  `field_order` INT(5) DEFAULT NULL COMMENT '页面展示顺序',
  `field_type` VARCHAR(50) DEFAULT 'TEXT' COMMENT '页面字段类型',
  `field_type_key` VARCHAR(50) DEFAULT NULL COMMENT '页面字段类型对应字典',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='告警字段字典';
-- 数据源数据字典
DROP TABLE IF EXISTS `d_data_source_plugin`;
CREATE TABLE `d_data_source_plugin` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_type` VARCHAR(20) NOT NULL COMMENT '数据源类型枚举',
  `field_name` VARCHAR(100) DEFAULT NULL COMMENT '页面字段名称',
  `field_code` VARCHAR(100) DEFAULT NULL COMMENT '字段英文名',
  `field_order` INT(5) DEFAULT NULL COMMENT '页面展示顺序',
  `field_type` VARCHAR(20) DEFAULT 'TEXT' COMMENT '页面字段类型',
  `field_type_key` VARCHAR(50) DEFAULT NULL COMMENT '页面字段类型对应字典',
  `field_validate` VARCHAR(50) DEFAULT NULL COMMENT '页面字段对应正则校验',
  `field_explain` VARCHAR(200) DEFAULT NULL COMMENT '页面字段对应校验说明',
  `state` INT(5) DEFAULT '1' COMMENT '状态1启用 -1禁用',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注字段',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='数据源数据字典';
-- 数据字典表
DROP TABLE IF EXISTS `d_dictionary`;
CREATE TABLE `d_dictionary` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` VARCHAR(20) NOT NULL COMMENT '编码',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `parentcode` CHAR(20) NOT NULL DEFAULT '-1' COMMENT '父类编码',
  `level` INT(2) NOT NULL DEFAULT '1' COMMENT '树形等级',
  `dictype` CHAR(20) NOT NULL COMMENT '字典类别',
  `state` INT(2) NOT NULL DEFAULT '1' COMMENT '数据状态1正常-1作废0警告',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- 公共数据源配置表
DROP TABLE IF EXISTS `b_public_data_source`;
CREATE TABLE `b_public_data_source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` char(35) NOT NULL COMMENT '数据源编码',
  `name` varchar(50) DEFAULT NULL COMMENT '数据源名称',
  `xml_text` text COMMENT '数据源xml文本',
  `json_text` text COMMENT '数据源json文本',
  `declares` varchar(200) DEFAULT NULL COMMENT '数据源说明',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` int(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `type` int(5) NOT NULL DEFAULT '1' COMMENT '类型',
  `ispush` int(5) DEFAULT '0' COMMENT '推送状态(0:新增 -1：回收 1：已推送)',
  `iscancel` int(2) DEFAULT '0' COMMENT '是否作废',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CODE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='公共数据源配置表';

-- 同步任务表
DROP TABLE IF EXISTS `job_tasks`;
CREATE TABLE `job_tasks` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `java_class_name` VARCHAR(200) DEFAULT NULL COMMENT '自定义处理类包路径和类名',
  `java_class_content` VARCHAR(200) DEFAULT NULL COMMENT '自定义处理类文件路径',
  `job_state` VARCHAR(100) NOT NULL DEFAULT 'NEW' COMMENT '任务状态',
  `job_type` int(2) DEFAULT '1' COMMENT '任务类型',
  `source_consume_adt` VARCHAR(100) NOT NULL COMMENT '来源数据-消费插件',
  `source_convert_adt` VARCHAR(100) DEFAULT NULL COMMENT '来源数据-消费转换插件',
  `source_data_source_id` BIGINT(20) NOT NULL COMMENT '来源数据-元数据表数据源id',
  `source_data_tables_id` BIGINT(20) NOT NULL COMMENT '来源数据-元数据表分组id',
  `source_data_tables_name` VARCHAR(800) DEFAULT NULL COMMENT '来源数据-元数据表分组名称',
  `source_data_id` BIGINT(20) NOT NULL COMMENT '来源数据-同步数据源id(kafka\\cancl)',
  `source_data_name` VARCHAR(100) DEFAULT NULL COMMENT '来源数据-同步数据源名称',
  `target_load_adt` VARCHAR(100) NOT NULL COMMENT '目标数据-载入插件',
  `target_data_source_id` BIGINT(20) NOT NULL COMMENT '目标数据-数据源id',
  `target_data_tables_id` BIGINT(20) NOT NULL COMMENT '目标数据-载入数据表分组id',
  `target_data_tables_name` VARCHAR(800) DEFAULT NULL COMMENT '目标数据-载入数据表分组名称',
  `job_xml_text` text DEFAULT NULL COMMENT '任务-xml文本',
  `job_json_text` text DEFAULT NULL COMMENT '任务-json文本',
  `creater` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `type` int(5) DEFAULT '1' COMMENT '类型',
  `iscancel` INT(2) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='同步任务表';
-- 任务所有权控制表
DROP TABLE IF EXISTS `job_tasks_owner`;
CREATE TABLE `job_tasks_owner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id` bigint(20) DEFAULT '-1' COMMENT '任务id',
  `owner_level` int(2) DEFAULT '1' COMMENT '权限控制类型(1:人2:部门3:角色组)',
  `owner_id` bigint(20) DEFAULT '-1' COMMENT '所有者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` bigint(20) DEFAULT '-1' COMMENT '操作人',
  `type` int(5) DEFAULT '1' COMMENT '类型(1：权限所有人 2：权限共享者)',
  `iscancel` int(2) DEFAULT '0' COMMENT '是否作废',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务所有权控制表';
-- 任务节点分发表
DROP TABLE IF EXISTS `job_task_nodes`;
CREATE TABLE `job_task_nodes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_task_id` bigint(20) NOT NULL COMMENT '任务id',
  `node_id` varchar(100) NOT NULL COMMENT '节点id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务节点分发表';
-- 任务数据表对照关系表
DROP TABLE IF EXISTS `job_tasks_table`;
CREATE TABLE `job_tasks_table` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_task_id` BIGINT(20) NOT NULL COMMENT '任务id',
  `ignore_target_case` INT(2) NOT NULL DEFAULT '0' COMMENT '忽略目标端大小写',
  `force_matched` INT(2) NOT NULL DEFAULT '1' COMMENT '强制目标端字段和源端字段一致',
  `direct_map_table` INT(2) NOT NULL DEFAULT '0' COMMENT '直接映射表，不进行表字段映射配置',
  `source_table_name` VARCHAR(200) NOT NULL COMMENT '来源数据-数据表名-记录全名',
  `target_table_name` VARCHAR(200) NOT NULL COMMENT '目标数据-数据表名-记录全名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务数据表对照关系表';
-- 任务数据字段对照关系表
DROP TABLE IF EXISTS `job_tasks_field`;
CREATE TABLE `job_tasks_field` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_task_id` BIGINT(20) NOT NULL COMMENT '任务id',
  `job_tasks_table_id` BIGINT(20) NOT NULL COMMENT '任务表对照关系id',
  `source_table_name` VARCHAR(200) NOT NULL COMMENT '来源数据-数据表名-记录全名',
  `source_table_field` VARCHAR(200) NOT NULL COMMENT '来源数据-数据表字段名称',
  `target_table_name` VARCHAR(200) NOT NULL COMMENT '目标数据-数据表名-记录全名',
  `target_table_field` VARCHAR(200) NOT NULL COMMENT '目标字段-数据表字段名称',
  `sort_order` INT(10) DEFAULT '-1' COMMENT '顺序，暂不启用',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务数据字段对照关系表';
-- 任务与人员对照关系表
DROP TABLE IF EXISTS `job_tasks_user`;
CREATE TABLE `job_tasks_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_task_id` BIGINT(20) NOT NULL COMMENT '任务id',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务与人员对照关系表';

-- 任务泳道进度表
DROP TABLE IF EXISTS `mr_job_tasks_schedule`;
CREATE TABLE `mr_job_tasks_schedule` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id` VARCHAR(100) DEFAULT NULL COMMENT '任务id',
  `job_name` VARCHAR(100) DEFAULT NULL COMMENT '任务名称',
  `swimlane_id` VARCHAR(100) DEFAULT NULL COMMENT '任务泳道',
  `node_id` VARCHAR(200) DEFAULT NULL COMMENT '节点id',
  `node_id_ip` VARCHAR(200) DEFAULT NULL COMMENT '节点id[ip]',
  `schema_table` VARCHAR(200) DEFAULT NULL COMMENT '数据表',
  `register_time` DATETIME DEFAULT NULL COMMENT '注册时间',
  `heart_beat_date` DATETIME DEFAULT NULL COMMENT '最后心跳时间',
  `alarm_number` BIGINT(20) DEFAULT NULL COMMENT '告警次数',
  `last_checked_time` DATETIME DEFAULT NULL COMMENT '最近告警检查时间',
  `insert_success` BIGINT(20) DEFAULT NULL COMMENT '插入次数success',
  `insert_failure` BIGINT(20) DEFAULT NULL COMMENT '插入次数failure',
  `update_success` BIGINT(20) DEFAULT NULL COMMENT '更新次数success',
  `update_failure` BIGINT(20) DEFAULT NULL COMMENT '更新次数failure',
  `delete_success` BIGINT(20) DEFAULT NULL COMMENT '删除次数success',
  `delete_failure` BIGINT(20) DEFAULT NULL COMMENT '删除次数failure',
  `dispose_schedule` VARCHAR(500) DEFAULT NULL COMMENT '处理进度',
  `last_loaded_data_time` DATETIME DEFAULT NULL COMMENT '最近导入数据时间',
  `last_loaded_system_time` DATETIME DEFAULT NULL COMMENT '最近导入系统时间',
  `create_user_id` BIGINT(20) DEFAULT '-1' COMMENT '创建人',
  `update_user_id` BIGINT(20) DEFAULT '-1' COMMENT '修改人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `partition_day` DATE DEFAULT NULL COMMENT '预留时间分区字段',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务泳道进度表';
-- 任务泳道表名实时监控表
DROP TABLE IF EXISTS `mr_job_tasks_monitor`;
CREATE TABLE `mr_job_tasks_monitor` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id` VARCHAR(100) DEFAULT NULL COMMENT '任务id',
  `node_id` VARCHAR(100) DEFAULT NULL COMMENT '节点id',
  `node_id_ip` VARCHAR(200) DEFAULT NULL COMMENT '节点id[ip]',
  `swimlane_id` VARCHAR(100) DEFAULT NULL COMMENT '任务泳道',
  `schema_table` varchar(200) DEFAULT NULL COMMENT '表全名',
  `monitor_date` DATETIME DEFAULT NULL COMMENT '实时监控时间',
  `monitor_ymd` DATE DEFAULT NULL COMMENT '实时监控年月日',
  `monitor_hour` INT(5) DEFAULT NULL COMMENT '实时监控小时(24h)',
  `monitor_minute` INT(5) DEFAULT NULL COMMENT '实时监控分',
  `monitor_second` INT(5) DEFAULT NULL COMMENT '实时监控秒',
  `insert_succes` BIGINT(20) DEFAULT NULL COMMENT '插入成功',
  `insert_failure` BIGINT(20) DEFAULT NULL COMMENT '插入失败',
  `update_succes` BIGINT(20) DEFAULT NULL COMMENT '更新成功',
  `update_failure` BIGINT(20) DEFAULT NULL COMMENT '更新失败',
  `delete_succes` BIGINT(20) DEFAULT NULL COMMENT '删除成功',
  `delete_failure` BIGINT(20) DEFAULT NULL COMMENT '删除失败',
  `alarm_number` BIGINT(20) DEFAULT NULL COMMENT '告警次数',
  `partition_day` DATE DEFAULT NULL COMMENT '预留时间分区字段',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务泳道表名实时监控表';
-- 日志信息表
DROP TABLE IF EXISTS `mr_log_monitor`;
CREATE TABLE `mr_log_monitor` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_id` VARCHAR(100) DEFAULT NULL COMMENT '节点id',
  `job_id` VARCHAR(100) DEFAULT NULL COMMENT '任务id',
  `job_name` VARCHAR(100) DEFAULT NULL COMMENT '任务标题',
  `ip_adress` VARCHAR(20) DEFAULT NULL COMMENT 'IP地址',
  `log_date` DATETIME DEFAULT NULL COMMENT '日志时间',
  `log_title` VARCHAR(200) DEFAULT NULL COMMENT '日志标题',
  `log_content` text DEFAULT NULL COMMENT '日志内容',
  `create_user_id` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `update_user_id` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '修改人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `partition_day` DATE DEFAULT NULL COMMENT '预留分区字段',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='日志信息表';
-- 节点任务监控表
DROP TABLE IF EXISTS `mr_nodes_schedule`;
CREATE TABLE `mr_nodes_schedule` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_id` VARCHAR(100) DEFAULT NULL COMMENT '节点id',
  `computer_name` VARCHAR(100) DEFAULT NULL COMMENT '机器名',
  `ip_address` VARCHAR(100) DEFAULT NULL COMMENT 'ip_address',
  `heart_beat_date` DATETIME DEFAULT NULL COMMENT '心跳时间',
  `process_number` VARCHAR(100) DEFAULT NULL COMMENT '进程号',
  `job_id_json` VARCHAR(1000) DEFAULT NULL COMMENT '任务json信息',
  `job_name_json` VARCHAR(2000) DEFAULT NULL COMMENT '任务json-name信息',
  `health_level` VARCHAR(100) DEFAULT NULL COMMENT '节点健康级别',
  `health_level_desc` text DEFAULT NULL COMMENT '节点健康级别描述',
  `create_user_id` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `update_user_id` BIGINT(20) NOT NULL DEFAULT '-1' COMMENT '修改人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` INT(5) NOT NULL DEFAULT '1' COMMENT '状态',
  `iscancel` INT(5) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `partition_day` DATE DEFAULT NULL COMMENT '预留时间分区字段',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='节点任务监控表';
-- 节点任务实时监控表
DROP TABLE IF EXISTS `mr_nodes_monitor`;
CREATE TABLE `mr_nodes_monitor` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_id` VARCHAR(100) DEFAULT NULL COMMENT '节点id',
  `monitor_date` DATETIME DEFAULT NULL COMMENT '实时监控时间',
  `monitor_ymd` DATE DEFAULT NULL COMMENT '实时监控年月日',
  `monitor_hour` INT(2) DEFAULT NULL COMMENT '实时监控小时',
  `monitor_minute` INT(5) DEFAULT NULL COMMENT '实时监控分',
  `monitor_second` INT(10) DEFAULT NULL COMMENT '实时监控秒',
  `monitor_tps` INT(20) DEFAULT NULL COMMENT '并发数',
  `monitor_alarm` INT(20) DEFAULT NULL COMMENT '告警次数',
  `partition_day` DATE DEFAULT NULL COMMENT '预留时间分区字段',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='节点任务实时监控表';

-- 告警配置表
DROP TABLE IF EXISTS `s_alarm`;
CREATE TABLE `s_alarm` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alarm_type` VARCHAR(100) DEFAULT NULL COMMENT '告警方式',
  `create_user_id` BIGINT(20) DEFAULT '-1' COMMENT '创建人',
  `update_user_id` BIGINT(20) DEFAULT '-1' COMMENT '修改人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='告警配置表';
-- 告警配置策略内容表
DROP TABLE IF EXISTS `s_alarm_plugin`;
CREATE TABLE `s_alarm_plugin` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alarm_id` BIGINT(20) DEFAULT NULL COMMENT '告警策略id',
  `alarm_type` VARCHAR(100) DEFAULT NULL COMMENT '告警方式',
  `plugin_code` VARCHAR(100) DEFAULT NULL COMMENT '字段code',
  `plugin_name` VARCHAR(100) DEFAULT NULL COMMENT '字段名称',
  `plugin_value` VARCHAR(200) DEFAULT NULL COMMENT '字段内容',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='告警配置策略内容表';
-- 告警用户关联表
DROP TABLE IF EXISTS `s_alarm_user`;
CREATE TABLE `s_alarm_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alarm_id` BIGINT(20) NOT NULL COMMENT '告警信息表id',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='告警用户关联表';
-- 日志级别表
DROP TABLE IF EXISTS `s_log_grade`;
CREATE TABLE `s_log_grade` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_level` VARCHAR(20) DEFAULT NULL COMMENT '日志级别',
  `create_user_id` BIGINT(20) DEFAULT '-1' COMMENT '创建人',
  `update_user_id` BIGINT(20) DEFAULT '-1' COMMENT '修改人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` INT(5) DEFAULT '1' COMMENT '状态',
  `iscancel` INT(2) DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='日志级别表';
-- 任务权限操作类型表
DROP TABLE IF EXISTS `d_control_type_plugin`;
CREATE TABLE `d_control_type_plugin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alert_type` varchar(100) DEFAULT NULL COMMENT '告警策略',
  `field_name` varchar(100) DEFAULT NULL COMMENT '页面字段名称',
  `field_code` varchar(100) DEFAULT NULL COMMENT '字段实际名',
  `field_order` int(10) DEFAULT NULL COMMENT '页面展示顺序',
  `field_type` varchar(100) DEFAULT NULL COMMENT '页面字段类型',
  `field_type_key` varchar(100) DEFAULT NULL COMMENT '页面字段类型对应字典',
  `state` int(10) DEFAULT NULL COMMENT '状态',
  `iscancel` int(10) DEFAULT NULL COMMENT '是否删除',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务权限操作类型表';
-- 权限控制操作类型表
DROP TABLE IF EXISTS `r_owner_control`;
CREATE TABLE `r_owner_control` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_type` int(10) DEFAULT NULL COMMENT '所有者权限类型(1:所有者 2.共享者 0:管理员).',
  `control_type_id` bigint(20) DEFAULT NULL COMMENT '操作类型id.',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限控制操作类型表';