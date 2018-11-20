-- 2018-10-23新增
ALTER TABLE job_tasks ADD job_xml_text TEXT DEFAULT NULL COMMENT '任务-xml文本';
ALTER TABLE job_tasks ADD job_json_text TEXT DEFAULT NULL COMMENT '任务-json文本';
INSERT INTO `c_menu` VALUES (20, 'J001002', 'J001', '本地任务', '/specialTask', 'fa-tags', 2, 2, 1, 0, 1, 1, NULL);
INSERT INTO `c_role_menu` VALUES (29, 'A0002', 'J001002');
-- 2018-11-13删除
DELETE FROM `c_role_menu` where id = 23;
DELETE FROM `c_role_menu` where id = 24;
DELETE FROM `c_role_menu` where id = 25;
DELETE FROM `c_role_menu` where id = 26;
DELETE FROM `c_role_menu` where id = 27;
DELETE FROM `c_role_menu` where id = 28;
-- 2018-11-20作废
UPDATE c_menu SET iscancel = 1 WHERE CODE LIKE 'O001%';