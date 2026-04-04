-- Campus Appointment System - Sample Data
-- 插入示例数据

USE cas_db;

-- 插入示例服务数据
-- service_state: 0-disabled, 1-enabled
INSERT INTO `services` (`service_name`, `service_describe`, `service_state`) VALUES 
('心理咨询', '提供专业的心理咨询服务，帮助学生缓解压力和焦虑', 1),
('学业辅导', '提供学科辅导和学习建议，帮助学生提高成绩', 1),
('就业指导', '提供简历修改和面试指导，帮助学生顺利就业', 1),
('健康检查', '提供常规健康检查和医疗咨询', 1),
('法律咨询', '提供法律知识咨询和法律问题解答', 1);
