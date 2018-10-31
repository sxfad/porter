-- 2018-10-23新增
ALTER TABLE job_tasks ADD job_xml_text TEXT DEFAULT NULL COMMENT '任务-xml文本';
ALTER TABLE job_tasks ADD job_json_text TEXT DEFAULT NULL COMMENT '任务-json文本';