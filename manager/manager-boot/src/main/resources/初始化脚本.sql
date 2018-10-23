use ds_data;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

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
  `source_convert_adt` VARCHAR(100) NOT NULL COMMENT '来源数据-消费转换插件',
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
  `iscancel` INT(2) NOT NULL DEFAULT '0' COMMENT '是否作废',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='同步任务表';
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
-- ogg表数据信息
DROP TABLE IF EXISTS `ogg_tables`;
CREATE TABLE `ogg_tables` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip_address` varchar(100) DEFAULT NULL COMMENT 'ip地址',
  `ip_name` varchar(100) DEFAULT NULL COMMENT 'ip名称',
  `table_name` varchar(200) DEFAULT NULL COMMENT '表名汉字',
  `table_value` varchar(200) DEFAULT NULL COMMENT '表名字段',
  `table_marker` varchar(200) DEFAULT NULL COMMENT '表名标识',
  `heart_beat_time` varchar(100) DEFAULT '0000-00-00 00:00:00' COMMENT '心跳时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `iscancel` int(2) DEFAULT '0' COMMENT '是否作废',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `related_task_info` varchar(500) DEFAULT NULL COMMENT '关联任务信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='ogg表数据信息'; 

-- 初始用户
INSERT  INTO `c_user`(`id`,`loginname`,`loginpw`,`nickname`,`email`,`mobile`,`depart_ment`,`role_code`,`state`,`remark`) VALUES 
(1,'admin','21232f297a57a5a743894a0e4a801fc3','超级管理员','admin@qq.com','13800138000','CTO','A0001',1,'');
-- 初始化角色
INSERT  INTO `c_role`(`id`,`role_code`,`role_name`,`sort`,`iscancel`,`type`,`state`,`remark`) VALUES 
(1,'A0001','超级管理员',0,0,1,1,'超级管理员'),
(2,'A0002','普通管理员',1,0,1,1,'普通管理员'),
(3,'B0001','监控观察者',2,0,1,1,'观察者'),
(4,'C0001','访客',4,0,1,1,'访客');
-- 初始化菜单
INSERT INTO `c_menu` VALUES (1, 'F001', '-1', '首页', '/', 'home', 1, 1, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (2, 'J001', '-1', '同步管理', '#', 'sync', 1, 2, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (3, 'J001001', 'J001', '任务管理', '/synchTask', 'fa-tasks', 2, 1, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (4, 'N001', '-1', '集群管理', '#', 'fa-cubes', 1, 3, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (5, 'N001001', 'N001', '同步节点管理', '/nodeCluster', 'scan', 2, 1, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (6, 'B001', '-1', '配置管理', '#', 'fa-gears', 1, 4, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (7, 'B001001', 'B001', '数据源配置', '/dataSource', 'fa-sliders', 2, 1, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (8, 'B001002', 'B001', '数据表配置', '/dataTable', 'api', 2, 2, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (9, 'B001003', 'B001', '全局配置', '/globalConfig', 'fa-database', 2, 3, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (10, 'M001', '-1', '监控管理', '#', 'line-chart', 1, 5, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (11, 'M001001', 'M001', '运行日志', '/logMonitor', 'calendar', 2, 1, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (12, 'M001002', 'M001', '任务监控', '/taskMonitor', 'fa-dashboard', 2, 2, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (13, 'M001003', 'M001', '节点监控', '/nodeMonitor', 'dot-chart', 2, 3, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (14, 'C001', '-1', '系统设置', '#', 'setting', 1, 10, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (15, 'C001001', 'C001', '用户管理', '/user', 'fa-user-md', 2, 1, 1, 0, 1, 1, NULL);
    -- ogg管理模块新增
INSERT INTO `c_menu` VALUES (18, 'O001', '-1', 'OGG管理', '#', 'fa-legal', 1, 6, 0, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (19, 'O001001', 'O001', '表名查询', '/oggTables', 'fa-table', 2, 1, 1, 0, 1, 1, NULL);
-- 初始化菜单权限
INSERT INTO `c_role_menu` VALUES (1, 'A0002', 'F001');
INSERT INTO `c_role_menu` VALUES (2, 'A0002', 'J001');
INSERT INTO `c_role_menu` VALUES (3, 'A0002', 'J001001');
INSERT INTO `c_role_menu` VALUES (4, 'A0002', 'N001');
INSERT INTO `c_role_menu` VALUES (5, 'A0002', 'N001001');
INSERT INTO `c_role_menu` VALUES (6, 'A0002', 'B001');
INSERT INTO `c_role_menu` VALUES (7, 'A0002', 'B001001');
INSERT INTO `c_role_menu` VALUES (8, 'A0002', 'B001002');
INSERT INTO `c_role_menu` VALUES (9, 'A0002', 'M001');
INSERT INTO `c_role_menu` VALUES (10, 'A0002', 'M001001');
INSERT INTO `c_role_menu` VALUES (11, 'A0002', 'M001002');
INSERT INTO `c_role_menu` VALUES (12, 'A0002', 'M001003');
INSERT INTO `c_role_menu` VALUES (13, 'B0001', 'F001');
INSERT INTO `c_role_menu` VALUES (14, 'B0001', 'M001');
INSERT INTO `c_role_menu` VALUES (15, 'B0001', 'M001001');
INSERT INTO `c_role_menu` VALUES (16, 'B0001', 'M001002');
INSERT INTO `c_role_menu` VALUES (17, 'B0001', 'M001003');
INSERT INTO `c_role_menu` VALUES (18, 'C0001', 'F001');
INSERT INTO `c_role_menu` VALUES (19, 'C0001', 'M001');
INSERT INTO `c_role_menu` VALUES (20, 'C0001', 'M001001');
INSERT INTO `c_role_menu` VALUES (21, 'C0001', 'M001002');
INSERT INTO `c_role_menu` VALUES (22, 'C0001', 'M001003');
    -- ogg管理模块新增
INSERT INTO `c_role_menu` VALUES (23, 'A0002', 'O001');
INSERT INTO `c_role_menu` VALUES (24, 'A0002', 'O001001');
INSERT INTO `c_role_menu` VALUES (25, 'B0001', 'O001');
INSERT INTO `c_role_menu` VALUES (26, 'B0001', 'O001001');
INSERT INTO `c_role_menu` VALUES (27, 'C0001', 'O001');
INSERT INTO `c_role_menu` VALUES (28, 'C0001', 'O001001');

-- 初始化告警数据字典
INSERT  INTO `d_alarm_plugin`(`id`,`alert_type`,`field_name`,`field_code`,`field_order`,`field_type`,`field_type_key`,`state`,`iscancel`,`remark`) VALUES
(1,'EMAIL','邮件服务器','host',2,'TEXT',NULL,1,0,NULL),
(2,'EMAIL','邮件账户','username',3,'TEXT',NULL,1,0,NULL),
(3,'EMAIL','邮箱密码','password',4,'TEXT',NULL,1,0,NULL),
(4,'MOBILE','手机号','phone',1,'TEXT',NULL,1,0,NULL);
-- 初始化数据源数据字典
INSERT  INTO `d_data_source_plugin`(`id`,`source_type`,`field_name`,`field_code`,`field_order`,`field_type`,`field_type_key`,`field_validate`,`field_explain`,`state`,`iscancel`,`remark`) VALUES
(1,'JDBC','数据库类型','dbtype',1,'RADIO','DbType','','',1,0,NULL),
(2,'JDBC','url','url',2,'TEXT',NULL,'','例如:mysql(jdbc:mysql://0.0.0.0:3306/test?useUnicode=true&characterEncoding=utf8) oracle(jdbc:oracle:thin:@0.0.0.0:1521:test)',1,0,NULL),
(3,'JDBC','用户名','userName',3,'TEXT',NULL,'','',1,0,NULL),
(4,'JDBC','密码','password',4,'TEXT',NULL,'','',1,0,NULL),
(5,'KAFKA','服务器列表','servers',1,'TEXT',NULL,'','例如:0.0.0.1:9092,0.0.0.2:9092',1,0,NULL),
(6,'KAFKA','主题','topics',2,'TEXT',NULL,'','例如:test1(多个,隔开)',1,0,NULL),
(7,'KAFKA','查询超时时间','pollTimeOut',3,'TEXT',NULL,'','例如:5000',1,0,NULL),
(8,'KAFKA','单次查询数量','oncePollSize',4,'TEXT',NULL,'','例如:1000',1,0,NULL),
(9,'CANAL','地址','address',2,'TEXT',NULL,'','例如:0.0.0.0:3306',1,0,NULL),
(10,'CANAL','数据库','database',3,'TEXT',NULL,'','例如:paytest',1,0,NULL),
(11,'CANAL','用户','username',4,'TEXT',NULL,'','',1,0,NULL),
(12,'CANAL','密码','password',5,'TEXT',NULL,'','',1,0,NULL),
(13,'CANAL','过滤器','filter',6,'TEXT',NULL,'','例如:paytest\.(aaa|aab|test_table|test_table2)',1,0,NULL),
(14,'KAFKA_PRODUCE','服务器列表','servers',1,'TEXT',NULL,'','例如:0.0.0.1:9092,0.0.0.2:9092',1,0,NULL),
(15,'KAFKA_PRODUCE','主题','topics',2,'TEXT',NULL,'','例如:test1(只能一个主题)',1,0,NULL),
(16,'KAFKA_PRODUCE','是否ogg-json格式','oggJson',3,'TEXT',true,'','例如:true or false',1,0,NULL),
(17,'KAFKA_PRODUCE','分片字段名','partitionKey',4,'TEXT',NULL,'','例如:{"db.table_name":"id,name","db2.table_name":"id,gender"}(格式:schema.表名->字段名1,字段名2)',1,0,NULL);
-- 初始化字典表
INSERT  INTO `d_dictionary`(`id`,`code`,`name`,`parentcode`,`level`,`dictype`,`state`,`remark`) VALUES 
(1,'Manual','手动','-1',1,'PTmaintain',1,NULL),
(2,'Automatic','自动','-1',1,'PTmaintain',1,NULL);
-- 初始化邮箱
insert  into `s_alarm`(`id`,`alarm_type`,`create_user_id`,`update_user_id`,`create_time`,`update_time`,`state`,`iscancel`,`remark`) values (1,'EMAIL',-1,-1,'2018-04-19 15:15:21','2018-04-19 15:15:21',1,0,NULL);
insert  into `s_alarm_plugin`(`id`,`alarm_id`,`alarm_type`,`plugin_code`,`plugin_name`,`plugin_value`) values 
(1,1,'EMAIL','host','邮件服务器','smtp.163.com'),
(2,1,'EMAIL','username','邮件账户','1@163.com'),
(3,1,'EMAIL','password','邮箱密码','account');
insert  into `s_alarm_user`(`id`,`alarm_id`,`user_id`) values (1,1,1);
-- 2018-10-23新增
ALTER TABLE job_tasks ADD job_xml_text TEXT DEFAULT NULL COMMENT '任务-xml文本';
ALTER TABLE job_tasks ADD job_json_text TEXT DEFAULT NULL COMMENT '任务-json文本';