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
-- 新增本地任务抓取
INSERT INTO `c_menu` VALUES (20, 'J001002', 'J001', '本地任务', '/specialTask', 'fa-tags', 2, 2, 1, 0, 1, 1, NULL);
-- 新增菜单管理、菜单权限配置
INSERT INTO `c_menu` VALUES (21, 'C001002', 'C001', '菜单管理', '/menus','fa-table', 2, 2, 1, 0, 1, 1, NULL);
INSERT INTO `c_menu` VALUES (22, 'C001003', 'C001', '菜单权限配置', '/permission','fa-table', 2, 3, 1, 0, 1, 1, NULL);
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
    -- 新增本地任务
INSERT INTO `c_role_menu` VALUES (29, 'A0002', 'J001002');
-- 初始化告警数据字典
INSERT  INTO `d_alarm_plugin`(`id`,`alert_type`,`field_name`,`field_code`,`field_order`,`field_type`,`field_type_key`,`state`,`iscancel`,`remark`) VALUES
(1,'EMAIL','邮件服务器','host',2,'TEXT',NULL,1,0,NULL),
(2,'EMAIL','邮件账户','username',3,'TEXT',NULL,1,0,NULL),
(3,'EMAIL','邮箱密码','password',4,'TEXT',NULL,1,0,NULL),
(4,'MOBILE','手机号','phone',1,'TEXT',NULL,1,0,NULL),
('5', 'EMAIL', '安全协议', 'smtpSslEnable', '5', 'RADIO', 'EnableType', '1', '0', NULL);
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
(13,'CANAL','过滤器','filter',6,'TEXT',NULL,'','例如:paytest\\.(aaa|aab|test_table|test_table2)',1,0,NULL),
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
-- 初始化任务权限操作类型
INSERT INTO `d_control_type_plugin` VALUES (1, 'CHANGE', '移交', 'CHANGE', 1, 'RADIO', NULL, 1, 0, NULL);
INSERT INTO `d_control_type_plugin` VALUES (2, 'SHARE', '共享', 'SHARE', 2, 'RADIO', NULL, 1, 0, NULL);
INSERT INTO `d_control_type_plugin` VALUES (3, 'CANCEL', '作废', 'CANCEL', 3, 'RADIO', NULL, 1, 0, NULL);
INSERT INTO `d_control_type_plugin` VALUES (4, 'RECYCLE_C', '回收所有者', 'RECYCLE_C', 4, 'RADIO', NULL, 1, 0, NULL);
INSERT INTO `d_control_type_plugin` VALUES (5, 'RECYCLE_S', '回收共享者', 'RECYCLE_S', 5, 'RADIO', NULL, 1, 0, NULL);
INSERT INTO `d_control_type_plugin` VALUES (6, 'RECYCLE_A', '回收所有权限', 'RECYCLE_A', 6, 'RADIO', NULL, 1, 0, NULL);
-- 权限控制操作类型
INSERT INTO `r_owner_control` VALUES (1, 1, 1);
INSERT INTO `r_owner_control` VALUES (2, 1, 2);
INSERT INTO `r_owner_control` VALUES (3, 1, 3);
INSERT INTO `r_owner_control` VALUES (4, 2, 3);
INSERT INTO `r_owner_control` VALUES (5, 0, 1);
INSERT INTO `r_owner_control` VALUES (6, 0, 2);
INSERT INTO `r_owner_control` VALUES (7, 0, 4);
INSERT INTO `r_owner_control` VALUES (8, 0, 5);
INSERT INTO `r_owner_control` VALUES (9, 0, 6);

-- 复制统计数据表结构(需要创建今明两天的数据表)
-- CREATE TABLE mr_job_tasks_monitor_yyyymmdd SELECT * FROM mr_job_tasks_monitor WHERE 1=2;
-- CREATE TABLE mr_log_monitor_yyyymmdd SELECT * FROM mr_log_monitor WHERE 1=2;
-- CREATE TABLE mr_nodes_monitor_yyyymmdd SELECT * FROM mr_nodes_monitor WHERE 1=2;
